"""
Model + texture definitions for every critter in the mod.

Each critter is described once here as a tree of parts made of cuboids with a
paint "style".  generate_assets.py turns these into:
  * Java LayerDefinition code (CritterLayers.java), with UVs packed automatically
  * matching entity textures (and optional emissive "_glow" textures)

Coordinate system is the normal Minecraft entity-model one: units are 1/16 of a
block (before the renderer scale), +Y points DOWN, the ground is at y = 24 and
the front of the critter faces -Z.

Part names matter: CritterModel.java animates parts by name
(leg_l0.., leg_r0.., antenna_l/r, eyestalk_l/r, wing_l/r, claw_l/r,
pincer_l/r, hindleg_l/r, shin_l/r, tail_fin, fin_l/r, head, body, shell).
"""
import math


# ---------------------------------------------------------------------------
# Small helpers
# ---------------------------------------------------------------------------

def hexc(v):
    return ((v >> 16) & 255, (v >> 8) & 255, v & 255)


class Cube:
    def __init__(self, x, y, z, w, h, d, style, grow=0.0):
        self.x, self.y, self.z = x, y, z
        self.w, self.h, self.d = int(w), int(h), int(d)
        assert (self.w, self.h, self.d) == (w, h, d), "cube dims must be integers"
        self.style = style
        self.grow = grow
        self.u = self.v = 0


class Part:
    def __init__(self, name, pivot=(0, 0, 0), rot=(0, 0, 0), cubes=None, children=None):
        self.name = name
        self.pivot = pivot
        self.rot = rot
        self.cubes = cubes or []
        self.children = children or []

    def add(self, *children):
        self.children.extend(children)
        return self

    def all_parts(self):
        yield self
        for c in self.children:
            yield from c.all_parts()


def S(c, **kw):
    """Paint style: base colour plus overrides / patterns."""
    d = {'c': hexc(c) if isinstance(c, int) else c}
    for k, v in kw.items():
        if k in ('top', 'bottom', 'front', 'back', 'sides') and isinstance(v, int):
            v = hexc(v)
        d[k] = v
    return d


EYE = S(0x101010, eye=True)
BLACK = 0x161212


class Model:
    def __init__(self, sid, parts, scale, glow=False):
        self.id = sid
        self.parts = parts  # root-level parts
        self.scale = scale
        self.glow = glow

    def all_parts(self):
        for p in self.parts:
            yield from p.all_parts()


# ---------------------------------------------------------------------------
# Leg helper: a 1x1xL leg sticking out sideways, tilted so its tip touches the
# ground (y = 24).  side = +1 for the left (+x) leg, -1 for the right leg.
# ---------------------------------------------------------------------------

def leg(name, side, px, py, pz, length, spread, color, ground=24.0, thick=1):
    drop = ground - py - 0.5
    s = spread
    tilt = math.asin(max(-0.98, min(0.98, drop / (length * math.cos(s)))))
    if side > 0:
        cube = Cube(0, -thick / 2, -thick / 2, length, thick, thick, S(color))
        rot = (0, s, tilt)
    else:
        cube = Cube(-length, -thick / 2, -thick / 2, length, thick, thick, S(color))
        rot = (0, -s, -tilt)
    return Part(name, (side * px, py, pz), rot, [cube])


def six_legs(xs, py, zs, length, color, spreads=(0.55, -0.1, -0.6), lengths=None):
    parts = []
    for i, z in enumerate(zs):
        ln = lengths[i] if lengths else length
        parts.append(leg('leg_l%d' % i, 1, xs[i], py, z, ln, spreads[i], color))
        parts.append(leg('leg_r%d' % i, -1, xs[i], py, z, ln, spreads[i], color))
    return parts


def antennae(px, py, pz, length, color, up=-0.45, out=0.45, thick=1):
    l = Part('antenna_l', (px, py, pz), (up, out, 0),
             [Cube(-thick / 2, -thick / 2, -length, thick, thick, length, S(color))])
    r = Part('antenna_r', (-px, py, pz), (up, -out, 0),
             [Cube(-thick / 2, -thick / 2, -length, thick, thick, length, S(color))])
    return [l, r]


# ---------------------------------------------------------------------------
# SNAILS
# ---------------------------------------------------------------------------

def snail(sid, foot, shell_style, scale=0.55, extras=None, glow=False, shell_size=(5, 6, 6)):
    sw, sh, sd = shell_size
    body = Part('body', (0, 24, 0), (0, 0, 0), [
        Cube(-1.5, -2, -4, 3, 2, 9, S(foot, top=lighten(foot, 0.08))),
        Cube(-1, -1, 5, 2, 1, 2, S(foot)),
    ])
    head = Part('head', (0, 24, -4), (0, 0, 0), [
        Cube(-1.5, -4, -2, 3, 4, 2, S(foot, front=lighten(foot, 0.05))),
    ])
    for side, nm in ((1, 'l'), (-1, 'r')):
        stalk = Part('eyestalk_' + nm, (side * 1.0, -4, -1.2), (-0.35, 0, side * 0.3), [
            Cube(-0.5, -4, -0.5, 1, 4, 1, S(darken(foot, 0.1))),
            Cube(-0.5, -5, -0.5, 1, 1, 1, EYE, grow=0.2),
        ])
        tent = Part('tentacle_' + nm, (side * 1.0, -0.8, -2), (0.3, side * 0.3, 0), [
            Cube(-0.5, -0.5, -1, 1, 1, 1, S(darken(foot, 0.1))),
        ])
        head.add(stalk, tent)
    shell = Part('shell', (0, 22, 1.5), (-0.12, 0, 0), [
        Cube(-sw / 2, -sh, -sd / 2, sw, sh, sd, shell_style),
    ])
    if extras:
        shell.add(*extras)
    return Model(sid, [body, head, shell], scale, glow)


def lighten(c, f):
    r, g, b = hexc(c) if isinstance(c, int) else c
    r = int(r + (255 - r) * f); g = int(g + (255 - g) * f); b = int(b + (255 - b) * f)
    return (r << 16) | (g << 8) | b


def darken(c, f):
    r, g, b = hexc(c) if isinstance(c, int) else c
    r = int(r * (1 - f)); g = int(g * (1 - f)); b = int(b * (1 - f))
    return (r << 16) | (g << 8) | b


def snails():
    out = []
    out.append(snail('garden_snail', 0x9C8E7A,
                     S(0x8B5A2B, pat=[('spiral', hexc(0x4E2E14), ('west', 'east')),
                                      ('bands', hexc(0x5E3A1A), 3, 1, 'u', ('top', 'front', 'back'))])))
    out.append(snail('banded_snail', 0xCFC3A6,
                     S(0xE8C547, pat=[('bands', hexc(0x5A3416), 3, 1, 'v', ('west', 'east', 'front', 'back')),
                                      ('bands', hexc(0x5A3416), 3, 1, 'u', ('top',))])))
    moss = 0x5E8C2E
    out.append(snail('moss_snail', 0x6E7356,
                     S(0x4A6B2A, pat=[('spiral', hexc(0x2C4318), ('west', 'east')),
                                      ('speckle', hexc(0x7FAE45), 0.25, ('top', 'front', 'back', 'west', 'east'))]),
                     extras=[
                         Part('moss_a', (0, -6, -1), (0, 0.3, 0), [Cube(-1.5, -1, -1.5, 3, 1, 3, S(moss, pat=[('speckle', hexc(0x8FC24F), 0.3, ('top', 'west', 'east', 'front', 'back'))]))]),
                         Part('moss_b', (1, -5, 2), (0, -0.4, 0.2), [Cube(-1, -1, -1, 2, 1, 2, S(moss))]),
                         Part('sprout', (-1, -7, -1), (0, 0.7, 0), [Cube(0, -2, -1, 0, 2, 2, S(0x6FB33A))]),
                     ]))
    crystal = S(0xC9A3F2, pat=[('rim', hexc(0xF4E6FF))], glow_all=hexc(0x6A3C9A))
    out.append(snail('amethyst_snail', 0x9C8FB0,
                     S(0x7B4FB5, pat=[('spiral', hexc(0xB58AE6), ('west', 'east')),
                                      ('speckle', hexc(0xA77AE0), 0.2, ('top', 'front', 'back'))]),
                     glow=True,
                     extras=[
                         Part('crystal_a', (0, -6, -1), (0.2, 0, 0.25), [Cube(-0.5, -3, -0.5, 1, 3, 1, crystal)]),
                         Part('crystal_b', (1.5, -5, 1.5), (-0.3, 0, -0.35), [Cube(-0.5, -2, -0.5, 1, 2, 1, crystal)]),
                         Part('crystal_c', (-1.5, -5, 1), (-0.1, 0, 0.5), [Cube(-0.5, -2, -0.5, 1, 2, 1, crystal)]),
                     ]))
    out.append(snail('magma_snail', 0x3A2A2A,
                     S(0x3B1A12, pat=[('cracks', hexc(0xFF7A1A), 0.18, ('top', 'front', 'back', 'west', 'east'), True),
                                      ('spiral', hexc(0xE0501A), ('west', 'east'), True)]),
                     glow=True))
    return out


# ---------------------------------------------------------------------------
# CRABS
# ---------------------------------------------------------------------------

def crab(sid, shell, under, claw_l, claw_r, legc, scale, big_right=False, spots=None):
    pat = []
    if spots:
        pat.append(('speckle', hexc(spots), 0.15, ('top',)))
    body = Part('body', (0, 22, 0), (0, 0, 0), [
        Cube(-5, -3, -3.5, 10, 3, 7, S(shell, bottom=under, pat=pat + [('rim', hexc(darken(shell, 0.25)))])),
        Cube(-4, -4, -2.5, 8, 1, 5, S(lighten(shell, 0.08), pat=pat)),
    ])
    for side, nm in ((1, 'l'), (-1, 'r')):
        body.add(Part('eyestalk_' + nm, (side * 2, -3.5, -3), (-0.2, 0, side * 0.15), [
            Cube(-0.5, -3, -0.5, 1, 3, 1, S(darken(shell, 0.1))),
            Cube(-0.5, -3.6, -0.5, 1, 1, 1, EYE, grow=0.25),
        ]))
    parts = [body]
    zs = (-1.8, 0.5, 2.6)
    for i, z in enumerate(zs):
        parts.append(leg('leg_l%d' % i, 1, 4.5, 21.0, z, 5, (0.45, 0.05, -0.45)[i], legc))
        parts.append(leg('leg_r%d' % i, -1, 4.5, 21.0, z, 5, (0.45, 0.05, -0.45)[i], legc))
    # claws: an arm reaching forward, with a pincer on the end
    for side, nm, col in ((1, 'l', claw_l), (-1, 'r', claw_r)):
        big = big_right and side < 0
        small = big_right and side > 0
        arm = Part('claw_' + nm, (side * 3.5, 20.5, -3.5), (0.15, side * 0.55, 0), [
            Cube(-1, -1, -3, 2, 2, 3, S(darken(col, 0.1)))])
        if big:
            pincer = Part('pincer_' + nm, (0, 0, -3), (0, -0.3 * side, 0), [
                Cube(-2.5, -2.5, -6, 5, 4, 6, S(col, pat=[('rim', hexc(lighten(col, 0.35)))])),
                Cube(-1.5, 1.5, -5, 3, 1, 4, S(lighten(col, 0.4))),
            ])
        elif small:
            pincer = Part('pincer_' + nm, (0, 0, -3), (0, 0, 0), [
                Cube(-1, -1, -2, 2, 2, 2, S(col)),
            ])
        else:
            pincer = Part('pincer_' + nm, (0, 0, -3), (0, 0.2 * side, 0), [
                Cube(-1.5, -1.5, -4, 3, 3, 4, S(col, front=0x1A1414)),
                Cube(-0.5, 1.5, -3, 1, 1, 3, S(0x1A1414)),
            ])
        arm.add(pincer)
        parts.append(arm)
    return Model(sid, parts, scale)


def crabs():
    return [
        crab('fiddler_crab', 0x2F6F8F, 0xD9C9A8, 0x3B7F9C, 0xF2A33A, 0x2A5E78, 0.5,
             big_right=True, spots=0x9FD3E8),
        crab('rock_crab', 0xB8322A, 0xE8B49A, 0xB8322A, 0xB8322A, 0x9A2A22, 0.5,
             spots=0xD8574A),
    ]


# ---------------------------------------------------------------------------
# SMALL INSECTS
# ---------------------------------------------------------------------------

def firefly():
    body = Part('body', (0, 20, 0), (0, 0, 0), [
        Cube(-1.5, -1.5, -2, 3, 3, 4, S(0x2A2420)),
        Cube(-1.5, -1.5, 2, 3, 3, 4, S(0xD8E65A, top=0x4A3E30, glow_faces=('bottom', 'back', 'west', 'east'),
                                         glow_color=hexc(0xE8FF6A))),
    ])
    head = Part('head', (0, -0.5, -2), (0, 0, 0), [
        Cube(-1.5, -1.5, -2, 3, 2, 2, S(0xD9572B, pat=[('dot', hexc(0x201818), ('top',))])),
        Cube(-1, -0.5, -3, 2, 2, 1, S(0x201818)),
    ])
    head.add(*antennae(0.6, -0.2, -3, 4, 0x201818, up=-0.6, out=0.5))
    body.add(head)
    body.add(Part('wing_l', (0.2, -1.6, -1.5), (0, -0.15, 0.1), [
        Cube(0, 0, 0, 4, 0, 7, S((200, 210, 200), alpha=150, pat=[('rim', (150, 160, 150))]))]))
    body.add(Part('wing_r', (-0.2, -1.6, -1.5), (0, 0.15, -0.1), [
        Cube(-4, 0, 0, 4, 0, 7, S((200, 210, 200), alpha=150, pat=[('rim', (150, 160, 150))]))]))
    legs = six_legs((1.0, 1.0, 1.0), 21, (-1.2, 0.0, 1.2), 3, 0x201818)
    return Model('firefly', [body] + legs, 0.45, glow=True)


def cricket():
    brown = 0x5B3A1E
    body = Part('body', (0, 21, 0), (0, 0, 0), [
        Cube(-2, -3, -3, 4, 3, 8, S(brown, top=0x3E2614, pat=[('midline', hexc(0x2A190C))])),
    ])
    head = Part('head', (0, -1.5, -3), (0.2, 0, 0), [
        Cube(-2, -2, -3, 4, 3, 3, S(0x4A2E17, front=0x3A2410)),
        Cube(1.6, -1.6, -2.6, 1, 1, 1, EYE, grow=0.05),
        Cube(-2.6, -1.6, -2.6, 1, 1, 1, EYE, grow=0.05),
    ])
    head.add(*antennae(1.0, -1.5, -3, 9, 0x2A1A0E, up=-0.35, out=0.3))
    body.add(head)
    parts = [body]
    parts += six_legs((1.8, 1.8), 21, (-2.2, -0.2), 4, 0x3A2410, spreads=(0.6, -0.1))[:4]
    for side, nm in ((1, 'l'), (-1, 'r')):
        thigh = Part('hindleg_' + nm, (side * 2.0, 20.5, 1.0), (0.45, side * -0.15, 0), [
            Cube(-0.5 if side > 0 else -1.5, -1, 0, 2, 2, 6, S(0x6A4426, pat=[('bands', hexc(0x3A2410), 2, 1, 'v', ('west', 'east'))]))])
        thigh.add(Part('shin_' + nm, (side * 0.5, 0, 6), (-0.25, 0, 0), [
            Cube(-0.5, 0, -0.5, 1, 6, 1, S(0x3A2410))]))
        parts.append(thigh)
    return Model('cricket', parts, 0.42)


def ant():
    c = 0x1E1A1A
    thorax = Part('body', (0, 21, 0), (0, 0, 0), [
        Cube(-1, -1.5, -2, 2, 2, 4, S(c, top=0x2C2626)),
    ])
    head = Part('head', (0, -1, -2), (0.15, 0, 0), [
        Cube(-1.5, -1.5, -3, 3, 3, 3, S(c, top=0x2C2626)),
        Cube(-1.5, 0.5, -4, 1, 1, 1, S(0x5A2A1A)),
        Cube(0.5, 0.5, -4, 1, 1, 1, S(0x5A2A1A)),
    ])
    head.add(*antennae(0.8, -1.2, -2.5, 5, c, up=-0.7, out=0.5))
    thorax.add(head)
    thorax.add(Part('abdomen', (0, -0.5, 2), (-0.3, 0, 0), [
        Cube(-2, -2, 0, 4, 3, 5, S(c, top=0x332B2B, pat=[('bands', hexc(0x3B3232), 2, 1, 'v', ('top',))]))]))
    legs = six_legs((0.8, 0.8, 0.8), 21, (-1.4, 0.0, 1.4), 5, c, spreads=(0.65, 0.0, -0.65))
    return Model('ant', [thorax] + legs, 0.38)


# ---------------------------------------------------------------------------
# BEETLES
# ---------------------------------------------------------------------------

def beetle(sid, body, pron, head, elytra_style, pron_style, head_style, legc, scale,
           leg_len=5, ant_len=4, antc=None, extras=None, leg_lengths=None):
    bw, bh, bd = body
    pw, ph, pd = pron
    hw, hh, hd = head
    base = 22.0  # body bottom
    z0e = -bd / 2 + pd / 2  # front of the elytra
    b = Part('body', (0, base, 0), (0, 0, 0), [
        Cube(-bw / 2, -bh, z0e, bw, bh, bd, elytra_style),
        Cube(-pw / 2, -ph, z0e - pd, pw, ph, pd, pron_style),
    ])
    hz = z0e - pd
    h = Part('head', (0, -ph / 2 + 0.5, hz), (0.1, 0, 0), [
        Cube(-hw / 2, -hh / 2, -hd, hw, hh, hd, head_style),
        Cube(hw / 2 - 0.6, -hh / 2 + 0.2, -hd + 0.2, 1, 1, 1, EYE, grow=0.05),
        Cube(-hw / 2 - 0.4, -hh / 2 + 0.2, -hd + 0.2, 1, 1, 1, EYE, grow=0.05),
    ])
    if ant_len:
        h.add(*antennae(hw / 2 - 0.5, -hh / 2 + 0.5, -hd + 0.5, ant_len, antc or legc))
    if extras:
        h.add(*extras(hw, hh, hd))
    b.add(h)
    lx = min(bw, pw) / 2 - 0.5
    zs = (hz + pd / 2, z0e + 1.0, z0e + min(bd * 0.45, 4.0))
    legs = six_legs((lx, lx, lx), base - 1.0, zs, leg_len, legc, lengths=leg_lengths)
    return Model(sid, [b] + legs, scale)


def beetles():
    out = []
    # 1. Ladybug - red with black spots
    out.append(beetle('ladybug', (8, 4, 7), (6, 3, 2), (4, 2, 2),
                      S(0xD8261C, pat=[('spots', hexc(0x141010), 7, 1, ('top', 'west', 'east', 'back')), ('midline', hexc(0x141010))]),
                      S(0x141010, pat=[('spots', hexc(0xF2EEE6), 2, 0, ('top',))]),
                      S(0x141010), 0x141010, 0.36, leg_len=4, ant_len=2))
    # 2. Stag beetle - dark with huge mandibles
    def stag_jaws(hw, hh, hd):
        col = S(0x6A2A16, top=0x8A3A1E)
        return [
            Part('mandible_l', (hw / 2 - 0.5, 0, -hd), (0, 0.35, 0), [Cube(-0.5, -0.5, -6, 1, 1, 6, col), Cube(-1.5, -0.5, -6, 1, 1, 1, col)]),
            Part('mandible_r', (-hw / 2 + 0.5, 0, -hd), (0, -0.35, 0), [Cube(-0.5, -0.5, -6, 1, 1, 6, col), Cube(0.5, -0.5, -6, 1, 1, 1, col)]),
        ]
    out.append(beetle('stag_beetle', (8, 3, 9), (7, 3, 3), (6, 2, 3),
                      S(0x3A2216, top=0x4A2C1C, pat=[('midline', hexc(0x1C100A)), ('shine', hexc(0x7A5844))]),
                      S(0x24160F, pat=[('shine', hexc(0x5A4030))]),
                      S(0x24160F), 0x1C120C, 0.45, leg_len=6, ant_len=3, extras=stag_jaws))
    # 3. Rhinoceros beetle - big curved horn
    def rhino_horn(hw, hh, hd):
        col = S(0x2A1A10, top=0x3A2618)
        return [Part('horn', (0, -hh / 2, -hd + 0.5), (-0.6, 0, 0), [
            Cube(-0.5, -6, -1, 1, 6, 1, col),
            Cube(-1, -7, -1.5, 2, 1, 1, col)])]
    out.append(beetle('rhinoceros_beetle', (9, 4, 9), (8, 4, 4), (4, 2, 2),
                      S(0x5A3418, pat=[('midline', hexc(0x2A1A0C)), ('shine', hexc(0x9A6A3C))]),
                      S(0x3E2412, pat=[('shine', hexc(0x7A5030))]),
                      S(0x2A1A10), 0x2A1A10, 0.5, leg_len=6, ant_len=2, extras=rhino_horn))
    # 4. Dung beetle - broad, black, shovel head
    def shovel(hw, hh, hd):
        return [Part('shovel', (0, 0.5, -hd), (0.2, 0, 0), [Cube(-3, -0.5, -1, 6, 1, 1, S(0x1A1A1E))])]
    out.append(beetle('dung_beetle', (8, 3, 7), (8, 3, 3), (6, 1, 2),
                      S(0x1A1A1E, pat=[('midline', hexc(0x0C0C10)), ('shine', hexc(0x45464E))]),
                      S(0x16161A, pat=[('shine', hexc(0x3A3A44))]),
                      S(0x16161A), 0x16161A, 0.4, leg_len=5, ant_len=2, extras=shovel))
    # 5. Jewel beetle - iridescent green/gold
    out.append(beetle('jewel_beetle', (6, 3, 9), (6, 2, 3), (4, 2, 2),
                      S(0x1FA05A, pat=[('gradient', hexc(0xE6C23A), 'u', ('top', 'west', 'east')), ('midline', hexc(0x0E5A3A)), ('speckle', hexc(0x7AE0B0), 0.08, ('top',))]),
                      S(0x1C7A8A, pat=[('gradient', hexc(0x2AB06A), 'u', ('top',))]),
                      S(0x1C6A7A), 0x154A52, 0.38, leg_len=4, ant_len=3))
    # 6. Golden scarab
    def scarab_horn(hw, hh, hd):
        return [Part('clypeus', (0, 0, -hd), (0, 0, 0), [Cube(-1.5, -0.5, -1, 3, 1, 1, S(0xC8962A))])]
    out.append(beetle('scarab_beetle', (8, 4, 8), (7, 3, 3), (5, 2, 2),
                      S(0xD9A82C, pat=[('shine', hexc(0xFFE58A)), ('midline', hexc(0x8A6414)), ('bands', hexc(0xB8861E), 2, 1, 'u', ('top',))]),
                      S(0xC8962A, pat=[('shine', hexc(0xFFE070))]),
                      S(0xB8861E), 0x8A6414, 0.42, leg_len=5, ant_len=2, extras=scarab_horn))
    # 7. Bombardier beetle - orange head, blue-black elytra
    out.append(beetle('bombardier_beetle', (6, 3, 7), (4, 2, 3), (4, 2, 3),
                      S(0x1E2A4A, pat=[('midline', hexc(0x0E1424)), ('bands', hexc(0x2E3E66), 2, 1, 'u', ('top',))]),
                      S(0xE07A22),
                      S(0xE07A22, front=0xC2621A), 0xD06A1E, 0.36, leg_len=5, ant_len=4))
    # 8. Weevil - long snout, speckled grey-brown
    def snout(hw, hh, hd):
        col = S(0x6A5A48, pat=[('speckle', hexc(0x9A8A74), 0.2, ('top', 'west', 'east'))])
        return [Part('snout', (0, 0, -hd), (0.5, 0, 0), [Cube(-0.5, -0.5, -4, 1, 1, 4, col)])]
    out.append(beetle('weevil', (6, 4, 7), (4, 3, 3), (3, 3, 2),
                      S(0x6A5A48, pat=[('speckle', hexc(0xA89878), 0.22, ('top', 'west', 'east', 'back')), ('midline', hexc(0x3E3428))]),
                      S(0x5A4A3A, pat=[('speckle', hexc(0x9A8A74), 0.2, ('top',))]),
                      S(0x5A4A3A), 0x3E3428, 0.36, leg_len=4, ant_len=3))
    # 9. Goliath beetle - large, white-and-black patterned
    out.append(beetle('goliath_beetle', (10, 4, 10), (9, 3, 4), (5, 2, 2),
                      S(0x6A3E1E, pat=[('bands', hexc(0xEDE6D4), 3, 1, 'v', ('west', 'east')), ('stripes_top', hexc(0xEDE6D4)), ('midline', hexc(0x2A160A))]),
                      S(0xEDE6D4, pat=[('stripes_top', hexc(0x161212))]),
                      S(0x161212), 0x161212, 0.55, leg_len=7, ant_len=2))
    # 10. Tiger beetle - metallic green with cream spots, long legs
    def tiger_jaws(hw, hh, hd):
        col = S(0xEDE2B8)
        return [
            Part('mandible_l', (hw / 2 - 0.8, 0.3, -hd), (0, 0.4, 0), [Cube(-0.5, -0.5, -2, 1, 1, 2, col)]),
            Part('mandible_r', (-hw / 2 + 0.8, 0.3, -hd), (0, -0.4, 0), [Cube(-0.5, -0.5, -2, 1, 1, 2, col)]),
        ]
    out.append(beetle('tiger_beetle', (6, 3, 7), (5, 2, 2), (6, 2, 2),
                      S(0x2E7A3E, pat=[('spots', hexc(0xEDE2B8), 6, 0, ('top',)), ('gradient', hexc(0x2A5A8A), 'v', ('west', 'east')), ('midline', hexc(0x1A4A26))]),
                      S(0x2A6A5A),
                      S(0x2A6A5A), 0x3A8A5A, 0.36, leg_len=7, ant_len=4, extras=tiger_jaws,
                      leg_lengths=(6, 7, 8)))
    return out


# ---------------------------------------------------------------------------
# FISH
# ---------------------------------------------------------------------------

def fish(sid, body_cubes, head_cubes, fins, scale, glow=False):
    body = Part('body', (0, 21, 0), (0, 0, 0), body_cubes)
    body.add(Part('head', (0, 0, -3), (0, 0, 0), head_cubes))
    for f in fins:
        body.add(f)
    return Model(sid, [body], scale, glow)


def fishes():
    out = []
    # Goldfish - round orange body, double fan tail
    gold = 0xF08A1C
    fin = S(0xFFB44A, alpha=200, pat=[('bands', hexc(0xFFD07A), 2, 1, 'v', ('west', 'east'))])
    out.append(fish('goldfish',
                    [Cube(-2, -2.5, -3, 4, 5, 6, S(gold, top=0xE0700E, bottom=0xFFC060, pat=[('scales', hexc(0xFFA83A), ('west', 'east', 'top'))]))],
                    [Cube(-1.5, -2, -2, 3, 4, 2, S(gold)),
                     Cube(1.2, -1.5, -1.5, 1, 1, 1, EYE, grow=0.1),
                     Cube(-2.2, -1.5, -1.5, 1, 1, 1, EYE, grow=0.1)],
                    [Part('tail_fin', (0, 0, 3), (0, 0, 0), [], [
                        Part('tail_a', (0, 0, 0), (0, 0.35, 0), [Cube(0, -3, 0, 0, 6, 5, fin)]),
                        Part('tail_b', (0, 0, 0), (0, -0.35, 0), [Cube(0, -3, 0, 0, 6, 5, fin)])]),
                     Part('dorsal_fin', (0, -2.5, -1), (0, 0, 0), [Cube(0, -2, 0, 0, 2, 4, fin)]),
                     Part('fin_l', (2, 1.5, -1.5), (0, 0, 0.7), [Cube(0, 0, 0, 2, 0, 2, fin)]),
                     Part('fin_r', (-2, 1.5, -1.5), (0, 0, -0.7), [Cube(-2, 0, 0, 2, 0, 2, fin)])],
                    0.8))
    # Betta - deep blue body, huge flowing red/blue fins
    bfin = S(0xC8243A, alpha=210, pat=[('gradient', hexc(0x3A3AD8), 'u', ('west', 'east')), ('bands', hexc(0xE85A6A), 2, 1, 'v', ('west', 'east'))])
    out.append(fish('betta',
                    [Cube(-1.5, -2, -3, 3, 4, 6, S(0x2A3FA8, pat=[('scales', hexc(0x4A6AE0), ('west', 'east', 'top'))]))],
                    [Cube(-1, -1.5, -2, 2, 3, 2, S(0x22348C)),
                     Cube(0.7, -1, -1.5, 1, 1, 1, EYE, grow=0.1),
                     Cube(-1.7, -1, -1.5, 1, 1, 1, EYE, grow=0.1)],
                    [Part('tail_fin', (0, 0, 3), (0, 0, 0), [Cube(0, -5, 0, 0, 10, 8, bfin)]),
                     Part('dorsal_fin', (0, -2, 0), (0.3, 0, 0), [Cube(0, -4, 0, 0, 4, 5, bfin)]),
                     Part('anal_fin', (0, 2, -1), (-0.3, 0, 0), [Cube(0, 0, 0, 0, 4, 6, bfin)]),
                     Part('fin_l', (1.5, 1, -2), (0, 0, 0.6), [Cube(0, 0, 0, 2, 0, 2, bfin)]),
                     Part('fin_r', (-1.5, 1, -2), (0, 0, -0.6), [Cube(-2, 0, 0, 2, 0, 2, bfin)])],
                    0.8))
    # Neon tetra - small, glowing blue stripe, red rear
    tfin = S((220, 230, 235), alpha=120)
    out.append(fish('neon_tetra',
                    [Cube(-1, -1.5, -3, 2, 3, 6, S(0xB8C4C8, top=0x5A6A70, pat=[('neon', hexc(0x2AD8FF), hexc(0xE8243A), ('west', 'east'))]))],
                    [Cube(-1, -1, -1, 2, 2, 1, S(0xA8B4B8)),
                     Cube(0.4, -0.8, -0.8, 1, 1, 1, EYE, grow=0.05),
                     Cube(-1.4, -0.8, -0.8, 1, 1, 1, EYE, grow=0.05)],
                    [Part('tail_fin', (0, 0, 3), (0, 0, 0), [Cube(0, -2, 0, 0, 4, 3, tfin)]),
                     Part('dorsal_fin', (0, -1.5, 0), (0, 0, 0), [Cube(0, -1, 0, 0, 1, 2, tfin)]),
                     Part('fin_l', (1, 1, -1.5), (0, 0, 0.6), [Cube(0, 0, 0, 1, 0, 1, tfin)]),
                     Part('fin_r', (-1, 1, -1.5), (0, 0, -0.6), [Cube(-1, 0, 0, 1, 0, 1, tfin)])],
                    0.65, glow=True))
    # Angelfish - tall, flat, silver with black vertical stripes
    afin = S(0xD8D8D0, alpha=200, pat=[('bands', hexc(0x202020), 3, 1, 'u', ('west', 'east'))])
    out.append(fish('angelfish',
                    [Cube(-1, -3.5, -3, 2, 7, 6, S(0xD8D8D0, top=0x9A9A90, pat=[('bands', hexc(0x202020), 3, 1, 'u', ('west', 'east', 'top', 'bottom')), ('gradient', hexc(0xE8D89A), 'v', ('west', 'east'))]))],
                    [Cube(-1, -2, -2, 2, 4, 2, S(0xC8C8C0)),
                     Cube(0.4, -1.5, -1.5, 1, 1, 1, EYE, grow=0.1),
                     Cube(-1.4, -1.5, -1.5, 1, 1, 1, EYE, grow=0.1)],
                    [Part('tail_fin', (0, 0, 3), (0, 0, 0), [Cube(0, -3, 0, 0, 6, 4, afin)]),
                     Part('dorsal_fin', (0, -3.5, -1), (0.55, 0, 0), [Cube(0, -6, 0, 0, 6, 5, afin)]),
                     Part('anal_fin', (0, 3.5, -1), (-0.55, 0, 0), [Cube(0, 0, 0, 0, 6, 5, afin)]),
                     Part('fin_l', (1, 2.5, -2), (0.4, 0, 0.3), [Cube(0, 0, 0, 0, 6, 1, S(0xD8D8D0, alpha=200))]),
                     Part('fin_r', (-1, 2.5, -2), (0.4, 0, -0.3), [Cube(0, 0, 0, 0, 6, 1, S(0xD8D8D0, alpha=200))])],
                    0.75))
    return out


def all_models():
    return snails() + crabs() + [firefly(), cricket(), ant()] + beetles() + fishes()
