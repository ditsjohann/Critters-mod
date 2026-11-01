package com.critters.client.render;

import com.critters.CrittersMod;
import com.critters.block.CritterDisplayBlockEntity;
import com.critters.block.DisplayCritter;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/** Draws the critters living inside a Terrarium or Aquarium. */
public class CritterDisplayRenderer implements BlockEntityRenderer<CritterDisplayBlockEntity> {
    private final EntityRenderDispatcher dispatcher;
    private boolean loggedError;

    public CritterDisplayRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = context.getEntityRenderer();
    }

    @Override
    public void render(CritterDisplayBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffers,
                       int packedLight, int packedOverlay) {
        for (DisplayCritter critter : be.getDisplay()) {
            Entity entity = critter.entity();
            double x = Mth.lerp(partialTick, critter.xo, critter.x);
            double y = Mth.lerp(partialTick, critter.yo, critter.y);
            double z = Mth.lerp(partialTick, critter.zo, critter.z);
            float yaw = Mth.rotLerp(partialTick, critter.yawO, critter.yaw);
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            float s = critter.scale();
            poseStack.scale(s, s, s);
            try {
                EntityRenderer<? super Entity> renderer = dispatcher.getRenderer(entity);
                renderer.render(entity, yaw, partialTick, poseStack, buffers, packedLight);
            } catch (RuntimeException e) {
                if (!loggedError) {
                    loggedError = true;
                    CrittersMod.LOGGER.warn("Could not render {} in a display block", entity.getType(), e);
                }
            }
            poseStack.popPose();
        }
    }

    @Override
    public int getViewDistance() {
        return 48;
    }
}
