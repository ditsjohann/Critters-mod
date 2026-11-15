# Asset sources

Every asset below comes from DrZhark's Mo' Creatures, licensed GPL-3.0:
<https://github.com/DrZhark/mocreaturesdev>, branch `1.12.1`
(paths relative to `src/main/resources/assets/mocreatures/` there).

| Original | Menagerie (`src/main/resources/assets/menagerie/`) | Changes |
|---|---|---|
| `textures/models/*.png` | `textures/entity/*.png` | file names lowercased (required by Minecraft 1.21) |
| `textures/items/*.png` | `textures/item/*.png` | file names lowercased |
| `textures/armor/<name>_1.png`, `_2.png` | `textures/models/armor/<name>_layer_1.png`, `_layer_2.png` | renamed to the 1.21 armor layer convention |
| `textures/blocks/*.png` | `textures/block/*.png` | none |
| `textures/misc/twinsuns.png` | `textures/environment/twinsuns.png` | none |
| `sounds/**/*.ogg` | `sounds/**/*.ogg` | none |
| `sounds.json` | `sounds.json` | namespace `mocreatures:` changed to `menagerie:` |

New assets made for Menagerie will be listed in a separate section here.
