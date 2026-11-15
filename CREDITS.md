# Credits and license

Menagerie is a reboot of **Mo' Creatures**, created by **DrZhark** with the
Mo' Creatures team (first released in 2010). DrZhark released the original
source code, models, textures and sounds as open source under the
**GNU General Public License v3** in December 2019:
<https://github.com/DrZhark/mocreaturesdev>

Menagerie builds on that work, so Menagerie is also licensed under the
**GPL-3.0** (see `LICENSE`).

## What comes from Mo' Creatures
- Entity textures, item textures and sounds under `src/main/resources/assets/menagerie/`,
  copied from the `1.12.1` branch of the original repository (sometimes renamed or
  reorganised). Each one is listed in `ASSET_SOURCES.md`.
- Creature models, converted from the original Java model classes to Minecraft
  1.21.1's model format with `tools/convert_model.py` (the box layout and texture
  mapping are unchanged).
- Creature behaviour and game rules (breeding tables, transformations, recipes)
  re-implemented on Minecraft 1.21.1 / Fabric, based on the original code.

## Changes from the original
Menagerie is a rewrite for Fabric 1.21.1, not a straight port. The code is new
and follows modern Minecraft's entity, AI and data systems. Gameplay that came
from the original is marked in the source with comments that point to the class
it came from.

Menagerie is not affiliated with or endorsed by DrZhark or the Mo' Creatures team.
