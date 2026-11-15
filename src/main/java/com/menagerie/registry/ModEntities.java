package com.menagerie.registry;

import com.menagerie.Menagerie;
import com.menagerie.entity.horse.MenagerieHorse;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

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

    private ModEntities() {
    }

    public static void init() {
        FabricDefaultAttributeRegistry.register(HORSE, MenagerieHorse.createAttributes());
        SpawnPlacements.register(HORSE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules);

        // Wild horses on the plains and meadows, zebras on the savanna (breed is picked in finalizeSpawn).
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.MEADOW)
                .or(BiomeSelectors.tag(BiomeTags.IS_SAVANNA)), MobCategory.CREATURE, HORSE, 6, 2, 4);
    }
}
