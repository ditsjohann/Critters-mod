package com.menagerie.client.render;

import com.menagerie.Menagerie;
import com.menagerie.client.model.BigCatModel;
import com.menagerie.client.model.ModModelLayers;
import com.menagerie.entity.bigcat.BigCat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/** Renders every big cat species with the original textures, scaled to the species' adult size. */
public class BigCatRenderer extends MobRenderer<BigCat, BigCatModel> {
    private static final Map<String, ResourceLocation> TEXTURES = new HashMap<>();

    public BigCatRenderer(EntityRendererProvider.Context context) {
        super(context, new BigCatModel(context.bakeLayer(ModModelLayers.BIG_CAT)), 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(BigCat cat) {
        String name = cat.species().texture(cat.getVariant());
        return TEXTURES.computeIfAbsent(name, n -> Menagerie.id("textures/entity/" + n + ".png"));
    }

    @Override
    protected void scale(BigCat cat, PoseStack poseStack, float partialTick) {
        // the original scales the whole model by "edad" / 100 (an adult lion is 1.2)
        float s = cat.species().size(cat.getVariant());
        poseStack.scale(s, s, s);
    }
}
