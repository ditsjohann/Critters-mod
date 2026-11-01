package com.critters.client;

import com.critters.CrittersMod;
import com.critters.client.model.CritterLayers;
import com.critters.client.render.CritterDisplayRenderer;
import com.critters.client.render.CritterRenderer;
import com.critters.registry.CritterSpecies;
import com.critters.registry.ModBlockEntities;
import com.critters.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;

public class CrittersClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (CritterSpecies species : CritterSpecies.values()) {
            ModelLayerLocation layer = new ModelLayerLocation(CrittersMod.id(species.id), "main");
            EntityModelLayerRegistry.registerModelLayer(layer, () -> CritterLayers.create(species.id));
            EntityRendererRegistry.register(species.type(), context -> new CritterRenderer(context, species, layer));
        }
        BlockEntityRendererRegistry.register(ModBlockEntities.CRITTER_DISPLAY, CritterDisplayRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(),
                ModBlocks.TERRARIUM, ModBlocks.AQUARIUM, ModBlocks.SLIME_TRAIL);
    }
}
