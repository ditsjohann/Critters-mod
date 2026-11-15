package com.menagerie.client.render;

import com.menagerie.Menagerie;
import com.menagerie.client.model.ModModelLayers;
import com.menagerie.entity.wyvern.Wyvern;
import com.menagerie.entity.wyvern.WyvernEgg;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** Draws a wyvern egg with the original egg model ({@code MoCModelEgg}) and texture, tinted by kind. */
public class WyvernEggRenderer extends EntityRenderer<WyvernEgg> {
    private static final ResourceLocation TEXTURE = Menagerie.id("textures/entity/egg.png");
    private final ModelPart egg;

    public WyvernEggRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.egg = context.bakeLayer(ModModelLayers.EGG);
        this.shadowRadius = 0.25F;
    }

    @Override
    public void render(WyvernEgg entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        poseStack.pushPose();
        if (entity.wobbleTicks > 0) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin((entity.wobbleTicks - partialTick) * 1.3F) * 14.0F));
        }
        float s = entity.getKind() == Wyvern.MOTHER ? 2.6F : 2.0F;
        poseStack.scale(s, s, s);
        // the usual entity-model flip, then centre the 3-pixel egg on the entity
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        poseStack.translate(-1.5F / 16.0F, 0.0F, -1.5F / 16.0F);
        int color = 0xFF000000 | Wyvern.eggColor(entity.getKind());
        this.egg.render(poseStack, buffers.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, color);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(WyvernEgg entity) {
        return TEXTURE;
    }
}
