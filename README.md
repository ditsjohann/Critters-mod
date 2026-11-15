# Menagerie

A reboot of DrZhark's classic **Mo' Creatures** for Minecraft **1.21.1** (Fabric), built on the
original's open-source (GPL-3.0) code, models, textures and sounds. See [CREDITS.md](CREDITS.md).

## Status

### Horses (first creature)
- **44 breeds**, from the original: 14 coat colors, zebras, donkeys, mules, zorses, zonkies,
  unicorns, pegasi, dark pegasi, bat horses, nightmares, 12 fairy horses, ghosts, undead and skeleton horses.
- **Original model and animation**: converted box by box from `MoCModelNewHorse` (galloping knee joints,
  rearing, grazing, swishing tail, flapping feathered wings, fairy butterfly wings, see-through ghosts).
- **Breeding table** from the original (e.g. white + black = grey, zebra + horse = zorse,
  unicorn + pegasus = fairy, and the parents fade away).
- **Essences** transform tamed horses: fire turns a zorse into a nightmare, darkness turns a zorse into a bat horse
  and a pegasus into a dark pegasus, light turns a nightmare into a unicorn and a bat horse above Y=128 into a pegasus,
  undead turns horses into undead horses, which slowly rot into skeletons.
- **Fairy horses** change color when fed dye.
- **Gear**: saddle, saddle bags (donkeys, mules, pegasi, fairies), iron/gold/diamond armor (regular horses, zebras,
  zorses), crystal armor (magic horses), each drawn with the original armored textures.
- **Care**: wheat, sugar lumps, bread and apples (taming), pumpkin/cake/golden carrot (breeding),
  haystack (stay and eat), whip (toggle stay).
- **New in Menagerie**: pegasi and fairies fly while ridden (hold jump to climb, release to glide); a galloping unicorn
  knocks monsters aside; a ridden nightmare sets nearby monsters alight; foals of tamed parents are born tame;
  zebras live on the savanna; until the original monsters are ported, hearts drop from vanilla monsters
  (undead → heart of undead, blazes/magma cubes/ghasts → heart of fire, endermen/phantoms/wither skeletons → heart of darkness).
- Armor is put on by using it on a tamed horse.
- Creative tab has a spawn egg for every breed.

### Big cats
- **10 species** with the original model, animation and textures: lions (lionesses, lions, white lions),
  tigers (orange and white), panthers, leopards and snow leopards, and the hybrids **liger, liard, lither,
  leoger, panthger and panthard**, bred by crossing species as in the original.
- **Winged cats**: essence of light gives a male lion, white lion, white tiger or liger wings;
  essence of darkness gives a panther wings. Winged cats fly while ridden.
- **Wild behaviour**: adults stalk players and go hunting for smaller animals; lionesses spot prey from further away;
  cats walk over to eat meat dropped on the ground.
- **Taming (original)**: feed a cub meat (drop it, or new: hand it over), then give it a **medallion**. Tamed cats wear
  a collar, follow you, sit on command (whip or empty hand), defend you, carry a **chest** (sneak + use to open)
  and can be ridden with a **saddle**. A tamed cat may come back as a **ghost cub** when it dies.
- Big cats drop **claws**, which craft the **whip** (as in the original).
- Spawns: lions on the savanna, tigers and panthers in jungles, leopards on the savanna, snow leopards on snowy slopes,
  groves and snowy taiga.

### Wyverns
- **12 kinds** with the original model, animation and textures: jungle, swamp, desert, sun, arctic, cave, mountain
  and sea wyverns, the huge **mother wyvern**, and the **undead, light and dark** wyverns that mothers transform into
  (give a tamed mother essence of undead, light or darkness).
- **Eggs**: wild wyverns sometimes drop an egg of their kind (mothers often). Set an egg down and it hatches after about
  half a minute into a hatchling that is **tame to the nearest player** and grows up over about twenty minutes.
  Essence of light makes a grown, tamed wyvern lay an egg. New: eggs are tinted by kind.
- **Wild wyverns** take off and land at random, swoop at their targets, hunt small animals and bite players
  (sometimes with poison).
- **Tamed wyverns** (once grown): saddle to ride, iron/gold/diamond horse armor shown as armor plates, a chest
  (sneak + use), whip or empty hand to sit, raw meat or fish to heal. They fly back to an owner who wanders off.
- **Flying** (new): a ridden wyvern flies where you look while you hold forward; jump climbs; let go to glide.
- Spawns (new, until the Wyvern Lair is ported): rare wild wyverns in the biome matching their colours
  (jungle, swamp, desert, savanna/badlands, mountains, snowy peaks, stony shores).

## Building
`./gradlew build` (Java 21). The jar is written to `build/libs/`.

Every push is also built by GitHub Actions (`.github/workflows/build.yml`); the jar is attached to each run as
the `menagerie` artifact. `.github/workflows/api-dump.yml` prints real Minecraft 1.21.1 method signatures for the
classes in `tools/api-classes.txt`, filtered by `tools/api-grep.txt` (note: the dumped jar already has this mod's
access widener applied).

## Tools
- `tools/convert_model.py`: converts an original 1.12 model class into 1.21.1 model geometry.
