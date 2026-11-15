package com.menagerie.registry;

import com.menagerie.Menagerie;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ModCreativeTab {
    public static final CreativeModeTab TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Menagerie.id(Menagerie.MOD_ID),
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup." + Menagerie.MOD_ID))
                    .icon(() -> new ItemStack(ModItems.ALL.isEmpty() ? Items.BOOK : ModItems.ALL.getFirst()))
                    .displayItems((params, output) -> ModItems.ALL.forEach(output::accept))
                    .build());

    private ModCreativeTab() {
    }

    public static void init() {
        // class loading registers the tab
    }
}
