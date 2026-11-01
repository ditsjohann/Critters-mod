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
public final class GoldfishLayer {
    private GoldfishLayer() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder b;
        b = CubeListBuilder.create();
        b.texOffs(36, 98).addBox(-2.0F, -3.0F, -12.0F, 4.0F, 6.0F, 2.0F);
        b.texOffs(0, 98).addBox(-3.5F, -3.0F, -10.0F, 7.0F, 6.0F, 2.0F);
        b.texOffs(80, 55).addBox(-2.0F, -5.0F, -10.0F, 4.0F, 10.0F, 2.0F);
        b.texOffs(60, 98).addBox(-4.0F, -2.5F, -8.0F, 8.0F, 5.0F, 2.0F);
        b.texOffs(16, 68).addBox(-3.5F, -4.5F, -8.0F, 7.0F, 9.0F, 2.0F);
        b.texOffs(52, 38).addBox(-1.5F, -6.0F, -8.0F, 3.0F, 12.0F, 2.0F);
        b.texOffs(72, 90).addBox(-4.5F, -3.0F, -6.0F, 9.0F, 6.0F, 2.0F);
        b.texOffs(40, 55).addBox(-4.0F, -5.0F, -6.0F, 8.0F, 10.0F, 2.0F);
        b.texOffs(12, 38).addBox(-2.0F, -7.0F, -6.0F, 4.0F, 14.0F, 2.0F);
        b.texOffs(84, 80).addBox(-5.0F, -3.0F, -4.0F, 10.0F, 6.0F, 2.0F);
        b.texOffs(0, 55).addBox(-4.0F, -5.5F, -4.0F, 8.0F, 11.0F, 2.0F);
        b.texOffs(92, 0).addBox(-2.0F, -7.5F, -4.0F, 4.0F, 15.0F, 2.0F);
        b.texOffs(0, 90).addBox(-5.0F, -3.0F, -2.0F, 10.0F, 6.0F, 2.0F);
        b.texOffs(72, 38).addBox(-4.5F, -5.5F, -2.0F, 9.0F, 11.0F, 2.0F);
        b.texOffs(104, 0).addBox(-2.0F, -7.5F, -2.0F, 4.0F, 15.0F, 2.0F);
        b.texOffs(24, 90).addBox(-5.0F, -3.0F, 0.0F, 10.0F, 6.0F, 2.0F);
        b.texOffs(94, 38).addBox(-4.5F, -5.5F, 0.0F, 9.0F, 11.0F, 2.0F);
        b.texOffs(116, 0).addBox(-2.0F, -7.5F, 0.0F, 4.0F, 15.0F, 2.0F);
        b.texOffs(48, 90).addBox(-5.0F, -3.0F, 2.0F, 10.0F, 6.0F, 2.0F);
        b.texOffs(20, 55).addBox(-4.0F, -5.5F, 2.0F, 8.0F, 11.0F, 2.0F);
        b.texOffs(0, 38).addBox(-2.0F, -7.5F, 2.0F, 4.0F, 15.0F, 2.0F);
        b.texOffs(94, 90).addBox(-4.5F, -3.0F, 4.0F, 9.0F, 6.0F, 2.0F);
        b.texOffs(60, 55).addBox(-4.0F, -5.0F, 4.0F, 8.0F, 10.0F, 2.0F);
        b.texOffs(24, 38).addBox(-2.0F, -7.0F, 4.0F, 4.0F, 14.0F, 2.0F);
        b.texOffs(80, 98).addBox(-4.0F, -2.5F, 6.0F, 8.0F, 5.0F, 2.0F);
        b.texOffs(34, 68).addBox(-3.5F, -4.5F, 6.0F, 7.0F, 9.0F, 2.0F);
        b.texOffs(62, 38).addBox(-1.5F, -6.0F, 6.0F, 3.0F, 12.0F, 2.0F);
        b.texOffs(18, 98).addBox(-3.5F, -3.0F, 8.0F, 7.0F, 6.0F, 2.0F);
        b.texOffs(92, 55).addBox(-2.0F, -5.0F, 8.0F, 4.0F, 10.0F, 2.0F);
        b.texOffs(48, 98).addBox(-2.0F, -3.0F, 10.0F, 4.0F, 6.0F, 2.0F);
        PartDefinition p0 = root.addOrReplaceChild("body", b, PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(28, 113).addBox(-2.0F, -1.9F, -7.5F, 4.0F, 5.0F, 1.0F);
        b.texOffs(0, 113).addBox(-3.0F, -1.9F, -6.5F, 6.0F, 5.0F, 1.0F);
        b.texOffs(44, 80).addBox(-2.0F, -3.4F, -6.5F, 4.0F, 8.0F, 1.0F);
        b.texOffs(100, 98).addBox(-3.5F, -2.4F, -5.5F, 7.0F, 6.0F, 1.0F);
        b.texOffs(52, 68).addBox(-2.5F, -4.4F, -5.5F, 5.0F, 10.0F, 1.0F);
        b.texOffs(48, 106).addBox(-4.0F, -1.9F, -4.5F, 8.0F, 5.0F, 1.0F);
        b.texOffs(12, 80).addBox(-3.5F, -3.4F, -4.5F, 7.0F, 8.0F, 1.0F);
        b.texOffs(0, 68).addBox(-1.5F, -4.9F, -4.5F, 3.0F, 11.0F, 1.0F);
        b.texOffs(66, 106).addBox(-4.0F, -1.9F, -3.5F, 8.0F, 5.0F, 1.0F);
        b.texOffs(76, 68).addBox(-3.5F, -3.9F, -3.5F, 7.0F, 9.0F, 1.0F);
        b.texOffs(104, 55).addBox(-2.0F, -4.9F, -3.5F, 4.0F, 11.0F, 1.0F);
        b.texOffs(84, 106).addBox(-4.0F, -1.9F, -2.5F, 8.0F, 5.0F, 1.0F);
        b.texOffs(92, 68).addBox(-3.5F, -3.9F, -2.5F, 7.0F, 9.0F, 1.0F);
        b.texOffs(114, 55).addBox(-2.0F, -4.9F, -2.5F, 4.0F, 11.0F, 1.0F);
        b.texOffs(102, 106).addBox(-4.0F, -1.9F, -1.5F, 8.0F, 5.0F, 1.0F);
        b.texOffs(28, 80).addBox(-3.5F, -3.4F, -1.5F, 7.0F, 8.0F, 1.0F);
        b.texOffs(8, 68).addBox(-1.5F, -4.9F, -1.5F, 3.0F, 11.0F, 1.0F);
        b.texOffs(0, 106).addBox(-3.5F, -2.4F, -0.5F, 7.0F, 6.0F, 1.0F);
        b.texOffs(64, 68).addBox(-2.5F, -4.4F, -0.5F, 5.0F, 10.0F, 1.0F);
        b.texOffs(14, 113).addBox(-3.0F, -1.9F, 0.5F, 6.0F, 5.0F, 1.0F);
        b.texOffs(54, 80).addBox(-2.0F, -3.4F, 0.5F, 4.0F, 8.0F, 1.0F);
        b.texOffs(38, 113).addBox(-2.0F, -1.9F, 1.5F, 4.0F, 5.0F, 1.0F);
        b.texOffs(12, 119).addBox(2.65F, -3.0562F, -4.5F, 2.0F, 1.0F, 2.0F);
        b.texOffs(88, 113).addBox(2.65F, -1.9563F, -5.0F, 2.0F, 1.0F, 3.0F);
        b.texOffs(20, 119).addBox(2.65F, -0.8563F, -4.5F, 2.0F, 1.0F, 2.0F);
        b.texOffs(28, 119).addBox(-4.65F, -3.0562F, -4.5F, 2.0F, 1.0F, 2.0F);
        b.texOffs(98, 113).addBox(-4.65F, -1.9563F, -5.0F, 2.0F, 1.0F, 3.0F);
        b.texOffs(36, 119).addBox(-4.65F, -0.8563F, -4.5F, 2.0F, 1.0F, 2.0F);
        p0.addOrReplaceChild("head", b, PartPose.offsetAndRotation(0.0F, 0.0F, -9.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(0, 119).addBox(-1.0F, -1.5F, -1.5F, 2.0F, 3.0F, 1.0F);
        b.texOffs(48, 113).addBox(-2.0F, -2.5F, -0.5F, 4.0F, 5.0F, 1.0F);
        b.texOffs(108, 113).addBox(-2.0F, -1.5F, 0.5F, 4.0F, 3.0F, 1.0F);
        b.texOffs(16, 106).addBox(-1.5F, -3.0F, 0.5F, 3.0F, 6.0F, 1.0F);
        b.texOffs(68, 113).addBox(-2.0F, -2.0F, 1.5F, 4.0F, 4.0F, 1.0F);
        b.texOffs(24, 106).addBox(-1.5F, -3.0F, 1.5F, 3.0F, 6.0F, 1.0F);
        b.texOffs(78, 113).addBox(-2.0F, -2.0F, 2.5F, 4.0F, 4.0F, 1.0F);
        b.texOffs(32, 106).addBox(-1.5F, -3.0F, 2.5F, 3.0F, 6.0F, 1.0F);
        b.texOffs(118, 113).addBox(-2.0F, -1.5F, 3.5F, 4.0F, 3.0F, 1.0F);
        b.texOffs(40, 106).addBox(-1.5F, -3.0F, 3.5F, 3.0F, 6.0F, 1.0F);
        b.texOffs(58, 113).addBox(-2.0F, -2.5F, 4.5F, 4.0F, 5.0F, 1.0F);
        b.texOffs(6, 119).addBox(-1.0F, -1.5F, 5.5F, 2.0F, 3.0F, 1.0F);
        PartDefinition p2 = p0.addOrReplaceChild("tail_base", b, PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p3 = p2.addOrReplaceChild("tail_fin", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(0, 0).addBox(0.0F, -11.0F, 0.0F, 0.0F, 22.0F, 16.0F);
        p3.addOrReplaceChild("tail_a", b, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.35F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(32, 0).addBox(0.0F, -11.0F, 0.0F, 0.0F, 22.0F, 16.0F);
        p3.addOrReplaceChild("tail_b", b, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.35F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(64, 0).addBox(0.0F, -7.0F, 0.0F, 0.0F, 7.0F, 14.0F);
        p0.addOrReplaceChild("dorsal_fin", b, PartPose.offsetAndRotation(0.0F, -6.7F, -4.2F, 0.1F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(36, 38).addBox(0.0F, 0.0F, 0.0F, 0.0F, 6.0F, 8.0F);
        p0.addOrReplaceChild("anal_fin", b, PartPose.offsetAndRotation(0.0F, 6.7F, 3.6F, -0.1F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(108, 68).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 6.0F);
        p0.addOrReplaceChild("fin_l", b, PartPose.offsetAndRotation(4.0F, 2.25F, -5.4F, 0.0F, 0.7F, 0.3F));
        b = CubeListBuilder.create();
        b.texOffs(64, 80).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 5.0F);
        p0.addOrReplaceChild("pelvic_l", b, PartPose.offsetAndRotation(2.5F, 6.375F, -0.6F, -0.5F, 0.0F, 0.35F));
        b = CubeListBuilder.create();
        b.texOffs(0, 80).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 6.0F);
        p0.addOrReplaceChild("fin_r", b, PartPose.offsetAndRotation(-4.0F, 2.25F, -5.4F, 0.0F, -0.7F, -0.3F));
        b = CubeListBuilder.create();
        b.texOffs(74, 80).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 5.0F);
        p0.addOrReplaceChild("pelvic_r", b, PartPose.offsetAndRotation(-2.5F, 6.375F, -0.6F, -0.5F, 0.0F, -0.35F));
        return LayerDefinition.create(mesh, 128, 128);
    }
}
