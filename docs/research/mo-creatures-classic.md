# Mo' Creatures (classic era): feature research

Reference notes for rebooting Mo' Creatures on Minecraft 1.21.1 (Fabric).

## Sources and how reliable each part is

- **Primary source: DrZhark's original code**, <https://github.com/DrZhark/mocreaturesdev>, `master` branch, mod version **5.1.5** (`@Mod(... version = "5.1.5")`). This is the late classic era, around 2013 for MC 1.5/1.6, and is a superset of the 2012 v3/v4 releases. Every creature, item, recipe and breeding rule below comes from that code unless marked *(web)*.
- **Era timeline** *(web)*: DrZhark created the mod on 17 Nov 2010. v3.x targeted MC 1.2.5. v4.0.1 (22 Oct 2012) moved to MC 1.3.2 and added turkeys, butterflies, bees and pandas. v5 covered MC 1.5–1.6. The repo has branches for 1.7.10 up to 1.12.1. Original development stopped in Dec 2019, when the mod went open source.
- **License**: the repo's `License.txt` reads *"This mod is open source, licensed under the GNU GENERAL PUBLIC LICENSE Version 3"* (committed 14 Dec 2019). See [Licensing implications](#licensing-implications-for-our-reboot).
- Pages I could not open from this environment (blocked network): the Mo' Creatures fandom wiki and TV Tropes. Search results from those pages were used only for the timeline.

---

## 1. Creature roster (v5.1.5)

### Passive and tameable land animals
| Creature | Variants (from texture names) | Notable mechanics |
|---|---|---|
| **Horse** | ~40 types, see §2 | Tamed by riding until it stops bucking. Saddle, chest, armor, breeding, magic transformations |
| **Big Cat** | lion (♀/♂), tiger, white tiger, panther, leopard, cheetah | Hunt other animals. Tamed with raw fish/pork. Controlled with whip/medallion. Drops claws |
| **Bear** | black, brown, polar, panda | Stand up / sit poses. Panda tamed with sugar cane or sugar lump. Drops hide |
| **Elephant** | African, Indian, "pretty" Indian, mammoth, Songhua mammoth | Rideable. Harness, chest set, garment, howdah, **platform** (multi-rider). Tusk weapons (wood/iron/diamond). Fed hay, cake, bread, baked potato |
| **Ostrich** | chick → female / male, albino male, "demon" (via essences) | Lays eggs that hatch. Rideable. Wears any helmet (vanilla or mod). Carries a flag (wool) and saddlebags |
| **Kitty** | 8 coats (pussycat a–h) | The famous "Sims-like" cat. Has a state machine (hungry, looking for litter box, sleeping in kitty bed, playing with wool ball, pregnant/giving birth, climbing trees, being carried; the state list is from memory and the wiki, the code confirms the emoticon set). Thought-bubble emoticons over its head |
| **Deer** | doe, buck, fawn | Skittish, flees players. Drops fur |
| **Goat** | 3 female and 3 male looks + kid | Head-butts. Milkable with a bucket. Eats almost anything (even "eats" pickaxes in its interact code) |
| **Boar** | 2 looks | Hostile when provoked. Drops hide |
| **Fox** | red, snow (arctic) | Hunts rats, turkeys and rabbits. Drops fur |
| **Bunny** | 5 coats | Tamed with carrot/wheat. Can be picked up and carried on the player's head |
| **Mouse** | brown, grey, white | Can be picked up |
| **Turkey** | — | Seeds tempt. Raw/cooked turkey food |
| **Duck** | — | Swims |
| **Bird** | black, blue, green, red, white, yellow | Tamed with seeds. Perches on your shoulder |
| **Turtle** | normal + 4 ninja easter-egg skins | Hides in shell. Fed melon/reeds. Turtle meat → soup. *(The ninja skins and nunchaku/sai/bo/katana weapons reference a third-party franchise, so leave them out.)* |
| **Snake** | 8 species | Tongue animation. Fed raw rat. Some species are venomous |
| **Komodo dragon** | — | Rideable with saddle. Drops reptile hide and eggs |
| **Crocodile** | — | Death-roll: grabs prey and drags it into water. Drops reptile hide |
| **Snail** | 6 shells | Slow ambient creature |

### Insects and ambient creatures
Bee, Fly, Dragonfly (4 colors), Firefly (glows), Cricket (2), Butterfly (7 real species) + Moth (3 real species), Roach, Maggot. Flies and roaches are attracted to rotten flesh. Bees and butterflies to flowers.

### Water creatures
| Creature | Notes |
|---|---|
| **Dolphin** | 6 color types. Rideable. Tamed with raw fish |
| **Shark** | Hostile. Drops shark teeth (→ shark sword, shark-tooth armor) and eggs |
| **Fishy** | 10 small fish types. Catchable in a **fish bowl** |
| **Ray** | Manta ray (rideable) and stingray (stings) |
| **Jellyfish** | 12 colors. Some glow. Stings |
| **Crab** | 2 looks. Crab meat |

### Hostile mobs
| Mob | Notes |
|---|---|
| **Ogre** | Green (overworld), red/**fire** ogre (Nether, drops heart of fire), blue/cave ogre. 1- and 2-headed versions. Smashes blocks |
| **Werewolf** | Human villager by day, transforms at night (`!isDaytime()`). 5 wolf looks + human. **Weak to gold tools/weapons** (golden sword, axe, pickaxe, shovel, hoe in its damage code) |
| **Wild wolf (WWolf)** | 5 coats. Hostile pack wolf, unlike vanilla wolves |
| **Wraith** / **Flame Wraith** | Flying ghosts. The flame wraith sets you on fire |
| **Horse mobs** | Hostile nightmare, undead horse, skeleton horse, bat horse. Drop hearts (darkness/fire/undead) and unicorn horns |
| **Rat** / **Hell Rat** | Rats in caves. Hell rats in the Nether |
| **Scorpion** | Dirt, cave, frost, nether. Carries babies on its back. Each type's sting has a different effect. Drops chitin + stingers |
| **Big Golem** | Builds its body out of nearby blocks and throws rocks. Gets stronger as it absorbs blocks |
| **Wyvern** | See §4 |

---

## 2. The horse system (centerpiece of the mod)

### Horse types (comment in `MoCEntityHorse.java`)
- **Regular (1–17):** 1 White, 2 Creamy, 3 Brown, 4 Dark Brown, 5 Black, 6 Bright Creamy, 7 Speckled, 8 Pale Brown, 9 Grey, 11 Pinto, 12 Bright Pinto, 13 Pale Speckles, 16 Spotted, 17 Cow-patterned.
- **Ghost:** 21 winged ghost, 22 ghost. Spawned when certain magic horses die.
- **Undead:** 23 undead, 24 undead unicorn, 25 undead pegasus. These decay over time into 26–28, skeleton / skeleton unicorn / skeleton pegasus.
- **Magic:** 30 bug horse, 32 bat horse, 36 unicorn, 38 nightmare, 39 white pegasus, 40 black (dark) pegasus.
- **Fairy (48–59):** white fairy (50), plus colors made by **feeding dye** to a white fairy: orange, light blue, yellow, light green, pink, cyan, purple, dark blue, green, red, black.
- **Wild hybrids:** 60 zebra, 61 zorse, 65 donkey, 66 mule, 67 zonky.

### Breeding rules (`HorseGenetics`)
- Two of the same type → same type.
- Zebra + any regular horse → **zorse**. Donkey + regular horse → **mule**. Zebra + donkey → **zonky**.
- Rare horse + regular horse → the regular one.
- **Unicorn + white pegasus → white fairy horse.** Both parents vanish.
- Two different rare horses → random regular horse (1–5).
- Two different regular horses: 25% parent A, 25% parent B, 50% a defined color mix (e.g. White+Creamy→Bright Creamy, White+Brown→Creamy, Creamy+Black→Dark Brown, …). An "easy breeding" config skips the 50/50 parent roll.

### Transformations with essence vials (tamed horses only)
- **Essence of Fire** (nightmare vial) on a **zorse** → **Nightmare**.
- **Essence of Darkness** on a **zorse** → **Bat Horse**. On a **pegasus** → **Dark Pegasus**.
- **Essence of Light** on a **nightmare** → **Unicorn**. On a **bat horse above Y=128** → **Pegasus**. Also restores decayed skeleton/undead horses. *(Corrected after reading the 1.12.1 code; an earlier draft said "zorse".)*
- Magic horses don't eat. To breed, give them their essence at full health (fire for nightmares, darkness for bat horses, light for other magic horses). Regular horses breed after eating pumpkin, mushroom stew, cake or golden carrot.
- **Essence of Undead**: pegasus/bat → undead pegasus, unicorn/fairy → undead unicorn, regular/zebra/zorse → undead horse.

### Equipment and care
- Taming: ride it until it stops bucking. Harder breeds take longer (a per-type "tame difficulty").
- Horse saddle (iron + leather, or vanilla saddle + iron). **Chest** on suitable breeds, opened with the **key**.
- Horse armor: iron, gold, diamond. **Crystal armor** (glass + diamonds) is for magic horses.
- Foods: wheat, hay stack (makes the horse stay and eat), sugar lump, apple, golden apple, bread, cake, mushroom stew, pumpkin.
- **Whip** (big cat claw + leather + iron): command a tamed horse to stay or follow.
- Pegasus flies. Fairy and dark horses fall slowly. Nightmares leave fire. Unicorns charge with their horn.
- **Amulets** store a magic horse in an item and release it later: bone (skeleton horses), ghost, fairy and pegasus amulets. Each has an empty and a "full" item.
- Easter egg: playing music discs near a zebra makes it "shuffle" dance (custom record "Shuffling").

---

## 3. Items (v5.1.5 registry)

**Pet care and ownership:** medallion (name tag, also works with a book), whip, rope (leash), pet food, wool ball, kitty bed (16 dye colors), litter box, fish bowl (empty / with water / with fish), hay stack, sugar lump, key, Creaturepedia (shows a creature's info), **Scroll of Freedom** (release a pet), **Scroll of Sale** (transfer a pet), Scroll of Owner, Builder's Hammer, Staff of Teleport, Portal Staff, unicorn staff, diamond staff.

**Mount gear:** horse saddle; horse armor (iron, gold, diamond, crystal); elephant harness, chest set, garment, howdah, platform; tusks (wood / iron / diamond).

**Magic:** hearts of darkness / fire / undead (mob drops), essences of darkness / fire / undead / light (bottled vials; light = the other three combined), unicorn horn, the four amulets.

**Materials and armor sets:**
- Animal hide → hide armor (hide also crafts into leather). Fur → fur armor (also crafts into wool). Reptile hide → croc armor.
- Shark teeth → shark armor + shark sword.
- Scorpion chitin (dirt / cave / frost / nether) → 4 scorpion armor sets.
- Scorpion stings + diamond sword → **4 scorpion swords** with poison / blind / slow / fire effects.

**Food:** turkey, ostrich, rat (and the **Rat Burger**), crab, turtle meat and turtle soup, omelet (smelted from ostrich or fish eggs).

**Weapons:** shark sword, scorpion swords, ogre hammer (the turtle-easter-egg weapons are excluded, see above).

### Selected recipes (from `MoCreatures.java`)
- Amulets: `bone / gold nugget / [ender pearl | ghast tear | unicorn horn or essence of light | diamond]` in a ring, giving the bone / ghost / fairy / pegasus amulet.
- Essence vials: `[rotten flesh | blaze powder | ender pearl]` + `[heart]` + glass bottle. Light = undead + fire + darkness.
- Portal Staff: eye of ender + unicorn horn (or essence of light) + blaze rod.
- Crystal horse armor: diamonds + glass. Metal / gold / diamond horse armor = ingot shape + colored wool.
- Medallion: leather + gold (+ diamond for the fancier recipe).
- Kitty bed: planks + colored wool + iron. Re-dyeable.

---

## 4. Wyverns and the Wyvern Lair dimension
- **Wyvern types:** jungle, mix, sand (desert), sun, light, dark, nether, undead, and the **mother wyvern** (largest, drops eggs most often).
- **Eggs** drop from wyverns and hatch into baby wyverns that grow up. Tame ones take a saddle, horse armor and a chest, and fly when ridden. They react to essences like horses do (undead/dark/light/nether forms).
- **Wyvern Lair:** a sky-island dimension (custom sky renderer, its own biome and chunk generator) reached with the **Portal Staff**. It has its own blocks (stone, grass, dirt, logs, planks, leaves, tall grass, farmland), big trees and **wyvern towers**. Items lost when you die there stay in the Lair *(web)*.

---

## 5. Core systems worth reusing in the reboot
1. **Tameable pet base:** owner, name (medallion), stay/follow (whip), health display, ownership transfer and release scrolls, pet amulets.
2. **Variant + genetics system:** a type ID per creature, texture per type, breeding table, transformations. Horses use this most, but dolphins, ostriches, scorpions and wyverns use the same pattern.
3. **Rideable mount base:** buck-to-tame, saddle, chest/inventory, armor slot, flying and swimming mount controllers, multi-rider (elephant platform).
4. **Item-in-entity storage:** amulets, fish bowls and pet capture.
5. **Behavior state machines:** kitty states, werewolf day/night cycle, golem block absorption, crocodile death roll.
6. **Spawn control:** the original had its own "CustomMobSpawner" with per-biome config. On 1.21.1, use Fabric `BiomeModifications` + a config file.

## 6. 1.21.1 notes (where vanilla now overlaps)
Vanilla now has horses, donkeys, mules, pandas, polar bears, foxes, goats, turtles, dolphins, bees, frogs, armadillos and wolf variants. A reboot should **add the Mo' Creatures twist on top** rather than duplicate vanilla. Examples: the extended horse breeds and magic horses as their own entity (or a layered system on vanilla horses), big cats, elephants, ostriches, kitties, ogres, werewolves, scorpions, wyverns. The Wyvern Lair can be a datapack dimension (JSON dimension type + custom chunk generator settings), which is much simpler than in 1.6.

---

## Licensing implications for our reboot
- The original is **GPLv3**. Copying or porting any of its **code, models, textures or sounds** makes our mod a derivative work. It must then be released under **GPLv3**, credit DrZhark, and say what was changed. This repo is currently **MIT** (`LICENSE`), so it would have to switch.
- Re-implementing the **features and mechanics** with our own code and our own art doesn't copy anything. We can stay MIT that way, though crediting the inspiration is still good practice.
- **Name:** many ports reuse "Mo' Creatures", but the name is DrZhark's, and DrZhark only endorses *Mo' Creatures Extended* (1.12.2). An original name for our reboot avoids confusion and stops it looking like an official release.
- Leave out the third-party easter eggs (the ninja turtles and their weapons).
