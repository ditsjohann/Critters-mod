package com.critters.registry;

import com.critters.CrittersMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTab {
    public static final CreativeModeTab CRITTERS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CrittersMod.id("critters"),
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup.critters"))
                    .icon(() -> new ItemStack(CritterSpecies.LADYBUG.container()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.CRITTER_JAR);
                        output.accept(ModItems.TERRARIUM);
                        output.accept(ModItems.AQUARIUM);
                        for (CritterSpecies species : CritterSpecies.values()) {
                            output.accept(species.container());
                        }
                        for (CritterSpecies species : CritterSpecies.values()) {
                            output.accept(species.spawnEgg());
                        }
                    })
                    .build());

    private ModCreativeTab() {
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            for (CritterSpecies species : CritterSpecies.values()) {
                entries.accept(species.spawnEgg());
            }
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.accept(ModItems.TERRARIUM);
            entries.accept(ModItems.AQUARIUM);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.accept(ModItems.CRITTER_JAR);
            for (CritterSpecies species : CritterSpecies.values()) {
                if (species.isAquatic()) {
                    entries.accept(species.container());
                }
            }
        });
    }
}
