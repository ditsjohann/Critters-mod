"""
Vectorised (numpy) procedural shading toolkit used to paint critter textures.

Every texel of every cube face is mapped back to its 3D position on the model; materials are
functions of that position (plus an analytic surface normal), so patterns flow continuously across
the many small cuboids a curved body is built from, and lighting looks smooth instead of blocky.
"""
import numpy as np

# ---------------------------------------------------------------------------
# Small helpers
# ---------------------------------------------------------------------------


def C(v):
    """Hex int or (r, g, b) -> float colour array."""
    if isinstance(v, int):
        return np.array([(v >> 16) & 255, (v >> 8) & 255, v & 255], dtype=np.float64)
    return np.array(v, dtype=np.float64)


def mix(a, b, t):
    t = np.asarray(t, dtype=np.float64)
    if t.ndim == 1:
        t = t[:, None]
    return a + (b - a) * t


def smooth(e0, e1, x):
    t = np.clip((x - e0) / (e1 - e0), 0.0, 1.0)
    return t * t * (3.0 - 2.0 * t)


def fract(x):
    return x - np.floor(x)


def normalize(v):
    n = np.linalg.norm(v, axis=-1, keepdims=True)
    n[n < 1e-9] = 1.0
    return v / n


# ---------------------------------------------------------------------------
# Noise
# ---------------------------------------------------------------------------

_M = np.uint64(0xFFFFFFFF)


def _hash(ix, iy, iz, seed):
    h = (ix.astype(np.int64) * 374761393 + iy.astype(np.int64) * 668265263
         + iz.astype(np.int64) * 1274126177 + seed * 144665).astype(np.uint64) & _M
    h = ((h ^ (h >> np.uint64(13))) * np.uint64(1274126177)) & _M
    h = h ^ (h >> np.uint64(16))
    return h.astype(np.float64) / 4294967296.0


def vnoise(p, seed=0):
    """Smooth value noise in [-1, 1]. p: (N, 3)."""
    f = np.floor(p)
    t = p - f
    t = t * t * (3 - 2 * t)
    i = f.astype(np.int64)
    out = 0.0
    for dx in (0, 1):
        for dy in (0, 1):
            for dz in (0, 1):
                w = (t[:, 0] if dx else 1 - t[:, 0]) * (t[:, 1] if dy else 1 - t[:, 1]) * (t[:, 2] if dz else 1 - t[:, 2])
                out = out + w * _hash(i[:, 0] + dx, i[:, 1] + dy, i[:, 2] + dz, seed)
    return out * 2.0 - 1.0


def fbm(p, octaves=4, seed=0, lac=2.03, gain=0.5):
    amp, tot, norm = 1.0, 0.0, 0.0
    q = p
    for o in range(octaves):
        tot = tot + amp * vnoise(q, seed + o * 31)
        norm += amp
        amp *= gain
        q = q * lac + 17.17
    return tot / norm


def worley(p, seed=0, jitter=0.9):
    """Cellular noise. Returns (f1, f2, cell_random) for points p (N, 3)."""
    f = np.floor(p).astype(np.int64)
    n = p.shape[0]
    f1 = np.full(n, 9.0)
    f2 = np.full(n, 9.0)
    cid = np.zeros(n)
    for dx in (-1, 0, 1):
        for dy in (-1, 0, 1):
            for dz in (-1, 0, 1):
                cx, cy, cz = f[:, 0] + dx, f[:, 1] + dy, f[:, 2] + dz
                fx = cx + 0.5 + (_hash(cx, cy, cz, seed) - 0.5) * jitter
                fy = cy + 0.5 + (_hash(cx, cy, cz, seed + 7) - 0.5) * jitter
                fz = cz + 0.5 + (_hash(cx, cy, cz, seed + 13) - 0.5) * jitter
                d = np.sqrt((p[:, 0] - fx) ** 2 + (p[:, 1] - fy) ** 2 + (p[:, 2] - fz) ** 2)
                closer = d < f1
                f2 = np.where(closer, f1, np.minimum(f2, d))
                cid = np.where(closer, _hash(cx, cy, cz, seed + 99), cid)
                f1 = np.where(closer, d, f1)
    return f1, f2, cid


# ---------------------------------------------------------------------------
# Shading context and output
# ---------------------------------------------------------------------------


class Ctx:
    """Everything a material may look at for a batch of texels."""

    def __init__(self, p, q, n, uv, face):
        self.p = p      # (N,3) part-local position (model units, +y down, -z front)
        self.q = q      # (N,3) position normalised to the shape (ellipsoid -> unit sphere)
        self.n = n      # (N,3) surface normal, part-local
        self.uv = uv    # (N,2) 0..1 coordinates across the face / plane
        self.face = face
        self.N = p.shape[0]

    @property
    def x(self):
        return self.p[:, 0]

    @property
    def y(self):
        return self.p[:, 1]

    @property
    def z(self):
        return self.p[:, 2]


class Out:
    def __init__(self, n, col):
        self.col = np.broadcast_to(np.asarray(col, dtype=np.float64), (n, 3)).copy()
        self.alpha = np.full(n, 255.0)
        self.spec = np.zeros(n)
        self.shin = np.full(n, 20.0)
        self.metal = np.zeros(n)
        self.glow = None  # (N,3) or None
        self.flat = np.zeros(n)  # 1 = skip baked lighting (e.g. glowing parts)

    def add_glow(self, col, strength):
        if self.glow is None:
            self.glow = np.zeros((len(self.alpha), 3))
        strength = np.clip(np.broadcast_to(np.asarray(strength, dtype=np.float64), self.alpha.shape), 0, 1)
        self.glow = np.maximum(self.glow, np.asarray(col, dtype=np.float64)[None, :] * strength[:, None])


# ---------------------------------------------------------------------------
# Lighting baked into the textures
# ---------------------------------------------------------------------------

LIGHT = normalize(np.array([[-0.35, -1.0, -0.55]]))[0]
VIEW = normalize(np.array([[0.15, -0.6, -1.0]]))[0]
HALF = normalize((LIGHT + VIEW)[None, :])[0]


def light(out, n_model):
    ndl = n_model @ LIGHT
    diffuse = 0.74 + 0.36 * np.clip(ndl * 0.65 + 0.35, 0.0, 1.0)
    under = 1.0 - 0.22 * np.clip(n_model[:, 1], 0.0, 1.0)
    shade = diffuse * under
    shade = np.where(out.flat > 0.5, 1.0, shade)
    col = out.col * shade[:, None]
    ndh = np.clip(n_model @ HALF, 0.0, 1.0)
    s = out.spec * np.power(ndh, out.shin) * 255.0
    tint = mix(np.ones_like(out.col), out.col / 255.0 * 1.4, out.metal)
    col = col + s[:, None] * tint
    return np.clip(col, 0, 255)


def vanilla_light(n_model):
    """Yaw-averaged brightness Minecraft's two directional entity lights give a normal (model space, +y down)."""
    up = np.clip(-n_model[:, 1], 0, 1)
    horiz = np.sqrt(n_model[:, 0] ** 2 + n_model[:, 2] ** 2)
    return np.clip(0.4 + 0.6 * (up * 1.62 + horiz * 0.36), 0.4, 1.0)


def compensate(col, n_shape_model, n_face_model, strength=1.0):
    """Counter-shade a texel so Minecraft's per-face lighting reproduces the smooth (analytic) lighting."""
    want = vanilla_light(n_shape_model)
    got = vanilla_light(n_face_model)
    k = np.clip(want / got, 0.75, 1.6)
    k = 1 + (k - 1) * strength
    return np.clip(col * k[:, None], 0, 255)
