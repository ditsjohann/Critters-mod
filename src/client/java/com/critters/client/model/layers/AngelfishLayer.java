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
public final class AngelfishLayer {
    private AngelfishLayer() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder b;
        b = CubeListBuilder.create();
        b.texOffs(0, 92).addBox(-2.6F, -6.5F, -2.5F, 1.0F, 13.0F, 5.0F);
        b.texOffs(84, 66).addBox(-2.6F, -5.5F, -4.0F, 1.0F, 11.0F, 8.0F);
        b.texOffs(24, 92).addBox(-2.6F, -2.5F, -5.5F, 1.0F, 5.0F, 11.0F);
        b.texOffs(112, 36).addBox(-1.56F, -9.5F, -3.5F, 1.0F, 19.0F, 7.0F);
        b.texOffs(0, 36).addBox(-1.56F, -8.5F, -6.5F, 1.0F, 17.0F, 13.0F);
        b.texOffs(16, 66).addBox(-1.56F, -4.0F, -8.0F, 1.0F, 8.0F, 16.0F);
        b.texOffs(56, 36).addBox(-0.52F, -10.5F, -4.0F, 1.0F, 21.0F, 8.0F);
        b.texOffs(88, 0).addBox(-0.52F, -9.0F, -7.0F, 1.0F, 18.0F, 14.0F);
        b.texOffs(74, 36).addBox(-0.52F, -4.5F, -9.0F, 1.0F, 9.0F, 18.0F);
        b.texOffs(0, 66).addBox(0.52F, -9.5F, -3.5F, 1.0F, 19.0F, 7.0F);
        b.texOffs(28, 36).addBox(0.52F, -8.5F, -6.5F, 1.0F, 17.0F, 13.0F);
        b.texOffs(50, 66).addBox(0.52F, -4.0F, -8.0F, 1.0F, 8.0F, 16.0F);
        b.texOffs(12, 92).addBox(1.56F, -6.5F, -2.5F, 1.0F, 13.0F, 5.0F);
        b.texOffs(102, 66).addBox(1.56F, -5.5F, -4.0F, 1.0F, 11.0F, 8.0F);
        b.texOffs(48, 92).addBox(1.56F, -2.5F, -5.5F, 1.0F, 5.0F, 11.0F);
        PartDefinition p0 = root.addOrReplaceChild("body", b, PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(72, 119).addBox(-1.0F, -1.16F, -7.5F, 2.0F, 4.0F, 1.0F);
        b.texOffs(44, 110).addBox(-1.5F, -2.66F, -6.5F, 3.0F, 7.0F, 1.0F);
        b.texOffs(0, 110).addBox(-2.0F, -3.16F, -5.5F, 4.0F, 8.0F, 1.0F);
        b.texOffs(72, 110).addBox(-2.0F, -2.16F, -4.5F, 4.0F, 6.0F, 1.0F);
        b.texOffs(96, 92).addBox(-1.5F, -3.66F, -4.5F, 3.0F, 9.0F, 1.0F);
        b.texOffs(82, 110).addBox(-2.0F, -2.16F, -3.5F, 4.0F, 6.0F, 1.0F);
        b.texOffs(104, 92).addBox(-1.5F, -3.66F, -3.5F, 3.0F, 9.0F, 1.0F);
        b.texOffs(92, 110).addBox(-2.0F, -2.16F, -2.5F, 4.0F, 6.0F, 1.0F);
        b.texOffs(112, 92).addBox(-1.5F, -3.66F, -2.5F, 3.0F, 9.0F, 1.0F);
        b.texOffs(102, 110).addBox(-2.0F, -2.16F, -1.5F, 4.0F, 6.0F, 1.0F);
        b.texOffs(120, 92).addBox(-1.5F, -3.66F, -1.5F, 3.0F, 9.0F, 1.0F);
        b.texOffs(10, 110).addBox(-2.0F, -3.16F, -0.5F, 4.0F, 8.0F, 1.0F);
        b.texOffs(52, 110).addBox(-1.5F, -2.66F, 0.5F, 3.0F, 7.0F, 1.0F);
        b.texOffs(78, 119).addBox(-1.0F, -1.16F, 1.5F, 2.0F, 4.0F, 1.0F);
        b.texOffs(92, 119).addBox(0.61F, -3.4912F, -5.0F, 2.0F, 1.0F, 3.0F);
        b.texOffs(16, 119).addBox(0.11F, -2.5673F, -5.5F, 3.0F, 1.0F, 4.0F);
        b.texOffs(112, 110).addBox(0.11F, -1.6432F, -6.0F, 3.0F, 1.0F, 5.0F);
        b.texOffs(30, 119).addBox(0.11F, -0.7192F, -5.5F, 3.0F, 1.0F, 4.0F);
        b.texOffs(102, 119).addBox(0.61F, 0.2048F, -5.0F, 2.0F, 1.0F, 3.0F);
        b.texOffs(112, 119).addBox(-2.61F, -3.4912F, -5.0F, 2.0F, 1.0F, 3.0F);
        b.texOffs(44, 119).addBox(-3.11F, -2.5673F, -5.5F, 3.0F, 1.0F, 4.0F);
        b.texOffs(0, 119).addBox(-3.11F, -1.6432F, -6.0F, 3.0F, 1.0F, 5.0F);
        b.texOffs(58, 119).addBox(-3.11F, -0.7192F, -5.5F, 3.0F, 1.0F, 4.0F);
        b.texOffs(0, 125).addBox(-2.61F, 0.2048F, -5.0F, 2.0F, 1.0F, 3.0F);
        p0.addOrReplaceChild("head", b, PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(84, 119).addBox(-0.5F, -2.0F, -1.5F, 1.0F, 4.0F, 1.0F);
        b.texOffs(60, 110).addBox(-1.0F, -3.5F, -0.5F, 2.0F, 7.0F, 1.0F);
        b.texOffs(20, 110).addBox(-1.0F, -4.0F, 0.5F, 2.0F, 8.0F, 1.0F);
        b.texOffs(26, 110).addBox(-1.0F, -4.0F, 1.5F, 2.0F, 8.0F, 1.0F);
        b.texOffs(32, 110).addBox(-1.0F, -4.0F, 2.5F, 2.0F, 8.0F, 1.0F);
        b.texOffs(38, 110).addBox(-1.0F, -4.0F, 3.5F, 2.0F, 8.0F, 1.0F);
        b.texOffs(66, 110).addBox(-1.0F, -3.5F, 4.5F, 2.0F, 7.0F, 1.0F);
        b.texOffs(88, 119).addBox(-0.5F, -2.0F, 5.5F, 1.0F, 4.0F, 1.0F);
        PartDefinition p2 = p0.addOrReplaceChild("tail_base", b, PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(64, 0).addBox(0.0F, -11.0F, 0.0F, 0.0F, 22.0F, 12.0F);
        p2.addOrReplaceChild("tail_fin", b, PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(0, 0).addBox(0.0F, -20.0F, 0.0F, 0.0F, 20.0F, 16.0F);
        p0.addOrReplaceChild("dorsal_fin", b, PartPose.offsetAndRotation(0.0F, -9.7F, -3.15F, 0.25F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(32, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 20.0F, 16.0F);
        p0.addOrReplaceChild("anal_fin", b, PartPose.offsetAndRotation(0.0F, 9.7F, -0.9F, -0.25F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(72, 92).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 6.0F);
        p0.addOrReplaceChild("fin_l", b, PartPose.offsetAndRotation(2.08F, 3.15F, -4.05F, 0.0F, 0.7F, 0.3F));
        b = CubeListBuilder.create();
        b.texOffs(120, 66).addBox(0.0F, 0.0F, 0.0F, 0.0F, 18.0F, 1.0F);
        p0.addOrReplaceChild("pelvic_l", b, PartPose.offsetAndRotation(0.8F, 7.875F, -3.6F, -0.15F, 0.0F, 0.12F));
        b = CubeListBuilder.create();
        b.texOffs(84, 92).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 6.0F);
        p0.addOrReplaceChild("fin_r", b, PartPose.offsetAndRotation(-2.08F, 3.15F, -4.05F, 0.0F, -0.7F, -0.3F));
        b = CubeListBuilder.create();
        b.texOffs(122, 66).addBox(0.0F, 0.0F, 0.0F, 0.0F, 18.0F, 1.0F);
        p0.addOrReplaceChild("pelvic_r", b, PartPose.offsetAndRotation(-0.8F, 7.875F, -3.6F, -0.15F, 0.0F, -0.12F));
        return LayerDefinition.create(mesh, 128, 256);
    }
}
