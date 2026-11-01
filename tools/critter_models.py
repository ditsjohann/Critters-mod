"""
High-detail model + material definitions for every critter.

Units: models are authored at UPB units per block (5x vanilla resolution) and the renderer scales
them down (scale = 16 / UPB).  +Y points DOWN, the ground is at y = 24, the front faces -Z.

Curved shapes (domes, shells, bodies, eyes) are built from stacked rounded slices that approximate
an ellipsoid; every cube remembers the analytic shape it belongs to so the texture painter can use
the true curved surface normal for lighting and continuous 3D patterns.

Part names drive the animations in CritterModel.java:
  leg_{l,r}N (+ femur_/tibia_/tarsus_ children), antenna_l/r, eyestalk_l/r, tentacle_l/r,
  wing_l/r, elytron_l/r, hindleg_l/r + shin_l/r, claw_l/r + pincer_l/r, tail_base, tail_fin,
  fin_l/r, dorsal_fin, anal_fin, head, body, shell, abdomen
"""
import math

import numpy as np

from shading import C, Out, mix, smooth, fract, fbm, worley, normalize

UPB = 80.0                # model units per block
SCALE = 16.0 / UPB        # renderer scale
GROUND = 24.0


# ===========================================================================
# Geometry
# ===========================================================================

class Ell:
    """Analytic ellipsoid used for normals / normalised coordinates."""

    def __init__(self, c, r):
        self.c = np.array(c, dtype=np.float64)
        self.r = np.maximum(np.array(r, dtype=np.float64), 1e-3)

    def q(self, p):
        return (p - self.c) / self.r

    def normal(self, p, fn):
        g = (p - self.c) / (self.r ** 2)
        n = normalize(g)
        bad = np.linalg.norm(g, axis=1) < 1e-6
        n[bad] = fn[bad]
        return n


class Cyl:
    """Analytic cylinder from a to b (for legs, stalks, horns)."""

    def __init__(self, a, b, r):
        self.a = np.array(a, dtype=np.float64)
        self.b = np.array(b, dtype=np.float64)
        self.r = r
        d = self.b - self.a
        self.len = max(1e-6, float(np.linalg.norm(d)))
        self.d = d / self.len

    def q(self, p):
        rel = p - self.a
        t = rel @ self.d
        radial = rel - t[:, None] * self.d[None, :]
        out = np.zeros_like(p)
        out[:, 0] = t / self.len
        out[:, 1] = np.linalg.norm(radial, axis=1) / self.r
        out[:, 2] = 0
        return out

    def normal(self, p, fn):
        rel = p - self.a
        t = rel @ self.d
        radial = rel - t[:, None] * self.d[None, :]
        n = normalize(radial)
        bad = np.linalg.norm(radial, axis=1) < 1e-6
        n[bad] = fn[bad]
        return n


class Cube:
    def __init__(self, x, y, z, w, h, d, mat, shape=None, grow=0.0, plane=False):
        self.x, self.y, self.z = float(x), float(y), float(z)
        self.w, self.h, self.d = int(round(w)), int(round(h)), int(round(d))
        self.mat = mat
        self.shape = shape
        self.grow = grow
        self.plane = plane
        self.u = self.v = 0


class Part:
    def __init__(self, name, pivot=(0, 0, 0), rot=(0, 0, 0), cubes=None, children=None):
        self.name = name
        self.pivot = tuple(float(v) for v in pivot)
        self.rot = tuple(float(v) for v in rot)
        self.cubes = list(cubes or [])
        self.children = list(children or [])

    def add(self, *items):
        for it in items:
            if isinstance(it, Part):
                self.children.append(it)
            elif isinstance(it, Cube):
                self.cubes.append(it)
            else:
                self.add(*it)
        return self

    def all_parts(self):
        yield self
        for c in self.children:
            yield from c.all_parts()


class Model:
    def __init__(self, sid, parts, glow=False, scale=SCALE):
        self.id = sid
        self.parts = parts
        self.glow = glow
        self.scale = scale

    def all_parts(self):
        for p in self.parts:
            yield from p.all_parts()


def _rect_set(a, b):
    """Cross-section rectangles approximating an ellipse with half extents a, b."""
    if 2 * a < 4 or 2 * b < 4:
        return [(1.0, 1.0)]
    if 2 * a < 10 and 2 * b < 10:
        return [(1.0, 0.62), (0.62, 1.0)]
    return [(1.0, 0.42), (0.86, 0.76), (0.42, 1.0)]


def ellipsoid(c, r, mat, axis='y', step=1.0, half=None, flat=None, min_dim=1):
    """
    Approximate an ellipsoid with slices perpendicular to `axis`.
    half: None | 'neg' (only the half on the negative side of the axis, e.g. the top for axis y)
          | 'pos'.
    flat: for axis 'z'/'x' slices - align every slice's bottom to this y (flat soles).
    """
    cx, cy, cz = c
    rx, ry, rz = r
    shape = Ell(c, r)
    ai = {'x': 0, 'y': 1, 'z': 2}[axis]
    ra = r[ai]
    ca = c[ai]
    lo, hi = ca - ra, ca + ra
    if half == 'neg':
        hi = ca
    elif half == 'pos':
        lo = ca
    n = max(1, int(round((hi - lo) / step)))
    th = (hi - lo) / n
    cubes = []
    others = [i for i in range(3) if i != ai]
    for i in range(n):
        a0 = lo + i * th
        mid = a0 + th / 2
        t = (mid - ca) / ra
        s = math.sqrt(max(0.0, 1.0 - t * t))
        if half is not None:
            s = max(s, 0.25)
        ea = r[others[0]] * s
        eb = r[others[1]] * s
        thick = max(1, int(round(th)))
        for fa, fb in _rect_set(ea, eb):
            da = max(min_dim, int(round(2 * ea * fa)))
            db = max(min_dim, int(round(2 * eb * fb)))
            pos = [0.0, 0.0, 0.0]
            dims = [0, 0, 0]
            pos[ai] = a0
            dims[ai] = thick
            pos[others[0]] = c[others[0]] - da / 2
            dims[others[0]] = da
            pos[others[1]] = c[others[1]] - db / 2
            dims[others[1]] = db
            if flat is not None and 1 in others:
                pos[1] = flat - dims[1]
            cubes.append(Cube(pos[0], pos[1], pos[2], dims[0], dims[1], dims[2], mat, shape))
    return cubes


def box(x, y, z, w, h, d, mat, shape=None, grow=0.0):
    return Cube(x, y, z, w, h, d, mat, shape, grow)


def rod(axis, start, length, thick, mat, center=(0.0, 0.0), sign=1):
    """A thin limb segment along an axis ('x', 'y', 'z') with a cylinder normal."""
    t = thick
    if axis == 'x':
        x0 = start if sign > 0 else start - length
        cube = Cube(x0, center[0] - t / 2, center[1] - t / 2, length, t, t, mat,
                    Cyl((start, center[0], center[1]), (start + sign * length, center[0], center[1]), t / 2))
    elif axis == 'y':
        y0 = start if sign > 0 else start - length
        cube = Cube(center[0] - t / 2, y0, center[1] - t / 2, t, length, t, mat,
                    Cyl((center[0], start, center[1]), (center[0], start + sign * length, center[1]), t / 2))
    else:
        z0 = start if sign > 0 else start - length
        cube = Cube(center[0] - t / 2, center[1] - t / 2, z0, t, t, length, mat,
                    Cyl((center[0], center[1], start), (center[0], center[1], start + sign * length), t / 2))
    return cube


def plane(kind, x, y, z, a, b, mat):
    """Zero-thickness card. kind 'yz' (side-on, e.g. fish fins), 'xz' (flat wings), 'xy'."""
    if kind == 'yz':
        return Cube(x, y, z, 0, b, a, mat, plane=True)      # a along z, b along y
    if kind == 'xz':
        return Cube(x, y, z, a, 0, b, mat, plane=True)      # a along x, b along z
    return Cube(x, y, z, a, b, 0, mat, plane=True)          # a along x, b along y


def leg(side, idx, pivot, spread, femur, tibia, tarsus, mat, up=0.55, foot=0.35,
        thick=(2, 1, 1), tars_mat=None, prefix='leg'):
    """
    Articulated insect/crab leg whose foot rests on the ground.
    The root part only carries the sideways spread (yRot, animated for walking); the femur rises,
    the tibia comes down and the tarsus rests on the ground.
    """
    nm = ('l' if side > 0 else 'r') + str(idx)
    s = 1 if side > 0 else -1
    px, py, pz = pivot
    need = GROUND - py - thick[2] / 2.0
    rem = need + femur * math.sin(up) - tarsus * math.sin(foot)
    b = math.asin(max(-0.95, min(0.995, rem / tibia)))
    root = Part('%s_%s' % (prefix, nm), (s * px, py, pz), (0, s * spread, 0))
    fem = Part('femur_' + nm, (0, 0, 0), (0, 0, -s * up), [rod('x', 0, femur, thick[0], mat, sign=s)])
    tib = Part('tibia_' + nm, (s * (femur - 0.5), 0, 0), (0, 0, s * (up + b)), [rod('x', 0, tibia, thick[1], mat, sign=s)])
    tar = Part('tarsus_' + nm, (s * (tibia - 0.3), 0, 0), (0, 0, s * (foot - b)),
               [rod('x', 0, tarsus, thick[2], tars_mat or mat, sign=s)])
    tib.add(tar)
    fem.add(tib)
    root.add(fem)
    return root


def chain(name, n, seg, thick, mat, bend=(0, 0, 0), first_rot=(0, 0, 0), pivot=(0, 0, 0), club=None, axis='-z'):
    """Segmented antenna/horn/filament: n segments along -z, each bent a little relative to the last."""
    parts = []
    root = None
    parent = None
    for i in range(n):
        t = thick
        if club and i >= n - club[0]:
            t = club[1]
        cube = rod('z', 0, seg, t, mat, sign=-1) if axis == '-z' else rod('y', 0, seg, t, mat, sign=-1)
        p = Part(name if i == 0 else '%s_%d' % (name, i),
                 pivot if i == 0 else ((0, 0, -seg + 0.2) if axis == '-z' else (0, -seg + 0.2, 0)),
                 first_rot if i == 0 else bend, [cube])
        if parent is None:
            root = p
        else:
            parent.add(p)
        parent = p
        parts.append(p)
    return root, parts


# ===========================================================================
# Materials
# ===========================================================================

def M(fn):
    return fn


def chitin(base, spec=0.55, shin=40.0, var=0.10, freq=0.35, seed=1, metal=0.0, pits=0.0, hair=0.0,
           dark_joints=None):
    base = C(base)

    def m(c):
        o = Out(c.N, base)
        nz = fbm(c.p * freq, 3, seed)
        o.col = o.col * (1 + var * nz)[:, None]
        if pits > 0:
            f1, f2, _ = worley(c.p * 0.9, seed + 3)
            pit = smooth(0.0, 0.25, f1)
            o.col = o.col * (1 - pits * (1 - pit))[:, None]
        if hair > 0:
            h = fbm(c.p * 2.2, 2, seed + 5)
            o.col = mix(o.col, o.col * 1.35 + 20, np.clip(h - 0.35, 0, 1) * hair)
        if dark_joints is not None:
            q = c.q[:, 0]
            j = np.maximum(smooth(0.18, 0.0, q), smooth(0.82, 1.0, q))
            o.col = mix(o.col, C(dark_joints), j * 0.8)
        o.spec[:] = spec
        o.shin[:] = shin
        o.metal[:] = metal
        return o
    return m


def eye_mat(iris=0x101010, pupil=0x050505, out_axis=None, ring=0.55, glossy=0.9, compound=False, base=None):
    iris = C(iris)
    pupil = C(pupil)
    basec = C(base) if base is not None else iris

    def m(c):
        o = Out(c.N, basec)
        if compound:
            f1, f2, _ = worley(c.p * 2.6, 77)
            facet = smooth(0.0, 0.2, f2 - f1)
            o.col = mix(basec * 0.45, basec * 1.15, facet)
        elif out_axis is not None:
            d = c.q @ np.array(out_axis, dtype=np.float64)
            o.col = mix(np.broadcast_to(basec, (c.N, 3)), np.broadcast_to(iris, (c.N, 3)), smooth(0.2, 0.35, d))
            o.col = mix(o.col, np.broadcast_to(pupil, (c.N, 3)), smooth(ring, ring + 0.08, d))
        o.spec[:] = glossy
        o.shin[:] = 80.0
        return o
    return m


def skin(base, groove=0x000000, freq=0.55, spec=0.35, seed=2, var=0.12, sole=None, glow=None, glow_amt=0.0):
    """Moist, tuberculate mollusc skin."""
    base = C(base)
    gcol = C(groove) if isinstance(groove, int) else groove

    def m(c):
        f1, f2, cid = worley(c.p * freq, seed)
        edge = smooth(0.0, 0.22, f2 - f1)
        col = base * (1 + var * fbm(c.p * 0.3, 3, seed + 1))[:, None]
        col = col * (0.95 + 0.12 * (cid - 0.5))[:, None]
        col = mix(gcol * 0.4 + col * 0.6, col, edge)
        col = col * (0.85 + 0.15 * smooth(0.0, 0.6, f1))[:, None]  # tubercle domes
        o = Out(c.N, col)
        if sole is not None:
            o.col = mix(o.col, C(sole), (c.face == 'bottom') * 1.0 + 0 * c.x)
        o.spec[:] = spec * (0.6 + 0.4 * edge)
        o.shin[:] = 28.0
        if glow is not None:
            g = smooth(0.06, 0.0, f2 - f1) * glow_amt
            o.add_glow(C(glow), g)
            o.col = mix(o.col, C(glow), g * 0.8)
        return o
    return m


def fin_mat(base, edge=None, alpha=200, outline=None, rays=14, ray_origin=(0.0, 0.5), seed=4,
            grad_axis=0, iridescent=None, stripes=None):
    base = C(base)
    edgec = C(edge) if edge is not None else base

    def m(c):
        u, v = c.uv[:, 0], c.uv[:, 1]
        o = Out(c.N, base)
        # gradient towards fin edge
        t = u if grad_axis == 0 else v
        o.col = mix(np.broadcast_to(base, (c.N, 3)), np.broadcast_to(edgec, (c.N, 3)), smooth(0.2, 1.0, t))
        # rays radiating from the base
        ang = np.arctan2(v - ray_origin[1], u - ray_origin[0] + 1e-6)
        r = np.abs(np.sin(ang * rays + fbm(np.stack([u * 6, v * 6, u * 0], 1), 2, seed) * 0.6))
        ray = smooth(0.85, 1.0, r)
        o.col = o.col * (1 + 0.25 * ray)[:, None]
        if stripes is not None:
            sc, period, axis = stripes
            k = u if axis == 0 else v
            band = smooth(0.35, 0.45, np.abs(fract(k * period) - 0.5) * 2)
            o.col = mix(o.col, C(sc), 1 - band)
        if iridescent is not None:
            o.col = mix(o.col, C(iridescent), 0.35 * (0.5 + 0.5 * np.sin(u * 9 + v * 5)))
        a = np.full(c.N, float(alpha)) * (0.75 + 0.25 * ray)
        a = a * (1 - 0.35 * smooth(0.75, 1.0, t))
        if outline is not None:
            a = a * outline(u, v)
        o.alpha = np.clip(a, 0, 255)
        o.spec[:] = 0.25
        o.shin[:] = 30
        o.flat[:] = 0.3
        return o
    return m


def wing_mat(tint=(200, 205, 195), vein=(60, 50, 40), alpha=120, outline=None, seed=9):
    tint = C(tint)
    veinc = C(vein)

    def m(c):
        u, v = c.uv[:, 0], c.uv[:, 1]
        p = np.stack([u * 7, v * 9, u * 0 + 0.5], 1)
        f1, f2, _ = worley(p, seed)
        cell = smooth(0.0, 0.08, f2 - f1)
        long = smooth(0.06, 0.0, np.abs(fract(u * 5 + v * 0.6) - 0.5) - 0.44)
        veins = np.maximum(1 - cell, long)
        o = Out(c.N, tint)
        o.col = mix(o.col, np.broadcast_to(veinc, (c.N, 3)), veins * 0.85)
        # thin-film rainbow sheen
        o.col = o.col + (np.stack([np.sin(u * 11), np.sin(u * 11 + 2), np.sin(u * 11 + 4)], 1) * 18)
        a = alpha + veins * 110
        if outline is not None:
            a = a * outline(u, v)
        o.alpha = np.clip(a, 0, 255)
        o.spec[:] = 0.6
        o.shin[:] = 60
        return o
    return m


def ellipse_outline(cu=0.5, cv=0.5, ru=0.5, rv=0.5):
    def f(u, v):
        d = ((u - cu) / ru) ** 2 + ((v - cv) / rv) ** 2
        return smooth(1.0, 0.85, d)
    return f


# ===========================================================================
# SNAILS
# ===========================================================================

def shell_mat(kind, coil_c, R):
    """Spiral shell.  coil_c = (y, z) of the coil axis (which runs along x)."""
    yc, zc = coil_c

    def m(c):
        dy = c.y - yc
        dz = c.z - zc
        th = np.arctan2(dy, dz)
        rho = np.sqrt(dy * dy + dz * dz) / R + 0.02
        s = np.log(rho) / np.log(3.6) + th / (2 * np.pi)
        f = fract(s)                                     # position across the whorl
        suture = smooth(0.06, 0.0, np.minimum(f, 1 - f))
        growth = 0.5 + 0.5 * np.sin(th * 30 + fbm(c.p * 0.4, 2, 5) * 3)
        nz = fbm(c.p * 0.35, 4, 11)
        if kind == 'garden':
            col = mix(C(0x9A7040), C(0xC9A468), smooth(-0.3, 0.5, nz))
            band = (smooth(0.18, 0.24, f) * smooth(0.52, 0.46, f)) * smooth(-0.15, 0.25, fbm(c.p * np.array([0.6, 0.2, 0.2]), 3, 21))
            col = mix(col, C(0x4A2C14), band * 0.85)
            fleck = smooth(0.55, 0.75, fbm(c.p * 1.1, 2, 31))
            col = mix(col, C(0xE6C98A), fleck * 0.6)
            spec, shin = 0.45, 35
        elif kind == 'banded':
            col = mix(C(0xE8CC5A), C(0xF6E596), smooth(-0.4, 0.6, nz))
            bands = 0
            for a, b in ((0.16, 0.30), (0.48, 0.58), (0.72, 0.86)):
                bands = np.maximum(bands, smooth(a - 0.015, a + 0.015, f) * smooth(b + 0.015, b - 0.015, f))
            col = mix(col, C(0x3A2210), bands * 0.92)
            spec, shin = 0.65, 50
        elif kind == 'moss':
            col = mix(C(0x5A4A2A), C(0x7A6638), smooth(-0.4, 0.5, nz))
            moss = smooth(0.0, 0.25, fbm(c.p * 0.22, 4, 41) + 0.15 * fbm(c.p * 1.6, 2, 43))
            mtex = mix(C(0x3E6B1E), C(0x8CBF46), smooth(-0.5, 0.7, fbm(c.p * 1.8, 3, 45)))
            col = mix(col, mtex, moss)
            spore = smooth(0.62, 0.7, fbm(c.p * 2.5, 2, 47)) * moss
            col = mix(col, C(0xD8C25A), spore)
            spec, shin = 0.35 * (1 - moss), 30
        elif kind == 'amethyst':
            f1, f2, cid = worley(c.p * 0.22, 51)
            edge = smooth(0.12, 0.0, f2 - f1)
            col = mix(C(0x4B2378), C(0xA372E0), cid)
            col = mix(col, C(0x2C1150), smooth(0.0, 0.9, f1) * 0.35)
            col = mix(col, C(0xEBD8FF), edge * 0.9)
            spec, shin = 0.9, 90
        else:  # magma
            f1, f2, cid = worley(c.p * 0.17, 61)
            crack = smooth(0.09, 0.0, f2 - f1)
            col = mix(C(0x1E1A1A), C(0x3C302C), smooth(-0.5, 0.6, nz))
            col = col * (0.85 + 0.3 * cid)[:, None]
            heat = smooth(0.35, 0.0, f2 - f1)
            col = mix(col, C(0x5A1A0A), heat * 0.6)
            spec, shin = 0.2, 15
        col = col * (0.9 + 0.1 * growth)[:, None]
        col = mix(col, col * 0.35, suture)
        o = Out(c.N, col)
        o.spec[:] = spec
        o.shin[:] = shin
        if kind == 'magma':
            lava = mix(C(0xFF4A0A), C(0xFFD24A), smooth(0.04, 0.0, f2 - f1))
            o.col = mix(o.col, lava, crack)
            o.add_glow(C(0xFF7A1A), crack)
            o.flat = crack
        if kind == 'amethyst':
            o.add_glow(C(0x6A3CA8), edge * 0.55)
        return o
    return m


def crystal_mat():
    def m(c):
        t = np.clip(-c.q[:, 1] * 0.5 + 0.5, 0, 1)
        col = mix(C(0x5E2C9A), C(0xF0DCFF), t)
        facet = 0.85 + 0.25 * np.sign(np.sin((c.x + c.z) * 1.7))
        o = Out(c.N, col * facet[:, None])
        o.spec[:] = 1.0
        o.shin[:] = 120
        o.add_glow(C(0x7A4AC0), smooth(0.4, 1.0, t) * 0.8)
        return o
    return m


def moss_tuft_mat():
    def m(c):
        n = fbm(c.p * 1.9, 3, 91)
        col = mix(C(0x2F5A16), C(0x9AD054), smooth(-0.6, 0.8, n))
        o = Out(c.N, col)
        spore = smooth(0.55, 0.65, fbm(c.p * 3.0, 2, 93))
        o.col = mix(o.col, C(0xE0C860), spore)
        return o
    return m


SNAILS = {
    'garden_snail': dict(shell='garden', body=0x8A7A66, groove=0x3A3028, sole=0xA89884),
    'banded_snail': dict(shell='banded', body=0xD8CBAA, groove=0x7A6A50, sole=0xEAE0C8),
    'moss_snail': dict(shell='moss', body=0x6E6A4E, groove=0x2E2E1E, sole=0x8A8668),
    'amethyst_snail': dict(shell='amethyst', body=0x9C90B4, groove=0x4A3E60, sole=0xC0B8D4),
    'magma_snail': dict(shell='magma', body=0x3A302E, groove=0x120C0C, sole=0x4A3C38, glow=0xFF6A1A),
}


def snail(sid):
    cfg = SNAILS[sid]
    sk = skin(cfg['body'], cfg['groove'], sole=cfg['sole'], glow=cfg.get('glow'), glow_amt=0.9 if cfg.get('glow') else 0)
    eye = eye_mat(base=0x141010, glossy=1.0)
    stalk = skin(cfg['body'], cfg['groove'], freq=1.0, seed=7)
    # foot: front rounded, long tapering tail, flat sole
    body = Part('body', (0, GROUND, 0), (0, 0, 0))
    body.add(ellipsoid((0, -3, -3), (5.5, 3.5, 11), sk, axis='z', step=2, flat=0))
    body.add(ellipsoid((0, -2, 9), (4.5, 2.5, 10), sk, axis='z', step=2, flat=0))
    body.add(ellipsoid((0, -0.5, 1), (6.5, 0.5, 15), sk, axis='z', step=3, flat=0))   # skirt / sole
    # head + neck rising at the front
    head = Part('head', (0, GROUND, -12), (0, 0, 0))
    head.add(ellipsoid((0, -5.5, -1.5), (4, 5.5, 3.6), sk, axis='y', step=1))
    head.add(ellipsoid((0, -1.5, -3.5), (3.2, 1.5, 2.2), sk, axis='y', step=1))   # snout / lips
    for side, nm in ((1, 'l'), (-1, 'r')):
        st = Part('eyestalk_' + nm, (side * 1.8, -10, -2.5), (-0.5, 0, side * 0.32))
        st.add(rod('y', 0, 6, 2, stalk, sign=-1), rod('y', -5.5, 5, 1, stalk, sign=-1))
        st.add(ellipsoid((0, -11.3, 0), (1.4, 1.5, 1.4), eye, axis='y', step=1))
        tent = Part('tentacle_' + nm, (side * 1.6, -2.5, -4.5), (0.9, -side * 0.5, 0))
        tent.add(rod('y', 0, 3, 1, stalk, sign=-1))
        head.add(st, tent)
    # shell on the back, coil axis sideways
    kind = cfg['shell']
    shell = Part('shell', (0, GROUND - 6, 4), (-0.18, 0, 0))
    sm = shell_mat(kind, (-8.5, 1.0), 10.5)
    shell.add(ellipsoid((0, -9, 0.5), (6.5, 9.5, 10), sm, axis='x', step=1))
    shell.add(ellipsoid((4.2, -9.5, 1.5), (3.0, 4.5, 4.5), sm, axis='x', step=1))   # spire
    shell.add(ellipsoid((-4.0, -8.5, 0.5), (2.5, 6.5, 7.0), sm, axis='x', step=1))  # umbilical side
    if kind == 'amethyst':
        cm = crystal_mat()
        for i, (x, y, z, rx, rz, h) in enumerate(((0, -17.5, -1, 0.25, 0.1, 7), (3, -16, 4, -0.3, -0.35, 5),
                                                   (-2.5, -16, 3, 0.2, 0.45, 5), (1, -15, -6, 0.6, -0.2, 4))):
            cr = Part('crystal_%d' % i, (x, y, z), (rx, 0, rz))
            cr.add(Cube(-1, -h, -1, 2, h, 2, cm, Ell((0, -h / 2, 0), (1, h / 2, 1))))
            cr.add(Cube(-1.5, -h * 0.6, -0.5, 3, h * 0.6, 1, cm, Ell((0, -h / 2, 0), (1.5, h / 2, 1))))
            cr.add(Cube(-0.5, -h - 1, -0.5, 1, 1, 1, cm, Ell((0, -h, 0), (0.5, 1, 0.5))))
            shell.add(cr)
    if kind == 'moss':
        mm = moss_tuft_mat()
        for i, (x, y, z, rx, ry, rz) in enumerate(((0, -18, 0, 4, 1.5, 4), (3, -16, 5, 3, 1.5, 3), (-3, -15, -4, 3, 1.5, 3))):
            shell.add(ellipsoid((x, y, z), (rx, ry, rz), mm, axis='y', step=1))
        sprout = Part('sprout', (0, -19, 1), (0.1, 0.5, 0))
        sprout.add(rod('y', 0, 4, 1, mm, sign=-1), Cube(-1, -5, -1, 2, 1, 2, moss_tuft_mat()))
        shell.add(sprout)
    return Model(sid, [body, head, shell], glow=(kind in ('amethyst', 'magma')))


# ===========================================================================
# CRABS
# ===========================================================================

def carapace_mat(kind):
    def m(c):
        nz = fbm(c.p * 0.25, 4, 101)
        f1, f2, cid = worley(c.p * 0.8, 103)
        gran = smooth(0.35, 0.1, f1)
        if kind == 'rock':
            col = mix(C(0x9E2A22), C(0xC8483A), smooth(-0.5, 0.6, nz))
            col = mix(col, C(0x6A1E2E), smooth(0.2, 0.55, fbm(c.p * 0.5, 3, 107)) * 0.6)
            col = mix(col, C(0xF07A5A), gran * 0.45)
            rim = smooth(0.75, 0.98, np.sqrt(c.q[:, 0] ** 2 + c.q[:, 2] ** 2)) if c.q is not None else 0
            col = mix(col, C(0x5A1414), rim * 0.5)
            spec = 0.35
        else:
            col = mix(C(0x1E4A5E), C(0x2E6E86), smooth(-0.5, 0.6, nz))
            speck = smooth(0.62, 0.72, fbm(c.p * 1.4, 2, 109))
            col = mix(col, C(0x8FE0F0), speck * 0.8)
            hgroove = smooth(0.08, 0.0, np.abs(np.abs(c.q[:, 0]) - 0.3)) * smooth(0.6, 0.2, np.abs(c.q[:, 2]))
            col = mix(col, C(0x0E2430), hgroove * 0.7)
            col = mix(col, C(0x6ACADA), gran * 0.25)
            spec = 0.4
        o = Out(c.N, col)
        under = np.clip(c.n[:, 1], 0, 1)
        o.col = mix(o.col, C(0xE8D2B4) if kind == 'rock' else C(0xC8C0A8), smooth(0.2, 0.7, under))
        o.spec[:] = spec
        o.shin[:] = 30
        return o
    return m


def crab_leg_mat(kind):
    base = 0xB0362A if kind == 'rock' else 0x7A3A30

    def m(c):
        o = chitin(base, spec=0.35, shin=30, var=0.15, freq=0.6, seed=111)(c)
        t = c.q[:, 0]
        band = smooth(0.12, 0.0, t) + smooth(0.88, 1.0, t)
        o.col = mix(o.col, C(0xF0C8A0) if kind == 'rock' else C(0xD8B890), np.clip(band, 0, 1) * 0.6)
        hair = smooth(0.6, 0.8, fbm(c.p * 2.0, 2, 113))
        o.col = mix(o.col, o.col * 0.6, hair * 0.5)
        return o
    return m


def claw_mat(kind, big=False):
    def m(c):
        if kind == 'rock':
            o = chitin(0xB8382C, spec=0.5, shin=40, var=0.15, freq=0.4, seed=121)(c)
            tip = smooth(0.3, 0.9, -c.q[:, 2]) if c.q is not None else 0
            o.col = mix(o.col, C(0x1A1010), tip)
        else:
            if big:
                o = chitin(0xF0A030, spec=0.6, shin=45, var=0.12, freq=0.3, seed=123)(c)
                o.col = mix(o.col, C(0xFFF0D8), smooth(0.2, 0.8, c.q[:, 1]) * 0.6)
                f1, _, _ = worley(c.p * 0.9, 125)
                o.col = mix(o.col, C(0xFFE8C0), smooth(0.3, 0.05, f1) * 0.6)
            else:
                o = chitin(0x8A4A38, spec=0.4, shin=30, var=0.12, freq=0.5, seed=127)(c)
        return o
    return m


def crab(sid):
    kind = 'rock' if sid == 'rock_crab' else 'fiddler'
    cm = carapace_mat(kind)
    lm = crab_leg_mat(kind)
    tip = chitin(0x1A1210, spec=0.6, shin=50, var=0.05)
    eye = eye_mat(base=0x101014, glossy=1.0)
    by = GROUND - 6.5
    body = Part('body', (0, by, 0), (0, 0, 0))
    rx, rz = (13.0, 10.0) if kind == 'rock' else (11.0, 8.0)
    body.add(ellipsoid((0, 0, 0), (rx, 5.5, rz), cm, axis='y', step=1, half='neg'))
    body.add(ellipsoid((0, 0, 0.5), (rx - 1.5, 3.0, rz - 1.5), cm, axis='y', step=1, half='pos'))
    # frontal margin and anterolateral teeth
    body.add(box(-4, -2.5, -rz - 0.5, 8, 2, 2, cm))
    if kind == 'rock':
        for side in (1, -1):
            for i, ang in enumerate((0.45, 0.75, 1.05, 1.35)):
                x = side * rx * math.sin(ang) * 0.98
                z = -rz * math.cos(ang) * 0.98
                body.add(box(x - 1, -2.5, z - 1, 2, 2, 2, tip if i == 3 else cm))
    # mouthparts
    body.add(box(-3, 1, -rz + 0.5, 6, 3, 2, chitin(0xD8B898, spec=0.2, var=0.15, freq=0.8)))
    stalk_len = 4 if kind == 'rock' else 8
    for side, nm in ((1, 'l'), (-1, 'r')):
        es = Part('eyestalk_' + nm, (side * 3.0, -3.5, -rz + 1.5), (-0.25, 0, side * 0.18))
        es.add(rod('y', 0, stalk_len, 2, lm, sign=-1))
        es.add(ellipsoid((0, -stalk_len - 1.2, 0), (1.6, 1.8, 1.6), eye, axis='y', step=1))
        body.add(es)
    parts = [body]
    zs = (-4.5, -1.0, 2.5, 6.0)
    spreads = (0.55, 0.18, -0.2, -0.55)
    lens = ((9, 9, 4), (10, 10, 4), (10, 10, 4), (9, 9, 4))
    for i, z in enumerate(zs):
        f, t, ta = lens[i]
        for side in (1, -1):
            parts.append(leg(side, i, (rx - 2.5, by + 0.5, z), spreads[i], f, t, ta, lm, up=0.55, foot=0.9,
                             thick=(3, 2, 1), tars_mat=tip))
    # claws
    for side, nm in ((1, 'l'), (-1, 'r')):
        big = kind == 'fiddler' and side < 0
        small = kind == 'fiddler' and side > 0
        clm = claw_mat(kind, big)
        arm = Part('claw_' + nm, (side * (rx - 4), by + 1, -rz + 2), (0.15, side * 0.35, side * 0.2))
        arm.add(rod('z', 0, 6, 3 if not small else 2, clm, sign=-1))
        hand = Part('hand_' + nm, (0, 0, -6), (0, -side * 0.3, 0))
        if small:
            hand.add(ellipsoid((0, 0, -2), (1.2, 1.5, 2), clm, axis='z', step=1))
            hand.add(rod('z', -3.5, 2, 1, tip, sign=-1))
            finger = Part('pincer_' + nm, (0, -1, -3.5), (0.1, 0, 0), [rod('z', 0, 2.5, 1, tip, sign=-1)])
        elif big:
            hand.add(ellipsoid((0, 0, -6), (4.5, 6, 7.5), clm, axis='z', step=1))
            hand.add(box(-1.5, 2, -24, 3, 3, 11, clm))           # fixed finger
            hand.add(box(-1, 1.5, -24, 2, 1, 10, chitin(0xFFF6E8, spec=0.6)))  # teeth
            finger = Part('pincer_' + nm, (0, -3, -13), (0.12, 0, 0), [box(-1.5, -1.5, -12, 3, 3, 12, clm),
                                                                     box(-1, 1.5, -11, 2, 1, 10, chitin(0xFFF6E8, spec=0.6))])
        else:
            hand.add(ellipsoid((0, 0, -3.5), (2.6, 3.2, 4.5), clm, axis='z', step=1))
            hand.add(box(-1, 1, -12, 2, 2, 5, clm), box(-0.5, 1.5, -13, 1, 1, 2, tip))
            finger = Part('pincer_' + nm, (0, -1.5, -7.5), (0.12, 0, 0), [box(-1, -1, -5, 2, 2, 5, clm),
                                                                        box(-0.5, -0.5, -6, 1, 1, 1, tip)])
        hand.add(finger)
        arm.add(hand)
        parts.append(arm)
    return Model(sid, parts)


# ===========================================================================
# SMALL INSECTS
# ===========================================================================

def firefly():
    shield = None

    def shield_mat(c):
        q = c.q
        r = np.sqrt(q[:, 0] ** 2 + q[:, 2] ** 2)
        col = mix(C(0xE8C850), C(0xF0A0A0), smooth(0.2, 0.6, r))
        col = mix(col, C(0xD02A20), smooth(0.45, 0.3, r))
        col = mix(col, C(0x141010), smooth(0.2, 0.1, np.abs(q[:, 0])) * smooth(0.5, 0.2, r))
        o = Out(c.N, col)
        o.spec[:] = 0.5
        o.shin[:] = 30
        return o

    def elytron_mat(c):
        q = c.q
        col = C(0x2A2420) * (1 + 0.15 * fbm(c.p * 0.8, 3, 131))[:, None]
        margin = np.maximum(smooth(0.6, 0.85, np.abs(q[:, 0])), smooth(-0.15, 0.1, q[:, 1]) * 0)
        col = mix(col, C(0xE8D488), margin)
        o = Out(c.N, col)
        o.spec[:] = 0.3
        o.shin[:] = 20
        return o

    def abdomen_mat(c):
        q = c.q
        seg = smooth(0.4, 0.5, np.abs(fract(c.z * 0.55) - 0.5) * 2)
        col = mix(C(0x3A2A24), C(0x5A3E30), seg)
        lantern = smooth(0.15, 0.35, q[:, 2]) * smooth(-0.3, 0.2, q[:, 1])
        col = mix(col, C(0xF4F0A0), lantern)
        o = Out(c.N, col)
        o.add_glow(C(0xE8FF60), lantern)
        o.flat = lantern
        o.spec[:] = 0.3
        return o

    dark = chitin(0x231C18, spec=0.4, var=0.1, freq=0.8)
    by = GROUND - 6
    body = Part('body', (0, by, 0))
    body.add(ellipsoid((0, -1.0, -6), (4.2, 2.0, 3.6), shield_mat, axis='y', step=1, half='neg'))
    body.add(ellipsoid((0, 0.8, 3), (3.0, 1.8, 7.5), abdomen_mat, axis='y', step=1))
    body.add(ellipsoid((0, 0, -2), (2.4, 1.6, 2.5), dark, axis='y', step=1))
    head = Part('head', (0, 0.5, -7), (0.2, 0, 0))
    head.add(ellipsoid((0, 0, -1.5), (2.0, 1.5, 1.8), dark, axis='y', step=1))
    for side in (1, -1):
        head.add(ellipsoid((side * 1.8, -0.2, -1.5), (1.1, 1.2, 1.2), eye_mat(base=0x1A1A1A, compound=True), axis='y', step=1))
    for side, nm in ((1, 'l'), (-1, 'r')):
        a, _ = chain('antenna_' + nm, 8, 1.2, 1, dark, bend=(0.04, -side * 0.04, 0), first_rot=(-0.5, -side * 0.45, 0),
                     pivot=(side * 0.8, -0.8, -3))
        head.add(a)
    body.add(head)
    for side, nm in ((1, 'l'), (-1, 'r')):
        el = Part('elytron_' + nm, (side * 0.2, -1.6, -3), (0, 0, 0))
        el.add(ellipsoid((side * 1.9, 0, 6), (2.1, 1.4, 6.8), elytron_mat, axis='y', step=1, half='neg'))
        body.add(el)
        wg = Part('wing_' + nm, (side * 0.5, -1.0, -2), (0, side * -0.12, 0))
        wg.add(plane('xz', 0 if side > 0 else -8, 0, 0, 8, 13, wing_mat(outline=ellipse_outline(0.5, 0.5, 0.5, 0.5))))
        body.add(wg)
    parts = [body]
    for i, z in enumerate((-4.0, -1.5, 1.0)):
        for side in (1, -1):
            parts.append(leg(side, i, (1.5, by + 1, z), (0.6, 0.0, -0.6)[i], 3, 4, 2, dark, up=0.6, foot=0.3, thick=(1, 1, 1)))
    return Model('firefly', parts, glow=True)


def cricket():
    def body_mat(c):
        o = chitin(0x2A1C12, spec=0.75, shin=60, var=0.12, freq=0.4, seed=141)(c)
        return o

    def abd_mat(c):
        o = chitin(0x30200F, spec=0.5, shin=40, var=0.12, freq=0.4, seed=143)(c)
        seg = smooth(0.35, 0.5, np.abs(fract(c.z * 0.5) - 0.5) * 2)
        o.col = mix(o.col, o.col * 0.55, 1 - seg)
        return o

    def tegmen_mat(c):
        u, v = c.uv[:, 0], c.uv[:, 1]
        p = np.stack([u * 5, v * 9, u * 0], 1)
        f1, f2, _ = worley(p, 151)
        cells = smooth(0.0, 0.1, f2 - f1)
        col = mix(C(0x1A120A), C(0x6A4A2A), cells)
        col = mix(col, C(0x9A7448), smooth(0.85, 1.0, u) * 0.6)
        o = Out(c.N, col)
        o.alpha = 255 * ellipse_outline(0.5, 0.45, 0.52, 0.56)(u, v)
        o.spec[:] = 0.5
        o.shin[:] = 40
        return o

    def femur_mat(c):
        o = chitin(0x5A3A1E, spec=0.4, shin=30, var=0.1, freq=0.5, seed=145)(c)
        her = smooth(0.25, 0.4, np.abs(fract(c.z * 0.9 + np.abs(c.y) * 0.5) - 0.5))
        o.col = mix(o.col, C(0x2A1A0C), (1 - her) * 0.6)
        return o

    dark = chitin(0x1E140C, spec=0.5, var=0.1, freq=0.8, seed=147)
    by = GROUND - 6.5
    body = Part('body', (0, by, 0))
    body.add(ellipsoid((0, -0.5, -3.5), (3.8, 3.2, 3.2), body_mat, axis='y', step=1))     # pronotum
    body.add(ellipsoid((0, 0, 5), (3.6, 2.8, 8.5), abd_mat, axis='y', step=1))          # abdomen
    for side, nm in ((1, 'l'), (-1, 'r')):
        tg = Part('tegmen_' + nm, (0, -2.6 + (0.2 if side < 0 else 0), -1), (0, 0, side * 0.12))
        tg.add(plane('xz', 0 if side > 0 else -4.5, 0, 0, 4.5, 13, tegmen_mat))
        body.add(tg)
        ce, _ = chain('cercus_' + nm, 4, 2, 1, dark, bend=(0.05, 0, 0), first_rot=(-0.1, -side * (math.pi - 0.35), 0),
                      pivot=(side * 1.2, 0, 13))
        body.add(ce)
    head = Part('head', (0, -1.0, -6.5), (0.15, 0, 0))
    head.add(ellipsoid((0, 0, -2.5), (3.4, 3.6, 3.0), body_mat, axis='y', step=1))
    for side in (1, -1):
        head.add(ellipsoid((side * 2.9, -1.2, -3.0), (1.0, 1.4, 1.2), eye_mat(base=0x2A2018, compound=True), axis='y', step=1))
        head.add(rod('y', 2.5, 2, 1, dark, center=(side * 1.2, -5)))  # palps
    for side, nm in ((1, 'l'), (-1, 'r')):
        a, _ = chain('antenna_' + nm, 14, 1.8, 1, dark, bend=(0.05, -side * 0.07, 0), first_rot=(-0.55, -side * 0.3, 0),
                     pivot=(side * 1.2, -2.5, -4.5))
        head.add(a)
    body.add(head)
    parts = [body]
    for i, z in enumerate((-4.0, -1.0)):
        for side in (1, -1):
            parts.append(leg(side, i, (2.0, by + 1.5, z), (0.7, -0.05)[i], 4, 5, 3, dark, up=0.55, foot=0.3, thick=(2, 1, 1)))
    for side, nm in ((1, 'l'), (-1, 'r')):
        hl = Part('hindleg_' + nm, (side * 3.0, by + 0.5, 2.0), (0.5, side * 0.12, 0))
        hl.add(ellipsoid((0, 0, 5.5), (1.6, 2.4, 6.0), femur_mat, axis='z', step=2))
        shin = Part('shin_' + nm, (0, 0.5, 11), (-0.8, 0, 0))
        shin.add(rod('y', 0, 12, 1, dark))
        for k in range(4):
            shin.add(box(-0.5, 3 + k * 2.2, 0.3, 1, 1, 1, chitin(0xC8A070, spec=0.3)))  # spines
        shin.add(Part('foot_' + nm, (0, 12, 0), (0.3, 0, 0), [rod('z', 0, 3, 1, dark)]))
        hl.add(shin)
        parts.append(hl)
    return Model('cricket', parts)


def ant():
    red = chitin(0xA0381A, spec=0.45, shin=35, var=0.12, freq=0.6, seed=161, hair=0.2)
    black = chitin(0x1A1414, spec=0.85, shin=70, var=0.08, freq=0.5, seed=163, hair=0.15)

    def gaster_mat(c):
        o = black(c)
        seg = smooth(0.05, 0.0, np.abs(fract(c.z * 0.32 + 0.1) - 0.5) - 0.44)
        o.col = mix(o.col, C(0x6A5040), seg * 0.6)
        return o

    by = GROUND - 7
    body = Part('body', (0, by, 0))
    body.add(ellipsoid((0, -0.5, -2.5), (2.2, 2.2, 2.4), red, axis='y', step=1))
    body.add(ellipsoid((0, -0.8, 0.6), (1.7, 1.9, 1.9), red, axis='y', step=1))
    body.add(ellipsoid((0, -0.3, 3.0), (1.6, 1.6, 1.5), red, axis='y', step=1))
    body.add(ellipsoid((0, -1.2, 4.6), (0.9, 1.9, 0.8), red, axis='y', step=1))   # petiole node
    head = Part('head', (0, -1.0, -4.5), (0.15, 0, 0))
    head.add(ellipsoid((0, -0.3, -3.0), (3.1, 2.7, 3.0), black, axis='y', step=1))
    for side in (1, -1):
        head.add(ellipsoid((side * 2.7, -1.0, -3.2), (0.9, 1.1, 1.1), eye_mat(base=0x2A2626, compound=True), axis='y', step=1))
    for side, nm in ((1, 'l'), (-1, 'r')):
        mand = Part('mandible_' + nm, (side * 1.6, 1.2, -5.6), (0, side * 0.1, 0))
        mand.add(box(-0.5, -0.5, -3, 1, 1, 3, red), box(-side * 0.5 - 0.5, -0.5, -3.5, 1, 1, 1, black))
        head.add(mand)
        ant_root = Part('antenna_' + nm, (side * 1.2, -2.0, -5.0), (-0.9, -side * 0.55, 0))
        ant_root.add(rod('z', 0, 5, 1, red, sign=-1))
        fun, _ = chain('funiculus_' + nm, 6, 1.1, 1, black, bend=(0.05, 0, 0), first_rot=(1.3, side * 0.2, 0),
                       pivot=(0, 0, -5), club=(3, 1))
        ant_root.add(fun)
        head.add(ant_root)
    body.add(head)
    gas = Part('abdomen', (0, -0.8, 5.5), (-0.3, 0, 0))
    gas.add(ellipsoid((0, 0, 5), (3.8, 3.4, 5.4), gaster_mat, axis='y', step=1))
    body.add(gas)
    parts = [body]
    for i, z in enumerate((-2.5, 0.6, 2.6)):
        for side in (1, -1):
            parts.append(leg(side, i, (1.5, by + 0.5, z), (0.7, 0.0, -0.7)[i], 6, 7, 4, red, up=0.7, foot=0.25,
                             thick=(1, 1, 1), tars_mat=black))
    return Model('ant', parts)


# ===========================================================================
# BEETLES
# ===========================================================================

def elytra_mat(kind):
    def m(c):
        q = c.q
        qx, qy, qz = q[:, 0], q[:, 1], q[:, 2]
        nz = fbm(c.p * 0.4, 3, 201)
        suture = smooth(0.05, 0.0, np.abs(qx))
        stria = smooth(0.12, 0.0, np.abs(fract(qx * 5.0) - 0.5) - 0.38)
        punct = smooth(0.35, 0.2, np.abs(fract(qz * 14.0) - 0.5)) * stria
        margin = smooth(-0.25, 0.0, qy)
        spec, shin, metal = 0.6, 50, 0.0
        o_glow = None
        if kind == 'ladybug':
            col = mix(C(0xC81E12), C(0xE8401E), smooth(-0.5, 0.6, nz))
            spots = np.zeros(c.N)
            for sx, sz, r in ((0.0, -0.75, 0.32), (0.45, -0.35, 0.28), (-0.45, -0.35, 0.28), (0.55, 0.25, 0.3),
                              (-0.55, 0.25, 0.3), (0.3, 0.7, 0.24), (-0.3, 0.7, 0.24)):
                d = np.sqrt((qx - sx) ** 2 + (qz - sz) ** 2)
                spots = np.maximum(spots, smooth(r, r - 0.06, d + 0.03 * nz))
            col = mix(col, C(0x0E0A0A), spots)
            col = mix(col, C(0x0E0A0A), suture)
            spec, shin = 1.0, 70
        elif kind == 'stag':
            col = mix(C(0x3E2014), C(0x5E3420), smooth(-0.5, 0.6, nz))
            col = mix(col, C(0x1E100A), suture * 0.9 + margin * 0.5)
            col = col * (1 + 0.12 * fbm(c.p * np.array([0.3, 0.3, 2.0]), 2, 205))[:, None]   # silky sheen
            spec, shin = 0.55, 30
        elif kind == 'rhino':
            col = mix(C(0x4A2410), C(0x7A3E1C), smooth(-0.6, 0.7, nz))
            col = mix(col, C(0x1A0C06), suture * 0.9)
            spec, shin = 0.95, 80
        elif kind == 'dung':
            col = mix(C(0x121214), C(0x26262C), smooth(-0.5, 0.6, nz))
            col = mix(col, C(0x060608), stria * 0.8 + suture)
            spec, shin = 0.25, 15
        elif kind == 'jewel':
            col = mix(C(0x0E9A4A), C(0x34D07A), smooth(-0.6, 0.6, nz))
            stripe = smooth(0.08, 0.0, np.abs(np.abs(qx) - 0.55) - 0.12)
            col = mix(col, C(0xC8641E), stripe * 0.9)
            irid = 0.5 + 0.5 * np.sin(c.n[:, 0] * 5 + c.n[:, 2] * 4)
            col = mix(col, C(0xE8C83A), irid * 0.35)
            col = mix(col, col * 0.5, punct)
            spec, shin, metal = 1.0, 60, 0.9
        elif kind == 'scarab':
            col = mix(C(0xC89418), C(0xF8D868), smooth(-0.6, 0.6, nz))
            col = mix(col, C(0x9AC84A), (0.5 + 0.5 * np.sin(c.n[:, 0] * 4 - c.n[:, 2] * 3)) * 0.18)
            col = mix(col, C(0x8A6010), suture * 0.7 + stria * 0.25)
            spec, shin, metal = 1.4, 90, 1.0
        elif kind == 'bombardier':
            col = mix(C(0x14204A), C(0x2A3E7A), smooth(-0.5, 0.6, nz))
            col = mix(col, C(0x0A0E20), stria * 0.9 + suture)
            col = mix(col, C(0x4A6AC8), smooth(0.5, 0.9, -qy) * 0.25)
            spec, shin, metal = 0.7, 50, 0.4
        elif kind == 'weevil':
            col = mix(C(0x5E5040), C(0x8A7860), smooth(-0.5, 0.6, nz))
            scales = smooth(0.55, 0.75, fbm(c.p * 1.6, 2, 211))
            col = mix(col, C(0xE6DCC8), scales * 0.75)
            f1, _, _ = worley(c.p * 0.7, 213)
            col = col * (0.75 + 0.35 * smooth(0.0, 0.5, f1))[:, None]
            spec, shin = 0.15, 10
        elif kind == 'goliath':
            col = mix(C(0xE8E0CC), C(0xFFF8EC), smooth(-0.5, 0.6, nz))
            patch = smooth(0.05, 0.25, fbm(c.p * 0.18, 3, 221)) * smooth(0.3, 0.9, np.abs(qx))
            col = mix(col, C(0x2A1A10), patch * 0.85)
            col = mix(col, C(0x6A3A1A), suture)
            col = col * (1 + 0.08 * fbm(c.p * 3.0, 2, 223))[:, None]       # velvet
            spec, shin = 0.15, 8
        else:  # tiger
            col = mix(C(0x1E7A3A), C(0x3AA85A), smooth(-0.6, 0.6, nz))
            col = mix(col, C(0x6A4A2A), smooth(0.7, 0.95, np.abs(qx)) * 0.6 + margin * 0.4)
            spots = np.zeros(c.N)
            for sx, sz, r in ((0.6, -0.55, 0.16), (0.45, 0.05, 0.13), (0.6, 0.6, 0.15)):
                for sgn in (1, -1):
                    d = np.sqrt((qx - sgn * sx) ** 2 + (qz - sz) ** 2)
                    spots = np.maximum(spots, smooth(r, r - 0.05, d))
            col = mix(col, C(0xF2E8C0), spots)
            col = mix(col, col * 0.6, punct * 0.6)
            spec, shin, metal = 0.9, 60, 0.7
        o = Out(c.N, col)
        o.spec[:] = spec
        o.shin[:] = shin
        o.metal[:] = metal
        return o
    return m


def pronotum_mat(kind):
    def m(c):
        q = c.q
        qx, qz = q[:, 0], q[:, 2]
        nz = fbm(c.p * 0.5, 3, 231)
        if kind == 'ladybug':
            col = C(0x0E0A0A) * (1 + 0.1 * nz)[:, None]
            patch = smooth(0.1, 0.0, np.abs(np.abs(qx) - 0.75) - 0.18) * smooth(0.6, 0.2, qz + 0.3)
            col = mix(col, C(0xF4F0E4), patch)
            return _o(c, col, 1.0, 70)
        if kind == 'goliath':
            col = C(0xF0E8D8) * (1 + 0.06 * nz)[:, None]
            stripes = smooth(0.08, 0.0, np.minimum(np.abs(qx), np.abs(np.abs(qx) - 0.45)) - 0.08)
            col = mix(col, C(0x141010), stripes)
            return _o(c, col, 0.1, 8)
        if kind == 'bombardier':
            return _o(c, mix(C(0xD8601A), C(0xF08A2A), smooth(-0.5, 0.6, nz)), 0.5, 40)
        if kind == 'tiger':
            return _o(c, mix(C(0x1E6A4A), C(0x6A3A2A), smooth(0.4, 0.9, np.abs(qx))), 0.9, 60, 0.7)
        if kind == 'jewel':
            return _o(c, mix(C(0x0E6A8A), C(0x2AC07A), smooth(-0.5, 0.6, nz + qx)), 1.0, 60, 0.9)
        if kind == 'scarab':
            return _o(c, mix(C(0xD8A42A), C(0xFFE07A), smooth(-0.5, 0.6, nz)), 1.4, 90, 1.0)
        if kind == 'weevil':
            col = mix(C(0x5A4A3A), C(0x8A7860), smooth(-0.5, 0.6, nz))
            col = mix(col, C(0xE6DCC8), smooth(0.55, 0.75, fbm(c.p * 1.6, 2, 233)) * 0.7)
            return _o(c, col, 0.1, 10)
        if kind == 'dung':
            f1, _, _ = worley(c.p * 1.0, 235)
            col = C(0x18181C) * (0.8 + 0.3 * smooth(0, 0.4, f1))[:, None]
            return _o(c, col, 0.3, 15)
        if kind == 'rhino':
            return _o(c, mix(C(0x2A140A), C(0x4A2410), smooth(-0.5, 0.6, nz)), 1.0, 80)
        return _o(c, mix(C(0x160E0A), C(0x2A1A12), smooth(-0.5, 0.6, nz)), 0.6, 40)  # stag
    return m


def _o(c, col, spec, shin, metal=0.0):
    o = Out(c.N, col)
    o.spec[:] = spec
    o.shin[:] = shin
    o.metal[:] = metal
    return o


BEETLES = {
    #            elytra (W, H, L)   pron (w, h, l)   head (w, h, l)   clearance  legs (f, t, ta)  antenna
    'ladybug': dict(el=(15, 7, 15), pr=(12, 3.5, 4.5), hd=(7, 3, 3), base=2.0, legs=(4, 4, 3), ant=('club', 4),
                    leg=0x141010, head=0x141010),
    'stag_beetle': dict(el=(16, 5, 22), pr=(15, 4, 7), hd=(16, 4, 7), base=4.0, legs=(7, 8, 5), ant=('elbow', 7),
                        leg=0x140C08, head=0x1C120C),
    'rhinoceros_beetle': dict(el=(19, 9, 22), pr=(17, 7, 10), hd=(8, 4, 5), base=4.0, legs=(7, 8, 5), ant=('lamellate', 3),
                              leg=0x1E0E06, head=0x2A140A),
    'dung_beetle': dict(el=(17, 6, 15), pr=(17, 5, 8), hd=(15, 2, 6), base=3.5, legs=(6, 7, 4), ant=('lamellate', 3),
                        leg=0x101012, head=0x141418),
    'jewel_beetle': dict(el=(13, 5, 24), pr=(11, 3.5, 6), hd=(8, 3, 3), base=3.0, legs=(5, 6, 4), ant=('filiform', 7),
                         leg=0x0E4A3A, head=0x0E6A5A),
    'scarab_beetle': dict(el=(17, 8, 17), pr=(15, 5, 7), hd=(9, 3, 4), base=3.0, legs=(5, 6, 4), ant=('lamellate', 3),
                          leg=0x9A6E14, head=0xC8962A),
    'bombardier_beetle': dict(el=(12, 4.5, 15), pr=(9, 3, 6), hd=(7, 3, 5), base=3.0, legs=(5, 6, 4), ant=('filiform', 10),
                              leg=0xD8661E, head=0xE07A22),
    'weevil': dict(el=(12, 9, 14), pr=(9, 5, 7), hd=(5, 4, 4), base=3.5, legs=(5, 6, 3), ant=('elbow', 5),
                   leg=0x4A3E30, head=0x5A4A3A),
    'goliath_beetle': dict(el=(22, 7, 26), pr=(20, 5, 11), hd=(10, 4, 5), base=5.0, legs=(9, 10, 6), ant=('lamellate', 4),
                           leg=0x141010, head=0x1A1414),
    'tiger_beetle': dict(el=(12, 4, 15), pr=(8, 3, 5), hd=(11, 4, 5), base=5.5, legs=(8, 10, 6), ant=('filiform', 10),
                         leg=0x6A4A2A, head=0x1E6A4A),
}
KIND = {'ladybug': 'ladybug', 'stag_beetle': 'stag', 'rhinoceros_beetle': 'rhino', 'dung_beetle': 'dung',
        'jewel_beetle': 'jewel', 'scarab_beetle': 'scarab', 'bombardier_beetle': 'bombardier', 'weevil': 'weevil',
        'goliath_beetle': 'goliath', 'tiger_beetle': 'tiger'}


def beetle(sid):
    cfg = BEETLES[sid]
    kind = KIND[sid]
    W, H, L = cfg['el']
    pw, ph, pl = cfg['pr']
    hw, hh, hl = cfg['hd']
    by = GROUND - cfg['base'] - 2.0       # bottom of the domes
    total = L + pl + hl
    z0 = -total / 2
    zh = z0 + hl / 2 + 0.5
    zp = z0 + hl + pl / 2 - 0.5
    ze = z0 + hl + pl + L / 2 - 1.0
    em = elytra_mat(kind)
    pm = pronotum_mat(kind)
    legm = chitin(cfg['leg'], spec=0.6, shin=40, var=0.1, freq=0.8, seed=241, hair=0.1, dark_joints=0x0A0808)
    if kind in ('scarab', 'jewel', 'tiger'):
        legm = chitin(cfg['leg'], spec=1.0, shin=60, var=0.1, freq=0.8, seed=241, metal=0.8)
    headm = chitin(cfg['head'], spec=0.6, shin=40, var=0.1, freq=0.6, seed=243, pits=0.3 if kind in ('dung', 'weevil') else 0)
    if kind in ('scarab', 'jewel', 'tiger'):
        headm = pm
    under = chitin(0x141010 if kind not in ('scarab',) else 0x7A5A10, spec=0.4, var=0.1, freq=0.6, seed=245)

    body = Part('body', (0, by, 0))
    body.add(ellipsoid((0, 0, ze), (W / 2, H, L / 2), em, axis='y', step=1, half='neg'))
    body.add(ellipsoid((0, 0, ze - 1), (W / 2 - 1.5, 2.0, L / 2 - 1.5), under, axis='y', step=1, half='pos'))
    body.add(ellipsoid((0, 0.5, zp), (pw / 2, ph, pl / 2), pm, axis='y', step=1, half='neg'))
    body.add(ellipsoid((0, 0.5, zp), (pw / 2 - 1, 1.5, pl / 2 - 0.5), under, axis='y', step=1, half='pos'))
    body.add(box(-1, -H * 0.85, ze - L / 2 - 0.5, 2, 1, 2, em))     # scutellum

    head = Part('head', (0, -ph * 0.3, zp - pl / 2 + 0.5), (0.12, 0, 0))
    hcz = -hl / 2
    head.add(ellipsoid((0, 0, hcz), (hw / 2, hh / 2, hl / 2 + 0.5), headm, axis='y', step=1))
    eye = eye_mat(base=0x1A1612 if kind != 'tiger' else 0x3A4A2A, compound=True)
    er = 1.2 if kind != 'tiger' else 2.0
    for side in (1, -1):
        head.add(ellipsoid((side * (hw / 2 - 0.3), -hh * 0.15, hcz - hl * 0.1), (er, er, er * 1.1), eye, axis='y', step=1))
    # antennae
    akind, alen = cfg['ant']
    for side, nm in ((1, 'l'), (-1, 'r')):
        pv = (side * (hw / 2 - 1.0), -hh * 0.3, -hl + 0.5)
        if akind == 'club':
            a, _ = chain('antenna_' + nm, 5, 1.0, 1, legm, bend=(0.1, -side * 0.08, 0), first_rot=(-0.3, -side * 0.7, 0), pivot=pv, club=(2, 2))
        elif akind == 'filiform':
            a, _ = chain('antenna_' + nm, alen, 1.6, 1, legm, bend=(0.06, -side * 0.03, 0), first_rot=(-0.45, -side * 0.45, 0), pivot=pv)
        elif akind == 'elbow':
            a = Part('antenna_' + nm, pv, (-0.5, -side * 0.7, 0), [rod('z', 0, alen * 0.6, 1, legm, sign=-1)])
            f, _ = chain('funiculus_' + nm, 4, 1.2, 1, legm, bend=(0.05, 0, 0), first_rot=(0.2, side * 0.9, 0),
                         pivot=(0, 0, -alen * 0.6), club=(2, 2))
            a.add(f)
        else:  # lamellate fan
            a = Part('antenna_' + nm, pv, (-0.2, -side * 0.6, 0), [rod('z', 0, alen, 1, legm, sign=-1)])
            for k in range(3):
                a.add(Part('lamella_%s%d' % (nm, k), (0, 0, -alen), (0, side * (k - 1) * 0.35, 0),
                           [box(-0.5, -1, -3, 1, 2, 3, chitin(0x8A6A3A, spec=0.3))]))
        head.add(a)
    _beetle_extras(kind, head, hw, hh, hl, legm, headm, pm)
    body.add(head)

    parts = [body]
    lx = min(W, pw) * 0.24
    zs = (zp, ze - L * 0.32, ze - L * 0.12)
    f, t, ta = cfg['legs']
    for i, z in enumerate(zs):
        for side in (1, -1):
            parts.append(leg(side, i, (lx, by + 1.0, z), (0.65, -0.05, -0.65)[i], f + (i == 2), t + (i == 2), ta, legm,
                             up=0.55, foot=0.3, thick=(2 if f >= 6 else 1, 1, 1)))
    return Model(sid, parts)


def _beetle_extras(kind, head, hw, hh, hl, legm, headm, pm):
    if kind == 'stag':
        jaw = chitin(0x7A3418, spec=0.8, shin=60, var=0.12, freq=0.5, seed=251)
        for side, nm in ((1, 'l'), (-1, 'r')):
            root, segs = chain('mandible_' + nm, 4, 4.0, 2, jaw, bend=(0, side * 0.22, 0), first_rot=(-0.2, -side * 0.45, 0),
                               pivot=(side * (hw / 2 - 2), 0.5, -hl))
            segs[1].add(box(-side * 2 - 0.5, -0.5, -2, 2, 1, 1, jaw))       # inner tooth
            segs[3].add(box(-0.5, -2, -3.5, 1, 2, 1, jaw), box(-0.5, 0, -3.5, 1, 1, 1, jaw))  # forked tip
            head.add(root)
    elif kind == 'rhino':
        horn = chitin(0x1E0E06, spec=1.0, shin=80, var=0.08, freq=0.5, seed=253)
        root, segs = chain('horn', 6, 3.2, 3, horn, bend=(-0.32, 0, 0), first_rot=(-0.25, 0, 0), pivot=(0, -0.5, -hl + 0.5))
        segs[-1].add(box(-1.5, -1, -2, 3, 1, 1, horn))
        head.add(root)
    elif kind == 'dung':
        rake = chitin(0x141418, spec=0.3)
        for k in range(5):
            ang = (k - 2) * 0.35
            head.add(box(math.sin(ang) * hw / 2 - 1, -0.5, -hl - 1.5 + abs(k - 2) * 0.6, 2, 1, 2, rake))
    elif kind == 'scarab':
        head.add(box(-3, -0.5, -hl - 1, 6, 1, 1.5, headm))
    elif kind == 'weevil':
        sn = chitin(0x5A4A3A, spec=0.15, var=0.15, freq=1.0, seed=255, pits=0.3)
        root, segs = chain('snout', 4, 2.2, 2, sn, bend=(0.18, 0, 0), first_rot=(0.35, 0, 0), pivot=(0, 0.3, -hl))
        head.add(root)
    elif kind == 'goliath':
        horn = chitin(0x141010, spec=0.5, shin=40)
        y = Part('horn', (0, -hh / 2, -hl + 0.5), (-0.7, 0, 0), [box(-1, -1, -4, 2, 2, 4, horn)])
        for side in (1, -1):
            y.add(Part('horn_tip_%d' % (side + 1), (0, 0, -4), (0, -side * 0.6, 0), [box(-0.5, -0.5, -3, 1, 1, 3, horn)]))
        head.add(y)
    elif kind == 'tiger':
        jaw = chitin(0xF4ECD0, spec=0.6, shin=40)
        for side, nm in ((1, 'l'), (-1, 'r')):
            root, segs = chain('mandible_' + nm, 3, 2.0, 1, jaw, bend=(0, side * 0.45, 0), first_rot=(0.1, -side * 0.3, 0),
                               pivot=(side * 1.8, 0.8, -hl))
            segs[0].add(box(-side * 1 - 0.5, -0.5, -2, 1, 1, 1, jaw))
            head.add(root)


# ===========================================================================
# FISH
# ===========================================================================

def fish_body_mat(kind):
    def m(c):
        q = c.q
        qy, qz = q[:, 1], q[:, 2]
        nz = fbm(c.p * 0.3, 3, 301)
        # overlapping scales on the flanks
        row = np.floor(c.y / 1.8)
        sx = c.z / 2.2 + row * 0.5
        lu, lv = fract(sx), fract(c.y / 1.8)
        rim = smooth(0.75, 0.95, np.sqrt((lu - 0.15) ** 2 + (lv - 0.5) ** 2) * 1.3)
        belly = smooth(-0.1, 0.7, qy)
        back = smooth(0.1, -0.7, qy)
        spec, shin, metal = 0.7, 50, 0.6
        if kind == 'goldfish':
            col = mix(C(0xF07818), C(0xFFA83A), smooth(-0.5, 0.6, nz))
            col = mix(col, C(0xC84A0A), back * 0.6)
            col = mix(col, C(0xFFE8C0), belly * 0.85)
            col = mix(col, col * 1.25 + 15, (1 - rim) * 0.35)
            col = mix(col, col * 0.7, rim * 0.6)
        elif kind == 'betta':
            col = mix(C(0x1A2A9A), C(0x3A5AE8), smooth(-0.5, 0.6, nz))
            col = mix(col, C(0xA0142A), smooth(0.2, 0.9, qz) * 0.7 + belly * 0.3)
            col = mix(col, C(0x6AE0FF), (1 - rim) * 0.35 * (0.5 + 0.5 * np.sin(c.z * 1.3 + c.y)))
            col = mix(col, col * 0.6, rim * 0.5)
        elif kind == 'neon_tetra':
            col = mix(C(0x8A9A8A), C(0xC8D4D0), belly)
            col = mix(col, C(0x5A6A4A), back * 0.8)
            stripe = smooth(0.18, 0.08, np.abs(qy + 0.12)) * smooth(-0.95, -0.75, qz) * smooth(0.65, 0.45, qz)
            red = smooth(0.0, 0.25, qy) * smooth(-0.25, 0.0, qz)
            col = mix(col, C(0xE01A2A), red * 0.95)
            col = mix(col, C(0x20E0FF), stripe)
            col = mix(col, col * 0.85, rim * 0.3)
        else:  # angelfish
            col = mix(C(0xC8C8C0), C(0xF2F2EA), smooth(-0.5, 0.6, nz))
            col = mix(col, C(0xD8C890), back * 0.35)
            bands = np.zeros(c.N)
            for bz, bw in ((-0.62, 0.07), (-0.15, 0.1), (0.35, 0.1), (0.75, 0.07)):
                bands = np.maximum(bands, smooth(bw, bw - 0.04, np.abs(qz - bz + 0.08 * qy)))
            col = mix(col, C(0x161616), bands * 0.92)
            col = mix(col, col * 1.15, (1 - rim) * 0.2)
            spec, shin, metal = 0.9, 60, 0.3
        # lateral line + gill cover
        lateral = smooth(0.06, 0.0, np.abs(qy + 0.1)) * smooth(0.3, 0.9, np.abs(np.sin(c.z * 1.6)))
        col = mix(col, col * 0.7, lateral * 0.5)
        o = Out(c.N, col)
        o.spec[:] = spec
        o.shin[:] = shin
        o.metal[:] = metal
        if kind == 'neon_tetra':
            o.add_glow(C(0x20D8FF), stripe)
        return o
    return m


def fish_head_mat(kind):
    body = fish_body_mat(kind)

    def m(c):
        o = body(c)
        gill = smooth(0.35, 0.0, np.abs(c.z + 0.8 + 0.25 * c.q[:, 1] ** 2))
        o.col = mix(o.col, o.col * 0.55, gill * 0.6)
        lips = smooth(-0.75, -0.95, c.q[:, 2])
        o.col = mix(o.col, o.col * 0.8 + 20, lips * 0.4)
        return o
    return m


def fan(lobed=False, fork=False, pointy=None):
    def f(u, v):
        # u: 0 at fin base -> 1 at tip, v: 0..1 across
        half = 0.25 + 0.75 * smooth(0.0, 0.7, u)
        d = np.abs(v - 0.5) * 2
        a = smooth(half, half - 0.12, d)
        if fork:
            notch = smooth(0.25, 0.0, np.abs(v - 0.5)) * smooth(0.55, 0.95, u)
            a = a * (1 - notch)
        if lobed:
            a = a * smooth(1.0, 0.85, u + 0.12 * np.cos(v * 12))
        else:
            a = a * smooth(1.0, 0.9, u)
        return a
    return f


def sail(rake=0.5):
    def f(u, v):
        # triangular fin: tall at the front (u=0), trailing back; v=1 at the base
        top = 1.0 - (1 - u) * 1.0 - rake * 0
        edge = (1 - v) < (1.0 - u * 0.75) * 1.0
        a = np.where(edge, 1.0, 0.0) * smooth(1.0, 0.92, u)
        return a
    return f


def fish(sid):
    kind = sid
    bm = fish_body_mat(kind)
    hm = fish_head_mat(kind)
    eye = eye_mat(iris=0xD8A830 if kind != 'neon_tetra' else 0x40A0E0, pupil=0x050505, base=0xC8C8C0,
                  out_axis=None, glossy=1.0)
    cy = GROUND - 8
    if kind == 'goldfish':
        R = (5.0, 7.5, 12)
        fin_c, fin_e, fin_a = 0xFF9A30, 0xFFE0A0, 190
    elif kind == 'betta':
        R = (3.6, 5.5, 11)
        fin_c, fin_e, fin_a = 0xB0142A, 0x2A3AD8, 215
    elif kind == 'neon_tetra':
        R = (2.6, 4.0, 10)
        fin_c, fin_e, fin_a = 0xD8E0E0, 0xF8F8F8, 80
    else:
        R = (2.6, 10.5, 9)
        fin_c, fin_e, fin_a = 0xD8D8D0, 0x303030, 190
    rx, ry, rz = R
    body = Part('body', (0, cy, 0))
    if kind == 'angelfish':
        body.add(ellipsoid((0, 0, 0), R, bm, axis='x', step=1))
    else:
        body.add(ellipsoid((0, 0, 0), R, bm, axis='z', step=2))
    head = Part('head', (0, 0, -rz + 3), (0, 0, 0))
    hr = (rx * 0.85, ry * (0.75 if kind != 'angelfish' else 0.45), 5.0)
    head.add(ellipsoid((0, ry * 0.08, -2.5), hr, hm, axis='z', step=1))
    er = max(1.4, ry * 0.22)
    if kind == 'neon_tetra':
        er = 1.6
    for side in (1, -1):
        e = eye_mat(iris=0xD8A830 if kind != 'neon_tetra' else 0x40B0F0, base=0xD8D8D0, out_axis=(side, 0, 0), ring=0.75)
        head.add(ellipsoid((side * (hr[0] - 0.6), -hr[1] * 0.25, -3.5), (er * 0.7, er, er), e, axis='y', step=1))
    body.add(head)
    # tail: peduncle + caudal fin
    tb = Part('tail_base', (0, 0, rz - 2), (0, 0, 0))
    tb.add(ellipsoid((0, 0, 2.5), (rx * 0.45, ry * 0.4, 4), bm, axis='z', step=1))
    tf = Part('tail_fin', (0, 0, 6), (0, 0, 0))
    fm = fin_mat(fin_c, fin_e, fin_a, outline=fan(lobed=kind in ('betta', 'goldfish'), fork=kind in ('neon_tetra', 'goldfish', 'angelfish')),
                 ray_origin=(0.0, 0.5), rays=16, iridescent=0x6AE0FF if kind == 'betta' else None,
                 stripes=(0x1A1A1A, 2.5, 0) if kind == 'angelfish' else None)
    th = {'goldfish': 22, 'betta': 32, 'neon_tetra': 10, 'angelfish': 22}[kind]
    tl = {'goldfish': 16, 'betta': 22, 'neon_tetra': 7, 'angelfish': 12}[kind]
    if kind == 'goldfish':
        for k, yr in enumerate((0.35, -0.35)):
            tf.add(Part('tail_%s' % 'ab'[k], (0, 0, 0), (0, yr, 0), [plane('yz', 0, -th / 2, 0, tl, th, fm)]))
    else:
        tf.add(plane('yz', 0, -th / 2, 0, tl, th, fm))
    tb.add(tf)
    body.add(tb)
    # dorsal / anal fins
    dm = fin_mat(fin_c, fin_e, fin_a, outline=sail(), ray_origin=(0.0, 1.0), rays=10, grad_axis=1,
                 iridescent=0x6AE0FF if kind == 'betta' else None,
                 stripes=(0x1A1A1A, 2.5, 0) if kind == 'angelfish' else None)
    am = fin_mat(fin_c, fin_e, fin_a, outline=lambda u, v: sail()(u, 1 - v), ray_origin=(0.0, 0.0), rays=10, grad_axis=1,
                 iridescent=0x6AE0FF if kind == 'betta' else None,
                 stripes=(0x1A1A1A, 2.5, 0) if kind == 'angelfish' else None)
    dsz = {'goldfish': (14, 7), 'betta': (14, 10), 'neon_tetra': (5, 4), 'angelfish': (16, 20)}[kind]
    asz = {'goldfish': (8, 6), 'betta': (20, 12), 'neon_tetra': (6, 4), 'angelfish': (16, 20)}[kind]
    body.add(Part('dorsal_fin', (0, -ry + 0.8, -rz * 0.35), (0.25 if kind == 'angelfish' else 0.1, 0, 0),
                  [plane('yz', 0, -dsz[1], 0, dsz[0], dsz[1], dm)]))
    body.add(Part('anal_fin', (0, ry - 0.8, -rz * 0.1 if kind != 'goldfish' else rz * 0.3), (-0.25 if kind == 'angelfish' else -0.1, 0, 0),
                  [plane('yz', 0, 0, 0, asz[0], asz[1], am)]))
    if kind == 'neon_tetra':
        body.add(Part('adipose', (0, -ry + 1, rz * 0.6), (0, 0, 0), [plane('yz', 0, -2, 0, 2, 2, dm)]))
    # pectoral + pelvic fins
    pm_ = fin_mat(fin_c, fin_e, max(60, fin_a - 60), outline=fan(), ray_origin=(0.0, 0.5), rays=8)
    for side, nm in ((1, 'l'), (-1, 'r')):
        body.add(Part('fin_' + nm, (side * rx * 0.8, ry * 0.3, -rz * 0.45), (0, side * 0.7, side * 0.3),
                      [plane('yz', 0, -2, 0, 6 if kind != 'neon_tetra' else 3, 4 if kind != 'neon_tetra' else 2, pm_)]))
        if kind == 'angelfish':
            pel = fin_mat(0xE8E8E0, 0xF8F8F0, 200, outline=lambda u, v: smooth(1.0, 0.95, u))
            body.add(Part('pelvic_' + nm, (side * 0.8, ry * 0.75, -rz * 0.4), (-0.15, 0, side * 0.12),
                          [plane('yz', 0, 0, 0, 1, 18, pel)]))
        elif kind != 'neon_tetra':
            body.add(Part('pelvic_' + nm, (side * rx * 0.5, ry * 0.85, -rz * 0.05), (-0.5, 0, side * 0.35),
                          [plane('yz', 0, 0, 0, 5, 4, pm_)]))
    return Model(sid, [body], glow=(kind == 'neon_tetra'))


# ===========================================================================

def all_models():
    out = [snail(s) for s in ('garden_snail', 'banded_snail', 'moss_snail', 'amethyst_snail', 'magma_snail')]
    out += [crab('fiddler_crab'), crab('rock_crab')]
    out += [firefly(), cricket(), ant()]
    out += [beetle(b) for b in BEETLES]
    out += [fish(f) for f in ('goldfish', 'betta', 'neon_tetra', 'angelfish')]
    return out
