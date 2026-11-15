package com.menagerie;

import com.menagerie.registry.ModBlocks;
import com.menagerie.registry.ModCreativeTab;
import com.menagerie.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Menagerie implements ModInitializer {
    public static final String MOD_ID = "menagerie";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.init();
        ModCreativeTab.init();
        LOGGER.info("{} loaded", MOD_ID);
    }
}
