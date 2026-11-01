package com.critters;

import com.critters.registry.ModBlockEntities;
import com.critters.registry.ModBlocks;
import com.critters.registry.ModCreativeTab;
import com.critters.registry.ModEntities;
import com.critters.registry.ModItems;
import com.critters.registry.ModSpawns;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrittersMod implements ModInitializer {
    public static final String MOD_ID = "critters";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.init();
        ModBlockEntities.init();
        ModEntities.init();
        ModSpawns.init();
        ModCreativeTab.init();
        LOGGER.info("Critters loaded: {} critters ready to be caught!", com.critters.registry.CritterSpecies.values().length);
    }
}
