package com.menagerie;

import com.menagerie.event.HeartDrops;
import com.menagerie.registry.ModBlocks;
import com.menagerie.registry.ModCreativeTab;
import com.menagerie.registry.ModEntities;
import com.menagerie.registry.ModItems;
import com.menagerie.registry.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Menagerie: a reboot of DrZhark's Mo' Creatures. See CREDITS.md. */
public class Menagerie implements ModInitializer {
    public static final String MOD_ID = "menagerie";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModSounds.init();
        ModItems.init();
        ModBlocks.init();
        ModEntities.init();
        ModCreativeTab.init();
        HeartDrops.init();
        LOGGER.info("Menagerie loaded");
    }
}
