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
public final class NudibranchLayer {
    private NudibranchLayer() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder b;
        b = CubeListBuilder.create();
        b.texOffs(28, 36).addBox(-1.5F, -2.0F, -10.5F, 3.0F, 2.0F, 2.0F);
        b.texOffs(16, 18).addBox(-2.5F, -3.0F, -8.4F, 5.0F, 3.0F, 2.0F);
        b.texOffs(36, 13).addBox(-3.0F, -3.0F, -6.3F, 6.0F, 3.0F, 2.0F);
        b.texOffs(48, 0).addBox(-2.0F, -4.0F, -6.3F, 4.0F, 4.0F, 2.0F);
        b.texOffs(12, 7).addBox(-3.5F, -3.0F, -4.2F, 7.0F, 3.0F, 2.0F);
        b.texOffs(0, 0).addBox(-2.0F, -5.0F, -4.2F, 4.0F, 5.0F, 2.0F);
        b.texOffs(30, 7).addBox(-3.5F, -3.0F, -2.1F, 7.0F, 3.0F, 2.0F);
        b.texOffs(12, 0).addBox(-2.0F, -5.0F, -2.1F, 4.0F, 5.0F, 2.0F);
        b.texOffs(0, 13).addBox(-3.5F, -3.0F, 0.0F, 7.0F, 3.0F, 2.0F);
        b.texOffs(24, 0).addBox(-2.0F, -5.0F, 0.0F, 4.0F, 5.0F, 2.0F);
        b.texOffs(18, 13).addBox(-3.5F, -3.0F, 2.1F, 7.0F, 3.0F, 2.0F);
        b.texOffs(36, 0).addBox(-2.0F, -5.0F, 2.1F, 4.0F, 5.0F, 2.0F);
        b.texOffs(0, 18).addBox(-3.0F, -3.0F, 4.2F, 6.0F, 3.0F, 2.0F);
        b.texOffs(0, 7).addBox(-2.0F, -4.0F, 4.2F, 4.0F, 4.0F, 2.0F);
        b.texOffs(30, 18).addBox(-2.5F, -3.0F, 6.3F, 5.0F, 3.0F, 2.0F);
        b.texOffs(38, 36).addBox(-1.5F, -2.0F, 8.4F, 3.0F, 2.0F, 2.0F);
        b.texOffs(0, 36).addBox(-2.0F, -1.0F, -11.5F, 4.0F, 1.0F, 3.0F);
        b.texOffs(24, 32).addBox(-3.5F, -1.0F, -8.625F, 7.0F, 1.0F, 3.0F);
        b.texOffs(24, 23).addBox(-4.5F, -1.0F, -5.75F, 9.0F, 1.0F, 3.0F);
        b.texOffs(0, 28).addBox(-4.5F, -1.0F, -2.875F, 9.0F, 1.0F, 3.0F);
        b.texOffs(24, 28).addBox(-4.5F, -1.0F, 0.0F, 9.0F, 1.0F, 3.0F);
        b.texOffs(0, 32).addBox(-4.5F, -1.0F, 2.875F, 9.0F, 1.0F, 3.0F);
        b.texOffs(44, 32).addBox(-3.5F, -1.0F, 5.75F, 7.0F, 1.0F, 3.0F);
        b.texOffs(14, 36).addBox(-2.0F, -1.0F, 8.625F, 4.0F, 1.0F, 3.0F);
        PartDefinition p0 = root.addOrReplaceChild("body", b, PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(0, 40).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F);
        b.texOffs(44, 18).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 3.0F, 2.0F);
        p0.addOrReplaceChild("rhinophore_l", b, PartPose.offsetAndRotation(1.6F, -4.2F, -7.5F, -0.2F, 0.0F, 0.25F));
        b = CubeListBuilder.create();
        b.texOffs(48, 36).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F);
        p0.addOrReplaceChild("tentacle_l", b, PartPose.offsetAndRotation(2.5F, -1.5F, -10.0F, 0.6F, -0.6F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(4, 40).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F);
        b.texOffs(52, 18).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 3.0F, 2.0F);
        p0.addOrReplaceChild("rhinophore_r", b, PartPose.offsetAndRotation(-1.6F, -4.2F, -7.5F, -0.2F, 0.0F, -0.25F));
        b = CubeListBuilder.create();
        b.texOffs(56, 36).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F);
        p0.addOrReplaceChild("tentacle_r", b, PartPose.offsetAndRotation(-2.5F, -1.5F, -10.0F, 0.6F, 0.6F, 0.0F));
        PartDefinition p5 = p0.addOrReplaceChild("gills", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.2F, 6.5F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(60, 18).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F);
        b.texOffs(8, 40).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 1.0F, 1.0F);
        p5.addOrReplaceChild("gill_0", b, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.35F, 0.0F, -1.26F));
        b = CubeListBuilder.create();
        b.texOffs(0, 23).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F);
        b.texOffs(14, 40).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 1.0F, 1.0F);
        p5.addOrReplaceChild("gill_1", b, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.35F, 0.0F, -0.84F));
        b = CubeListBuilder.create();
        b.texOffs(4, 23).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F);
        b.texOffs(20, 40).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 1.0F, 1.0F);
        p5.addOrReplaceChild("gill_2", b, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.35F, 0.0F, -0.42F));
        b = CubeListBuilder.create();
        b.texOffs(8, 23).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F);
        b.texOffs(26, 40).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 1.0F, 1.0F);
        p5.addOrReplaceChild("gill_3", b, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.35F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(12, 23).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F);
        b.texOffs(32, 40).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 1.0F, 1.0F);
        p5.addOrReplaceChild("gill_4", b, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.35F, 0.0F, 0.42F));
        b = CubeListBuilder.create();
        b.texOffs(16, 23).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F);
        b.texOffs(38, 40).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 1.0F, 1.0F);
        p5.addOrReplaceChild("gill_5", b, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.35F, 0.0F, 0.84F));
        b = CubeListBuilder.create();
        b.texOffs(20, 23).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F);
        b.texOffs(44, 40).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 1.0F, 1.0F);
        p5.addOrReplaceChild("gill_6", b, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.35F, 0.0F, 1.26F));
        return LayerDefinition.create(mesh, 64, 64);
    }
}
