"""
Fish, sea animals and amphibians (second wave of critters).

Same conventions as critter_models.py: UPB units per block, +Y down, ground at y = 24, front = -Z.
Looks: blobfish + anglerfish are the ugly ones, dumbo octopus + rain frog the cute ones.
"""
import math

import numpy as np

from shading import C, Out, mix, smooth, fract, fbm, worley
from critter_models import (GROUND, Cube, Ell, Model, Part, box, chitin, ellipsoid, eye_mat, fan, fin_mat, leg,
                            plane, rod, sail, skin, ellipse_outline)


# ---------------------------------------------------------------------------
# shared helpers
# ---------------------------------------------------------------------------

def seg_chain(name, n, seg, thick, mat, direction='+y', bend=(0, 0, 0), first_rot=(0, 0, 0), pivot=(0, 0, 0),
              taper=None):
    """Chain of segments along an axis; each child bends a little more (curling tails, tentacles, arms)."""
    root = parent = None
    parts = []
    for i in range(n):
        t = thick if taper is None else max(1, int(round(thick + (taper - thick) * i / max(1, n - 1))))
        axis, sign = direction[1], (1 if direction[0] == '+' else -1)
        cube = rod(axis, 0, seg, t, mat, sign=sign)
        off = [0.0, 0.0, 0.0]
        off['xyz'.index(axis)] = sign * (seg - 0.3)
        p = Part(name if i == 0 else '%s_%d' % (name, i), pivot if i == 0 else tuple(off),
                 first_rot if i == 0 else bend, [cube])
        if parent is None:
            root = p
        else:
            parent.add(p)
        parent = p
        parts.append(p)
    return root, parts


def cute_eye(base=0x0A0A10, side=1):
    """Big glossy eye with two catch-lights."""
    basec = C(base)

    def m(c):
        o = Out(c.N, basec)
        d1 = np.sqrt((c.q[:, 0] - side * 0.35) ** 2 + (c.q[:, 1] + 0.45) ** 2 + (c.q[:, 2] + 0.55) ** 2)
        d2 = np.sqrt((c.q[:, 0] - side * 0.55) ** 2 + (c.q[:, 1] - 0.2) ** 2 + (c.q[:, 2] + 0.5) ** 2)
        o.col = mix(o.col, C(0xFFFFFF), smooth(0.38, 0.25, d1))
        o.col = mix(o.col, C(0xE8F0FF), smooth(0.22, 0.12, d2) * 0.8)
        o.spec[:] = 1.0
        o.shin[:] = 90
        o.flat = smooth(0.38, 0.25, d1)
        return o
    return m


def wet(base, var=0.15, freq=0.3, seed=1, spec=0.6, blotch=None, blotch_amt=0.0, veins=None, alpha=255):
    """Soft, slimy tissue (blobfish, octopus, slugs)."""
    base = C(base)

    def m(c):
        col = base * (1 + var * fbm(c.p * freq, 3, seed))[:, None]
        if blotch is not None:
            col = mix(col, C(blotch), smooth(0.05, 0.45, fbm(c.p * 0.22, 3, seed + 3)) * blotch_amt)
        if veins is not None:
            f1, f2, _ = worley(c.p * 0.35, seed + 5)
            col = mix(col, C(veins), smooth(0.06, 0.0, f2 - f1) * 0.5)
        o = Out(c.N, col)
        o.spec[:] = spec
        o.shin[:] = 35
        o.alpha[:] = alpha
        return o
    return m


def standard_fins(body, rx, ry, rz, fin_c, fin_e, fin_a, dorsal=(10, 6), anal=(7, 5), tail=(12, 16), fork=False,
                  lobed=False, stripes=None, pect=(6, 4), dorsal_mat=None):
    tb = Part('tail_base', (0, 0, rz - 2), (0, 0, 0))
    tf = Part('tail_fin', (0, 0, 5.5), (0, 0, 0))
    fm = fin_mat(fin_c, fin_e, fin_a, outline=fan(lobed=lobed, fork=fork), ray_origin=(0.0, 0.5), rays=14, stripes=stripes)
    tf.add(plane('yz', 0, -tail[1] / 2, 0, tail[0], tail[1], fm))
    tb.add(tf)
    body.add(tb)
    dm = dorsal_mat or fin_mat(fin_c, fin_e, fin_a, outline=sail(), ray_origin=(0.0, 1.0), rays=9, grad_axis=1, stripes=stripes)
    am = fin_mat(fin_c, fin_e, fin_a, outline=lambda u, v: sail()(u, 1 - v), ray_origin=(0.0, 0.0), rays=9, grad_axis=1,
                 stripes=stripes)
    if dorsal:
        body.add(Part('dorsal_fin', (0, -ry + 0.8, -rz * 0.3), (0.1, 0, 0), [plane('yz', 0, -dorsal[1], 0, dorsal[0], dorsal[1], dm)]))
    if anal:
        body.add(Part('anal_fin', (0, ry - 0.8, rz * 0.15), (-0.1, 0, 0), [plane('yz', 0, 0, 0, anal[0], anal[1], am)]))
    pm = fin_mat(fin_c, fin_e, max(60, fin_a - 40), outline=fan(), ray_origin=(0.0, 0.5), rays=8, stripes=stripes)
    for side, nm in ((1, 'l'), (-1, 'r')):
        body.add(Part('fin_' + nm, (side * rx * 0.8, ry * 0.3, -rz * 0.45), (0, side * 0.7, side * 0.3),
                      [plane('yz', 0, -pect[1] / 2, 0, pect[0], pect[1], pm)]))
        body.add(Part('pelvic_' + nm, (side * rx * 0.5, ry * 0.85, -rz * 0.1), (-0.5, 0, side * 0.35),
                      [plane('yz', 0, 0, 0, 4, 4, pm)]))


# ---------------------------------------------------------------------------
# FISH
# ---------------------------------------------------------------------------

def clown_mat(zoff, rz, ry):
    def m(c):
        zb = (c.z + zoff) / rz
        yb = c.y / ry
        col = mix(C(0xF26A10), C(0xFF9A3A), smooth(-0.6, 0.6, fbm(c.p * 0.3, 3, 401)))
        col = mix(col, C(0xD8500A), smooth(0.1, -0.8, yb) * 0.4)
        white = np.zeros(c.N)
        black = np.zeros(c.N)
        for bz, bw in ((-0.58, 0.11), (-0.02, 0.12), (0.78, 0.07)):
            d = np.abs(zb - bz - 0.06 * np.sin(yb * 3))
            white = np.maximum(white, smooth(bw, bw - 0.03, d))
            black = np.maximum(black, smooth(bw + 0.045, bw + 0.015, d) * (1 - smooth(bw, bw - 0.03, d)))
        col = mix(col, C(0xFAFAF4), white)
        col = mix(col, C(0x141010), black)
        o = Out(c.N, col)
        o.spec[:] = 0.7
        o.shin[:] = 50
        return o
    return m


def fish_eye(side, iris=0xD8A830):
    return eye_mat(iris=iris, base=0xD8D8D0, out_axis=(side, 0, 0), ring=0.75)


def clownfish():
    R = (3.4, 5.6, 9.0)
    rx, ry, rz = R
    body = Part('body', (0, GROUND - 8, 0))
    body.add(ellipsoid((0, 0, 0), R, clown_mat(0, rz, ry), axis='z', step=2))
    head = Part('head', (0, 0, -rz + 3), (0, 0, 0))
    hm = clown_mat(-rz + 3, rz, ry)
    head.add(ellipsoid((0, 0.5, -2.5), (rx * 0.85, ry * 0.75, 4.5), hm, axis='z', step=1))
    for side in (1, -1):
        head.add(ellipsoid((side * (rx * 0.85 - 0.6), -0.8, -3.6), (0.9, 1.4, 1.4), fish_eye(side, 0xF07A20), axis='y', step=1))
    body.add(head)
    fin_c, fin_e = 0xF27A1A, 0x101010
    standard_fins(body, rx, ry, rz, fin_c, fin_e, 230, dorsal=(14, 5), anal=(6, 4), tail=(8, 11), lobed=True)
    return Model('clownfish', [body])


def lionfish():
    R = (3.4, 5.5, 10.0)
    rx, ry, rz = R

    def lion_mat(zoff):
        def m(c):
            zb = (c.z + zoff)
            stripe = 0.5 + 0.5 * np.sin(zb * 1.25 + c.y * 0.35 + fbm(c.p * 0.2, 2, 411) * 1.5)
            col = mix(C(0xF4ECE0), C(0x8A1A12), smooth(0.45, 0.6, stripe))
            col = mix(col, C(0x4A0E0A), smooth(0.85, 0.95, stripe) * 0.6)
            o = Out(c.N, col)
            o.spec[:] = 0.5
            o.shin[:] = 40
            return o
        return m

    def lion_fin(c):
        u, v = c.uv[:, 0], c.uv[:, 1]
        ang = np.arctan2(v - 0.5, u + 1e-6)
        rays = smooth(0.82, 0.95, np.abs(np.sin(ang * 9)))
        bands = smooth(0.4, 0.55, 0.5 + 0.5 * np.sin(u * 22))
        col = mix(C(0xF2E8DA), C(0x9A2014), bands)
        o = Out(c.N, col)
        # feathery fan with spiky ray tips poking out beyond the membrane
        r = u
        membrane = smooth(0.82, 0.7, r) * fan()(u, v)
        spikes = rays * smooth(1.0, 0.9, r) * smooth(0.45, 0.0, np.abs(v - 0.5) - 0.1 * u)
        o.alpha = np.clip(np.maximum(membrane * 190, spikes * 255), 0, 255)
        o.spec[:] = 0.2
        o.flat[:] = 0.3
        return o

    spine_m = chitin(0xF0E2D0, spec=0.3, var=0.1, freq=1.5)

    def spine_band(c):
        o = spine_m(c)
        band = smooth(0.4, 0.6, 0.5 + 0.5 * np.sin(c.y * 1.6))
        o.col = mix(o.col, C(0x8A1A12), band)
        return o

    body = Part('body', (0, GROUND - 9, 0))
    body.add(ellipsoid((0, 0, 0), R, lion_mat(0), axis='z', step=2))
    head = Part('head', (0, 0, -rz + 3), (0, 0, 0))
    head.add(ellipsoid((0, 0.5, -2.5), (rx * 0.85, ry * 0.8, 5), lion_mat(-rz + 3), axis='z', step=1))
    for side in (1, -1):
        head.add(ellipsoid((side * (rx * 0.85 - 0.5), -1.2, -3.5), (0.9, 1.4, 1.4), fish_eye(side, 0xC83020), axis='y', step=1))
        head.add(Part('brow_%d' % (side + 1), (side * 1.5, -3.5, -3.5), (-0.3, 0, side * 0.3),
                      [rod('y', 0, 4, 1, spine_band, sign=-1)]))       # feathery "eyebrow" tentacles
    body.add(head)
    # venomous dorsal spines, each with a scrap of striped membrane
    for i in range(10):
        h = 13 - i * 0.8
        z = -rz * 0.55 + i * 1.5
        sp = Part('spine_%d' % i, (0, -ry + 1.5, z), (0.35 + i * 0.03, 0, 0), [rod('y', 0, h, 1, spine_band, sign=-1)])
        sp.add(plane('yz', 0, -h * 0.55, 0, 1.6, h * 0.55, fin_mat(0xF0E0D0, 0x8A1A12, 150, stripes=(0x8A1A12, 3, 1))))
        body.add(sp)
    body.children[-1].name = 'dorsal_fin'   # last spine animates as the dorsal fin
    # huge fan-like pectoral fins
    for side, nm in ((1, 'l'), (-1, 'r')):
        body.add(Part('fin_' + nm, (side * rx * 0.7, ry * 0.25, -rz * 0.35), (0.15, side * 1.15, side * 0.25),
                      [plane('yz', 0, -8, 0, 17, 16, lion_fin)]))
        body.add(Part('pelvic_' + nm, (side * 1.0, ry * 0.85, -rz * 0.2), (-0.6, 0, side * 0.3),
                      [plane('yz', 0, 0, 0, 6, 6, lion_fin)]))
    tb = Part('tail_base', (0, 0, rz - 2), (0, 0, 0))
    tf = Part('tail_fin', (0, 0, 5.5), (0, 0, 0))
    tf.add(plane('yz', 0, -6, 0, 9, 12, fin_mat(0xF4ECE0, 0xF4ECE0, 170, outline=fan(), stripes=(0x8A1A12, 4, 0))))
    tb.add(tf)
    body.add(tb)
    body.add(Part('anal_fin', (0, ry - 0.8, rz * 0.2), (-0.2, 0, 0),
                  [plane('yz', 0, 0, 0, 6, 6, fin_mat(0xF4ECE0, 0xF4ECE0, 170, outline=lambda u, v: sail()(u, 1 - v),
                                                      stripes=(0x8A1A12, 3, 1)))]))
    return Model('lionfish', [body])


def seahorse():
    def hide(c):
        rings = smooth(0.35, 0.5, np.abs(fract(c.y * 0.42) - 0.5) * 2)
        col = mix(C(0xE8A828), C(0xF6CC50), smooth(-0.6, 0.6, fbm(c.p * 0.3, 3, 421)))
        col = mix(col, C(0x9A5A14), (1 - rings) * 0.6)
        dots = smooth(0.62, 0.7, fbm(c.p * 1.5, 2, 423))
        col = mix(col, C(0xFFF4D8), dots * 0.8)
        o = Out(c.N, col)
        o.spec[:] = 0.4
        o.shin[:] = 30
        return o

    body = Part('body', (0, GROUND - 18, 0))
    body.add(ellipsoid((0, -1, 0.5), (3.0, 7.0, 3.6), hide, axis='y', step=1))
    body.add(ellipsoid((0, 1.5, -0.8), (2.8, 4.0, 3.4), hide, axis='y', step=1))     # belly / pouch
    head = Part('head', (0, -8, -0.5), (0.0, 0, 0))
    head.add(ellipsoid((0, -2, -0.8), (2.2, 2.6, 3.0), hide, axis='y', step=1))
    snout = Part('snout', (0, -1.2, -3), (-0.15, 0, 0), [rod('z', 0, 6, 2, hide, sign=-1)])
    snout.add(box(-1, -1, -7, 2, 2, 1, hide))
    head.add(snout)
    head.add(box(-1, -6, -0.5, 2, 2, 2, hide), box(-0.5, -7, 0, 1, 1, 1, hide))       # coronet
    for side in (1, -1):
        head.add(ellipsoid((side * 2.0, -2.4, -1.4), (0.8, 1.1, 1.1), fish_eye(side, 0xE8B030), axis='y', step=1))
        head.add(Part('fin_' + ('l' if side > 0 else 'r'), (side * 2.0, -0.8, 1.0), (0, side * 0.6, 0),
                      [plane('yz', 0, -1.5, 0, 3, 3, fin_mat(0xF8E0A0, 0xFFFFFF, 120, outline=fan()))]))
    body.add(head)
    body.add(Part('dorsal_fin', (0, -1, 3.8), (0, 0, 0),
                  [plane('yz', 0, -3, 0, 3, 6, fin_mat(0xF8E0A0, 0xFFFFFF, 140, rays=12))]))
    tail, segs = seg_chain('tail_base', 7, 2.4, 3, hide, '+y', bend=(-0.38, 0, 0), first_rot=(0.15, 0, 0),
                           pivot=(0, 5.5, 0.8), taper=1)
    body.add(tail)
    return Model('seahorse', [body])


def blobfish():
    flesh = wet(0xE6A49A, var=0.18, freq=0.25, seed=431, spec=0.75, blotch=0xC8746C, blotch_amt=0.6, veins=0xB0485A)
    nose = wet(0xD88480, var=0.15, freq=0.4, seed=433, spec=0.8, blotch=0xB45A5A, blotch_amt=0.5, veins=0x9A3A4A)
    lip = wet(0xA8505A, var=0.12, freq=0.5, seed=435, spec=0.7)
    body = Part('body', (0, GROUND - 8, 0))
    body.add(ellipsoid((0, 0.5, 0.5), (7.0, 6.0, 7.0), flesh, axis='y', step=1))
    body.add(ellipsoid((0, 4.0, 0.5), (6.8, 3.0, 6.5), flesh, axis='y', step=1))     # sagging belly
    head = Part('head', (0, 0, -4.5), (0, 0, 0))
    head.add(ellipsoid((0, 0.5, -1.5), (6.2, 5.2, 4.8), flesh, axis='y', step=1))
    for side in (1, -1):
        head.add(ellipsoid((side * 4.2, 3.5, -2.5), (2.6, 2.8, 2.6), flesh, axis='y', step=1))   # sagging jowls
    nosep = Part('nose', (0, -2.0, -5.5), (0.3, 0, 0))
    nosep.add(ellipsoid((0, 3.5, -1.2), (3.2, 4.8, 3.0), nose, axis='y', step=1))     # the famous droopy nose
    head.add(nosep)
    jaw = Part('jaw', (0, 4.5, -4.5), (0, 0, 0))
    jaw.add(ellipsoid((0, 1.0, -0.5), (4.6, 1.3, 1.6), lip, axis='y', step=1))       # frowning lips
    for side in (1, -1):
        jaw.add(box(side * 4.0 - 0.5, 1.5, -0.5, 1, 3, 1, lip))                        # drooping corners
    head.add(jaw)
    for side in (1, -1):
        head.add(ellipsoid((side * 3.6, -2.0, -4.6), (0.7, 0.7, 0.7), eye_mat(base=0x0A0606, glossy=1.0), axis='y', step=1))
    body.add(head)
    tb = Part('tail_base', (0, 0, 6.5), (0, 0, 0))
    tb.add(ellipsoid((0, 0, 2), (2.0, 2.4, 3.0), flesh, axis='z', step=1))
    tf = Part('tail_fin', (0, 0, 4.5), (0, 0, 0))
    tf.add(plane('yz', 0, -4, 0, 5, 8, fin_mat(0xE8A8A0, 0xF0C8C0, 150, outline=fan(lobed=True))))
    tb.add(tf)
    body.add(tb)
    for side, nm in ((1, 'l'), (-1, 'r')):
        body.add(Part('fin_' + nm, (side * 5.5, 2, -3), (0, side * 0.9, side * 0.5),
                      [plane('yz', 0, -2, 0, 5, 4, fin_mat(0xE8A8A0, 0xF4D0C8, 140, outline=fan()))]))
    return Model('blobfish', [body])


def anglerfish():
    hide = wet(0x2A2220, var=0.25, freq=0.3, seed=441, spec=0.35, blotch=0x4A3A30, blotch_amt=0.6, veins=0x14100E)

    def lumpy(c):
        o = hide(c)
        f1, _, _ = worley(c.p * 0.6, 443)
        o.col = o.col * (0.7 + 0.45 * smooth(0.0, 0.6, f1))[:, None]
        return o

    def mouth(c):
        o = lumpy(c)
        inside = smooth(-0.2, 0.4, -c.q[:, 1]) if c.q is not None else 0
        o.col = mix(o.col, C(0x5A1A1A), inside * 0.7)
        return o

    tooth = chitin(0xEDE6D8, spec=0.8, var=0.08, freq=1.2)

    def esca(c):
        o = Out(c.N, C(0xD8FFF4))
        o.add_glow(C(0x9AFFE8), np.ones(c.N))
        o.flat[:] = 1
        return o

    body = Part('body', (0, GROUND - 8.5, 0))
    body.add(ellipsoid((0, 0, 1), (6.2, 6.6, 8.0), lumpy, axis='y', step=1))
    head = Part('head', (0, -1, -5), (0, 0, 0))
    head.add(ellipsoid((0, -1.0, -3.0), (5.6, 4.0, 5.0), lumpy, axis='y', step=1))
    for i in range(7):     # upper teeth hanging down
        x = -4.2 + i * 1.4
        head.add(box(x - 0.5, 2.0, -7.5 + abs(x) * 0.35, 1, 3 if i % 2 else 2, 1, tooth))
    for side in (1, -1):
        head.add(ellipsoid((side * 3.2, -2.5, -5.0), (0.7, 0.7, 0.7), eye_mat(base=0xB8C0A0, glossy=1.0), axis='y', step=1))
    lure, segs = seg_chain('lure', 4, 3.0, 1, lumpy, '-y', bend=(-0.35, 0, 0), first_rot=(-0.3, 0, 0), pivot=(0, -4.5, -5))
    segs[-1].add(ellipsoid((0, -3.4, -0.3), (1.4, 1.5, 1.4), esca, axis='y', step=1))
    head.add(lure)
    jaw = Part('jaw', (0, 2.0, -2.0), (0.0, 0, 0))
    jaw.add(ellipsoid((0, 1.5, -4.0), (5.8, 2.6, 6.0), mouth, axis='y', step=1))
    for i in range(8):     # lower fangs pointing up, splayed outwards
        x = -4.8 + i * 1.37
        jaw.add(box(x - 0.5, -3.5 + (0 if i % 2 else 1), -9.4 + abs(x) * 0.4, 1, 4 if i % 2 else 3, 1, tooth))
    head.add(jaw)
    body.add(head)
    tb = Part('tail_base', (0, 0, 8.5), (0, 0, 0))
    tb.add(ellipsoid((0, 0, 2), (2.2, 2.6, 3.0), lumpy, axis='z', step=1))
    tf = Part('tail_fin', (0, 0, 4.5), (0, 0, 0))
    tf.add(plane('yz', 0, -5, 0, 7, 10, fin_mat(0x2A2220, 0x14100E, 170, outline=fan(lobed=True), rays=10)))
    tb.add(tf)
    body.add(tb)
    for side, nm in ((1, 'l'), (-1, 'r')):
        body.add(Part('fin_' + nm, (side * 5.8, 2, -1), (0, side * 0.8, side * 0.4),
                      [plane('yz', 0, -2, 0, 5, 4, fin_mat(0x2A2220, 0x14100E, 170, outline=fan()))]))
    body.add(Part('dorsal_fin', (0, -6.3, 4), (0.2, 0, 0),
                  [plane('yz', 0, -3, 0, 6, 3, fin_mat(0x2A2220, 0x14100E, 170, outline=sail()))]))
    return Model('anglerfish', [body], glow=True)


# ---------------------------------------------------------------------------
# SEA ANIMALS
# ---------------------------------------------------------------------------

def jellyfish():
    def bell_mat(c):
        q = c.q
        r = np.sqrt(q[:, 0] ** 2 + q[:, 2] ** 2)
        ang = np.arctan2(q[:, 2], q[:, 0])
        o = Out(c.N, mix(C(0xD8E8F4), C(0xF4F8FF), smooth(0.2, 0.9, r)))
        # four horseshoe-shaped gonads
        lobe = 0.5 + 0.5 * np.cos(ang * 4)
        ring = smooth(0.12, 0.0, np.abs(r - (0.32 + 0.1 * lobe)))
        horse = ring * smooth(0.2, 0.6, lobe)
        o.col = mix(o.col, C(0xD878C8), horse)
        canals = smooth(0.06, 0.0, np.abs(np.sin(ang * 8))) * smooth(0.35, 0.9, r)
        o.col = mix(o.col, C(0xF0C8F0), canals * 0.5)
        rim = smooth(0.85, 1.0, r)
        o.alpha = 95 + 120 * horse + 60 * rim + 30 * canals
        o.add_glow(C(0xB868E8), horse * 0.8 + rim * 0.25)
        o.spec[:] = 0.9
        o.shin[:] = 60
        return o

    def tent_mat(c):
        o = Out(c.N, C(0xF0E8F8))
        o.alpha[:] = 150
        o.add_glow(C(0x8A60C0), np.full(c.N, 0.3))
        return o

    def oral_mat(c):
        u, v = c.uv[:, 0], c.uv[:, 1]
        frill = smooth(0.0, 0.15, 0.5 - np.abs(u - 0.5) - 0.12 * np.sin(v * 20) ** 2 * (0.3 + v))
        o = Out(c.N, mix(C(0xF4E0F4), C(0xD898D8), v))
        o.alpha = np.clip(frill * 200, 0, 255)
        o.add_glow(C(0xA060C0), frill * 0.35)
        return o

    bell = Part('body', (0, GROUND - 22, 0))
    bell.add(ellipsoid((0, 0, 0), (9, 6, 9), bell_mat, axis='y', step=1, half='neg'))
    bell.add(ellipsoid((0, 0, 0), (8.5, 1.5, 8.5), bell_mat, axis='y', step=1, half='pos'))
    for i in range(16):
        a = i / 16 * 2 * math.pi
        t, _ = seg_chain('tentacle_%d' % i, 5, 3.0, 1, tent_mat, '+y', bend=(0.06 * math.cos(a), 0, -0.06 * math.sin(a)),
                         pivot=(math.cos(a) * 8.2, 0.5, math.sin(a) * 8.2))
        bell.add(t)
    for i in range(4):
        a = i / 4 * math.pi + 0.4
        bell.add(Part('oral_%d' % i, (0, 1.0, 0), (0, a, 0.1 if i % 2 else -0.1),
                      [plane('xy', -2, 0, 0, 4, 15, oral_mat)]))
    return Model('jellyfish', [bell], glow=True)


def dumbo_octopus():
    flesh = wet(0xEE8A80, var=0.12, freq=0.3, seed=451, spec=0.55, blotch=0xF8A898, blotch_amt=0.5)

    def mantle_mat(c):
        o = flesh(c)
        o.col = mix(o.col, C(0xF8B0A0), smooth(-0.2, -0.9, c.q[:, 1]) * 0.35)
        speck = smooth(0.68, 0.74, fbm(c.p * 1.6, 2, 453))
        o.col = mix(o.col, C(0xD87870), speck * 0.6)
        blush = smooth(0.35, 0.0, np.sqrt((np.abs(c.q[:, 0]) - 0.6) ** 2 + (c.q[:, 1] - 0.05) ** 2 + (c.q[:, 2] + 0.7) ** 2))
        o.col = mix(o.col, C(0xFF8A8A), blush * 0.5)
        return o

    web = wet(0xF09A8C, var=0.1, freq=0.4, seed=455, spec=0.4, alpha=235)
    body = Part('body', (0, GROUND - 14, 0))
    body.add(ellipsoid((0, -4, 0), (6.2, 7.5, 6.2), mantle_mat, axis='y', step=1))
    body.add(ellipsoid((0, 3.2, 0), (9.0, 1.4, 9.0), web, axis='y', step=1))        # webbed umbrella
    body.add(ellipsoid((0, 1.8, 0), (7.0, 1.4, 7.0), web, axis='y', step=1))
    for side in (1, -1):
        body.add(ellipsoid((side * 3.0, -3.2, -4.6), (1.6, 2.0, 1.4), cute_eye(side=side), axis='y', step=1))
    for side, nm in ((1, 'l'), (-1, 'r')):
        ear = Part('ear_' + nm, (side * 4.8, -8.5, 0), (0, 0, -side * 0.35))
        ear.add(ellipsoid((side * 3.2, 0, 0), (3.4, 0.9, 2.6), flesh, axis='x', step=1))
        body.add(ear)
    for i in range(8):
        a = (i + 0.5) / 8 * 2 * math.pi
        arm, _ = seg_chain('arm_%d' % i, 3, 3.0, 2, flesh, '+z', bend=(0.35, 0, 0), first_rot=(-0.25, a, 0),
                           pivot=(math.sin(a) * 4.0, 3.2, math.cos(a) * 4.0), taper=1)
        body.add(arm)
    return Model('dumbo_octopus', [body])


def sea_star():
    def star_mat(c):
        f1, f2, _ = worley(c.p * 0.7, 461)
        bump = smooth(0.35, 0.08, f1)
        net = smooth(0.08, 0.0, f2 - f1)
        col = mix(C(0xC8401E), C(0xE86A2E), smooth(-0.5, 0.6, fbm(c.p * 0.3, 3, 463)))
        col = mix(col, C(0x7A1E24), net * 0.6)
        col = mix(col, C(0xFFC890), bump * 0.75)
        under = np.clip(c.n[:, 1], 0, 1)
        col = mix(col, C(0xF0D8B0), smooth(0.3, 0.8, under))
        o = Out(c.N, col)
        o.spec[:] = 0.25 + bump * 0.3
        o.shin[:] = 20
        return o

    body = Part('body', (0, GROUND, 0))
    body.add(ellipsoid((0, -2.0, 0), (4.5, 2.0, 4.5), star_mat, axis='y', step=1))
    for i in range(5):
        a = i / 5 * 2 * math.pi
        arm = Part('arm_%d' % i, (math.sin(a) * 2.5, -1.5, -math.cos(a) * 2.5), (0, -a, 0))
        parent = arm
        for k in range(4):
            w = 3.4 - k * 0.7
            seg = Part('arm_%d_%d' % (i, k), (0, 0, 0) if k == 0 else (0, 0.1, -3.0), (-0.04, 0, 0))
            seg.add(ellipsoid((0, -0.2 + k * 0.1, -1.8), (w, 1.6 - k * 0.25, 2.2), star_mat, axis='y', step=1))
            parent.add(seg)
            parent = seg
        body.add(arm)
    return Model('sea_star', [body])


def nudibranch():
    def slug(c):
        q = c.q
        col = mix(C(0x3A2AD8), C(0x6A5AF8), smooth(-0.5, 0.6, fbm(c.p * 0.3, 3, 471)))
        margin = smooth(0.7, 0.85, np.abs(q[:, 0]))
        col = mix(col, C(0xFFF0A0), margin)
        line = smooth(0.06, 0.0, np.abs(np.abs(q[:, 0]) - 0.5)) * (1 - margin)
        col = mix(col, C(0x0A0820), line)
        spots = smooth(0.66, 0.72, fbm(c.p * 1.3, 2, 473)) * (1 - margin)
        col = mix(col, C(0xFFFFFF), spots * 0.7)
        o = Out(c.N, col)
        o.spec[:] = 0.8
        o.shin[:] = 45
        return o

    orange = chitin(0xF06A14, spec=0.5, var=0.15, freq=1.0)

    def gill_mat(c):
        o = orange(c)
        tip = smooth(0.4, 0.9, -c.p[:, 1] / 4.0)
        o.col = mix(o.col, C(0xFFF4E0), tip)
        return o

    body = Part('body', (0, GROUND, 0))
    body.add(ellipsoid((0, -2.0, 0), (3.6, 2.4, 10.5), slug, axis='z', step=2, flat=0))
    body.add(ellipsoid((0, -0.5, 0), (4.6, 0.7, 11.5), slug, axis='z', step=3, flat=0))
    for side, nm in ((1, 'l'), (-1, 'r')):
        rh = Part('rhinophore_' + nm, (side * 1.6, -4.2, -7.5), (-0.2, 0, side * 0.25))
        rh.add(rod('y', 0, 2, 1, orange, sign=-1), box(-1, -5, -1, 2, 3, 2, orange))
        body.add(rh)
        tent = Part('tentacle_' + nm, (side * 2.5, -1.5, -10), (0.6, -side * 0.6, 0), [rod('z', 0, 3, 1, slug, sign=-1)])
        body.add(tent)
    gills = Part('gills', (0, -4.2, 6.5), (0, 0, 0))
    for i in range(7):
        a = (i - 3) * 0.42
        gills.add(Part('gill_%d' % i, (0, 0, 0), (-0.35, 0, a), [rod('y', 0, 4, 1, gill_mat, sign=-1),
                                                                  box(-1, -4, -0.5, 2, 1, 1, gill_mat)]))
    body.add(gills)
    return Model('nudibranch', [body])


# ---------------------------------------------------------------------------
# AMPHIBIANS
# ---------------------------------------------------------------------------

def rain_frog():
    def skin_mat(c):
        f1, f2, _ = worley(c.p * 0.8, 481)
        gran = smooth(0.4, 0.1, f1)
        col = mix(C(0xA8784A), C(0xC8985E), smooth(-0.5, 0.6, fbm(c.p * 0.25, 3, 483)))
        col = mix(col, C(0x6A4628), smooth(0.1, 0.4, fbm(c.p * 0.4, 3, 485)) * 0.5)
        col = mix(col, C(0xE6C08A), gran * 0.4)
        belly = smooth(0.2, 0.75, c.n[:, 1])
        col = mix(col, C(0xF2DCC8), belly)
        o = Out(c.N, col)
        o.spec[:] = 0.3
        o.shin[:] = 25
        return o

    def face_mat(c):
        o = skin_mat(c)
        # the famous grumpy downturned mouth
        x = c.x / 6.0
        mouth_y = 1.4 + 0.9 * x * x
        line = smooth(0.45, 0.0, np.abs(c.y - mouth_y)) * smooth(0.95, 0.75, np.abs(x)) * smooth(-0.4, -0.7, c.q[:, 2])
        o.col = mix(o.col, C(0x3A2214), line * 0.85)
        nost = smooth(0.5, 0.2, np.sqrt((np.abs(c.x) - 1.2) ** 2 + (c.y + 0.8) ** 2)) * smooth(-0.5, -0.8, c.q[:, 2])
        o.col = mix(o.col, C(0x3A2214), nost * 0.7)
        return o

    body = Part('body', (0, GROUND, 0))
    body.add(ellipsoid((0, -7.0, 1.0), (7.2, 7.0, 7.4), skin_mat, axis='y', step=1))
    head = Part('head', (0, -7.5, -3.5), (0, 0, 0))
    head.add(ellipsoid((0, 0.5, -1.5), (6.0, 5.0, 3.5), face_mat, axis='y', step=1))
    for side in (1, -1):
        head.add(ellipsoid((side * 3.4, -1.8, -3.4), (1.9, 2.0, 1.8), cute_eye(side=side), axis='y', step=1))
    body.add(head)
    stubby = skin_mat
    for side in (1, -1):
        nm = 'l' if side > 0 else 'r'
        front = Part('leg_%s0' % nm, (side * 4.2, -3.0, -3.0), (0, 0, side * -0.15))
        front.add(rod('y', 0, 3, 2, stubby), ellipsoid((side * 0.4, 3.0, -0.5), (1.4, 0.6, 1.4), stubby, axis='y'))
        back = Part('leg_%s1' % nm, (side * 5.2, -2.5, 4.0), (0, 0, 0))
        back.add(ellipsoid((0, 0, 0), (2.0, 2.4, 3.0), stubby, axis='y', step=1),
                 ellipsoid((side * 0.5, 2.3, -1.5), (1.4, 0.5, 1.8), stubby, axis='y'))
        body.add(front, back)
    return Model('rain_frog', [body])


def dart_frog():
    def dart_skin(c):
        f1, _, cid = worley(c.p * 0.38, 491)
        spot = smooth(0.42, 0.3, f1) * (cid > 0.45)
        col = mix(C(0x1E5AD8), C(0x3A8AF8), smooth(-0.5, 0.6, fbm(c.p * 0.3, 3, 493)))
        col = mix(col, C(0x0A1430), smooth(-0.1, 0.6, -c.n[:, 1]) * 0.25)
        col = mix(col, C(0x080810), spot)
        o = Out(c.N, col)
        o.spec[:] = 0.9
        o.shin[:] = 60
        return o

    def leg_skin(c):
        o = dart_skin(c)
        o.col = o.col * 0.75
        return o

    def pad(c):
        o = dart_skin(c)
        o.col = mix(o.col, C(0x1A2A5A), 0.6)
        return o

    by = GROUND
    body = Part('body', (0, by, 0))
    body.add(ellipsoid((0, -5.5, 1.5), (4.8, 3.8, 6.5), dart_skin, axis='y', step=1))
    head = Part('head', (0, -6.5, -4.0), (-0.1, 0, 0))
    head.add(ellipsoid((0, 0, -2.8), (4.3, 3.0, 3.8), dart_skin, axis='y', step=1))
    for side in (1, -1):
        head.add(ellipsoid((side * 3.0, -2.0, -2.6), (1.6, 1.7, 1.7), eye_mat(base=0x060608, glossy=1.0), axis='y', step=1))
    body.add(head)
    parts = [body]
    for side in (1, -1):
        parts.append(leg(side, 0, (3.2, by - 4.5, -3.5), 0.5, 3, 4, 2, leg_skin, up=0.2, foot=0.3, thick=(2, 1, 1),
                         tars_mat=pad))
    for side, nm in ((1, 'l'), (-1, 'r')):
        hl = Part('hindleg_' + nm, (side * 4.0, -3.2, 5.0), (0, -side * 0.75, 0))
        hl.add(ellipsoid((0, 0, -3.0), (1.6, 1.6, 3.6), leg_skin, axis='z', step=1))        # thigh, folded forward
        shin = Part('shin_' + nm, (0, 0.8, -6.0), (0, side * 0.0, 0))
        shin.add(rod('z', 0, 6, 2, leg_skin))                                              # shin, back
        foot = Part('foot_' + nm, (0, 1.2, 6.0), (0, -side * 0.5, 0))
        foot.add(rod('z', 0, 5, 1, leg_skin, sign=-1), ellipsoid((0, 0, -5), (1.0, 0.5, 1.0), pad, axis='y'))
        shin.add(foot)
        hl.add(shin)
        body.add(hl)
    return Model('dart_frog', parts)


def fire_salamander():
    def sal_skin(c):
        blot = smooth(0.12, 0.3, fbm(c.p * np.array([0.25, 0.3, 0.16]), 4, 501))
        col = mix(C(0x101012), C(0x22222A), smooth(-0.5, 0.6, fbm(c.p * 0.5, 2, 503)))
        yellow = mix(C(0xF4C010), C(0xF08A10), smooth(0.2, 0.8, fbm(c.p * 0.6, 2, 505)))
        col = mix(col, yellow, blot * smooth(0.3, -0.2, c.n[:, 1]))
        o = Out(c.N, col)
        o.spec[:] = 0.85
        o.shin[:] = 55
        return o

    body = Part('body', (0, GROUND, 0))
    body.add(ellipsoid((0, -3.5, 0), (3.8, 2.8, 10.0), sal_skin, axis='z', step=2))
    head = Part('head', (0, -3.6, -9.5), (0, 0, 0))
    head.add(ellipsoid((0, 0, -3.5), (3.9, 2.5, 4.4), sal_skin, axis='y', step=1))
    for side in (1, -1):
        head.add(ellipsoid((side * 2.8, -1.4, -0.5), (1.4, 1.0, 2.0), sal_skin, axis='y', step=1))   # parotoid glands
        head.add(ellipsoid((side * 2.6, -1.6, -4.2), (1.2, 1.3, 1.3), eye_mat(base=0x060606, glossy=1.0), axis='y', step=1))
    body.add(head)
    tail, _ = seg_chain('tail_base', 6, 3.2, 4, sal_skin, '+z', bend=(0.02, 0, 0), pivot=(0, -3.4, 9.0), taper=1)
    body.add(tail)
    parts = [body]
    for i, z in enumerate((-6.0, 6.0)):
        for side in (1, -1):
            parts.append(leg(side, i, (3.0, GROUND - 3.5, z), (0.55, -0.55)[i], 4, 4, 3, sal_skin, up=0.45, foot=0.25,
                             thick=(2, 2, 1)))
    return Model('fire_salamander', parts)


def sea_models():
    return [clownfish(), lionfish(), seahorse(), blobfish(), anglerfish(), jellyfish(), dumbo_octopus(),
            sea_star(), nudibranch(), rain_frog(), dart_frog(), fire_salamander()]
