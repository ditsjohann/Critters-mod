#!/usr/bin/env python3
"""
Generates all procedural assets for the Critters mod:

  * src/client/java/com/critters/client/model/CritterLayers.java  (entity model geometry)
  * assets/critters/textures/entity/*.png                        (entity skins + glow maps)
  * assets/critters/textures/item/*.png                          (jars, fish buckets)
  * assets/critters/textures/block/*.png                         (terrarium, aquarium, slime trail)
  * item models, block models, blockstates, lang, recipes, loot tables

Run from the repository root:   python3 tools/generate_assets.py
Requires Pillow.  Output is deterministic (fixed random seeds).
"""
import json
import math
import os
import random
import re
import sys

from PIL import Image

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from critter_models import all_models, hexc, darken, lighten  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, 'src/main/resources/assets/critters')
DATA = os.path.join(ROOT, 'src/main/resources/data/critters')
JAVA_LAYERS = os.path.join(ROOT, 'src/client/java/com/critters/client/model/CritterLayers.java')
SPECIES_JAVA = os.path.join(ROOT, 'src/main/java/com/critters/registry/CritterSpecies.java')

# id, display name, icon family, primary, secondary, detail
SPECIES = [
    ('garden_snail', 'Garden Snail', 'snail', 0x8B5A2B, 0x4E2E14, 0x9C8E7A),
    ('banded_snail', 'Banded Snail', 'snail', 0xE8C547, 0x5A3416, 0xCFC3A6),
    ('moss_snail', 'Moss Snail', 'snail', 0x4A6B2A, 0x7FAE45, 0x6E7356),
    ('amethyst_snail', 'Amethyst Snail', 'snail', 0x7B4FB5, 0xC9A3F2, 0x9C8FB0),
    ('magma_snail', 'Magma Snail', 'snail', 0x3B1A12, 0xFF7A1A, 0x3A2A2A),
    ('fiddler_crab', 'Fiddler Crab', 'crab', 0x2F6F8F, 0xF2A33A, 0x2A5E78),
    ('rock_crab', 'Red Rock Crab', 'crab', 0xB8322A, 0xB8322A, 0x7A1A16),
    ('firefly', 'Firefly', 'firefly', 0x2A2420, 0xD9572B, 0xE8FF6A),
    ('cricket', 'Cricket', 'cricket', 0x5B3A1E, 0x3A2410, 0x6A4426),
    ('ant', 'Ant', 'ant', 0x2A2424, 0x3E3434, 0x5A2A1A),
    ('ladybug', 'Ladybug', 'beetle', 0xD8261C, 0x141010, 0x141010),
    ('stag_beetle', 'Stag Beetle', 'stag', 0x4A2C1C, 0x24160F, 0x8A3A1E),
    ('rhinoceros_beetle', 'Rhinoceros Beetle', 'rhino', 0x5A3418, 0x3E2412, 0x2A1A10),
    ('dung_beetle', 'Dung Beetle', 'beetle', 0x2A2A30, 0x1A1A1E, None),
    ('jewel_beetle', 'Jewel Beetle', 'beetle', 0x1FA05A, 0x1C7A8A, 0xE6C23A),
    ('scarab_beetle', 'Golden Scarab', 'beetle', 0xD9A82C, 0xC8962A, 0xFFE58A),
    ('bombardier_beetle', 'Bombardier Beetle', 'beetle', 0x1E2A4A, 0xE07A22, None),
    ('weevil', 'Weevil', 'weevil', 0x6A5A48, 0x5A4A3A, 0xA89878),
    ('goliath_beetle', 'Goliath Beetle', 'beetle', 0x6A3E1E, 0xEDE6D4, 0xEDE6D4),
    ('tiger_beetle', 'Tiger Beetle', 'beetle', 0x2E7A3E, 0x2A6A5A, 0xEDE2B8),
    ('goldfish', 'Goldfish', 'fish', 0xF08A1C, 0xFFB44A, 0xFFD07A),
    ('betta', 'Betta Fish', 'fish', 0x2A3FA8, 0xC8243A, 0x3A3AD8),
    ('neon_tetra', 'Neon Tetra', 'fish', 0xB8C4C8, 0x2AD8FF, 0xE8243A),
    ('angelfish', 'Angelfish', 'fish', 0xD8D8D0, 0x202020, 0xE8D89A),
]
FISH = [s for s in SPECIES if s[2] == 'fish']
LAND = [s for s in SPECIES if s[2] != 'fish']


def write(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', newline='\n') as f:
        f.write(content)


def write_json(path, obj):
    write(path, json.dumps(obj, indent=2) + '\n')


def clamp(v):
    return max(0, min(255, int(round(v))))


def mul(c, f):
    return (clamp(c[0] * f), clamp(c[1] * f), clamp(c[2] * f))


def mix(a, b, t):
    return (clamp(a[0] + (b[0] - a[0]) * t), clamp(a[1] + (b[1] - a[1]) * t), clamp(a[2] + (b[2] - a[2]) * t))


def rgb(c):
    return hexc(c) if isinstance(c, int) else c


# ---------------------------------------------------------------------------
# Entity textures
# ---------------------------------------------------------------------------

SHADE = {'top': 1.0, 'bottom': 0.72, 'front': 0.92, 'back': 0.86, 'west': 0.84, 'east': 0.84}


def face_regions(c):
    u, v, w, h, d = c.u, c.v, c.w, c.h, c.d
    return {
        'top': (u + d, v, w, d),
        'bottom': (u + d + w, v, w, d),
        'west': (u, v + d, d, h),
        'front': (u + d, v + d, w, h),
        'east': (u + d + w, v + d, d, h),
        'back': (u + d + w + d, v + d, w, h),
    }


def zfrac(face, px, py, fw, fh):
    """0 at the front (-Z) of the cube, 1 at the back.  Derived from ModelPart.Cube UV layout."""
    if face in ('top', 'bottom'):
        return 1 - py / max(1, fh - 1)
    if face == 'west':
        return 1 - px / max(1, fw - 1)
    if face == 'east':
        return px / max(1, fw - 1)
    return 0.0 if face == 'front' else 1.0


def pattern_mask(pat, face, fw, fh, rng):
    """Returns (set of pixels, colour, glow?) for a pattern on one face, or None."""
    kind = pat[0]
    m = set()
    if kind == 'spots':
        _, col, n, r, faces = pat
        if face not in faces:
            return None
        count = n if face == 'top' else max(1, n // 3)
        for _ in range(count):
            if fw < 3 or fh < 3:
                break
            cx = rng.randint(1, fw - 2)
            cy = rng.randint(1, fh - 2)
            m.add((cx, cy))
            if r >= 1:
                for dx, dy in ((1, 0), (0, 1), (-1, 0), (0, -1)):
                    if 0 <= cx + dx < fw and 0 <= cy + dy < fh and rng.random() < 0.6:
                        m.add((cx + dx, cy + dy))
        return m, col, False
    if kind == 'speckle':
        _, col, p, faces = pat
        if face not in faces:
            return None
        for y in range(fh):
            for x in range(fw):
                if rng.random() < p:
                    m.add((x, y))
        return m, col, False
    if kind == 'bands':
        _, col, period, width, axis, faces = pat
        if face not in faces:
            return None
        for y in range(fh):
            for x in range(fw):
                k = x if axis == 'u' else y
                if k % period < width:
                    m.add((x, y))
        return m, col, False
    if kind == 'spiral':
        col, faces = pat[1], pat[2]
        glow = len(pat) > 3 and pat[3]
        if face not in faces:
            return None
        cx, cy = (fw - 1) / 2, (fh - 1) / 2
        for y in range(fh):
            for x in range(fw):
                dx, dy = x - cx, y - cy
                r = math.hypot(dx, dy)
                th = math.atan2(dy, dx) if face == 'west' else math.atan2(dy, -dx)
                val = (r - (th / (2 * math.pi)) * 2.0) % 2.0
                if val < 0.8:
                    m.add((x, y))
        return m, col, glow
    if kind == 'rim':
        col = pat[1]
        if fw < 3 or fh < 3:
            return None
        for y in range(fh):
            for x in range(fw):
                if x in (0, fw - 1) or y in (0, fh - 1):
                    m.add((x, y))
        return m, col, False
    if kind == 'midline':
        col = pat[1]
        if face not in ('top', 'back'):
            return None
        mx = fw // 2
        for y in range(fh):
            m.add((mx, y))
        return m, col, False
    if kind == 'shine':
        col = pat[1]
        if face != 'top' or fw < 4 or fh < 3:
            return None
        for y in range(1, fh - 1):
            m.add((1, y))
            if fw >= 6:
                m.add((fw - 2, y))
        return m, col, False
    if kind == 'stripes_top':
        col = pat[1]
        if face != 'top':
            return None
        for y in range(fh):
            for x in range(fw):
                if x % 3 == 1 and 0 < y < fh - 1:
                    m.add((x, y))
        return m, col, False
    if kind == 'dot':
        col, faces = pat[1], pat[2]
        if face not in faces:
            return None
        m.add((fw // 2, fh // 2))
        return m, col, False
    if kind == 'cracks':
        _, col, dens, faces, glow = pat
        if face not in faces:
            return None
        walks = max(1, int(fw * fh * dens / 4))
        for _ in range(walks):
            x, y = rng.randrange(fw), rng.randrange(fh)
            for _ in range(4):
                m.add((x, y))
                if rng.random() < 0.5:
                    x = max(0, min(fw - 1, x + rng.choice((-1, 1))))
                else:
                    y = max(0, min(fh - 1, y + rng.choice((-1, 1))))
        return m, col, glow
    if kind == 'scales':
        col, faces = pat[1], pat[2]
        if face not in faces:
            return None
        for y in range(fh):
            for x in range(fw):
                if (x + y) % 2 == 0 and y % 2 == 1:
                    m.add((x, y))
        return m, col, False
    return None


def paint_model(model, tw, th):
    img = Image.new('RGBA', (tw, th), (0, 0, 0, 0))
    glow = Image.new('RGBA', (tw, th), (0, 0, 0, 0))
    px_img = img.load()
    px_glow = glow.load()
    rng = random.Random(sum(map(ord, model.id)) * 7919)
    for part in model.all_parts():
        for c in part.cubes:
            st = c.style
            alpha = st.get('alpha', 255)
            for face, (x0, y0, fw, fh) in face_regions(c).items():
                if fw <= 0 or fh <= 0:
                    continue
                if face in st:
                    base = rgb(st[face])
                elif face in ('west', 'east') and 'sides' in st:
                    base = rgb(st['sides'])
                else:
                    base = st['c']
                layers = []
                for pat in st.get('pat', []):
                    if pat[0] == 'gradient':
                        continue
                    r = pattern_mask(pat, face, fw, fh, rng)
                    if r:
                        layers.append(r)
                grads = [p for p in st.get('pat', []) if p[0] == 'gradient' and face in p[3]]
                for py in range(fh):
                    for px in range(fw):
                        col = base
                        for g in grads:
                            t = (px / max(1, fw - 1)) if g[2] == 'u' else (py / max(1, fh - 1))
                            col = mix(col, g[1], t)
                        gcol = None
                        for mask, mcol, mglow in layers:
                            if (px, py) in mask:
                                col = mcol
                                if mglow:
                                    gcol = mcol
                        # neon tetra stripe
                        for pat in st.get('pat', []):
                            if pat[0] == 'neon' and face in pat[3]:
                                row = py / max(1, fh - 1)
                                if py == 1 or (fh > 3 and py == fh // 2 - 1):
                                    col = pat[1]
                                    gcol = pat[1]
                                elif py >= 2 and zfrac(face, px, py, fw, fh) > 0.4:
                                    col = pat[2]
                        if st.get('eye'):
                            out = (16, 16, 18)
                        else:
                            n = 1 + rng.uniform(-0.06, 0.06)
                            out = mul(col, SHADE[face] * n)
                        if 'glow_all' in st:
                            gcol = st['glow_all']
                        if 'glow_faces' in st and face in st['glow_faces']:
                            gcol = st['glow_color']
                        px_img[x0 + px, y0 + py] = out + (alpha,)
                        if gcol is not None:
                            px_glow[x0 + px, y0 + py] = tuple(gcol) + (255,)
    return img, glow


def pack(model):
    cubes = [c for p in model.all_parts() for c in p.cubes]
    order = sorted(cubes, key=lambda c: (-(c.d + c.h), -(2 * (c.d + c.w))))
    width = 64
    if any(2 * (c.d + c.w) > 64 for c in cubes):
        width = 128
    x = y = row_h = 0
    for c in order:
        cw, ch = 2 * (c.d + c.w), c.d + c.h
        if x + cw > width:
            x = 0
            y += row_h
            row_h = 0
        c.u, c.v = x, y
        x += cw
        row_h = max(row_h, ch)
    used = y + row_h
    height = 16
    while height < used:
        height *= 2
    return width, height


def jf(v):
    s = ('%.4f' % v).rstrip('0').rstrip('.')
    if s in ('-0', ''):
        s = '0'
    if '.' not in s:
        s += '.0'
    return s + 'F'


def camel(sid):
    p = sid.split('_')
    return p[0] + ''.join(x.capitalize() for x in p[1:])


def java_part(lines, parent_var, part, counter):
    var = 'p%d' % counter[0]
    counter[0] += 1
    builder = 'CubeListBuilder.create()'
    for c in part.cubes:
        builder += '\n                .texOffs(%d, %d).addBox(%s, %s, %s, %s, %s, %s, new CubeDeformation(%s))' % (
            c.u, c.v, jf(c.x), jf(c.y), jf(c.z), jf(c.w), jf(c.h), jf(c.d), jf(c.grow))
    px, py, pz = part.pivot
    rx, ry, rz = part.rot
    lines.append('        PartDefinition %s = %s.addOrReplaceChild("%s", %s,\n                PartPose.offsetAndRotation(%s, %s, %s, %s, %s, %s));' % (
        var, parent_var, part.name, builder, jf(px), jf(py), jf(pz), jf(rx), jf(ry), jf(rz)))
    for ch in part.children:
        java_part(lines, var, ch, counter)


def gen_models():
    models = all_models()
    ids = [m.id for m in models]
    assert ids == [s[0] for s in SPECIES], 'model list and SPECIES list disagree: %s' % ids
    methods = []
    for m in models:
        tw, th = pack(m)
        img, glow = paint_model(m, tw, th)
        out = os.path.join(ASSETS, 'textures/entity', m.id + '.png')
        os.makedirs(os.path.dirname(out), exist_ok=True)
        img.save(out)
        gpath = os.path.join(ASSETS, 'textures/entity', m.id + '_glow.png')
        if m.glow:
            glow.save(gpath)
        elif os.path.exists(gpath):
            os.remove(gpath)
        lines = ['    private static LayerDefinition %s() {' % camel(m.id),
                 '        MeshDefinition mesh = new MeshDefinition();',
                 '        PartDefinition root = mesh.getRoot();']
        counter = [0]
        for p in m.parts:
            java_part(lines, 'root', p, counter)
        lines.append('        return LayerDefinition.create(mesh, %d, %d);' % (tw, th))
        lines.append('    }')
        methods.append('\n'.join(lines))

    cases = '\n'.join('            case "%s" -> %s();' % (m.id, camel(m.id)) for m in models)
    scales = '\n'.join('            case "%s" -> %sF;' % (m.id, m.scale) for m in models)
    glows = ', '.join('"%s"' % m.id for m in models if m.glow)
    src = '''// GENERATED FILE - DO NOT EDIT BY HAND.
// Produced by tools/generate_assets.py from tools/critter_models.py.
package com.critters.client.model;

import java.util.Set;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@SuppressWarnings("unused")
public final class CritterLayers {
    /** Critters that have an emissive "_glow" texture. */
    public static final Set<String> GLOWING = Set.of(%s);

    private CritterLayers() {
    }

    public static LayerDefinition create(String id) {
        return switch (id) {
%s
            default -> throw new IllegalArgumentException("Unknown critter model: " + id);
        };
    }

    /** Scale the renderer applies to the model (models are authored larger than life for detail). */
    public static float renderScale(String id) {
        return switch (id) {
%s
            default -> 1.0F;
        };
    }

%s
}
''' % (glows, cases, scales, '\n\n'.join(methods))
    write(JAVA_LAYERS, src)
    print('models: %d' % len(models))


# ---------------------------------------------------------------------------
# Item textures
# ---------------------------------------------------------------------------

GLASS_EDGE = (176, 214, 226, 235)
GLASS_FILL = (196, 232, 242, 60)
GLASS_HI = (245, 252, 255, 190)
CORK = (150, 104, 62, 255)
CORK_D = (108, 72, 40, 255)


def draw_jar(px):
    # cork
    for x in range(5, 11):
        for y in range(1, 4):
            px[x, y] = CORK if y < 3 and 5 < x < 10 else CORK_D
    # neck
    for x in range(5, 11):
        px[x, 4] = GLASS_EDGE
    # body
    for y in range(5, 15):
        for x in range(3, 13):
            edge = x in (3, 12) or y in (5, 14)
            px[x, y] = GLASS_EDGE if edge else GLASS_FILL
    px[3, 5] = px[12, 5] = (0, 0, 0, 0)
    px[3, 14] = px[12, 14] = (0, 0, 0, 0)
    px[4, 4] = px[11, 4] = GLASS_EDGE


def jar_highlight(px):
    for y in range(6, 11):
        px[4, y] = GLASS_HI
    px[5, 6] = GLASS_HI


SPRITES = {
    'snail': ['....PPP.',
              'K..PSSPP',
              '.B.PSPSP',
              '.BBPPSPP',
              'BBBBBBBB'],
    'crab': ['S.K..K.S',
             'SSPPPPSS',
             '.PPPPPP.',
             'K.PPPP.K',
             '.K.KK.K.'],
    'beetle': ['..K..K..',
               '...KK...',
               'K.SSSS.K',
               '.PPPDPP.',
               'KPDPPPPK',
               '.PPPPDP.',
               'K.PPPP.K'],
    'stag': ['.D....D.',
             '..D..D..',
             '...KK...',
             'K.SSSS.K',
             '.PPPPPP.',
             'KPPPPPPK',
             'K.PPPP.K'],
    'rhino': ['...D....',
              '...DD...',
              '...SS...',
              'K.SSSS.K',
              '.PPPPPP.',
              'KPPPPPPK',
              'K.PPPP.K'],
    'weevil': ['...S....',
               '...S....',
               '..SSS...',
               'K.PDPP.K',
               '.PPPDPP.',
               'KPDPPPPK',
               '..PPPP..'],
    'firefly': ['..SS..',
                'WWKKWW',
                '.WKKW.',
                '..GG..',
                '..GG..'],
    'cricket': ['K.......',
                '.KSS....',
                '..PPPPPP',
                '.KPPPPPB',
                'K.K.K.BB'],
    'ant': ['K.......',
            '.K......',
            '.PP.P.SS',
            'K..K.KSS',
            '.K.K.K..'],
    'fish': ['...SS...',
             '.PPPPS..',
             'KPPPPPSS',
             '.PPPPS..'],
}


def sprite_colors(sp):
    sid, name, fam, p, s, d = sp
    P = rgb(p)
    S = rgb(s)
    D = rgb(d) if d is not None else P
    return {
        'P': P + (255,), 'S': S + (255,), 'D': D + (255,), 'B': D + (255,),
        'K': (34, 28, 26, 255) if sum(P) > 120 else (70, 66, 62, 255),
        'W': (225, 235, 230, 200), 'G': D + (255,),
    }


def draw_sprite(px, sprite, colors, ox, oy):
    for y, row in enumerate(sprite):
        for x, ch in enumerate(row):
            if ch != '.':
                px[ox + x, oy + y] = colors[ch]


def gen_items():
    tex = os.path.join(ASSETS, 'textures/item')
    os.makedirs(tex, exist_ok=True)
    # empty jar
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    p = img.load()
    draw_jar(p)
    jar_highlight(p)
    img.save(os.path.join(tex, 'critter_jar.png'))
    # filled jars
    for sp in LAND:
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        p = img.load()
        draw_jar(p)
        # moss floor
        for x in range(4, 12):
            p[x, 13] = (86, 120, 52, 255)
        sprite = SPRITES[sp[2]]
        w, h = len(sprite[0]), len(sprite)
        draw_sprite(p, sprite, sprite_colors(sp), 8 - w // 2, 13 - h)
        jar_highlight(p)
        img.save(os.path.join(tex, sp[0] + '_jar.png'))
    # fish buckets
    for sp in FISH:
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        p = img.load()
        for y in range(7, 15):
            inset = (y - 7) // 3
            for x in range(2 + inset, 14 - inset):
                edge = x in (2 + inset, 13 - inset) or y == 14
                p[x, y] = (88, 90, 98, 255) if edge else (168, 170, 178, 255)
                if not edge and x == 4 + inset:
                    p[x, y] = (205, 207, 214, 255)
        for x in range(2, 14):
            p[x, 7] = (60, 110, 220, 255) if 2 < x < 13 else (88, 90, 98, 255)
            p[x, 6] = (120, 122, 130, 255)
        draw_sprite(p, SPRITES['fish'], sprite_colors(sp), 4, 3)
        img.save(os.path.join(tex, sp[0] + '_bucket.png'))
    print('items done')


# ---------------------------------------------------------------------------
# Block textures
# ---------------------------------------------------------------------------

def noise_tex(name, base, var, seed, extra=None, alpha=255):
    rng = random.Random(seed)
    img = Image.new('RGBA', (16, 16))
    p = img.load()
    for y in range(16):
        for x in range(16):
            c = mul(base, 1 + rng.uniform(-var, var))
            p[x, y] = c + (alpha,)
    if extra:
        extra(p, rng)
    img.save(os.path.join(ASSETS, 'textures/block', name + '.png'))


def gen_blocks():
    os.makedirs(os.path.join(ASSETS, 'textures/block'), exist_ok=True)

    def grain(p, rng):
        for y in range(16):
            if y % 4 == 3:
                for x in range(16):
                    r, g, b, a = p[x, y]
                    p[x, y] = (int(r * 0.78), int(g * 0.78), int(b * 0.78), a)
        for _ in range(6):
            x, y = rng.randrange(16), rng.randrange(16)
            r, g, b, a = p[x, y]
            p[x, y] = (int(r * 1.15), int(g * 1.15), int(b * 1.15), a)
    noise_tex('terrarium_frame', (92, 62, 38), 0.08, 1, grain)

    def pebbles(p, rng):
        for _ in range(10):
            x, y = rng.randrange(16), rng.randrange(16)
            p[x, y] = mul((150, 140, 128), 1 + rng.uniform(-0.15, 0.15)) + (255,)
    noise_tex('terrarium_soil', (98, 70, 46), 0.12, 2, pebbles)

    def moss_tufts(p, rng):
        for _ in range(28):
            x, y = rng.randrange(16), rng.randrange(16)
            p[x, y] = mul((118, 160, 64), 1 + rng.uniform(-0.15, 0.15)) + (255,)
        for _ in range(4):
            x, y = rng.randrange(16), rng.randrange(16)
            p[x, y] = (210, 190, 80, 255)
    noise_tex('terrarium_moss', (78, 116, 44), 0.12, 3, moss_tufts)

    def metal(p, rng):
        for x in range(16):
            r, g, b, a = p[x, 0]
            p[x, 0] = (min(255, r + 30), min(255, g + 30), min(255, b + 30), a)
        for (x, y) in ((2, 2), (13, 2), (2, 13), (13, 13)):
            p[x, y] = (150, 152, 160, 255)
    noise_tex('aquarium_frame', (48, 52, 60), 0.06, 4, metal)

    def sand_bits(p, rng):
        for _ in range(14):
            x, y = rng.randrange(16), rng.randrange(16)
            p[x, y] = rng.choice([(140, 120, 96), (200, 190, 170), (96, 104, 118), (176, 120, 90)]) + (255,)
    noise_tex('aquarium_sand', (218, 202, 160), 0.07, 5, sand_bits)

    # glass: almost clear with a couple of highlight streaks
    for name, tint in (('terrarium_glass', (210, 236, 230)), ('aquarium_glass', (200, 228, 245))):
        img = Image.new('RGBA', (16, 16), tint + (34,))
        p = img.load()
        for i in range(16):
            for x, y in ((i, 15 - i), (i + 2, 15 - i), (i + 9, 15 - i)):
                if 0 <= x < 16 and 0 <= y < 16 and (x + y) % 16 not in (7, 8):
                    if (x - (15 - y)) in (0, 2) and 3 <= i <= 12:
                        p[x, y] = (255, 255, 255, 96)
        for x in range(16):
            p[x, 0] = tint + (90,)
            p[x, 15] = tint + (90,)
        for y in range(16):
            p[0, y] = tint + (90,)
            p[15, y] = tint + (90,)
        img.save(os.path.join(ASSETS, 'textures/block', name + '.png'))

    # water
    rng = random.Random(6)
    img = Image.new('RGBA', (16, 16))
    p = img.load()
    for y in range(16):
        for x in range(16):
            w = 0.5 + 0.5 * math.sin((x + y * 0.5) * 0.8) * math.cos(y * 0.6)
            c = mix((44, 96, 200), (90, 150, 235), w * 0.6 + rng.uniform(0, 0.15))
            p[x, y] = c + (112,)
    img.save(os.path.join(ASSETS, 'textures/block', 'aquarium_water.png'))

    # plants (cross sprites)
    def plant(name, stem, leaf, tall):
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        p = img.load()
        rng = random.Random(len(name))
        for strand in range(3):
            x = 4 + strand * 4
            for y in range(15, 15 - tall + strand * 2, -1):
                xx = x + int(round(math.sin(y * 0.7 + strand) * 1.2))
                if 0 <= xx < 16:
                    p[xx, y] = (leaf if rng.random() < 0.6 else stem) + (255,)
                    if y % 3 == 0 and xx + 1 < 16:
                        p[xx + 1, y] = leaf + (255,)
        img.save(os.path.join(ASSETS, 'textures/block', name + '.png'))
    plant('terrarium_plant', (60, 110, 40), (96, 168, 58), 10)
    plant('aquarium_plant', (40, 110, 70), (70, 170, 100), 13)

    # slime trail: transparent with a glossy meandering streak
    rng = random.Random(7)
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    p = img.load()
    for y in range(16):
        cx = 8 + 3.0 * math.sin(y * 0.45)
        for x in range(16):
            d = abs(x - cx)
            if d < 3.2:
                a = int(120 - d * 22 + rng.uniform(-10, 10))
                c = (206, 230, 196) if d > 1.2 else (236, 248, 228)
                p[x, y] = c + (max(30, a),)
    for _ in range(5):
        x, y = rng.randrange(16), rng.randrange(16)
        if p[x, y][3] == 0:
            p[x, y] = (220, 240, 210, 70)
    img.save(os.path.join(ASSETS, 'textures/block', 'slime_trail.png'))
    print('blocks done')


# ---------------------------------------------------------------------------
# JSON: models, blockstates, lang, recipes, loot
# ---------------------------------------------------------------------------

def box(frm, to, tex, faces=('north', 'south', 'east', 'west', 'up', 'down')):
    return {'from': frm, 'to': to, 'faces': {f: {'texture': tex} for f in faces}}


def frame_elements(tex):
    els = []
    for y0, y1 in ((0, 1), (15, 16)):
        els.append(box([0, y0, 0], [16, y1, 1], tex))
        els.append(box([0, y0, 15], [16, y1, 16], tex))
        els.append(box([0, y0, 1], [1, y1, 15], tex))
        els.append(box([15, y0, 1], [16, y1, 15], tex))
    for x, z in ((0, 0), (15, 0), (0, 15), (15, 15)):
        els.append(box([x, 1, z], [x + 1, 15, z + 1], tex, ('north', 'south', 'east', 'west')))
    return els


def glass_elements(tex, top=True):
    uv = [1, 1, 15, 15]
    els = [
        {'from': [1, 1, 0.5], 'to': [15, 15, 0.5], 'faces': {'north': {'uv': uv, 'texture': tex}, 'south': {'uv': uv, 'texture': tex}}},
        {'from': [1, 1, 15.5], 'to': [15, 15, 15.5], 'faces': {'north': {'uv': uv, 'texture': tex}, 'south': {'uv': uv, 'texture': tex}}},
        {'from': [0.5, 1, 1], 'to': [0.5, 15, 15], 'faces': {'east': {'uv': uv, 'texture': tex}, 'west': {'uv': uv, 'texture': tex}}},
        {'from': [15.5, 1, 1], 'to': [15.5, 15, 15], 'faces': {'east': {'uv': uv, 'texture': tex}, 'west': {'uv': uv, 'texture': tex}}},
    ]
    if top:
        els.append({'from': [1, 15.5, 1], 'to': [15, 15.5, 15], 'faces': {'up': {'uv': uv, 'texture': tex}, 'down': {'uv': uv, 'texture': tex}}})
    return els


def cross(x, z, h, y0, tex, size=5):
    els = []
    for ang in (45, -45):
        els.append({'from': [x - size / 2, y0, z], 'to': [x + size / 2, y0 + h, z],
                    'rotation': {'origin': [x, y0, z], 'axis': 'y', 'angle': ang},
                    'shade': False,
                    'faces': {'north': {'uv': [0, 16 - h, 16, 16], 'texture': tex},
                              'south': {'uv': [0, 16 - h, 16, 16], 'texture': tex}}})
    return els


def gen_json():
    mdl = os.path.join(ASSETS, 'models')
    # --- blocks ---
    terrarium = {
        'parent': 'minecraft:block/block',
        'ambientocclusion': False,
        'textures': {'particle': 'critters:block/terrarium_frame', 'frame': 'critters:block/terrarium_frame',
                     'glass': 'critters:block/terrarium_glass', 'moss': 'critters:block/terrarium_moss',
                     'soil': 'critters:block/terrarium_soil', 'plant': 'critters:block/terrarium_plant'},
        'elements': [
            {'from': [1, 0, 1], 'to': [15, 3, 15], 'faces': {
                'up': {'texture': '#moss'}, 'down': {'texture': '#soil'},
                'north': {'texture': '#soil'}, 'south': {'texture': '#soil'},
                'east': {'texture': '#soil'}, 'west': {'texture': '#soil'}}},
            {'from': [10, 3, 9], 'to': [13, 4, 12], 'faces': {f: {'texture': '#soil'} for f in ('north', 'south', 'east', 'west', 'up')}},
        ] + frame_elements('#frame') + cross(4, 4, 8, 3, '#plant') + cross(12, 12, 6, 3, '#plant', 4) + glass_elements('#glass'),
    }
    write_json(os.path.join(mdl, 'block/terrarium.json'), terrarium)
    aquarium = {
        'parent': 'minecraft:block/block',
        'ambientocclusion': False,
        'textures': {'particle': 'critters:block/aquarium_frame', 'frame': 'critters:block/aquarium_frame',
                     'glass': 'critters:block/aquarium_glass', 'sand': 'critters:block/aquarium_sand',
                     'water': 'critters:block/aquarium_water', 'plant': 'critters:block/aquarium_plant'},
        'elements': [
            {'from': [1, 0, 1], 'to': [15, 2, 15], 'faces': {f: {'texture': '#sand'} for f in ('north', 'south', 'east', 'west', 'up', 'down')}},
            {'from': [3, 2, 10], 'to': [6, 3, 13], 'faces': {f: {'texture': '#frame'} for f in ('north', 'south', 'east', 'west', 'up')}},
        ] + frame_elements('#frame') + cross(4, 4, 12, 2, '#plant') + cross(12, 11, 10, 2, '#plant', 4) + [
            {'from': [1, 2, 1], 'to': [15, 14, 15], 'faces': {f: {'texture': '#water'} for f in ('north', 'south', 'east', 'west', 'up', 'down')}},
        ] + glass_elements('#glass', top=False),
    }
    write_json(os.path.join(mdl, 'block/aquarium.json'), aquarium)
    write_json(os.path.join(mdl, 'block/slime_trail.json'), {
        'ambientocclusion': False,
        'textures': {'particle': 'critters:block/slime_trail', 'trail': 'critters:block/slime_trail'},
        'elements': [{'from': [0, 0, 0.1], 'to': [16, 16, 0.1], 'shade': False, 'faces': {
            'north': {'uv': [16, 0, 0, 16], 'texture': '#trail'},
            'south': {'uv': [0, 0, 16, 16], 'texture': '#trail'}}}],
    })
    bs = os.path.join(ASSETS, 'blockstates')
    for b in ('terrarium', 'aquarium'):
        write_json(os.path.join(bs, b + '.json'), {'variants': {'': {'model': 'critters:block/' + b}}})
        write_json(os.path.join(mdl, 'item', b + '.json'), {'parent': 'critters:block/' + b})
    rots = {'north': {}, 'east': {'y': 90}, 'south': {'y': 180}, 'west': {'y': 270}, 'up': {'x': 270}, 'down': {'x': 90}}
    mp = []
    for face, r in rots.items():
        apply = {'model': 'critters:block/slime_trail'}
        apply.update(r)
        if r:
            apply['uvlock'] = True
        mp.append({'when': {face: 'true'}, 'apply': apply})
    write_json(os.path.join(bs, 'slime_trail.json'), {'multipart': mp})

    # --- items ---
    write_json(os.path.join(mdl, 'item/critter_jar.json'), {'parent': 'minecraft:item/generated', 'textures': {'layer0': 'critters:item/critter_jar'}})
    for sp in LAND:
        write_json(os.path.join(mdl, 'item', sp[0] + '_jar.json'), {'parent': 'minecraft:item/generated', 'textures': {'layer0': 'critters:item/%s_jar' % sp[0]}})
    for sp in FISH:
        write_json(os.path.join(mdl, 'item', sp[0] + '_bucket.json'), {'parent': 'minecraft:item/generated', 'textures': {'layer0': 'critters:item/%s_bucket' % sp[0]}})
    for sp in SPECIES:
        write_json(os.path.join(mdl, 'item', sp[0] + '_spawn_egg.json'), {'parent': 'minecraft:item/template_spawn_egg'})

    # --- lang ---
    lang = {
        'itemGroup.critters': 'Critters',
        'block.critters.terrarium': 'Terrarium',
        'block.critters.aquarium': 'Aquarium',
        'block.critters.slime_trail': 'Slime Trail',
        'item.critters.critter_jar': 'Critter Jar',
        'tooltip.critters.jar.empty': 'Use on a critter to catch it',
        'tooltip.critters.jar.filled': 'Use on a block to release it, or on a Terrarium to display it',
        'tooltip.critters.jar.name': 'Name: %s',
        'tooltip.critters.display.terrarium': 'Use a Critter Jar on it to add a critter (holds %s)',
        'tooltip.critters.display.aquarium': 'Use a fish bucket on it to add a fish (holds %s)',
        'tooltip.critters.display.remove': 'Sneak + use with an empty hand to take one out',
        'message.critters.display.empty': 'This %s is empty.',
        'message.critters.display.contents': '%s (%s/%s): %s',
        'message.critters.display.full': 'There is no more room in here!',
        'message.critters.display.not_terrarium': 'Land critters belong in a Terrarium.',
        'message.critters.display.not_aquarium': 'Fish belong in an Aquarium.',
        'message.critters.jar.cannot_catch': 'That creature does not fit in a jar.',
    }
    for sid, name, fam, *_ in SPECIES:
        lang['entity.critters.' + sid] = name
        lang['item.critters.%s_spawn_egg' % sid] = name + ' Spawn Egg'
        if fam == 'fish':
            lang['item.critters.%s_bucket' % sid] = 'Bucket of ' + name
        else:
            lang['item.critters.%s_jar' % sid] = name + ' in a Jar'
    write_json(os.path.join(ASSETS, 'lang/en_us.json'), lang)

    # --- recipes ---
    rec = os.path.join(DATA, 'recipe')
    write_json(os.path.join(rec, 'critter_jar.json'), {
        'type': 'minecraft:crafting_shaped', 'category': 'misc',
        'pattern': [' W ', 'G G', 'GGG'],
        'key': {'W': {'tag': 'minecraft:wooden_buttons'}, 'G': {'item': 'minecraft:glass'}},
        'result': {'id': 'critters:critter_jar', 'count': 3}})
    write_json(os.path.join(rec, 'terrarium.json'), {
        'type': 'minecraft:crafting_shaped', 'category': 'decorations',
        'pattern': ['LGL', 'G G', 'LDL'],
        'key': {'L': {'tag': 'minecraft:planks'}, 'G': {'item': 'minecraft:glass'}, 'D': {'tag': 'minecraft:dirt'}},
        'result': {'id': 'critters:terrarium', 'count': 1}})
    write_json(os.path.join(rec, 'aquarium.json'), {
        'type': 'minecraft:crafting_shaped', 'category': 'decorations',
        'pattern': ['IGI', 'GWG', 'ISI'],
        'key': {'I': {'item': 'minecraft:iron_nugget'}, 'G': {'item': 'minecraft:glass'},
                'W': {'item': 'minecraft:water_bucket'}, 'S': {'tag': 'minecraft:sand'}},
        'result': {'id': 'critters:aquarium', 'count': 1}})

    # --- loot tables ---
    lt = os.path.join(DATA, 'loot_table')
    for b in ('terrarium', 'aquarium'):
        write_json(os.path.join(lt, 'blocks', b + '.json'), {
            'type': 'minecraft:block',
            'pools': [{'rolls': 1, 'bonus_rolls': 0, 'entries': [{'type': 'minecraft:item', 'name': 'critters:' + b}],
                       'conditions': [{'condition': 'minecraft:survives_explosion'}]}],
            'random_sequence': 'critters:blocks/' + b})
    snail_drops = {'garden_snail': 'minecraft:slime_ball', 'banded_snail': 'minecraft:slime_ball',
                   'moss_snail': 'minecraft:moss_carpet', 'amethyst_snail': 'minecraft:amethyst_shard',
                   'magma_snail': 'minecraft:magma_cream'}
    for sid, item in snail_drops.items():
        write_json(os.path.join(lt, 'entities', sid + '.json'), {
            'type': 'minecraft:entity',
            'pools': [{'rolls': 1, 'bonus_rolls': 0, 'entries': [{
                'type': 'minecraft:item', 'name': item,
                'functions': [{'function': 'minecraft:set_count', 'count': {'type': 'minecraft:uniform', 'min': 0, 'max': 1}},
                              {'function': 'minecraft:enchanted_count_increase', 'enchantment': 'minecraft:looting',
                               'count': {'type': 'minecraft:uniform', 'min': 0, 'max': 1}}]}]}],
            'random_sequence': 'critters:entities/' + sid})

    # --- tags ---
    write_json(os.path.join(ROOT, 'src/main/resources/data/minecraft/tags/entity_type/can_breathe_under_water.json'),
               {'replace': False, 'values': ['critters:fiddler_crab', 'critters:rock_crab']})

    # --- mod icon ---
    ladybug = Image.open(os.path.join(ASSETS, 'textures/item/ladybug_jar.png'))
    ladybug.resize((128, 128), Image.NEAREST).save(os.path.join(ASSETS, 'icon.png'))
    print('json done')


def check_java_species():
    if not os.path.exists(SPECIES_JAVA):
        return
    src = open(SPECIES_JAVA).read()
    ids = re.findall(r'^\s+[A-Z_]+\("([a-z_]+)"', src, re.M)
    want = [s[0] for s in SPECIES]
    if ids != want:
        raise SystemExit('CritterSpecies.java ids %s do not match generator %s' % (ids, want))


if __name__ == '__main__':
    gen_models()
    gen_items()
    gen_blocks()
    gen_json()
    check_java_species()
    print('ok')
