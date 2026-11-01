package com.critters.registry;

import com.critters.CrittersMod;
import com.critters.item.CritterJarItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public final class ModItems {
    public static final CritterJarItem CRITTER_JAR = register("critter_jar", new CritterJarItem(null, new Item.Properties().stacksTo(16)));
    public static final BlockItem TERRARIUM = register("terrarium", new BlockItem(ModBlocks.TERRARIUM, new Item.Properties()));
    public static final BlockItem AQUARIUM = register("aquarium", new BlockItem(ModBlocks.AQUARIUM, new Item.Properties()));

    private ModItems() {
    }

    public static <T extends Item> T register(String name, T item) {
        return Registry.register(BuiltInRegistries.ITEM, CrittersMod.id(name), item);
    }

    public static void init() {
        // class loading registers the items; per-critter items are registered by ModEntities
    }
}
