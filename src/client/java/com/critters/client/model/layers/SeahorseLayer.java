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
public final class SeahorseLayer {
    private SeahorseLayer() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder b;
        b = CubeListBuilder.create();
        b.texOffs(38, 87).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 1.0F, 3.0F);
        b.texOffs(0, 73).addBox(-2.0F, -7.0F, -1.5F, 4.0F, 1.0F, 4.0F);
        b.texOffs(38, 78).addBox(-2.5F, -6.0F, -1.0F, 5.0F, 1.0F, 3.0F);
        b.texOffs(16, 25).addBox(-1.5F, -6.0F, -2.5F, 3.0F, 1.0F, 6.0F);
        b.texOffs(40, 63).addBox(-2.5F, -5.0F, -1.5F, 5.0F, 1.0F, 4.0F);
        b.texOffs(34, 25).addBox(-1.5F, -5.0F, -2.5F, 3.0F, 1.0F, 6.0F);
        b.texOffs(0, 53).addBox(-3.0F, -4.0F, -1.5F, 6.0F, 1.0F, 4.0F);
        b.texOffs(44, 9).addBox(-1.5F, -4.0F, -3.0F, 3.0F, 1.0F, 7.0F);
        b.texOffs(20, 53).addBox(-3.0F, -3.0F, -1.5F, 6.0F, 1.0F, 4.0F);
        b.texOffs(6, 0).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 1.0F, 7.0F);
        b.texOffs(40, 53).addBox(-3.0F, -2.0F, -1.5F, 6.0F, 1.0F, 4.0F);
        b.texOffs(28, 0).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 1.0F, 7.0F);
        b.texOffs(0, 58).addBox(-3.0F, -1.0F, -1.5F, 6.0F, 1.0F, 4.0F);
        b.texOffs(0, 9).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 1.0F, 7.0F);
        b.texOffs(20, 58).addBox(-3.0F, 0.0F, -1.5F, 6.0F, 1.0F, 4.0F);
        b.texOffs(22, 9).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 1.0F, 7.0F);
        b.texOffs(40, 58).addBox(-3.0F, 1.0F, -1.5F, 6.0F, 1.0F, 4.0F);
        b.texOffs(0, 17).addBox(-1.5F, 1.0F, -3.0F, 3.0F, 1.0F, 7.0F);
        b.texOffs(0, 68).addBox(-2.5F, 2.0F, -1.5F, 5.0F, 1.0F, 4.0F);
        b.texOffs(0, 33).addBox(-1.5F, 2.0F, -2.5F, 3.0F, 1.0F, 6.0F);
        b.texOffs(0, 83).addBox(-2.5F, 3.0F, -1.0F, 5.0F, 1.0F, 3.0F);
        b.texOffs(18, 33).addBox(-1.5F, 3.0F, -2.5F, 3.0F, 1.0F, 6.0F);
        b.texOffs(16, 73).addBox(-2.0F, 4.0F, -1.5F, 4.0F, 1.0F, 4.0F);
        b.texOffs(48, 87).addBox(-1.0F, 5.0F, -1.0F, 2.0F, 1.0F, 3.0F);
        b.texOffs(14, 87).addBox(-1.5F, -2.5F, -2.3F, 3.0F, 1.0F, 3.0F);
        b.texOffs(16, 83).addBox(-2.0F, -1.5F, -2.3F, 4.0F, 1.0F, 3.0F);
        b.texOffs(36, 40).addBox(-1.5F, -1.5F, -3.3F, 3.0F, 1.0F, 5.0F);
        b.texOffs(18, 68).addBox(-2.5F, -0.5F, -2.8F, 5.0F, 1.0F, 4.0F);
        b.texOffs(36, 33).addBox(-1.5F, -0.5F, -3.8F, 3.0F, 1.0F, 6.0F);
        b.texOffs(0, 63).addBox(-3.0F, 0.5F, -2.8F, 6.0F, 1.0F, 4.0F);
        b.texOffs(20, 17).addBox(-1.5F, 0.5F, -4.3F, 3.0F, 1.0F, 7.0F);
        b.texOffs(20, 63).addBox(-3.0F, 1.5F, -2.8F, 6.0F, 1.0F, 4.0F);
        b.texOffs(40, 17).addBox(-1.5F, 1.5F, -4.3F, 3.0F, 1.0F, 7.0F);
        b.texOffs(36, 68).addBox(-2.5F, 2.5F, -2.8F, 5.0F, 1.0F, 4.0F);
        b.texOffs(0, 40).addBox(-1.5F, 2.5F, -3.8F, 3.0F, 1.0F, 6.0F);
        b.texOffs(30, 83).addBox(-2.0F, 3.5F, -2.3F, 4.0F, 1.0F, 3.0F);
        b.texOffs(0, 47).addBox(-1.5F, 3.5F, -3.3F, 3.0F, 1.0F, 5.0F);
        b.texOffs(26, 87).addBox(-1.5F, 4.5F, -2.3F, 3.0F, 1.0F, 3.0F);
        PartDefinition p0 = root.addOrReplaceChild("body", b, PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(48, 73).addBox(-1.5F, -4.6F, -2.8F, 3.0F, 1.0F, 4.0F);
        b.texOffs(44, 83).addBox(-2.0F, -3.56F, -2.3F, 4.0F, 1.0F, 3.0F);
        b.texOffs(16, 47).addBox(-1.5F, -3.56F, -3.3F, 3.0F, 1.0F, 5.0F);
        b.texOffs(32, 73).addBox(-2.0F, -2.52F, -2.8F, 4.0F, 1.0F, 4.0F);
        b.texOffs(18, 40).addBox(-1.5F, -2.52F, -3.8F, 3.0F, 1.0F, 6.0F);
        b.texOffs(0, 87).addBox(-2.0F, -1.48F, -2.3F, 4.0F, 1.0F, 3.0F);
        b.texOffs(32, 47).addBox(-1.5F, -1.48F, -3.3F, 3.0F, 1.0F, 5.0F);
        b.texOffs(0, 78).addBox(-1.5F, -0.44F, -2.8F, 3.0F, 1.0F, 4.0F);
        b.texOffs(0, 91).addBox(-1.0F, -6.0F, -0.5F, 2.0F, 2.0F, 2.0F);
        b.texOffs(8, 95).addBox(-0.5F, -7.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        b.texOffs(32, 91).addBox(1.5F, -3.5F, -2.4F, 1.0F, 1.0F, 2.0F);
        b.texOffs(38, 91).addBox(1.5F, -2.4F, -2.4F, 1.0F, 1.0F, 2.0F);
        b.texOffs(44, 91).addBox(-2.5F, -3.5F, -2.4F, 1.0F, 1.0F, 2.0F);
        b.texOffs(50, 91).addBox(-2.5F, -2.4F, -2.4F, 1.0F, 1.0F, 2.0F);
        PartDefinition p1 = p0.addOrReplaceChild("head", b, PartPose.offsetAndRotation(0.0F, -8.0F, -0.5F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(0, 25).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F);
        b.texOffs(56, 91).addBox(-1.0F, -1.0F, -7.0F, 2.0F, 2.0F, 1.0F);
        p1.addOrReplaceChild("snout", b, PartPose.offsetAndRotation(0.0F, -1.2F, -3.0F, -0.15F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(48, 47).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 3.0F);
        p1.addOrReplaceChild("fin_l", b, PartPose.offsetAndRotation(2.0F, -0.8F, 1.0F, 0.0F, 0.6F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(54, 47).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 3.0F);
        p1.addOrReplaceChild("fin_r", b, PartPose.offsetAndRotation(-2.0F, -0.8F, 1.0F, 0.0F, -0.6F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(0, 0).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 3.0F);
        p0.addOrReplaceChild("dorsal_fin", b, PartPose.offsetAndRotation(0.0F, -1.0F, 3.8F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(14, 78).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F);
        PartDefinition p6 = p0.addOrReplaceChild("tail_base", b, PartPose.offsetAndRotation(0.0F, 5.5F, 0.8F, 0.15F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(26, 78).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F);
        PartDefinition p7 = p6.addOrReplaceChild("tail_base_1", b, PartPose.offsetAndRotation(0.0F, 2.1F, 0.0F, -0.38F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(8, 91).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F);
        PartDefinition p8 = p7.addOrReplaceChild("tail_base_2", b, PartPose.offsetAndRotation(0.0F, 2.1F, 0.0F, -0.38F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(16, 91).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F);
        PartDefinition p9 = p8.addOrReplaceChild("tail_base_3", b, PartPose.offsetAndRotation(0.0F, 2.1F, 0.0F, -0.38F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(24, 91).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F);
        PartDefinition p10 = p9.addOrReplaceChild("tail_base_4", b, PartPose.offsetAndRotation(0.0F, 2.1F, 0.0F, -0.38F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(0, 95).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
        PartDefinition p11 = p10.addOrReplaceChild("tail_base_5", b, PartPose.offsetAndRotation(0.0F, 2.1F, 0.0F, -0.38F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(4, 95).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
        p11.addOrReplaceChild("tail_base_6", b, PartPose.offsetAndRotation(0.0F, 2.1F, 0.0F, -0.38F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 128);
    }
}
