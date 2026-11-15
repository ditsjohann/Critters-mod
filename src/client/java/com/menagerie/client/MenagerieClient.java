package com.menagerie.client;

import com.menagerie.client.model.ModModelLayers;
import com.menagerie.client.model.geom.NewHorseGeometry;
import com.menagerie.client.render.MenagerieHorseRenderer;
import com.menagerie.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MenagerieClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.HORSE, NewHorseGeometry::createBodyLayer);
        EntityRendererRegistry.register(ModEntities.HORSE, MenagerieHorseRenderer::new);
    }
}
