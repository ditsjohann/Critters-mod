package com.menagerie.registry;

import com.menagerie.Menagerie;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public final class ModItems {
    /** Every item registered through this class, in order; shown in the mod's creative tab. */
    public static final List<Item> ALL = new ArrayList<>();

    // --- Horse care (Mo' Creatures) ---
    public static final Item SUGAR_LUMP = register("sugar_lump", new Item(new Item.Properties()));
    public static final Item HAYSTACK = register("haystack", new Item(new Item.Properties()));
    public static final Item WHIP = register("whip", new Item(new Item.Properties().stacksTo(1)));
    public static final Item CRYSTAL_HORSE_ARMOR = register("crystal_horse_armor", new Item(new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.RARE)
            .attributes(ItemAttributeModifiers.builder()
                    .add(Attributes.ARMOR, new AttributeModifier(Menagerie.id("crystal_horse_armor"), 11.0,
                            AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.BODY)
                    .build())));

    // --- Magic: hearts drop from monsters, essences are brewed from them ---
    public static final Item HEART_OF_DARKNESS = register("heart_of_darkness", new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Item HEART_OF_FIRE = register("heart_of_fire", new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Item HEART_OF_UNDEAD = register("heart_of_undead", new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Item ESSENCE_OF_DARKNESS = register("essence_of_darkness", new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final Item ESSENCE_OF_FIRE = register("essence_of_fire", new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final Item ESSENCE_OF_UNDEAD = register("essence_of_undead", new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final Item ESSENCE_OF_LIGHT = register("essence_of_light", new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final Item UNICORN_HORN = register("unicorn_horn", new Item(new Item.Properties().rarity(Rarity.RARE)));

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
