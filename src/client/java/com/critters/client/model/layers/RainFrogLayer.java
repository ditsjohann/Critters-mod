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
public final class RainFrogLayer {
    private RainFrogLayer() {
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeListBuilder b;
        b = CubeListBuilder.create();
        b.texOffs(100, 164).addBox(-2.5F, -14.0F, -0.5F, 5.0F, 1.0F, 3.0F);
        b.texOffs(72, 149).addBox(-1.5F, -14.0F, -1.5F, 3.0F, 1.0F, 5.0F);
        b.texOffs(76, 117).addBox(-4.5F, -13.0F, -2.0F, 9.0F, 1.0F, 6.0F);
        b.texOffs(36, 92).addBox(-3.0F, -13.0F, -3.5F, 6.0F, 1.0F, 9.0F);
        b.texOffs(68, 131).addBox(-5.5F, -12.0F, -1.5F, 11.0F, 1.0F, 5.0F);
        b.texOffs(84, 81).addBox(-4.5F, -12.0F, -3.5F, 9.0F, 1.0F, 9.0F);
        b.texOffs(46, 69).addBox(-2.5F, -12.0F, -4.5F, 5.0F, 1.0F, 11.0F);
        b.texOffs(0, 131).addBox(-6.0F, -11.0F, -1.5F, 12.0F, 1.0F, 5.0F);
        b.texOffs(0, 81).addBox(-5.5F, -11.0F, -4.0F, 11.0F, 1.0F, 10.0F);
        b.texOffs(0, 31).addBox(-2.5F, -11.0F, -5.5F, 5.0F, 1.0F, 13.0F);
        b.texOffs(0, 117).addBox(-6.5F, -10.0F, -2.0F, 13.0F, 1.0F, 6.0F);
        b.texOffs(72, 31).addBox(-6.0F, -10.0F, -4.5F, 12.0F, 1.0F, 11.0F);
        b.texOffs(84, 0).addBox(-3.0F, -10.0F, -6.0F, 6.0F, 1.0F, 14.0F);
        b.texOffs(72, 102).addBox(-7.0F, -9.0F, -2.0F, 14.0F, 1.0F, 6.0F);
        b.texOffs(0, 45).addBox(-6.0F, -9.0F, -4.5F, 12.0F, 1.0F, 11.0F);
        b.texOffs(0, 16).addBox(-3.0F, -9.0F, -6.0F, 6.0F, 1.0F, 14.0F);
        b.texOffs(0, 110).addBox(-7.0F, -8.0F, -2.0F, 14.0F, 1.0F, 6.0F);
        b.texOffs(46, 45).addBox(-6.0F, -8.0F, -4.5F, 12.0F, 1.0F, 11.0F);
        b.texOffs(0, 0).addBox(-3.0F, -8.0F, -6.5F, 6.0F, 1.0F, 15.0F);
        b.texOffs(40, 110).addBox(-7.0F, -7.0F, -2.0F, 14.0F, 1.0F, 6.0F);
        b.texOffs(0, 57).addBox(-6.0F, -7.0F, -4.5F, 12.0F, 1.0F, 11.0F);
        b.texOffs(42, 0).addBox(-3.0F, -7.0F, -6.5F, 6.0F, 1.0F, 15.0F);
        b.texOffs(80, 110).addBox(-7.0F, -6.0F, -2.0F, 14.0F, 1.0F, 6.0F);
        b.texOffs(46, 57).addBox(-6.0F, -6.0F, -4.5F, 12.0F, 1.0F, 11.0F);
        b.texOffs(40, 16).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 1.0F, 14.0F);
        b.texOffs(38, 117).addBox(-6.5F, -5.0F, -2.0F, 13.0F, 1.0F, 6.0F);
        b.texOffs(0, 69).addBox(-6.0F, -5.0F, -4.5F, 12.0F, 1.0F, 11.0F);
        b.texOffs(80, 16).addBox(-3.0F, -5.0F, -6.0F, 6.0F, 1.0F, 14.0F);
        b.texOffs(34, 131).addBox(-6.0F, -4.0F, -1.5F, 12.0F, 1.0F, 5.0F);
        b.texOffs(42, 81).addBox(-5.5F, -4.0F, -4.0F, 11.0F, 1.0F, 10.0F);
        b.texOffs(36, 31).addBox(-2.5F, -4.0F, -5.5F, 5.0F, 1.0F, 13.0F);
        b.texOffs(0, 137).addBox(-5.5F, -3.0F, -1.5F, 11.0F, 1.0F, 5.0F);
        b.texOffs(0, 92).addBox(-4.5F, -3.0F, -3.5F, 9.0F, 1.0F, 9.0F);
        b.texOffs(78, 69).addBox(-2.5F, -3.0F, -4.5F, 5.0F, 1.0F, 11.0F);
        b.texOffs(0, 124).addBox(-4.5F, -2.0F, -2.0F, 9.0F, 1.0F, 6.0F);
        b.texOffs(66, 92).addBox(-3.0F, -2.0F, -3.5F, 6.0F, 1.0F, 9.0F);
        b.texOffs(0, 168).addBox(-2.5F, -1.0F, -0.5F, 5.0F, 1.0F, 3.0F);
        b.texOffs(88, 149).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 1.0F, 5.0F);
        PartDefinition p0 = root.addOrReplaceChild("body", b, PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(16, 168).addBox(-2.5F, -4.5F, -3.0F, 5.0F, 1.0F, 3.0F);
        b.texOffs(52, 164).addBox(-4.5F, -3.5F, -3.0F, 9.0F, 1.0F, 3.0F);
        b.texOffs(86, 143).addBox(-2.5F, -3.5F, -4.0F, 5.0F, 1.0F, 5.0F);
        b.texOffs(0, 164).addBox(-5.0F, -2.5F, -3.0F, 10.0F, 1.0F, 3.0F);
        b.texOffs(30, 143).addBox(-4.5F, -2.5F, -4.0F, 9.0F, 1.0F, 5.0F);
        b.texOffs(30, 124).addBox(-2.0F, -2.5F, -4.5F, 4.0F, 1.0F, 6.0F);
        b.texOffs(60, 160).addBox(-5.5F, -1.5F, -3.0F, 11.0F, 1.0F, 3.0F);
        b.texOffs(32, 137).addBox(-5.0F, -1.5F, -4.0F, 10.0F, 1.0F, 5.0F);
        b.texOffs(96, 92).addBox(-2.5F, -1.5F, -5.0F, 5.0F, 1.0F, 7.0F);
        b.texOffs(0, 160).addBox(-6.0F, -0.5F, -3.0F, 12.0F, 1.0F, 3.0F);
        b.texOffs(62, 137).addBox(-5.0F, -0.5F, -4.0F, 10.0F, 1.0F, 5.0F);
        b.texOffs(0, 102).addBox(-2.5F, -0.5F, -5.0F, 5.0F, 1.0F, 7.0F);
        b.texOffs(30, 160).addBox(-6.0F, 0.5F, -3.0F, 12.0F, 1.0F, 3.0F);
        b.texOffs(92, 137).addBox(-5.0F, 0.5F, -4.0F, 10.0F, 1.0F, 5.0F);
        b.texOffs(24, 102).addBox(-2.5F, 0.5F, -5.0F, 5.0F, 1.0F, 7.0F);
        b.texOffs(88, 160).addBox(-5.5F, 1.5F, -3.0F, 11.0F, 1.0F, 3.0F);
        b.texOffs(0, 143).addBox(-5.0F, 1.5F, -4.0F, 10.0F, 1.0F, 5.0F);
        b.texOffs(48, 102).addBox(-2.5F, 1.5F, -5.0F, 5.0F, 1.0F, 7.0F);
        b.texOffs(26, 164).addBox(-5.0F, 2.5F, -3.0F, 10.0F, 1.0F, 3.0F);
        b.texOffs(58, 143).addBox(-4.5F, 2.5F, -4.0F, 9.0F, 1.0F, 5.0F);
        b.texOffs(50, 124).addBox(-2.0F, 2.5F, -4.5F, 4.0F, 1.0F, 6.0F);
        b.texOffs(76, 164).addBox(-4.5F, 3.5F, -3.0F, 9.0F, 1.0F, 3.0F);
        b.texOffs(106, 143).addBox(-2.5F, 3.5F, -4.0F, 5.0F, 1.0F, 5.0F);
        b.texOffs(32, 168).addBox(-2.5F, 4.5F, -3.0F, 5.0F, 1.0F, 3.0F);
        b.texOffs(0, 172).addBox(1.9F, -3.8F, -4.4F, 3.0F, 1.0F, 2.0F);
        b.texOffs(48, 168).addBox(1.4F, -2.8F, -4.9F, 4.0F, 1.0F, 3.0F);
        b.texOffs(62, 168).addBox(1.4F, -1.8F, -4.9F, 4.0F, 1.0F, 3.0F);
        b.texOffs(10, 172).addBox(1.9F, -0.8F, -4.4F, 3.0F, 1.0F, 2.0F);
        b.texOffs(20, 172).addBox(-4.9F, -3.8F, -4.4F, 3.0F, 1.0F, 2.0F);
        b.texOffs(76, 168).addBox(-5.4F, -2.8F, -4.9F, 4.0F, 1.0F, 3.0F);
        b.texOffs(90, 168).addBox(-5.4F, -1.8F, -4.9F, 4.0F, 1.0F, 3.0F);
        b.texOffs(30, 172).addBox(-4.9F, -0.8F, -4.4F, 3.0F, 1.0F, 2.0F);
        p0.addOrReplaceChild("head", b, PartPose.offsetAndRotation(0.0F, -7.5F, -3.5F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(92, 155).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F);
        b.texOffs(104, 168).addBox(-1.1F, 2.4F, -2.0F, 3.0F, 1.0F, 3.0F);
        p0.addOrReplaceChild("leg_l0", b, PartPose.offsetAndRotation(4.2F, -3.0F, -3.0F, 0.0F, 0.0F, -0.15F));
        b = CubeListBuilder.create();
        b.texOffs(44, 155).addBox(-1.0F, -2.4F, -2.0F, 2.0F, 1.0F, 4.0F);
        b.texOffs(0, 149).addBox(-2.0F, -1.44F, -2.5F, 4.0F, 1.0F, 5.0F);
        b.texOffs(104, 149).addBox(-2.0F, -0.48F, -2.0F, 4.0F, 1.0F, 4.0F);
        b.texOffs(70, 124).addBox(-1.0F, -0.48F, -3.0F, 2.0F, 1.0F, 6.0F);
        b.texOffs(18, 149).addBox(-2.0F, 0.48F, -2.5F, 4.0F, 1.0F, 5.0F);
        b.texOffs(56, 155).addBox(-1.0F, 1.44F, -2.0F, 2.0F, 1.0F, 4.0F);
        b.texOffs(16, 155).addBox(-1.0F, 1.8F, -3.5F, 3.0F, 1.0F, 4.0F);
        p0.addOrReplaceChild("leg_l1", b, PartPose.offsetAndRotation(5.2F, -2.5F, 4.0F, 0.0F, 0.0F, 0.0F));
        b = CubeListBuilder.create();
        b.texOffs(100, 155).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F);
        b.texOffs(116, 168).addBox(-1.9F, 2.4F, -2.0F, 3.0F, 1.0F, 3.0F);
        p0.addOrReplaceChild("leg_r0", b, PartPose.offsetAndRotation(-4.2F, -3.0F, -3.0F, 0.0F, 0.0F, 0.15F));
        b = CubeListBuilder.create();
        b.texOffs(68, 155).addBox(-1.0F, -2.4F, -2.0F, 2.0F, 1.0F, 4.0F);
        b.texOffs(36, 149).addBox(-2.0F, -1.44F, -2.5F, 4.0F, 1.0F, 5.0F);
        b.texOffs(0, 155).addBox(-2.0F, -0.48F, -2.0F, 4.0F, 1.0F, 4.0F);
        b.texOffs(86, 124).addBox(-1.0F, -0.48F, -3.0F, 2.0F, 1.0F, 6.0F);
        b.texOffs(54, 149).addBox(-2.0F, 0.48F, -2.5F, 4.0F, 1.0F, 5.0F);
        b.texOffs(80, 155).addBox(-1.0F, 1.44F, -2.0F, 2.0F, 1.0F, 4.0F);
        b.texOffs(30, 155).addBox(-2.0F, 1.8F, -3.5F, 3.0F, 1.0F, 4.0F);
        p0.addOrReplaceChild("leg_r1", b, PartPose.offsetAndRotation(-5.2F, -2.5F, 4.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 256);
    }
}
