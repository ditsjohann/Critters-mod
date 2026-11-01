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
public final class DartFrogLayer {
    private DartFrogLayer() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder b;
        b = CubeListBuilder.create();
        b.texOffs(22, 67).addBox(-2.5F, -9.3F, -0.5F, 5.0F, 1.0F, 4.0F);
        b.texOffs(56, 47).addBox(-1.5F, -9.3F, -1.5F, 3.0F, 1.0F, 6.0F);
        b.texOffs(50, 61).addBox(-3.5F, -8.35F, -0.5F, 7.0F, 1.0F, 4.0F);
        b.texOffs(0, 38).addBox(-3.0F, -8.35F, -2.5F, 6.0F, 1.0F, 8.0F);
        b.texOffs(0, 27).addBox(-1.5F, -8.35F, -3.5F, 3.0F, 1.0F, 10.0F);
        b.texOffs(30, 55).addBox(-4.5F, -7.4F, -1.0F, 9.0F, 1.0F, 5.0F);
        b.texOffs(52, 27).addBox(-4.0F, -7.4F, -3.0F, 8.0F, 1.0F, 9.0F);
        b.texOffs(68, 0).addBox(-2.0F, -7.4F, -4.5F, 4.0F, 1.0F, 12.0F);
        b.texOffs(92, 47).addBox(-5.0F, -6.45F, -1.0F, 10.0F, 1.0F, 5.0F);
        b.texOffs(32, 14).addBox(-4.0F, -6.45F, -3.5F, 8.0F, 1.0F, 10.0F);
        b.texOffs(0, 0).addBox(-2.0F, -6.45F, -5.0F, 4.0F, 1.0F, 13.0F);
        b.texOffs(0, 55).addBox(-5.0F, -5.5F, -1.0F, 10.0F, 1.0F, 5.0F);
        b.texOffs(68, 14).addBox(-4.0F, -5.5F, -3.5F, 8.0F, 1.0F, 10.0F);
        b.texOffs(34, 0).addBox(-2.0F, -5.5F, -5.0F, 4.0F, 1.0F, 13.0F);
        b.texOffs(58, 55).addBox(-4.5F, -4.55F, -1.0F, 9.0F, 1.0F, 5.0F);
        b.texOffs(86, 27).addBox(-4.0F, -4.55F, -3.0F, 8.0F, 1.0F, 9.0F);
        b.texOffs(0, 14).addBox(-2.0F, -4.55F, -4.5F, 4.0F, 1.0F, 12.0F);
        b.texOffs(72, 61).addBox(-3.5F, -3.6F, -0.5F, 7.0F, 1.0F, 4.0F);
        b.texOffs(28, 38).addBox(-3.0F, -3.6F, -2.5F, 6.0F, 1.0F, 8.0F);
        b.texOffs(26, 27).addBox(-1.5F, -3.6F, -3.5F, 3.0F, 1.0F, 10.0F);
        b.texOffs(40, 67).addBox(-2.5F, -2.65F, -0.5F, 5.0F, 1.0F, 4.0F);
        b.texOffs(74, 47).addBox(-1.5F, -2.65F, -1.5F, 3.0F, 1.0F, 6.0F);
        PartDefinition p0 = root.addOrReplaceChild("body", b, PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(86, 67).addBox(-2.5F, -3.0F, -4.3F, 5.0F, 1.0F, 3.0F);
        b.texOffs(58, 67).addBox(-1.5F, -3.0F, -4.8F, 3.0F, 1.0F, 4.0F);
        b.texOffs(94, 61).addBox(-3.5F, -2.0F, -4.8F, 7.0F, 1.0F, 4.0F);
        b.texOffs(56, 38).addBox(-2.5F, -2.0F, -6.3F, 5.0F, 1.0F, 7.0F);
        b.texOffs(86, 55).addBox(-4.0F, -1.0F, -5.3F, 8.0F, 1.0F, 5.0F);
        b.texOffs(80, 38).addBox(-2.5F, -1.0F, -6.3F, 5.0F, 1.0F, 7.0F);
        b.texOffs(0, 61).addBox(-4.0F, 0.0F, -5.3F, 8.0F, 1.0F, 5.0F);
        b.texOffs(104, 38).addBox(-2.5F, 0.0F, -6.3F, 5.0F, 1.0F, 7.0F);
        b.texOffs(0, 67).addBox(-3.5F, 1.0F, -4.8F, 7.0F, 1.0F, 4.0F);
        b.texOffs(0, 47).addBox(-2.5F, 1.0F, -6.3F, 5.0F, 1.0F, 7.0F);
        b.texOffs(102, 67).addBox(-2.5F, 2.0F, -4.3F, 5.0F, 1.0F, 3.0F);
        b.texOffs(72, 67).addBox(-1.5F, 2.0F, -4.8F, 3.0F, 1.0F, 4.0F);
        b.texOffs(24, 72).addBox(2.0F, -3.7F, -4.1F, 2.0F, 1.0F, 3.0F);
        b.texOffs(0, 72).addBox(1.5F, -2.5667F, -4.1F, 3.0F, 1.0F, 3.0F);
        b.texOffs(34, 72).addBox(2.0F, -1.4333F, -4.1F, 2.0F, 1.0F, 3.0F);
        b.texOffs(44, 72).addBox(-4.0F, -3.7F, -4.1F, 2.0F, 1.0F, 3.0F);
        b.texOffs(12, 72).addBox(-4.5F, -2.5667F, -4.1F, 3.0F, 1.0F, 3.0F);
        b.texOffs(54, 72).addBox(-4.0F, -1.4333F, -4.1F, 2.0F, 1.0F, 3.0F);
        p0.addOrReplaceChild("head", b, PartPose.offsetAndRotation(0.0F, -6.5F, -4.0F, -0.1F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(56, 76).addBox(-1.0F, -1.0F, -6.6F, 2.0F, 2.0F, 1.0F);
        b.texOffs(84, 72).addBox(-1.5F, -1.5F, -5.5714F, 3.0F, 3.0F, 1.0F);
        b.texOffs(92, 72).addBox(-1.5F, -1.5F, -4.5429F, 3.0F, 3.0F, 1.0F);
        b.texOffs(100, 72).addBox(-1.5F, -1.5F, -3.5143F, 3.0F, 3.0F, 1.0F);
        b.texOffs(108, 72).addBox(-1.5F, -1.5F, -2.4857F, 3.0F, 3.0F, 1.0F);
        b.texOffs(116, 72).addBox(-1.5F, -1.5F, -1.4571F, 3.0F, 3.0F, 1.0F);
        b.texOffs(62, 76).addBox(-1.0F, -1.0F, -0.4286F, 2.0F, 2.0F, 1.0F);
        PartDefinition p2 = p0.addOrReplaceChild("hindleg_l", b, PartPose.offsetAndRotation(4.0F, -3.2F, 5.0F, 0.0F, -0.75F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(24, 47).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F);
        PartDefinition p3 = p2.addOrReplaceChild("shin_l", b, PartPose.offsetAndRotation(0.0F, 0.8F, -6.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(26, 61).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F);
        b.texOffs(40, 76).addBox(-1.0F, -0.5F, -6.0F, 2.0F, 1.0F, 2.0F);
        p3.addOrReplaceChild("foot_l", b, PartPose.offsetAndRotation(0.0F, 1.2F, 6.0F, 0.0F, -0.5F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(68, 76).addBox(-1.0F, -1.0F, -6.6F, 2.0F, 2.0F, 1.0F);
        b.texOffs(0, 76).addBox(-1.5F, -1.5F, -5.5714F, 3.0F, 3.0F, 1.0F);
        b.texOffs(8, 76).addBox(-1.5F, -1.5F, -4.5429F, 3.0F, 3.0F, 1.0F);
        b.texOffs(16, 76).addBox(-1.5F, -1.5F, -3.5143F, 3.0F, 3.0F, 1.0F);
        b.texOffs(24, 76).addBox(-1.5F, -1.5F, -2.4857F, 3.0F, 3.0F, 1.0F);
        b.texOffs(32, 76).addBox(-1.5F, -1.5F, -1.4571F, 3.0F, 3.0F, 1.0F);
        b.texOffs(74, 76).addBox(-1.0F, -1.0F, -0.4286F, 2.0F, 2.0F, 1.0F);
        PartDefinition p5 = p0.addOrReplaceChild("hindleg_r", b, PartPose.offsetAndRotation(-4.0F, -3.2F, 5.0F, 0.0F, 0.75F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(40, 47).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F);
        PartDefinition p6 = p5.addOrReplaceChild("shin_r", b, PartPose.offsetAndRotation(0.0F, 0.8F, -6.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(38, 61).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F);
        b.texOffs(48, 76).addBox(-1.0F, -0.5F, -6.0F, 2.0F, 1.0F, 2.0F);
        p6.addOrReplaceChild("foot_r", b, PartPose.offsetAndRotation(0.0F, 1.2F, 6.0F, 0.0F, 0.5F, 0.0F));
        PartDefinition p8 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create(), PartPose.offsetAndRotation(3.2F, 19.5F, -3.5F, 0.0F, 0.5F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(64, 72).addBox(0.0F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F);
        PartDefinition p9 = p8.addOrReplaceChild("femur_l0", b, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2F));
        b = CubeListBuilder.create();
        b.texOffs(80, 76).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F);
        PartDefinition p10 = p9.addOrReplaceChild("tibia_l0", b, PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, 0.0F, 0.0F, 1.6708F));
        b = CubeListBuilder.create();
        b.texOffs(100, 76).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F);
        p10.addOrReplaceChild("tarsus_l0", b, PartPose.offsetAndRotation(3.7F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1708F));
        PartDefinition p12 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.2F, 19.5F, -3.5F, 0.0F, -0.5F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(74, 72).addBox(-3.0F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F);
        PartDefinition p13 = p12.addOrReplaceChild("femur_r0", b, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2F));
        b = CubeListBuilder.create();
        b.texOffs(90, 76).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F);
        PartDefinition p14 = p13.addOrReplaceChild("tibia_r0", b, PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.6708F));
        b = CubeListBuilder.create();
        b.texOffs(106, 76).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F);
        p14.addOrReplaceChild("tarsus_r0", b, PartPose.offsetAndRotation(-3.7F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1708F));
        return LayerDefinition.create(mesh, 128, 128);
    }
}
