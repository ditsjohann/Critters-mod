"""
Turns the critter models from critter_models.py into:
  * one generated Java LayerDefinition class per critter (src/client/.../model/layers/)
  * the CritterLayers dispatcher class
  * painted entity textures (+ emissive _glow textures)

Every texel of every cube face is mapped back to the 3D point it covers (using the exact UV layout
of net.minecraft.client.model.geom.ModelPart.Cube), shaded by the cube's material and lit with the
analytic normal of the shape the cube approximates.
"""
import math
import os

import numpy as np
from PIL import Image

from critter_models import all_models
from shading import Ctx, compensate, light, normalize

FACE_NORMALS = {
    'top': (0.0, -1.0, 0.0),     # minY face: visually the top
    'bottom': (0.0, 1.0, 0.0),
    'west': (-1.0, 0.0, 0.0),
    'east': (1.0, 0.0, 0.0),
    'front': (0.0, 0.0, -1.0),   # north / minZ
    'back': (0.0, 0.0, 1.0),
}


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


def face_points(c, face, fw, fh):
    """3D part-local points (texel centres) for one face, plus plane-style uv. Mirrors ModelPart.Cube."""
    px, py = np.meshgrid(np.arange(fw) + 0.5, np.arange(fh) + 0.5)
    px = px.ravel()
    py = py.ravel()
    x0, y0, z0 = c.x, c.y, c.z
    x1, y1, z1 = x0 + c.w, y0 + c.h, z0 + c.d
    n = px.shape[0]
    if face == 'top':        # DOWN polygon: x left->right, row 0 = maxZ
        p = np.stack([x0 + px, np.full(n, y0), z1 - py], 1)
        uv = np.stack([px / max(fw, 1), 1 - py / max(fh, 1)], 1)
    elif face == 'bottom':   # UP polygon: x left->right, row 0 = maxZ
        p = np.stack([x0 + px, np.full(n, y1), z1 - py], 1)
        uv = np.stack([px / max(fw, 1), 1 - py / max(fh, 1)], 1)
    elif face == 'west':     # x = minX, z from maxZ (left) to minZ (right), y down
        p = np.stack([np.full(n, x0), y0 + py, z1 - px], 1)
        uv = np.stack([1 - px / max(fw, 1), py / max(fh, 1)], 1)
    elif face == 'east':     # x = maxX, z from minZ (left) to maxZ
        p = np.stack([np.full(n, x1), y0 + py, z0 + px], 1)
        uv = np.stack([px / max(fw, 1), py / max(fh, 1)], 1)
    elif face == 'front':    # z = minZ, x left->right
        p = np.stack([x0 + px, y0 + py, np.full(n, z0)], 1)
        uv = np.stack([px / max(fw, 1), py / max(fh, 1)], 1)
    else:                    # back: z = maxZ, x from maxX (left) to minX
        p = np.stack([x1 - px, y0 + py, np.full(n, z1)], 1)
        uv = np.stack([1 - px / max(fw, 1), py / max(fh, 1)], 1)
    if c.plane:
        # uv across the card in a stable orientation: u along its first axis, v along its second
        if c.w == 0:      # yz card: u along z, v along y
            uv = np.stack([(p[:, 2] - z0) / max(c.d, 1), (p[:, 1] - y0) / max(c.h, 1)], 1)
        elif c.h == 0:    # xz card
            uv = np.stack([(p[:, 0] - x0) / max(c.w, 1), (p[:, 2] - z0) / max(c.d, 1)], 1)
        else:             # xy card
            uv = np.stack([(p[:, 0] - x0) / max(c.w, 1), (p[:, 1] - y0) / max(c.h, 1)], 1)
    return px.astype(int), py.astype(int), p, uv


def rot_matrix(rx, ry, rz):
    cx, sx = math.cos(rx), math.sin(rx)
    cy, sy = math.cos(ry), math.sin(ry)
    cz, sz = math.cos(rz), math.sin(rz)
    Rx = np.array([[1, 0, 0], [0, cx, -sx], [0, sx, cx]])
    Ry = np.array([[cy, 0, sy], [0, 1, 0], [-sy, 0, cy]])
    Rz = np.array([[cz, -sz, 0], [sz, cz, 0], [0, 0, 1]])
    return Rz @ Ry @ Rx


def walk(parts, parent=np.eye(3)):
    for p in parts:
        m = parent @ rot_matrix(*p.rot)
        yield p, m
        yield from walk(p.children, m)


def paint(model, tw, th):
    img = np.zeros((th, tw, 4))
    glow = np.zeros((th, tw, 4))
    any_glow = False
    for part, mat in walk(model.parts):
        for c in part.cubes:
            for face, (x0, y0, fw, fh) in face_regions(c).items():
                if fw <= 0 or fh <= 0:
                    continue
                px, py, p, uv = face_points(c, face, fw, fh)
                fn = np.broadcast_to(np.array(FACE_NORMALS[face]), p.shape).copy()
                if c.shape is not None and not c.plane:
                    q = c.shape.q(p)
                    ns = c.shape.normal(p, fn)
                    n = normalize(ns * 0.8 + fn * 0.2)
                else:
                    q = p
                    ns = fn
                    n = fn
                out = c.mat(Ctx(p, q, n, uv, face))
                n_model = n @ mat.T
                col = light(out, n_model)
                if c.shape is not None and not c.plane:
                    col = compensate(col, ns @ mat.T, fn @ mat.T)
                ys = y0 + py
                xs = x0 + px
                img[ys, xs, :3] = col
                img[ys, xs, 3] = out.alpha
                if out.glow is not None and np.any(out.glow > 1):
                    g = np.clip(out.glow, 0, 255)
                    lit = g.max(axis=1) > 1
                    glow[ys[lit], xs[lit], :3] = g[lit]
                    glow[ys[lit], xs[lit], 3] = 255
                    any_glow = True
    return (Image.fromarray(np.clip(img, 0, 255).astype(np.uint8), 'RGBA'),
            Image.fromarray(np.clip(glow, 0, 255).astype(np.uint8), 'RGBA'), any_glow)


def pack(model):
    cubes = [c for p in model.all_parts() for c in p.cubes]
    order = sorted(cubes, key=lambda c: (-(c.d + c.h), -(2 * (c.d + c.w))))
    best = None
    for width in (64, 128, 256, 512, 1024):
        if any(2 * (c.d + c.w) > width for c in cubes):
            continue
        x = y = row_h = 0
        pos = []
        for c in order:
            cw, ch = 2 * (c.d + c.w), c.d + c.h
            if x + cw > width:
                x = 0
                y += row_h
                row_h = 0
            pos.append((x, y))
            x += cw
            row_h = max(row_h, ch)
        used = y + row_h
        height = 16
        while height < used:
            height *= 2
        area = width * height
        if best is None or area < best[0] or (area == best[0] and abs(width - height) < abs(best[1] - best[2])):
            best = (area, width, height, pos)
    _, width, height, pos = best
    for c, (u, v) in zip(order, pos):
        c.u, c.v = u, v
    return width, height


def jf(v):
    s = ('%.4f' % v).rstrip('0').rstrip('.')
    if s in ('-0', ''):
        s = '0'
    if '.' not in s:
        s += '.0'
    return s + 'F'


def pascal(sid):
    return ''.join(x.capitalize() for x in sid.split('_'))


def java_parts(lines, parent_var, part, counter):
    var = 'p%d' % counter[0]
    counter[0] += 1
    if part.cubes:
        lines.append('        b = CubeListBuilder.create();')
        for c in part.cubes:
            g = '' if c.grow == 0 else ', new CubeDeformation(%s)' % jf(c.grow)
            lines.append('        b.texOffs(%d, %d).addBox(%s, %s, %s, %s, %s, %s%s);' % (
                c.u, c.v, jf(c.x), jf(c.y), jf(c.z), jf(c.w), jf(c.h), jf(c.d), g))
        builder = 'b'
    else:
        builder = 'CubeListBuilder.create()'
    px, py, pz = part.pivot
    rx, ry, rz = part.rot
    decl = 'PartDefinition %s = ' % var if part.children else ''
    lines.append('        %s%s.addOrReplaceChild("%s", %s, PartPose.offsetAndRotation(%s, %s, %s, %s, %s, %s));' % (
        decl, parent_var, part.name, builder, jf(px), jf(py), jf(pz), jf(rx), jf(ry), jf(rz)))
    for ch in part.children:
        java_parts(lines, var, ch, counter)


def check_names(model):
    def rec(part, path):
        names = [c.name for c in part.children]
        dup = {n for n in names if names.count(n) > 1}
        assert not dup, '%s: duplicate child names %s under %s' % (model.id, dup, path)
        for ch in part.children:
            rec(ch, path + '/' + ch.name)
    roots = [p.name for p in model.parts]
    assert len(set(roots)) == len(roots), '%s: duplicate root parts' % model.id
    for p in model.parts:
        rec(p, p.name)


def gen_models(assets, java_dir, species_ids):
    models = all_models()
    ids = [m.id for m in models]
    assert ids == species_ids, 'model list and SPECIES list disagree: %s' % ids
    layer_dir = os.path.join(java_dir, 'layers')
    os.makedirs(layer_dir, exist_ok=True)
    for f in os.listdir(layer_dir):
        if f.endswith('.java'):
            os.remove(os.path.join(layer_dir, f))
    tex_dir = os.path.join(assets, 'textures/entity')
    os.makedirs(tex_dir, exist_ok=True)
    glowing = []
    stats = []
    for m in models:
        check_names(m)
        tw, th = pack(m)
        img, glow, has_glow = paint(m, tw, th)
        img.save(os.path.join(tex_dir, m.id + '.png'))
        gpath = os.path.join(tex_dir, m.id + '_glow.png')
        if has_glow:
            glow.save(gpath)
            glowing.append(m.id)
        elif os.path.exists(gpath):
            os.remove(gpath)
        ncubes = sum(len(p.cubes) for p in m.all_parts())
        stats.append('%-18s %4d cubes  %4dx%-4d tex%s' % (m.id, ncubes, tw, th, '  +glow' if has_glow else ''))

        lines = []
        counter = [0]
        for p in m.parts:
            java_parts(lines, 'root', p, counter)
        cls = pascal(m.id) + 'Layer'
        src = '''// GENERATED FILE - DO NOT EDIT BY HAND.
// Produced by tools/generate_assets.py from tools/critter_models.py.
package com.critters.client.model.layers;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@SuppressWarnings({"unused", "UnusedAssignment"})
public final class %s {
    private %s() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder b;
%s
        return LayerDefinition.create(mesh, %d, %d);
    }
}
''' % (cls, cls, '\n'.join(lines), tw, th)
        with open(os.path.join(layer_dir, cls + '.java'), 'w', newline='\n') as f:
            f.write(src)

    cases = '\n'.join('            case "%s" -> %sLayer.create();' % (m.id, pascal(m.id)) for m in models)
    scales = '\n'.join('            case "%s" -> %sF;' % (m.id, round(m.scale, 4)) for m in models)
    imports = '\n'.join('import com.critters.client.model.layers.%sLayer;' % pascal(m.id) for m in models)
    src = '''// GENERATED FILE - DO NOT EDIT BY HAND.
// Produced by tools/generate_assets.py from tools/critter_models.py.
package com.critters.client.model;

%s
import java.util.Set;
import net.minecraft.client.model.geom.builders.LayerDefinition;

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

    /** Scale the renderer applies to the model (models are authored at high resolution). */
    public static float renderScale(String id) {
        return switch (id) {
%s
            default -> 1.0F;
        };
    }
}
''' % (imports, ', '.join('"%s"' % g for g in glowing), cases, scales)
    with open(os.path.join(java_dir, 'CritterLayers.java'), 'w', newline='\n') as f:
        f.write(src)
    print('\n'.join(stats))
