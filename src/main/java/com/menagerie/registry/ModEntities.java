package com.menagerie.registry;

import com.menagerie.Menagerie;
import com.menagerie.entity.bigcat.BigCat;
import com.menagerie.entity.bigcat.BigCatSpecies;
import com.menagerie.entity.horse.MenagerieHorse;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.EnumMap;
import java.util.Map;

public final class ModEntities {
    public static final EntityType<MenagerieHorse> HORSE = Registry.register(BuiltInRegistries.ENTITY_TYPE, Menagerie.id("horse"),
            EntityType.Builder.of(MenagerieHorse::new, MobCategory.CREATURE)
                    .sized(1.3964844F, 1.6F)
                    .eyeHeight(1.52F)
                    .passengerAttachments(1.44375F)
                    .clientTrackingRange(10)
                    .build(Menagerie.id("horse").toString()));

    public static final Item HORSE_SPAWN_EGG = ModItems.register("horse_spawn_egg",
            new SpawnEggItem(HORSE, 0xC09E7D, 0xEEE500, new Item.Properties()));

    /** One entity type per big cat species (lion, tiger, ... and the hybrids), like the original. */
    public static final Map<BigCatSpecies, EntityType<BigCat>> BIG_CATS = new EnumMap<>(BigCatSpecies.class);
    public static final Map<BigCatSpecies, Item> BIG_CAT_EGGS = new EnumMap<>(BigCatSpecies.class);

    static {
        int[][] eggColors = {
                {0xD9A657, 0x8C5A2B}, {0xE58A2E, 0x1F1A17}, {0x1E1B22, 0x4A3F55}, {0xD9B26F, 0x3B2A1A},
                {0xD39A4E, 0x9C4F1D}, {0x3B3027, 0xC79552}, {0xD08D3F, 0x2A2420}, {0x2D2622, 0xB98C4E},
                {0x5A3B26, 0x1A1614}, {0xC9A160, 0x5B3B20}};
        for (BigCatSpecies species : BigCatSpecies.values()) {
            float z = species.adultSize;
            EntityType<BigCat> type = Registry.register(BuiltInRegistries.ENTITY_TYPE, Menagerie.id(species.id),
                    EntityType.Builder.<BigCat>of(BigCat::new, MobCategory.CREATURE)
                            .sized(1.2F * z, 1.1F * z)
                            .eyeHeight(0.9F * z)
                            .passengerAttachments(1.0F * z)
                            .clientTrackingRange(10)
                            .build(Menagerie.id(species.id).toString()));
            species.bind(type);
            BIG_CATS.put(species, type);
            int[] c = eggColors[species.ordinal()];
            BIG_CAT_EGGS.put(species, ModItems.register(species.id + "_spawn_egg", new SpawnEggItem(type, c[0], c[1], new Item.Properties())));
        }
    }

    private ModEntities() {
    }

    public static void init() {
        FabricDefaultAttributeRegistry.register(HORSE, MenagerieHorse.createAttributes());
        SpawnPlacements.register(HORSE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules);

        for (EntityType<BigCat> type : BIG_CATS.values()) {
            FabricDefaultAttributeRegistry.register(type, BigCat.createAttributes());
            SpawnPlacements.register(type, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Mob::checkMobSpawnRules);
        }
        // Lions on the savanna, tigers and panthers in the jungle, leopards on the savanna and (as snow
        // leopards) on snowy mountains. Hybrids are only bred.
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_SAVANNA), MobCategory.CREATURE,
                BIG_CATS.get(BigCatSpecies.LION), 4, 1, 3);
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_JUNGLE), MobCategory.CREATURE,
                BIG_CATS.get(BigCatSpecies.TIGER), 3, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_JUNGLE), MobCategory.CREATURE,
                BIG_CATS.get(BigCatSpecies.PANTHER), 3, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_SAVANNA)
                        .or(BiomeSelectors.includeByKey(Biomes.SNOWY_SLOPES, Biomes.GROVE, Biomes.SNOWY_TAIGA)),
                MobCategory.CREATURE, BIG_CATS.get(BigCatSpecies.LEOPARD), 3, 1, 1);

        // Wild horses on the plains and meadows, zebras on the savanna (breed is picked in finalizeSpawn).
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.MEADOW)
                .or(BiomeSelectors.tag(BiomeTags.IS_SAVANNA)), MobCategory.CREATURE, HORSE, 6, 2, 4);
    }
}
