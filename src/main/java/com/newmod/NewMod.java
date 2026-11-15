package com.newmod;

import com.newmod.registry.ModBlocks;
import com.newmod.registry.ModCreativeTab;
import com.newmod.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewMod implements ModInitializer {
    public static final String MOD_ID = "newmod";
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
