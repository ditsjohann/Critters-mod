package com.critters.client.render;

import com.critters.client.model.CritterModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

/** Draws the emissive "_glow" texture at full brightness (firefly lanterns, magma cracks, neon stripes...). */
public class CritterGlowLayer extends RenderLayer<Mob, CritterModel<Mob>> {
    private static final int FULL_BRIGHT = 0xF000F0;
    private final RenderType renderType;
    private final boolean pulse;

    public CritterGlowLayer(RenderLayerParent<Mob, CritterModel<Mob>> parent, ResourceLocation texture, boolean pulse) {
        super(parent);
        this.renderType = RenderType.eyes(texture);
        this.pulse = pulse;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffers, int packedLight, Mob entity, float limbSwing,
                       float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        float brightness = 1.0F;
        if (pulse) {
            // slow firefly blink, each one out of sync with the others
            brightness = Mth.clamp(0.35F + 0.65F * Mth.sin(ageInTicks * 0.09F + entity.getId() * 1.7F), 0.08F, 1.0F);
        }
        int v = (int) (brightness * 255.0F);
        int color = 0xFF000000 | (v << 16) | (v << 8) | v;
        this.getParentModel().renderToBuffer(poseStack, buffers.getBuffer(renderType), FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color);
    }
}
