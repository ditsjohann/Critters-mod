#!/usr/bin/env python3
"""Convert a Mo' Creatures 1.12 model class (ModelBase/ModelRenderer) into
Minecraft 1.21.1 model geometry (MeshDefinition / LayerDefinition).

Usage:
    python3 tools/convert_model.py <MoCModelX.java> <OutputClassName> [package]

The output is a Java class holding:
  * one ModelPart field per original ModelRenderer (same names, lowerCamelCase),
  * a constructor that fetches every part from the baked root,
  * createBodyLayer(), which rebuilds the original boxes exactly: same texture
    offsets, box origins and sizes, inflation, mirror flags, pivots, rotations
    and parent/child links (addChild).

1.12's ModelRenderer.render() and 1.21's ModelPart.render() apply transforms
in the same order (translate to pivot, then rotate Z, Y, X), and both renderers
flip the model the same way (scale -1,-1,1 and translate 1.501). So the
converted geometry lines up with the original textures pixel for pixel.

The original render()/setRotationAngles() logic is not converted: animation
code is ported by hand into setupAnim(). Any constructor statement the
converter does not understand is listed as a WARNING comment in the output, so
nothing is dropped silently.
"""
import math
import re
import sys


def to_camel(name):
    name = name[0].lower() + name[1:]
    return name


def to_snake(name):
    s = re.sub(r'(?<=[a-z0-9])([A-Z])', r'_\1', name)
    s = re.sub(r'(?<=[A-Z])([A-Z][a-z])', r'_\1', s)
    return s.lower()


def num(expr, consts):
    """Evaluate a simple Java numeric expression."""
    e = expr.strip()
    e = re.sub(r'\((?:float|double|int|byte)\)', '', e)
    e = re.sub(r'(?<=[0-9.])[FfDd]\b', '', e)
    e = e.replace('Math.PI', repr(math.pi)).replace('MathHelper.PI', repr(math.pi))
    e = e.replace('this.', '')
    for k, v in sorted(consts.items(), key=lambda kv: -len(kv[0])):
        e = re.sub(r'\b%s\b' % re.escape(k), repr(v), e)
    if not re.fullmatch(r'[0-9eE+\-*/(). ]+', e):
        raise ValueError(expr)
    return float(eval(e, {"__builtins__": {}}, {}))


def jf(v):
    """Format a float as a Java float literal."""
    if abs(v - round(v)) < 1e-9:
        return '%.1fF' % v
    s = ('%.7f' % v).rstrip('0')
    return s + 'F'


class Part:
    def __init__(self, name, u, v):
        self.name = name
        self.u, self.v = u, v
        self.boxes = []  # (u, v, mirror, x, y, z, w, h, d, inflate)
        self.pivot = [0.0, 0.0, 0.0]
        self.rot = [0.0, 0.0, 0.0]
        self.mirror = False
        self.children = []
        self.parent = None
        self.tex_size = None


def strip_comments(src):
    src = re.sub(r'/\*.*?\*/', '', src, flags=re.S)
    src = re.sub(r'//[^\n]*', '', src)
    return src


def find_constructor(src, cls):
    m = re.search(r'public\s+%s\s*\([^)]*\)\s*\{' % re.escape(cls), src)
    if not m:
        raise SystemExit('constructor not found')
    i = m.end()
    depth = 1
    j = i
    while depth:
        if src[j] == '{':
            depth += 1
        elif src[j] == '}':
            depth -= 1
        j += 1
    return src[i:j - 1]


def convert(path, out_cls, package):
    raw = open(path).read()
    src = strip_comments(raw)
    cls = re.search(r'class\s+(\w+)', src).group(1)
    body = find_constructor(src, cls)

    consts = {}
    # simple constant fields (e.g. private static final float X = 1F;)
    for m in re.finditer(r'(?:float|int|double)\s+(\w+)\s*=\s*([^;]+);', src):
        try:
            consts[m.group(1)] = num(m.group(2), consts)
        except ValueError:
            pass

    tex_w, tex_h = 64, 32
    parts = {}
    order = []
    warnings = []

    for stmt in [s.strip() for s in body.split(';') if s.strip()]:
        s = re.sub(r'\bthis\.', '', stmt)
        m = re.fullmatch(r'(?:final\s+)?(?:byte|short|int|float|double)\s+(\w+)\s*=\s*([^\[\]{}]+)', s)
        if m:
            try:
                consts[m.group(1)] = num(m.group(2), consts); continue
            except ValueError:
                pass
        m = re.fullmatch(r'setRotationG\s*\(\s*(\w+)\s*,(.*)\)', s)
        if m and m.group(1) in parts:
            parts[m.group(1)].rot = [math.radians(num(a, consts)) for a in m.group(2).split(',')]
            continue
        m = re.fullmatch(r'textureWidth\s*=\s*(.+)', s)
        if m:
            tex_w = int(num(m.group(1), consts)); continue
        m = re.fullmatch(r'textureHeight\s*=\s*(.+)', s)
        if m:
            tex_h = int(num(m.group(1), consts)); continue
        m = re.fullmatch(r'(?:ModelRenderer\s+)?(\w+)\s*=\s*new\s+ModelRenderer\s*\(\s*this\s*(?:,\s*([^,]+)\s*,\s*([^)]+))?\)', s)
        if m:
            name = m.group(1)
            u = int(num(m.group(2), consts)) if m.group(2) else 0
            v = int(num(m.group(3), consts)) if m.group(3) else 0
            p = Part(name, u, v)
            if name not in parts:
                order.append(name)
            parts[name] = p
            continue
        m = re.fullmatch(r'(\w+)\.addBox\s*\((.*)\)', s, flags=re.S)
        if m and m.group(1) in parts:
            p = parts[m.group(1)]
            args = [a for a in m.group(2).split(',')]
            if args and args[0].strip().startswith('"'):
                warnings.append('named addBox not supported: ' + stmt); continue
            vals = [num(a, consts) for a in args]
            infl = vals[6] if len(vals) > 6 else 0.0
            p.boxes.append((p.u, p.v, p.mirror, *vals[:6], infl))
            continue
        m = re.fullmatch(r'(\w+)\.setRotationPoint\s*\((.*)\)', s)
        if m and m.group(1) in parts:
            parts[m.group(1)].pivot = [num(a, consts) for a in m.group(2).split(',')]
            continue
        m = re.fullmatch(r'(?:setRotation|setRotateAngle|setRotationAngle)\s*\(\s*(\w+)\s*,(.*)\)', s)
        if m and m.group(1) in parts:
            parts[m.group(1)].rot = [num(a, consts) for a in m.group(2).split(',')]
            continue
        m = re.fullmatch(r'(\w+)\.rotateAngle([XYZ])\s*=\s*(.+)', s)
        if m and m.group(1) in parts:
            parts[m.group(1)].rot['XYZ'.index(m.group(2))] = num(m.group(3), consts)
            continue
        m = re.fullmatch(r'(\w+)\.rotationPoint([XYZ])\s*=\s*(.+)', s)
        if m and m.group(1) in parts:
            parts[m.group(1)].pivot['XYZ'.index(m.group(2))] = num(m.group(3), consts)
            continue
        m = re.fullmatch(r'(\w+)\.mirror\s*=\s*(true|false)', s)
        if m and m.group(1) in parts:
            parts[m.group(1)].mirror = m.group(2) == 'true'
            continue
        m = re.fullmatch(r'(\w+)\.setTextureOffset\s*\(([^,]+),([^)]+)\)', s)
        if m and m.group(1) in parts:
            p = parts[m.group(1)]
            p.u, p.v = int(num(m.group(2), consts)), int(num(m.group(3), consts))
            continue
        m = re.fullmatch(r'(\w+)\.setTextureSize\s*\(([^,]+),([^)]+)\)', s)
        if m and m.group(1) in parts:
            parts[m.group(1)].tex_size = (int(num(m.group(2), consts)), int(num(m.group(3), consts)))
            continue
        m = re.fullmatch(r'(\w+)\.addChild\s*\(\s*(\w+)\s*\)', s)
        if m and m.group(1) in parts and m.group(2) in parts:
            parent, child = parts[m.group(1)], parts[m.group(2)]
            parent.children.append(child)
            child.parent = parent
            continue
        warnings.append(stmt.replace('\n', ' '))

    for p in parts.values():
        if p.tex_size and p.tex_size != (tex_w, tex_h):
            warnings.append('%s.setTextureSize%s differs from the model texture size (%d, %d)'
                            % (p.name, p.tex_size, tex_w, tex_h))

    # ---- emit ----
    out = []
    w = out.append
    w('package %s;' % package)
    w('')
    w('import net.minecraft.client.model.geom.ModelPart;')
    w('import net.minecraft.client.model.geom.PartPose;')
    w('import net.minecraft.client.model.geom.builders.CubeDeformation;')
    w('import net.minecraft.client.model.geom.builders.CubeListBuilder;')
    w('import net.minecraft.client.model.geom.builders.LayerDefinition;')
    w('import net.minecraft.client.model.geom.builders.MeshDefinition;')
    w('import net.minecraft.client.model.geom.builders.PartDefinition;')
    w('')
    w('/**')
    w(' * Geometry of Mo\' Creatures\' {@code %s} (DrZhark, GPL-3.0),' % cls)
    w(' * converted with tools/convert_model.py. Do not edit by hand: re-run the converter.')
    for warn in warnings:
        w(' * WARNING not converted: %s' % warn)
    w(' */')
    w('public final class %s {' % out_cls)
    w('    public static final int TEXTURE_WIDTH = %d;' % tex_w)
    w('    public static final int TEXTURE_HEIGHT = %d;' % tex_h)
    w('')
    for n in order:
        w('    public final ModelPart %s;' % to_camel(n))
    w('')
    w('    public %s(ModelPart root) {' % out_cls)
    for n in order:
        chain = []
        p = parts[n]
        while p:
            chain.append(p)
            p = p.parent
        expr = 'root' + ''.join('.getChild("%s")' % to_snake(q.name) for q in reversed(chain))
        w('        this.%s = %s;' % (to_camel(n), expr))
    w('    }')
    w('')
    w('    public static LayerDefinition createBodyLayer() {')
    w('        MeshDefinition mesh = new MeshDefinition();')
    w('        PartDefinition root = mesh.getRoot();')

    def emit(p, parent_var, depth):
        cubes = 'CubeListBuilder.create()'
        cur_uv, cur_mirror = None, False
        for (u, v, mir, x, y, z, bw, bh, bd, infl) in p.boxes:
            if (u, v) != cur_uv:
                cubes += '.texOffs(%d, %d)' % (u, v)
                cur_uv = (u, v)
            if mir != cur_mirror:
                cubes += '.mirror(%s)' % ('true' if mir else 'false')
                cur_mirror = mir
            if infl:
                cubes += '.addBox(%s, %s, %s, %s, %s, %s, new CubeDeformation(%s))' % tuple(
                    jf(a) for a in (x, y, z, bw, bh, bd, infl))
            else:
                cubes += '.addBox(%s, %s, %s, %s, %s, %s)' % tuple(jf(a) for a in (x, y, z, bw, bh, bd))
        if any(p.rot):
            pose = 'PartPose.offsetAndRotation(%s, %s, %s, %s, %s, %s)' % tuple(jf(a) for a in (*p.pivot, *p.rot))
        else:
            pose = 'PartPose.offset(%s, %s, %s)' % tuple(jf(a) for a in p.pivot)
        var = 'p_' + to_snake(p.name)
        call = '%s.addOrReplaceChild("%s", %s, %s)' % (parent_var, to_snake(p.name), cubes, pose)
        if p.children:
            w('        PartDefinition %s = %s;' % (var, call))
            for c in p.children:
                emit(c, var, depth + 1)
        else:
            w('        %s;' % call)

    for n in order:
        if parts[n].parent is None:
            emit(parts[n], 'root', 0)
    w('        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);')
    w('    }')
    w('}')
    return '\n'.join(out) + '\n', warnings


if __name__ == '__main__':
    if len(sys.argv) < 3:
        print(__doc__)
        sys.exit(1)
    pkg = sys.argv[3] if len(sys.argv) > 3 else 'com.menagerie.client.model.geom'
    text, warns = convert(sys.argv[1], sys.argv[2], pkg)
    sys.stdout.write(text)
    for wv in warns:
        print('WARNING: ' + wv, file=sys.stderr)
