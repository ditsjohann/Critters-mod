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
public final class NeonTetraLayer {
    private NeonTetraLayer() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder b;
        b = CubeListBuilder.create();
        b.texOffs(50, 41).addBox(-1.0F, -1.5F, -10.0F, 2.0F, 3.0F, 2.0F);
        b.texOffs(40, 17).addBox(-2.0F, -3.0F, -8.0F, 4.0F, 6.0F, 2.0F);
        b.texOffs(24, 34).addBox(-2.5F, -2.0F, -6.0F, 5.0F, 4.0F, 2.0F);
        b.texOffs(10, 17).addBox(-1.5F, -3.5F, -6.0F, 3.0F, 7.0F, 2.0F);
        b.texOffs(0, 27).addBox(-2.5F, -2.5F, -4.0F, 5.0F, 5.0F, 2.0F);
        b.texOffs(26, 0).addBox(-1.5F, -4.0F, -4.0F, 3.0F, 8.0F, 2.0F);
        b.texOffs(14, 27).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 2.0F);
        b.texOffs(36, 0).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 8.0F, 2.0F);
        b.texOffs(28, 27).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 2.0F);
        b.texOffs(46, 0).addBox(-1.5F, -4.0F, 0.0F, 3.0F, 8.0F, 2.0F);
        b.texOffs(42, 27).addBox(-2.5F, -2.5F, 2.0F, 5.0F, 5.0F, 2.0F);
        b.texOffs(0, 17).addBox(-1.5F, -4.0F, 2.0F, 3.0F, 8.0F, 2.0F);
        b.texOffs(38, 34).addBox(-2.5F, -2.0F, 4.0F, 5.0F, 4.0F, 2.0F);
        b.texOffs(20, 17).addBox(-1.5F, -3.5F, 4.0F, 3.0F, 7.0F, 2.0F);
        b.texOffs(52, 17).addBox(-2.0F, -3.0F, 6.0F, 4.0F, 6.0F, 2.0F);
        b.texOffs(0, 47).addBox(-1.0F, -1.5F, 8.0F, 2.0F, 3.0F, 2.0F);
        PartDefinition p0 = root.addOrReplaceChild("body", b, PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(56, 47).addBox(-1.0F, -1.18F, -7.5F, 2.0F, 3.0F, 1.0F);
        b.texOffs(8, 47).addBox(-1.5F, -1.68F, -6.5F, 3.0F, 4.0F, 1.0F);
        b.texOffs(52, 34).addBox(-2.0F, -2.18F, -5.5F, 4.0F, 5.0F, 1.0F);
        b.texOffs(10, 41).addBox(-2.0F, -1.68F, -4.5F, 4.0F, 4.0F, 1.0F);
        b.texOffs(56, 27).addBox(-1.5F, -2.68F, -4.5F, 3.0F, 6.0F, 1.0F);
        b.texOffs(20, 41).addBox(-2.0F, -1.68F, -3.5F, 4.0F, 4.0F, 1.0F);
        b.texOffs(0, 34).addBox(-1.5F, -2.68F, -3.5F, 3.0F, 6.0F, 1.0F);
        b.texOffs(30, 41).addBox(-2.0F, -1.68F, -2.5F, 4.0F, 4.0F, 1.0F);
        b.texOffs(8, 34).addBox(-1.5F, -2.68F, -2.5F, 3.0F, 6.0F, 1.0F);
        b.texOffs(40, 41).addBox(-2.0F, -1.68F, -1.5F, 4.0F, 4.0F, 1.0F);
        b.texOffs(16, 34).addBox(-1.5F, -2.68F, -1.5F, 3.0F, 6.0F, 1.0F);
        b.texOffs(0, 41).addBox(-2.0F, -2.18F, -0.5F, 4.0F, 5.0F, 1.0F);
        b.texOffs(16, 47).addBox(-1.5F, -1.68F, 0.5F, 3.0F, 4.0F, 1.0F);
        b.texOffs(0, 52).addBox(-1.0F, -1.18F, 1.5F, 2.0F, 3.0F, 1.0F);
        b.texOffs(34, 52).addBox(0.61F, -2.35F, -4.5F, 2.0F, 1.0F, 2.0F);
        b.texOffs(36, 47).addBox(0.61F, -1.2833F, -5.0F, 2.0F, 1.0F, 3.0F);
        b.texOffs(42, 52).addBox(0.61F, -0.2167F, -4.5F, 2.0F, 1.0F, 2.0F);
        b.texOffs(50, 52).addBox(-2.61F, -2.35F, -4.5F, 2.0F, 1.0F, 2.0F);
        b.texOffs(46, 47).addBox(-2.61F, -1.2833F, -5.0F, 2.0F, 1.0F, 3.0F);
        b.texOffs(0, 56).addBox(-2.61F, -0.2167F, -4.5F, 2.0F, 1.0F, 2.0F);
        p0.addOrReplaceChild("head", b, PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(20, 56).addBox(-0.5F, -1.0F, -1.5F, 1.0F, 2.0F, 1.0F);
        b.texOffs(8, 56).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F);
        b.texOffs(6, 52).addBox(-1.0F, -1.5F, 0.5F, 2.0F, 3.0F, 1.0F);
        b.texOffs(12, 52).addBox(-1.0F, -1.5F, 1.5F, 2.0F, 3.0F, 1.0F);
        b.texOffs(18, 52).addBox(-1.0F, -1.5F, 2.5F, 2.0F, 3.0F, 1.0F);
        b.texOffs(24, 52).addBox(-1.0F, -1.5F, 3.5F, 2.0F, 3.0F, 1.0F);
        b.texOffs(14, 56).addBox(-1.0F, -1.0F, 4.5F, 2.0F, 2.0F, 1.0F);
        b.texOffs(24, 56).addBox(-0.5F, -1.0F, 5.5F, 1.0F, 2.0F, 1.0F);
        PartDefinition p2 = p0.addOrReplaceChild("tail_base", b, PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(0, 0).addBox(0.0F, -5.0F, 0.0F, 0.0F, 10.0F, 7.0F);
        p2.addOrReplaceChild("tail_fin", b, PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(30, 17).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 5.0F);
        p0.addOrReplaceChild("dorsal_fin", b, PartPose.offsetAndRotation(0.0F, -3.2F, -3.5F, 0.1F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(14, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 6.0F);
        p0.addOrReplaceChild("anal_fin", b, PartPose.offsetAndRotation(0.0F, 3.2F, -1.0F, -0.1F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(30, 52).addBox(0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 2.0F);
        p0.addOrReplaceChild("adipose", b, PartPose.offsetAndRotation(0.0F, -3.0F, 6.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(24, 47).addBox(0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 3.0F);
        p0.addOrReplaceChild("fin_l", b, PartPose.offsetAndRotation(2.08F, 1.2F, -4.5F, 0.0F, 0.7F, 0.3F));
        b = CubeListBuilder.create();
        b.texOffs(30, 47).addBox(0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 3.0F);
        p0.addOrReplaceChild("fin_r", b, PartPose.offsetAndRotation(-2.08F, 1.2F, -4.5F, 0.0F, -0.7F, -0.3F));
        return LayerDefinition.create(mesh, 64, 64);
    }
}
