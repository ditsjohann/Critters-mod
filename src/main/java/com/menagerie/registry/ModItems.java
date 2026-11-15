package com.menagerie.registry;

import com.menagerie.Menagerie;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public final class ModItems {
    /** Every item registered through this class, in order; shown in the mod's creative tab. */
    public static final List<Item> ALL = new ArrayList<>();

    // Example:
    // public static final Item MY_ITEM = register("my_item", new Item(new Item.Properties()));
    // public static final BlockItem MY_BLOCK = registerBlockItem("my_block", ModBlocks.MY_BLOCK);

    private ModItems() {
    }

    public static <T extends Item> T register(String name, T item) {
        T registered = Registry.register(BuiltInRegistries.ITEM, Menagerie.id(name), item);
        ALL.add(registered);
        return registered;
    }

    public static BlockItem registerBlockItem(String name, Block block) {
        return register(name, new BlockItem(block, new Item.Properties()));
    }

    public static void init() {
        // class loading registers the items
    }
}
