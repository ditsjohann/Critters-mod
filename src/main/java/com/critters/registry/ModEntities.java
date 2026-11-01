package com.critters.registry;

import com.critters.CrittersMod;
import com.critters.entity.CrabEntity;
import com.critters.entity.CritterEntity;
import com.critters.entity.CritterFishEntity;
import com.critters.entity.FlyingCritterEntity;
import com.critters.entity.HoppingCritterEntity;
import com.critters.entity.SnailEntity;
import com.critters.item.CritterJarItem;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;

/** Registers an entity type, spawn egg and jar/bucket item for every {@link CritterSpecies}. */
public final class ModEntities {

    private ModEntities() {
    }

    public static void init() {
        for (CritterSpecies species : CritterSpecies.values()) {
            EntityType<? extends Mob> type = Registry.register(BuiltInRegistries.ENTITY_TYPE, CrittersMod.id(species.id), buildType(species));

            Item egg = ModItems.register(species.id + "_spawn_egg",
                    new SpawnEggItem(type, species.eggPrimary, species.eggSecondary, new Item.Properties()));
            Item container;
            if (species.isAquatic()) {
                container = ModItems.register(species.id + "_bucket", new MobBucketItem(type, Fluids.WATER,
                        SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)
                        .component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)));
            } else {
                container = ModItems.register(species.id + "_jar", new CritterJarItem(species, new Item.Properties().stacksTo(1)));
            }
            species.bind(type, egg, container);

            FabricDefaultAttributeRegistry.register(type,
                    species.isAquatic() ? AbstractFish.createAttributes() : CritterEntity.createAttributes(species));
            registerPlacement(species, type);
        }
    }

    private static EntityType<? extends Mob> buildType(CritterSpecies species) {
        EntityType.Builder<? extends Mob> builder = switch (species.family) {
            case SNAIL -> EntityType.Builder.<SnailEntity>of(SnailEntity::new, MobCategory.AMBIENT);
            case CRAB -> EntityType.Builder.<CrabEntity>of(CrabEntity::new, MobCategory.AMBIENT);
            case FLYER -> EntityType.Builder.<FlyingCritterEntity>of(FlyingCritterEntity::new, MobCategory.AMBIENT);
            case HOPPER -> EntityType.Builder.<HoppingCritterEntity>of(HoppingCritterEntity::new, MobCategory.AMBIENT);
            case CRAWLER, BEETLE -> EntityType.Builder.<CritterEntity>of(CritterEntity::new, MobCategory.AMBIENT);
            case FISH -> EntityType.Builder.<CritterFishEntity>of(CritterFishEntity::new, MobCategory.WATER_AMBIENT);
        };
        builder.sized(species.width, species.height)
                .eyeHeight(species.height * 0.6F)
                .clientTrackingRange(species.isAquatic() ? 4 : 8);
        if (species == CritterSpecies.MAGMA_SNAIL) {
            builder.fireImmune();
        }
        return builder.build(CrittersMod.id(species.id).toString());
    }

    @SuppressWarnings("unchecked")
    private static void registerPlacement(CritterSpecies species, EntityType<? extends Mob> type) {
        switch (species.family) {
            case FISH -> placement((EntityType<CritterFishEntity>) type, SpawnPlacementTypes.IN_WATER,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
            case FLYER -> placement((EntityType<CritterEntity>) type, SpawnPlacementTypes.NO_RESTRICTIONS,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlyingCritterEntity::checkFireflySpawnRules);
            default -> placement((EntityType<CritterEntity>) type, SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CritterEntity::checkCritterSpawnRules);
        }
    }

    private static <T extends Mob> void placement(EntityType<T> type, SpawnPlacementType where, Heightmap.Types heightmap,
                                                  SpawnPlacements.SpawnPredicate<T> predicate) {
        SpawnPlacements.register(type, where, heightmap, predicate);
    }
}
