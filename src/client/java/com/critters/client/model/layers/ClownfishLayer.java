// GENERATED FILE - DO NOT EDIT BY HAND.
// Produced by tools/generate_assets.py from tools/critter_models.py.
package com.critters.client.model.layers;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@SuppressWarnings({"unused", "UnusedAssignment"})
public final class ClownfishLayer {
    private ClownfishLayer() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder b;
        b = CubeListBuilder.create();
        b.texOffs(0, 68).addBox(-1.5F, -2.5F, -9.0F, 3.0F, 5.0F, 2.0F);
        b.texOffs(36, 61).addBox(-2.5F, -2.5F, -7.0F, 5.0F, 5.0F, 2.0F);
        b.texOffs(24, 42).addBox(-1.5F, -4.0F, -7.0F, 3.0F, 8.0F, 2.0F);
        b.texOffs(36, 68).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 4.0F, 2.0F);
        b.texOffs(16, 32).addBox(-2.5F, -4.0F, -5.0F, 5.0F, 8.0F, 2.0F);
        b.texOffs(10, 19).addBox(-1.5F, -5.0F, -5.0F, 3.0F, 10.0F, 2.0F);
        b.texOffs(40, 52).addBox(-3.5F, -2.5F, -3.0F, 7.0F, 5.0F, 2.0F);
        b.texOffs(46, 19).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 2.0F);
        b.texOffs(44, 0).addBox(-1.5F, -5.5F, -3.0F, 3.0F, 11.0F, 2.0F);
        b.texOffs(0, 61).addBox(-3.5F, -2.5F, -1.0F, 7.0F, 5.0F, 2.0F);
        b.texOffs(30, 19).addBox(-3.0F, -4.5F, -1.0F, 6.0F, 9.0F, 2.0F);
        b.texOffs(54, 0).addBox(-1.5F, -5.5F, -1.0F, 3.0F, 11.0F, 2.0F);
        b.texOffs(18, 61).addBox(-3.5F, -2.5F, 1.0F, 7.0F, 5.0F, 2.0F);
        b.texOffs(0, 32).addBox(-3.0F, -4.0F, 1.0F, 6.0F, 8.0F, 2.0F);
        b.texOffs(0, 19).addBox(-1.5F, -5.5F, 1.0F, 3.0F, 11.0F, 2.0F);
        b.texOffs(0, 75).addBox(-3.0F, -2.0F, 3.0F, 6.0F, 4.0F, 2.0F);
        b.texOffs(30, 32).addBox(-2.5F, -4.0F, 3.0F, 5.0F, 8.0F, 2.0F);
        b.texOffs(20, 19).addBox(-1.5F, -5.0F, 3.0F, 3.0F, 10.0F, 2.0F);
        b.texOffs(50, 61).addBox(-2.5F, -2.5F, 5.0F, 5.0F, 5.0F, 2.0F);
        b.texOffs(34, 42).addBox(-1.5F, -4.0F, 5.0F, 3.0F, 8.0F, 2.0F);
        b.texOffs(10, 68).addBox(-1.5F, -2.5F, 7.0F, 3.0F, 5.0F, 2.0F);
        PartDefinition p0 = root.addOrReplaceChild("body", b, PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(44, 81).addBox(-1.5F, -1.5F, -7.0F, 3.0F, 4.0F, 1.0F);
        b.texOffs(24, 81).addBox(-2.0F, -1.5F, -6.0F, 4.0F, 4.0F, 1.0F);
        b.texOffs(20, 68).addBox(-1.5F, -2.5F, -6.0F, 3.0F, 6.0F, 1.0F);
        b.texOffs(0, 81).addBox(-2.5F, -2.0F, -5.0F, 5.0F, 5.0F, 1.0F);
        b.texOffs(54, 42).addBox(-1.5F, -3.5F, -5.0F, 3.0F, 8.0F, 1.0F);
        b.texOffs(16, 75).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 5.0F, 1.0F);
        b.texOffs(0, 52).addBox(-1.5F, -3.5F, -4.0F, 3.0F, 8.0F, 1.0F);
        b.texOffs(30, 75).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 5.0F, 1.0F);
        b.texOffs(44, 42).addBox(-2.0F, -3.5F, -3.0F, 4.0F, 8.0F, 1.0F);
        b.texOffs(44, 75).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 5.0F, 1.0F);
        b.texOffs(8, 52).addBox(-1.5F, -3.5F, -2.0F, 3.0F, 8.0F, 1.0F);
        b.texOffs(12, 81).addBox(-2.5F, -2.0F, -1.0F, 5.0F, 5.0F, 1.0F);
        b.texOffs(16, 52).addBox(-1.5F, -3.5F, -1.0F, 3.0F, 8.0F, 1.0F);
        b.texOffs(34, 81).addBox(-2.0F, -1.5F, 0.0F, 4.0F, 4.0F, 1.0F);
        b.texOffs(28, 68).addBox(-1.5F, -2.5F, 0.0F, 3.0F, 6.0F, 1.0F);
        b.texOffs(52, 81).addBox(-1.5F, -1.5F, 1.0F, 3.0F, 4.0F, 1.0F);
        b.texOffs(20, 87).addBox(1.79F, -2.2F, -4.6F, 1.0F, 1.0F, 2.0F);
        b.texOffs(0, 87).addBox(1.29F, -1.2667F, -5.1F, 2.0F, 1.0F, 3.0F);
        b.texOffs(26, 87).addBox(1.79F, -0.3333F, -4.6F, 1.0F, 1.0F, 2.0F);
        b.texOffs(32, 87).addBox(-2.79F, -2.2F, -4.6F, 1.0F, 1.0F, 2.0F);
        b.texOffs(10, 87).addBox(-3.29F, -1.2667F, -5.1F, 2.0F, 1.0F, 3.0F);
        b.texOffs(38, 87).addBox(-2.79F, -0.3333F, -4.6F, 1.0F, 1.0F, 2.0F);
        p0.addOrReplaceChild("head", b, PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p0.addOrReplaceChild("tail_base", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(28, 0).addBox(0.0F, -5.5F, 0.0F, 0.0F, 11.0F, 8.0F);
        p2.addOrReplaceChild("tail_fin", b, PartPose.offsetAndRotation(0.0F, 0.0F, 5.5F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(0, 0).addBox(0.0F, -5.0F, 0.0F, 0.0F, 5.0F, 14.0F);
        p0.addOrReplaceChild("dorsal_fin", b, PartPose.offsetAndRotation(0.0F, -4.8F, -2.7F, 0.1F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(44, 32).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 6.0F);
        p0.addOrReplaceChild("anal_fin", b, PartPose.offsetAndRotation(0.0F, 4.8F, 1.35F, -0.1F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(0, 42).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 6.0F);
        p0.addOrReplaceChild("fin_l", b, PartPose.offsetAndRotation(2.72F, 1.68F, -4.05F, 0.0F, 0.7F, 0.3F));
        b = CubeListBuilder.create();
        b.texOffs(24, 52).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 4.0F);
        p0.addOrReplaceChild("pelvic_l", b, PartPose.offsetAndRotation(1.7F, 4.76F, -0.9F, -0.5F, 0.0F, 0.35F));
        b = CubeListBuilder.create();
        b.texOffs(12, 42).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 6.0F);
        p0.addOrReplaceChild("fin_r", b, PartPose.offsetAndRotation(-2.72F, 1.68F, -4.05F, 0.0F, -0.7F, -0.3F));
        b = CubeListBuilder.create();
        b.texOffs(32, 52).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 4.0F);
        p0.addOrReplaceChild("pelvic_r", b, PartPose.offsetAndRotation(-1.7F, 4.76F, -0.9F, -0.5F, 0.0F, -0.35F));
        return LayerDefinition.create(mesh, 64, 128);
    }
}
