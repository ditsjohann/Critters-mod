package com.critters.registry;

import java.util.function.Predicate;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;

/** Where each critter lives in the wild. */
public final class ModSpawns {

    private ModSpawns() {
    }

    public static void init() {
        // --- Snails ---
        spawn(CritterSpecies.GARDEN_SNAIL, BiomeSelectors.tag(BiomeTags.IS_FOREST)
                .or(BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.MEADOW)), 12, 1, 2);
        spawn(CritterSpecies.BANDED_SNAIL, BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS,
                Biomes.MEADOW, Biomes.FLOWER_FOREST, Biomes.CHERRY_GROVE), 10, 1, 2);
        spawn(CritterSpecies.MOSS_SNAIL, BiomeSelectors.includeByKey(Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.LUSH_CAVES,
                Biomes.DARK_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA), 10, 1, 2);
        spawn(CritterSpecies.AMETHYST_SNAIL, BiomeSelectors.includeByKey(Biomes.LUSH_CAVES, Biomes.DRIPSTONE_CAVES), 8, 1, 1);
        spawn(CritterSpecies.MAGMA_SNAIL, BiomeSelectors.includeByKey(Biomes.NETHER_WASTES, Biomes.BASALT_DELTAS,
                Biomes.CRIMSON_FOREST), 8, 1, 2);

        // --- Crabs ---
        spawn(CritterSpecies.FIDDLER_CRAB, BiomeSelectors.includeByKey(Biomes.BEACH, Biomes.MANGROVE_SWAMP), 12, 1, 3);
        spawn(CritterSpecies.ROCK_CRAB, BiomeSelectors.includeByKey(Biomes.STONY_SHORE, Biomes.BEACH), 10, 1, 2);

        // --- Small insects ---
        spawn(CritterSpecies.FIREFLY, BiomeSelectors.includeByKey(Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.MEADOW,
                Biomes.FLOWER_FOREST, Biomes.FOREST, Biomes.DARK_FOREST, Biomes.BIRCH_FOREST, Biomes.CHERRY_GROVE,
                Biomes.PLAINS), 12, 2, 5);
        spawn(CritterSpecies.CRICKET, BiomeSelectors.tag(BiomeTags.IS_SAVANNA)
                .or(BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.MEADOW)), 10, 1, 3);
        spawn(CritterSpecies.ANT, BiomeSelectors.tag(BiomeTags.IS_FOREST).or(BiomeSelectors.tag(BiomeTags.IS_SAVANNA))
                .or(BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.DESERT)), 10, 2, 4);

        // --- Beetles ---
        spawn(CritterSpecies.LADYBUG, BiomeSelectors.includeByKey(Biomes.FLOWER_FOREST, Biomes.MEADOW, Biomes.PLAINS,
                Biomes.SUNFLOWER_PLAINS, Biomes.CHERRY_GROVE, Biomes.FOREST), 12, 1, 3);
        spawn(CritterSpecies.STAG_BEETLE, BiomeSelectors.includeByKey(Biomes.FOREST, Biomes.DARK_FOREST,
                Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST), 8, 1, 1);
        spawn(CritterSpecies.RHINOCEROS_BEETLE, BiomeSelectors.tag(BiomeTags.IS_JUNGLE), 8, 1, 1);
        spawn(CritterSpecies.DUNG_BEETLE, BiomeSelectors.tag(BiomeTags.IS_SAVANNA)
                .or(BiomeSelectors.includeByKey(Biomes.PLAINS)), 8, 1, 2);
        spawn(CritterSpecies.JEWEL_BEETLE, BiomeSelectors.tag(BiomeTags.IS_JUNGLE), 8, 1, 2);
        spawn(CritterSpecies.SCARAB_BEETLE, BiomeSelectors.tag(BiomeTags.IS_BADLANDS)
                .or(BiomeSelectors.includeByKey(Biomes.DESERT)), 8, 1, 2);
        spawn(CritterSpecies.BOMBARDIER_BEETLE, BiomeSelectors.tag(BiomeTags.IS_TAIGA)
                .or(BiomeSelectors.includeByKey(Biomes.FOREST)), 8, 1, 1);
        spawn(CritterSpecies.WEEVIL, BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST,
                Biomes.SWAMP), 8, 1, 2);
        spawn(CritterSpecies.GOLIATH_BEETLE, BiomeSelectors.tag(BiomeTags.IS_JUNGLE), 5, 1, 1);
        spawn(CritterSpecies.TIGER_BEETLE, BiomeSelectors.tag(BiomeTags.IS_SAVANNA)
                .or(BiomeSelectors.includeByKey(Biomes.BEACH, Biomes.DESERT)), 8, 1, 2);

        // --- Fish ---
        spawn(CritterSpecies.GOLDFISH, BiomeSelectors.includeByKey(Biomes.RIVER, Biomes.SWAMP), 10, 2, 4);
        spawn(CritterSpecies.BETTA, BiomeSelectors.tag(BiomeTags.IS_JUNGLE)
                .or(BiomeSelectors.includeByKey(Biomes.SWAMP, Biomes.MANGROVE_SWAMP)), 8, 1, 1);
        spawn(CritterSpecies.NEON_TETRA, BiomeSelectors.tag(BiomeTags.IS_JUNGLE)
                .or(BiomeSelectors.includeByKey(Biomes.MANGROVE_SWAMP)), 12, 4, 8);
        spawn(CritterSpecies.ANGELFISH, BiomeSelectors.includeByKey(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN,
                Biomes.DEEP_LUKEWARM_OCEAN, Biomes.MANGROVE_SWAMP), 8, 1, 3);

        // --- Second wave: fish, sea animals, amphibians ---
        spawn(CritterSpecies.CLOWNFISH, BiomeSelectors.includeByKey(Biomes.WARM_OCEAN), 10, 2, 3);
        spawn(CritterSpecies.LIONFISH, BiomeSelectors.includeByKey(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN), 6, 1, 1);
        spawn(CritterSpecies.SEAHORSE, BiomeSelectors.includeByKey(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN,
                Biomes.DEEP_LUKEWARM_OCEAN), 6, 1, 2);
        spawn(CritterSpecies.BLOBFISH, BiomeSelectors.includeByKey(Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN,
                Biomes.DEEP_LUKEWARM_OCEAN), 5, 1, 1);
        spawn(CritterSpecies.ANGLERFISH, BiomeSelectors.includeByKey(Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN,
                Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN), 5, 1, 1);
        spawn(CritterSpecies.DUMBO_OCTOPUS, BiomeSelectors.includeByKey(Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN), 5, 1, 2);
        spawn(CritterSpecies.JELLYFISH, BiomeSelectors.includeByKey(Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN,
                Biomes.COLD_OCEAN, Biomes.DEEP_OCEAN), 8, 2, 4);
        spawn(CritterSpecies.SEA_STAR, BiomeSelectors.includeByKey(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.OCEAN,
                Biomes.COLD_OCEAN), 8, 1, 2);
        spawn(CritterSpecies.NUDIBRANCH, BiomeSelectors.includeByKey(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN), 6, 1, 1);
        spawn(CritterSpecies.RAIN_FROG, BiomeSelectors.includeByKey(Biomes.DESERT, Biomes.BEACH), 6, 1, 1);
        spawn(CritterSpecies.DART_FROG, BiomeSelectors.tag(BiomeTags.IS_JUNGLE), 10, 1, 3);
        spawn(CritterSpecies.FIRE_SALAMANDER, BiomeSelectors.includeByKey(Biomes.FOREST, Biomes.DARK_FOREST,
                Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA), 8, 1, 1);
    }

    private static void spawn(CritterSpecies species, Predicate<BiomeSelectionContext> biomes, int weight, int min, int max) {
        BiomeModifications.addSpawn(biomes, species.type().getCategory(), species.type(), weight, min, max);
    }
}
