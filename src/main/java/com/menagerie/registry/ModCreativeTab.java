package com.menagerie.registry;

import com.menagerie.Menagerie;
import com.menagerie.entity.horse.HorseBreeds;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class ModCreativeTab {
    public static final CreativeModeTab TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Menagerie.id(Menagerie.MOD_ID),
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup." + Menagerie.MOD_ID))
                    .icon(() -> new ItemStack(ModItems.UNICORN_HORN))
                    .displayItems((params, output) -> {
                        ModItems.ALL.forEach(output::accept);
                        // one spawn egg per horse breed
                        for (int breed : HorseBreeds.ALL) {
                            output.accept(horseEgg(breed));
                        }
                    })
                    .build());

    private ModCreativeTab() {
    }

    /** A horse spawn egg that always hatches the given breed. */
    public static ItemStack horseEgg(int breed) {
        ItemStack egg = new ItemStack(ModEntities.HORSE_SPAWN_EGG);
        CompoundTag tag = new CompoundTag();
        tag.putString("id", Menagerie.id("horse").toString());
        tag.putInt("Breed", breed);
        egg.set(DataComponents.ENTITY_DATA, CustomData.of(tag));
        egg.set(DataComponents.ITEM_NAME, Component.translatable("item.menagerie.horse_spawn_egg.breed",
                Component.translatable("entity.menagerie.horse." + HorseBreeds.key(breed))));
        return egg;
    }

    public static void init() {
        // class loading registers the tab
    }
}
