# Critters

A Fabric mod for **Minecraft 1.21.1** that adds 24 small creatures. You catch them in jars and keep them in terrariums and aquariums, so you can build your own zoo.

![Snails and crabs](docs/snails_and_crabs.png)
![Beetles](docs/beetles.png)
![Terrariums, aquarium and fish](docs/terrarium_and_aquarium.png)

## The critters

| Group | Critters | Where to find them |
|---|---|---|
| **Snails** (5) | Garden Snail, Banded Snail, Moss Snail, Amethyst Snail, Magma Snail | forests, plains, meadows, swamps, lush/dripstone caves, the Nether |
| **Crabs** (2) | Fiddler Crab, Red Rock Crab | beaches, stony shores, mangrove swamps |
| **Small insects** (3) | Firefly, Cricket, Ant | swamps and forests at night, plains, savannas |
| **Beetles** (10) | Ladybug, Stag Beetle, Rhinoceros Beetle, Dung Beetle, Jewel Beetle, Golden Scarab, Bombardier Beetle, Weevil, Goliath Beetle, Tiger Beetle | flower fields, forests, jungles, deserts, badlands, taigas, beaches |
| **Fish** (4) | Goldfish, Betta Fish, Neon Tetra, Angelfish | rivers, swamps, jungles, mangroves, warm oceans |

Behaviour:

* **Snails** crawl slowly and **climb up walls**, turning onto the wall as they go. They leave a glossy **slime trail** on floors and walls, and the trail dries up after about a minute. A snail that gets hurt **hides in its shell** and takes half damage while hiding. The Magma Snail is fire-proof and has glowing cracks. The Amethyst Snail has glowing crystals.
* **Crabs** are amphibious. They breathe underwater, walk along the sea floor and **scuttle sideways** while snipping their claws. The fiddler crab has one giant claw.
* **Fireflies** drift through the air at night and **blink**. **Crickets** hop around on their big back legs. **Ants** run around quickly.
* **Beetles** each have their own look: stag mandibles, a rhinoceros horn, a weevil snout, the dung beetle's shovel head, iridescent jewel colours, and so on.
* **Fish** swim in schools. Neon tetras form big glowing schools, while bettas live alone.

## Catching and displaying

| Item / Block | How to use it |
|---|---|
| **Critter Jar** | Use it on any land critter to catch it. You get a jar with that critter in it, which keeps its name and health. Use the filled jar on a block to let the critter go. Released critters never despawn. |
| **Fish** | Catch them with a water bucket, like vanilla fish. You get a *Bucket of Goldfish* and so on. |
| **Terrarium** | Use a filled jar on it to move the critter inside. It holds 3 critters, which wander around on the moss inside the glass. Fireflies fly around inside. |
| **Aquarium** | Use a fish bucket on it to move the fish inside. It holds 4 fish. It accepts vanilla fish buckets too: cod, salmon, pufferfish and tropical fish, which keep their patterns. |

* Use a terrarium or aquarium with an empty hand to see who lives there.
* **Sneak and use** it with an empty hand to take the last critter back out, in its jar or bucket.
* Breaking the block drops every critter in its jar or bucket, so none are ever lost.

### Recipes

```
Critter Jar (x3)        Terrarium               Aquarium
 . B .                   P G P                   N G N
 G . G                   G . G                   G W G
 G G G                   P D P                   N S N

B = any wooden button   P = any planks          N = iron nugget
G = glass               D = dirt / grass / moss W = water bucket
                                                S = sand
```

Every critter also has a spawn egg. Everything is in the **Critters** creative tab.

## Building

You need Java 21.

```sh
./gradlew build          # mod jar ends up in build/libs/critters-1.0.0.jar
./gradlew runClient      # start a dev client
./gradlew runServer      # start a dev server
```

Install the jar together with [Fabric API](https://modrinth.com/mod/fabric-api) (0.116+ for 1.21.1) and Fabric Loader 0.16+.

## Project layout

```
src/main/java/com/critters/
  CrittersMod.java           mod entry point
  registry/CritterSpecies    the list of all 24 critters (size, health, speed, egg colours)
  registry/Mod*              blocks, items, entities, spawns, creative tab
  entity/                    Snail, Crab, Flying (firefly), Hopping (cricket), base critter, fish
  block/                     Terrarium, Aquarium, the shared display block entity, Slime Trail
  item/CritterJarItem        catching and releasing
src/client/java/com/critters/client/
  model/CritterModel         one animated model class for every critter
  model/CritterLayers        GENERATED dispatcher; model/layers/* GENERATED geometry (do not edit)
  render/                    entity renderer, glow layer, terrarium/aquarium renderer
tools/
  critter_models.py          model geometry + procedural materials for every critter
  shading.py                 numpy noise, material helpers and baked lighting
  model_painter.py           UV packing, 3D texture painting, Java model code generation
  generate_assets.py         runs everything + item/block textures, models, lang, recipes, loot
```

### How the models and textures are made

The critters are authored at 5x vanilla resolution (80 units per block) and drawn at 1/5 scale. That is
where their fine detail comes from.

* **Curvature**: domes, shells, bodies and eyes are stacked, rounded slices that approximate ellipsoids.
* **Anatomy**: legs are jointed (femur, tibia, tarsus) and antennae are segmented chains. Fins and wings
  are flat cards with alpha-cut silhouettes, veins and fin rays.
* **Textures**: every texel is mapped back to the 3D point it covers. Materials are procedural functions
  of that point: noise, cellular patterns, spiral shells, scales, chitin pits and grooves, iridescence,
  lava cracks. Patterns therefore flow seamlessly across all the small cubes.
* **Lighting**: shading uses the smooth normal of the shape each cube approximates, plus specular gloss.
  It also compensates for Minecraft's own per-face entity lighting, so curved surfaces don't look terraced.

### Changing or adding critters

The models and textures are generated together, so the texture UVs always match the model:

1. Edit the critter's shape or colours in `tools/critter_models.py`. For a new critter, also add it to `SPECIES` in `tools/generate_assets.py`, to `CritterSpecies.java` and to `ModSpawns.java`.
2. Run `python3 tools/generate_assets.py`. It needs Pillow and numpy.
3. Run `./gradlew build`.

## License

MIT
