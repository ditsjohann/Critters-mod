package com.newmod.registry;

import com.newmod.NewMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ModCreativeTab {
    public static final CreativeModeTab TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, NewMod.id(NewMod.MOD_ID),
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup." + NewMod.MOD_ID))
                    .icon(() -> new ItemStack(ModItems.ALL.isEmpty() ? Items.BOOK : ModItems.ALL.getFirst()))
                    .displayItems((params, output) -> ModItems.ALL.forEach(output::accept))
                    .build());

    private ModCreativeTab() {
    }

    public static void init() {
        // class loading registers the tab
    }
}
