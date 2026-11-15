package com.menagerie.client.model.geom;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Geometry of Mo' Creatures' {@code MoCModelEgg} (DrZhark, GPL-3.0),
 * converted with tools/convert_model.py. Do not edit by hand: re-run the converter.
 */
public final class EggGeometry {
    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 32;

    public final ModelPart egg1;
    public final ModelPart egg2;
    public final ModelPart egg3;
    public final ModelPart egg4;
    public final ModelPart egg5;

    public EggGeometry(ModelPart root) {
        this.egg1 = root.getChild("egg1");
        this.egg2 = root.getChild("egg2");
        this.egg3 = root.getChild("egg3");
        this.egg4 = root.getChild("egg4");
        this.egg5 = root.getChild("egg5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("egg1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(0.0F, 20.0F, 0.0F));
        root.addOrReplaceChild("egg2", CubeListBuilder.create().texOffs(10, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F), PartPose.offset(0.5F, 19.5F, 0.5F));
        root.addOrReplaceChild("egg3", CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F), PartPose.offset(0.5F, 22.5F, 0.5F));
        root.addOrReplaceChild("egg4", CubeListBuilder.create().texOffs(24, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 2.0F), PartPose.offset(-0.5F, 20.5F, 0.5F));
        root.addOrReplaceChild("egg5", CubeListBuilder.create().texOffs(18, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 2.0F), PartPose.offset(2.5F, 20.5F, 0.5F));
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
