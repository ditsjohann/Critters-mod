package com.critters.registry;

import com.critters.CrittersMod;
import com.critters.block.CritterDisplayBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {
    public static final BlockEntityType<CritterDisplayBlockEntity> CRITTER_DISPLAY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, CrittersMod.id("critter_display"),
            BlockEntityType.Builder.of(CritterDisplayBlockEntity::new, ModBlocks.TERRARIUM, ModBlocks.AQUARIUM).build(null));

    private ModBlockEntities() {
    }

    public static void init() {
    }
}
