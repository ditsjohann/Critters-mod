package com.menagerie.client.render;

import com.menagerie.Menagerie;
import com.menagerie.client.model.ModModelLayers;
import com.menagerie.client.model.WyvernModel;
import com.menagerie.entity.wyvern.Wyvern;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Renders wyverns with the original textures, scaled by age as in the original (age / 100). */
public class WyvernRenderer extends MobRenderer<Wyvern, WyvernModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[Wyvern.KINDS + 1];

    static {
        for (int kind = 1; kind <= Wyvern.KINDS; kind++) {
            TEXTURES[kind] = Menagerie.id("textures/entity/" + Wyvern.texture(kind) + ".png");
        }
    }

    public WyvernRenderer(EntityRendererProvider.Context context) {
        super(context, new WyvernModel(context.bakeLayer(ModModelLayers.WYVERN)), 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(Wyvern wyvern) {
        return TEXTURES[Math.max(1, Math.min(Wyvern.KINDS, wyvern.getKind()))];
    }

    @Override
    protected void scale(Wyvern wyvern, PoseStack poseStack, float partialTick) {
        float s = wyvern.sizeFactor();
        poseStack.scale(s, s, s);
        this.shadowRadius = 0.75F * s;
    }
}
