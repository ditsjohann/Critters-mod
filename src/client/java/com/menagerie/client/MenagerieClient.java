package com.menagerie.client;

import com.menagerie.client.model.ModModelLayers;
import com.menagerie.client.model.geom.BigCatGeometry;
import com.menagerie.client.model.geom.EggGeometry;
import com.menagerie.client.model.geom.WyvernGeometry;
import com.menagerie.client.render.WyvernEggRenderer;
import com.menagerie.client.render.WyvernRenderer;
import com.menagerie.entity.wyvern.Wyvern;
import com.menagerie.entity.wyvern.WyvernEggItem;
import com.menagerie.registry.ModItems;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import com.menagerie.client.model.geom.NewHorseGeometry;
import com.menagerie.client.render.BigCatRenderer;
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
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BIG_CAT, BigCatGeometry::createBodyLayer);
        ModEntities.BIG_CATS.values().forEach(type -> EntityRendererRegistry.register(type, BigCatRenderer::new));
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.WYVERN, WyvernGeometry::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.EGG, EggGeometry::createBodyLayer);
        EntityRendererRegistry.register(ModEntities.WYVERN, WyvernRenderer::new);
        EntityRendererRegistry.register(ModEntities.WYVERN_EGG, WyvernEggRenderer::new);
        // wyvern eggs are tinted by the kind of wyvern inside
        ColorProviderRegistry.ITEM.register((stack, layer) -> 0xFF000000 | Wyvern.eggColor(WyvernEggItem.kindOf(stack)),
                ModItems.WYVERN_EGG);
    }
}
