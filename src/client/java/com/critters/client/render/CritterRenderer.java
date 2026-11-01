package com.critters.client.render;

import com.critters.CrittersMod;
import com.critters.client.model.CritterLayers;
import com.critters.client.model.CritterModel;
import com.critters.entity.SnailEntity;
import com.critters.registry.CritterSpecies;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

/** Renderer shared by every critter; behaviour differences are keyed off the species family. */
public class CritterRenderer extends MobRenderer<Mob, CritterModel<Mob>> {
    private final CritterSpecies species;
    private final ResourceLocation texture;
    private final float modelScale;

    public CritterRenderer(EntityRendererProvider.Context context, CritterSpecies species, ModelLayerLocation layer) {
        super(context, new CritterModel<>(context.bakeLayer(layer), species), Math.max(0.15F, species.width * 0.45F));
        this.species = species;
        this.texture = CrittersMod.id("textures/entity/" + species.id + ".png");
        this.modelScale = CritterLayers.renderScale(species.id);
        if (CritterLayers.GLOWING.contains(species.id)) {
            this.addLayer(new CritterGlowLayer(this, CrittersMod.id("textures/entity/" + species.id + "_glow.png"),
                    species == CritterSpecies.FIREFLY));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Mob entity) {
        return texture;
    }

    @Override
    protected void scale(Mob entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(modelScale, modelScale, modelScale);
    }

    @Override
    protected void setupRotations(Mob entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        switch (species.family) {
            case CRAB -> poseStack.mulPose(Axis.YP.rotationDegrees(90.0F)); // crabs walk sideways
            case SNAIL -> {
                if (entity instanceof SnailEntity snail && snail.isClimbing()) {
                    // Turn the snail up onto the wall it is climbing, belly against the wall.
                    poseStack.translate(0.0F, 0.15F, -(entity.getBbWidth() / 2.0F - 0.03F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                }
            }
            case FISH -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(4.3F * Mth.sin(0.6F * bob)));
                if (!entity.isInWater()) {
                    poseStack.translate(0.1F, 0.1F, -0.1F);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                }
            }
            default -> {
            }
        }
        CritterPoses.apply(poseStack, entity, species, partialTick);
    }
}
