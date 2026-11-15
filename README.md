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
- Creative tab has a spawn egg for every breed.

## Building
`./gradlew build` (Java 21). The jar is written to `build/libs/`.

## Tools
- `tools/convert_model.py`: converts an original 1.12 model class into 1.21.1 model geometry.
