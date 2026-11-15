package com.newmod.registry;

import com.newmod.NewMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public final class ModBlocks {
    // Example:
    // public static final Block MY_BLOCK = register("my_block", new Block(BlockBehaviour.Properties.of().strength(1.5F)));

    private ModBlocks() {
    }

    public static <T extends Block> T register(String name, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, NewMod.id(name), block);
    }

    public static void init() {
        // class loading registers the blocks
    }
}
