// GENERATED FILE - DO NOT EDIT BY HAND.
// Produced by tools/generate_assets.py from tools/critter_models.py.
package com.critters.client.model;

import java.util.Set;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@SuppressWarnings("unused")
public final class CritterLayers {
    /** Critters that have an emissive "_glow" texture. */
    public static final Set<String> GLOWING = Set.of("amethyst_snail", "magma_snail", "firefly", "neon_tetra");

    private CritterLayers() {
    }

    public static LayerDefinition create(String id) {
        return switch (id) {
            case "garden_snail" -> gardenSnail();
            case "banded_snail" -> bandedSnail();
            case "moss_snail" -> mossSnail();
            case "amethyst_snail" -> amethystSnail();
            case "magma_snail" -> magmaSnail();
            case "fiddler_crab" -> fiddlerCrab();
            case "rock_crab" -> rockCrab();
            case "firefly" -> firefly();
            case "cricket" -> cricket();
            case "ant" -> ant();
            case "ladybug" -> ladybug();
            case "stag_beetle" -> stagBeetle();
            case "rhinoceros_beetle" -> rhinocerosBeetle();
            case "dung_beetle" -> dungBeetle();
            case "jewel_beetle" -> jewelBeetle();
            case "scarab_beetle" -> scarabBeetle();
            case "bombardier_beetle" -> bombardierBeetle();
            case "weevil" -> weevil();
            case "goliath_beetle" -> goliathBeetle();
            case "tiger_beetle" -> tigerBeetle();
            case "goldfish" -> goldfish();
            case "betta" -> betta();
            case "neon_tetra" -> neonTetra();
            case "angelfish" -> angelfish();
            default -> throw new IllegalArgumentException("Unknown critter model: " + id);
        };
    }

    /** Scale the renderer applies to the model (models are authored larger than life for detail). */
    public static float renderScale(String id) {
        return switch (id) {
            case "garden_snail" -> 0.55F;
            case "banded_snail" -> 0.55F;
            case "moss_snail" -> 0.55F;
            case "amethyst_snail" -> 0.55F;
            case "magma_snail" -> 0.55F;
            case "fiddler_crab" -> 0.5F;
            case "rock_crab" -> 0.5F;
            case "firefly" -> 0.45F;
            case "cricket" -> 0.42F;
            case "ant" -> 0.38F;
            case "ladybug" -> 0.36F;
            case "stag_beetle" -> 0.45F;
            case "rhinoceros_beetle" -> 0.5F;
            case "dung_beetle" -> 0.4F;
            case "jewel_beetle" -> 0.38F;
            case "scarab_beetle" -> 0.42F;
            case "bombardier_beetle" -> 0.36F;
            case "weevil" -> 0.36F;
            case "goliath_beetle" -> 0.55F;
            case "tiger_beetle" -> 0.36F;
            case "goldfish" -> 0.8F;
            case "betta" -> 0.8F;
            case "neon_tetra" -> 0.65F;
            case "angelfish" -> 0.75F;
            default -> 1.0F;
        };
    }

    private static LayerDefinition gardenSnail() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(22, 0).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-1.0F, -1.0F, 5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(46, 0).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("eyestalk_l", CubeListBuilder.create()
                .texOffs(56, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 12).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(1.0F, -4.0F, -1.2F, -0.35F, 0.0F, 0.3F));
        PartDefinition p3 = p1.addOrReplaceChild("tentacle_l", CubeListBuilder.create()
                .texOffs(12, 12).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -0.8F, -2.0F, 0.3F, 0.3F, 0.0F));
        PartDefinition p4 = p1.addOrReplaceChild("eyestalk_r", CubeListBuilder.create()
                .texOffs(60, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 12).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(-1.0F, -4.0F, -1.2F, -0.35F, 0.0F, -0.3F));
        PartDefinition p5 = p1.addOrReplaceChild("tentacle_r", CubeListBuilder.create()
                .texOffs(20, 12).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -0.8F, -2.0F, 0.3F, -0.3F, 0.0F));
        PartDefinition p6 = root.addOrReplaceChild("shell", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5F, -6.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 1.5F, -0.12F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 16);
    }

    private static LayerDefinition bandedSnail() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(22, 0).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-1.0F, -1.0F, 5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(46, 0).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("eyestalk_l", CubeListBuilder.create()
                .texOffs(56, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 12).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(1.0F, -4.0F, -1.2F, -0.35F, 0.0F, 0.3F));
        PartDefinition p3 = p1.addOrReplaceChild("tentacle_l", CubeListBuilder.create()
                .texOffs(12, 12).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -0.8F, -2.0F, 0.3F, 0.3F, 0.0F));
        PartDefinition p4 = p1.addOrReplaceChild("eyestalk_r", CubeListBuilder.create()
                .texOffs(60, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 12).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(-1.0F, -4.0F, -1.2F, -0.35F, 0.0F, -0.3F));
        PartDefinition p5 = p1.addOrReplaceChild("tentacle_r", CubeListBuilder.create()
                .texOffs(20, 12).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -0.8F, -2.0F, 0.3F, -0.3F, 0.0F));
        PartDefinition p6 = root.addOrReplaceChild("shell", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5F, -6.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 1.5F, -0.12F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 16);
    }

    private static LayerDefinition mossSnail() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(22, 0).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(16, 12).addBox(-1.0F, -1.0F, 5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(46, 0).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("eyestalk_l", CubeListBuilder.create()
                .texOffs(56, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 12).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(1.0F, -4.0F, -1.2F, -0.35F, 0.0F, 0.3F));
        PartDefinition p3 = p1.addOrReplaceChild("tentacle_l", CubeListBuilder.create()
                .texOffs(36, 12).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -0.8F, -2.0F, 0.3F, 0.3F, 0.0F));
        PartDefinition p4 = p1.addOrReplaceChild("eyestalk_r", CubeListBuilder.create()
                .texOffs(60, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 12).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(-1.0F, -4.0F, -1.2F, -0.35F, 0.0F, -0.3F));
        PartDefinition p5 = p1.addOrReplaceChild("tentacle_r", CubeListBuilder.create()
                .texOffs(44, 12).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -0.8F, -2.0F, 0.3F, -0.3F, 0.0F));
        PartDefinition p6 = root.addOrReplaceChild("shell", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5F, -6.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 1.5F, -0.12F, 0.0F, 0.0F));
        PartDefinition p7 = p6.addOrReplaceChild("moss_a", CubeListBuilder.create()
                .texOffs(0, 12).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, 0.0F, 0.3F, 0.0F));
        PartDefinition p8 = p6.addOrReplaceChild("moss_b", CubeListBuilder.create()
                .texOffs(24, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -5.0F, 2.0F, 0.0F, -0.4F, 0.2F));
        PartDefinition p9 = p6.addOrReplaceChild("sprout", CubeListBuilder.create()
                .texOffs(12, 12).addBox(0.0F, -2.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -7.0F, -1.0F, 0.0F, 0.7F, 0.0F));
        return LayerDefinition.create(mesh, 64, 16);
    }

    private static LayerDefinition amethystSnail() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(22, 0).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(4, 12).addBox(-1.0F, -1.0F, 5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(46, 0).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("eyestalk_l", CubeListBuilder.create()
                .texOffs(56, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 12).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(1.0F, -4.0F, -1.2F, -0.35F, 0.0F, 0.3F));
        PartDefinition p3 = p1.addOrReplaceChild("tentacle_l", CubeListBuilder.create()
                .texOffs(24, 12).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -0.8F, -2.0F, 0.3F, 0.3F, 0.0F));
        PartDefinition p4 = p1.addOrReplaceChild("eyestalk_r", CubeListBuilder.create()
                .texOffs(60, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 12).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(-1.0F, -4.0F, -1.2F, -0.35F, 0.0F, -0.3F));
        PartDefinition p5 = p1.addOrReplaceChild("tentacle_r", CubeListBuilder.create()
                .texOffs(32, 12).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -0.8F, -2.0F, 0.3F, -0.3F, 0.0F));
        PartDefinition p6 = root.addOrReplaceChild("shell", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5F, -6.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 1.5F, -0.12F, 0.0F, 0.0F));
        PartDefinition p7 = p6.addOrReplaceChild("crystal_a", CubeListBuilder.create()
                .texOffs(0, 12).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, 0.2F, 0.0F, 0.25F));
        PartDefinition p8 = p6.addOrReplaceChild("crystal_b", CubeListBuilder.create()
                .texOffs(12, 12).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, -5.0F, 1.5F, -0.3F, 0.0F, -0.35F));
        PartDefinition p9 = p6.addOrReplaceChild("crystal_c", CubeListBuilder.create()
                .texOffs(16, 12).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, -5.0F, 1.0F, -0.1F, 0.0F, 0.5F));
        return LayerDefinition.create(mesh, 64, 16);
    }

    private static LayerDefinition magmaSnail() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(22, 0).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-1.0F, -1.0F, 5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(46, 0).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("eyestalk_l", CubeListBuilder.create()
                .texOffs(56, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 12).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(1.0F, -4.0F, -1.2F, -0.35F, 0.0F, 0.3F));
        PartDefinition p3 = p1.addOrReplaceChild("tentacle_l", CubeListBuilder.create()
                .texOffs(12, 12).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -0.8F, -2.0F, 0.3F, 0.3F, 0.0F));
        PartDefinition p4 = p1.addOrReplaceChild("eyestalk_r", CubeListBuilder.create()
                .texOffs(60, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 12).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(-1.0F, -4.0F, -1.2F, -0.35F, 0.0F, -0.3F));
        PartDefinition p5 = p1.addOrReplaceChild("tentacle_r", CubeListBuilder.create()
                .texOffs(20, 12).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -0.8F, -2.0F, 0.3F, -0.3F, 0.0F));
        PartDefinition p6 = root.addOrReplaceChild("shell", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5F, -6.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 1.5F, -0.12F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 16);
    }

    private static LayerDefinition fiddlerCrab() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-5.0F, -3.0F, -3.5F, 10.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-4.0F, -4.0F, -2.5F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("eyestalk_l", CubeListBuilder.create()
                .texOffs(8, 16).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 20).addBox(-0.5F, -3.6F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F)),
                PartPose.offsetAndRotation(2.0F, -3.5F, -3.0F, -0.2F, 0.0F, 0.15F));
        PartDefinition p2 = p0.addOrReplaceChild("eyestalk_r", CubeListBuilder.create()
                .texOffs(12, 16).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 20).addBox(-0.5F, -3.6F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F)),
                PartPose.offsetAndRotation(-2.0F, -3.5F, -3.0F, -0.2F, 0.0F, -0.15F));
        PartDefinition p3 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(16, 16).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.5F, 21.0F, -1.8F, 0.0F, 0.45F, 0.5887F));
        PartDefinition p4 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(28, 16).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.5F, 21.0F, -1.8F, 0.0F, -0.45F, -0.5887F));
        PartDefinition p5 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(40, 16).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.5F, 21.0F, 0.5F, 0.0F, 0.05F, 0.5243F));
        PartDefinition p6 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(52, 16).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.5F, 21.0F, 0.5F, 0.0F, -0.05F, -0.5243F));
        PartDefinition p7 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(0, 20).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.5F, 21.0F, 2.6F, 0.0F, -0.45F, 0.5887F));
        PartDefinition p8 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(12, 20).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.5F, 21.0F, 2.6F, 0.0F, 0.45F, -0.5887F));
        PartDefinition p9 = root.addOrReplaceChild("claw_l", CubeListBuilder.create()
                .texOffs(40, 10).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.5F, 20.5F, -3.5F, 0.15F, 0.55F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("pincer_l", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = root.addOrReplaceChild("claw_r", CubeListBuilder.create()
                .texOffs(50, 10).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.5F, 20.5F, -3.5F, 0.15F, -0.55F, 0.0F));
        PartDefinition p12 = p11.addOrReplaceChild("pincer_r", CubeListBuilder.create()
                .texOffs(34, 0).addBox(-2.5F, -2.5F, -6.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(26, 10).addBox(-1.5F, 1.5F, -5.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.3F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition rockCrab() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-5.0F, -3.0F, -3.5F, 10.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-4.0F, -4.0F, -2.5F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("eyestalk_l", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 20).addBox(-0.5F, -3.6F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F)),
                PartPose.offsetAndRotation(2.0F, -3.5F, -3.0F, -0.2F, 0.0F, 0.15F));
        PartDefinition p2 = p0.addOrReplaceChild("eyestalk_r", CubeListBuilder.create()
                .texOffs(4, 16).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 20).addBox(-0.5F, -3.6F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F)),
                PartPose.offsetAndRotation(-2.0F, -3.5F, -3.0F, -0.2F, 0.0F, -0.15F));
        PartDefinition p3 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(8, 16).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.5F, 21.0F, -1.8F, 0.0F, 0.45F, 0.5887F));
        PartDefinition p4 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(20, 16).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.5F, 21.0F, -1.8F, 0.0F, -0.45F, -0.5887F));
        PartDefinition p5 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(32, 16).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.5F, 21.0F, 0.5F, 0.0F, 0.05F, 0.5243F));
        PartDefinition p6 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(44, 16).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.5F, 21.0F, 0.5F, 0.0F, -0.05F, -0.5243F));
        PartDefinition p7 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(0, 20).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.5F, 21.0F, 2.6F, 0.0F, -0.45F, 0.5887F));
        PartDefinition p8 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(12, 20).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.5F, 21.0F, 2.6F, 0.0F, 0.45F, -0.5887F));
        PartDefinition p9 = root.addOrReplaceChild("claw_l", CubeListBuilder.create()
                .texOffs(26, 10).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.5F, 20.5F, -3.5F, 0.15F, 0.55F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("pincer_l", CubeListBuilder.create()
                .texOffs(34, 0).addBox(-1.5F, -1.5F, -4.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(46, 10).addBox(-0.5F, 1.5F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.2F, 0.0F));
        PartDefinition p11 = root.addOrReplaceChild("claw_r", CubeListBuilder.create()
                .texOffs(36, 10).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.5F, 20.5F, -3.5F, 0.15F, -0.55F, 0.0F));
        PartDefinition p12 = p11.addOrReplaceChild("pincer_r", CubeListBuilder.create()
                .texOffs(48, 0).addBox(-1.5F, -1.5F, -4.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(54, 10).addBox(-0.5F, 1.5F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, -0.2F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition firefly() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(44, 0).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-1.5F, -1.5F, 2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(34, 7).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 7).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.5F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(14, 7).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.6F, -0.2F, -3.0F, -0.6F, 0.5F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(24, 7).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.6F, -0.2F, -3.0F, -0.6F, -0.5F, 0.0F));
        PartDefinition p4 = p0.addOrReplaceChild("wing_l", CubeListBuilder.create()
                .texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 4.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.2F, -1.6F, -1.5F, 0.0F, -0.15F, 0.1F));
        PartDefinition p5 = p0.addOrReplaceChild("wing_r", CubeListBuilder.create()
                .texOffs(22, 0).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.2F, -1.6F, -1.5F, 0.0F, 0.15F, -0.1F));
        PartDefinition p6 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(50, 7).addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, 21.0F, -1.2F, 0.0F, 0.55F, 1.3582F));
        PartDefinition p7 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(0, 14).addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 21.0F, -1.2F, 0.0F, -0.55F, -1.3582F));
        PartDefinition p8 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(8, 14).addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, 21.0F, 0.0F, 0.0F, -0.1F, 0.9927F));
        PartDefinition p9 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(16, 14).addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 21.0F, 0.0F, 0.0F, 0.1F, -0.9927F));
        PartDefinition p10 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(24, 14).addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, 21.0F, 1.2F, 0.0F, -0.6F, 1.3705F));
        PartDefinition p11 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(32, 14).addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 21.0F, 1.2F, 0.0F, 0.6F, -1.3705F));
        return LayerDefinition.create(mesh, 64, 16);
    }

    private static LayerDefinition cricket() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(40, 11).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(30, 19).addBox(1.6F, -1.6F, -2.6F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(34, 19).addBox(-2.6F, -1.6F, -2.6F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -1.5F, -3.0F, 0.2F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(24, 0).addBox(-0.5F, -0.5F, -9.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -1.5F, -3.0F, -0.35F, 0.3F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(44, 0).addBox(-0.5F, -0.5F, -9.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -1.5F, -3.0F, -0.35F, -0.3F, 0.0F));
        PartDefinition p4 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(54, 11).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.8F, 21.0F, -2.2F, 0.0F, 0.6F, 0.8591F));
        PartDefinition p5 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(0, 19).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.8F, 21.0F, -2.2F, 0.0F, -0.6F, -0.8591F));
        PartDefinition p6 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(10, 19).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.8F, 21.0F, -0.2F, 0.0F, -0.1F, 0.6792F));
        PartDefinition p7 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(20, 19).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.8F, 21.0F, -0.2F, 0.0F, 0.1F, -0.6792F));
        PartDefinition p8 = root.addOrReplaceChild("hindleg_l", CubeListBuilder.create()
                .texOffs(0, 11).addBox(-0.5F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 20.5F, 1.0F, 0.45F, -0.15F, 0.0F));
        PartDefinition p9 = p8.addOrReplaceChild("shin_l", CubeListBuilder.create()
                .texOffs(32, 11).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.5F, 0.0F, 6.0F, -0.25F, 0.0F, 0.0F));
        PartDefinition p10 = root.addOrReplaceChild("hindleg_r", CubeListBuilder.create()
                .texOffs(16, 11).addBox(-1.5F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, 20.5F, 1.0F, 0.45F, 0.15F, 0.0F));
        PartDefinition p11 = p10.addOrReplaceChild("shin_r", CubeListBuilder.create()
                .texOffs(36, 11).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.5F, 0.0F, 6.0F, -0.25F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition ant() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(18, 0).addBox(-1.0F, -1.5F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(30, 0).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(24, 14).addBox(-1.5F, 0.5F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 14).addBox(0.5F, 0.5F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -2.0F, 0.15F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(42, 0).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.8F, -1.2F, -2.5F, -0.7F, 0.5F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(0, 8).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.8F, -1.2F, -2.5F, -0.7F, -0.5F, 0.0F));
        PartDefinition p4 = p0.addOrReplaceChild("abdomen", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.5F, 2.0F, -0.3F, 0.0F, 0.0F));
        PartDefinition p5 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(12, 8).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.8F, 21.0F, -1.4F, 0.0F, 0.65F, 0.6791F));
        PartDefinition p6 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(24, 8).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.8F, 21.0F, -1.4F, 0.0F, -0.65F, -0.6791F));
        PartDefinition p7 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(36, 8).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.8F, 21.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
        PartDefinition p8 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(48, 8).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.8F, 21.0F, 0.0F, 0.0F, 0.0F, -0.5236F));
        PartDefinition p9 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(0, 14).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.8F, 21.0F, 1.4F, 0.0F, -0.65F, 0.6791F));
        PartDefinition p10 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(12, 14).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.8F, 21.0F, 1.4F, 0.0F, 0.65F, -0.6791F));
        return LayerDefinition.create(mesh, 64, 16);
    }

    private static LayerDefinition ladybug() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -4.0F, -2.5F, 8.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(30, 0).addBox(-3.0F, -3.0F, -4.5F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(46, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(10, 14).addBox(1.4F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(14, 14).addBox(-2.4F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -4.5F, 0.1F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(58, 0).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, -0.5F, -1.5F, -0.45F, 0.45F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(0, 11).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, -0.5F, -1.5F, -0.45F, -0.45F, 0.0F));
        PartDefinition p4 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(6, 11).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, 21.0F, -3.5F, 0.0F, 0.55F, 0.8229F));
        PartDefinition p5 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(16, 11).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.5F, 21.0F, -3.5F, 0.0F, -0.55F, -0.8229F));
        PartDefinition p6 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(26, 11).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, 21.0F, -1.5F, 0.0F, -0.1F, 0.6792F));
        PartDefinition p7 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(36, 11).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.5F, 21.0F, -1.5F, 0.0F, 0.1F, -0.6792F));
        PartDefinition p8 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(46, 11).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, 21.0F, 0.65F, 0.0F, -0.6F, 0.8591F));
        PartDefinition p9 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(0, 14).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.5F, 21.0F, 0.65F, 0.0F, 0.6F, -0.8591F));
        return LayerDefinition.create(mesh, 64, 16);
    }

    private static LayerDefinition stagBeetle() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -3.0F, -3.0F, 8.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-3.5F, -3.0F, -6.0F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(20, 12).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(28, 20).addBox(2.4F, -0.8F, -2.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(32, 20).addBox(-3.4F, -0.8F, -2.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -6.0F, 0.1F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(38, 12).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, -0.5F, -2.5F, -0.45F, 0.45F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(46, 12).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.5F, -0.5F, -2.5F, -0.45F, -0.45F, 0.0F));
        PartDefinition p4 = p1.addOrReplaceChild("mandible_l", CubeListBuilder.create()
                .texOffs(34, 0).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(36, 20).addBox(-1.5F, -0.5F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, 0.0F, -3.0F, 0.0F, 0.35F, 0.0F));
        PartDefinition p5 = p1.addOrReplaceChild("mandible_r", CubeListBuilder.create()
                .texOffs(48, 0).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(40, 20).addBox(0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.5F, 0.0F, -3.0F, 0.0F, -0.35F, 0.0F));
        PartDefinition p6 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(0, 18).addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.0F, 21.0F, -4.5F, 0.0F, 0.55F, 0.5107F));
        PartDefinition p7 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(14, 18).addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.0F, 21.0F, -4.5F, 0.0F, -0.55F, -0.5107F));
        PartDefinition p8 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(28, 18).addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.0F, 21.0F, -2.0F, 0.0F, -0.1F, 0.4321F));
        PartDefinition p9 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(42, 18).addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.0F, 21.0F, -2.0F, 0.0F, 0.1F, -0.4321F));
        PartDefinition p10 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(0, 20).addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.0F, 21.0F, 1.0F, 0.0F, -0.6F, 0.5292F));
        PartDefinition p11 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(14, 20).addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.0F, 21.0F, 1.0F, 0.0F, 0.6F, -0.5292F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition rhinocerosBeetle() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.5F, -4.0F, -2.5F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(36, 0).addBox(-4.0F, -4.0F, -6.5F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 13).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(1.4F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(4, 19).addBox(-2.4F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -1.5F, -6.5F, 0.1F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(12, 13).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, -0.5F, -1.5F, -0.45F, 0.45F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(18, 13).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, -0.5F, -1.5F, -0.45F, -0.45F, 0.0F));
        PartDefinition p4 = p1.addOrReplaceChild("horn", CubeListBuilder.create()
                .texOffs(60, 0).addBox(-0.5F, -6.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 17).addBox(-1.0F, -7.0F, -1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -1.5F, -0.6F, 0.0F, 0.0F));
        PartDefinition p5 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(24, 13).addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.5F, 21.0F, -4.5F, 0.0F, 0.55F, 0.5107F));
        PartDefinition p6 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(38, 13).addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.5F, 21.0F, -4.5F, 0.0F, -0.55F, -0.5107F));
        PartDefinition p7 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(0, 17).addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.5F, 21.0F, -1.5F, 0.0F, -0.1F, 0.4321F));
        PartDefinition p8 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(14, 17).addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.5F, 21.0F, -1.5F, 0.0F, 0.1F, -0.4321F));
        PartDefinition p9 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(28, 17).addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.5F, 21.0F, 1.5F, 0.0F, -0.6F, 0.5292F));
        PartDefinition p10 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(42, 17).addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.5F, 21.0F, 1.5F, 0.0F, 0.6F, -0.5292F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition dungBeetle() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -3.0F, -2.0F, 8.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(30, 0).addBox(-4.0F, -3.0F, -5.0F, 8.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 10).addBox(-3.0F, -0.5F, -2.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(60, 13).addBox(2.4F, -0.3F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(0, 15).addBox(-3.4F, -0.3F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -5.0F, 0.1F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(16, 10).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, 0.0F, -1.5F, -0.45F, 0.45F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(22, 10).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.5F, 0.0F, -1.5F, -0.45F, -0.45F, 0.0F));
        PartDefinition p4 = p1.addOrReplaceChild("shovel", CubeListBuilder.create()
                .texOffs(28, 10).addBox(-3.0F, -0.5F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.5F, -2.0F, 0.2F, 0.0F, 0.0F));
        PartDefinition p5 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(42, 10).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.5F, 21.0F, -3.5F, 0.0F, 0.55F, 0.6267F));
        PartDefinition p6 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(0, 13).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.5F, 21.0F, -3.5F, 0.0F, -0.55F, -0.6267F));
        PartDefinition p7 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(12, 13).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.5F, 21.0F, -1.0F, 0.0F, -0.1F, 0.5265F));
        PartDefinition p8 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(24, 13).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.5F, 21.0F, -1.0F, 0.0F, 0.1F, -0.5265F));
        PartDefinition p9 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(36, 13).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.5F, 21.0F, 1.15F, 0.0F, -0.6F, 0.6508F));
        PartDefinition p10 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(48, 13).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.5F, 21.0F, 1.15F, 0.0F, 0.6F, -0.6508F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition jewelBeetle() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(30, 0).addBox(-3.0F, -2.0F, -6.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(48, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 16).addBox(1.4F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(24, 16).addBox(-2.4F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -0.5F, -6.0F, 0.1F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(0, 12).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, -0.5F, -1.5F, -0.45F, 0.45F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(8, 12).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, -0.5F, -1.5F, -0.45F, -0.45F, 0.0F));
        PartDefinition p4 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(16, 12).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, 21.0F, -4.5F, 0.0F, 0.55F, 0.8229F));
        PartDefinition p5 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(26, 12).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.5F, 21.0F, -4.5F, 0.0F, -0.55F, -0.8229F));
        PartDefinition p6 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(36, 12).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, 21.0F, -2.0F, 0.0F, -0.1F, 0.6792F));
        PartDefinition p7 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(46, 12).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.5F, 21.0F, -2.0F, 0.0F, 0.1F, -0.6792F));
        PartDefinition p8 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(0, 16).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, 21.0F, 1.0F, 0.0F, -0.6F, 0.8591F));
        PartDefinition p9 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(10, 16).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.5F, 21.0F, 1.0F, 0.0F, 0.6F, -0.8591F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition scarabBeetle() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -4.0F, -2.5F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-3.5F, -3.0F, -5.5F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 12).addBox(-2.5F, -1.0F, -2.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 16).addBox(1.9F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(48, 16).addBox(-2.9F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -5.5F, 0.1F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(14, 12).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, -0.5F, -1.5F, -0.45F, 0.45F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(20, 12).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, -0.5F, -1.5F, -0.45F, -0.45F, 0.0F));
        PartDefinition p4 = p1.addOrReplaceChild("clypeus", CubeListBuilder.create()
                .texOffs(36, 16).addBox(-1.5F, -0.5F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p5 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(26, 12).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.0F, 21.0F, -4.0F, 0.0F, 0.55F, 0.6267F));
        PartDefinition p6 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(38, 12).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.0F, 21.0F, -4.0F, 0.0F, -0.55F, -0.6267F));
        PartDefinition p7 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(50, 12).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.0F, 21.0F, -1.5F, 0.0F, -0.1F, 0.5265F));
        PartDefinition p8 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.0F, 21.0F, -1.5F, 0.0F, 0.1F, -0.5265F));
        PartDefinition p9 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(12, 16).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.0F, 21.0F, 1.1F, 0.0F, -0.6F, 0.6508F));
        PartDefinition p10 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(24, 16).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.0F, 21.0F, 1.1F, 0.0F, 0.6F, -0.6508F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition bombardierBeetle() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(26, 0).addBox(-2.0F, -2.0F, -5.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(40, 0).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(24, 15).addBox(1.4F, -0.8F, -2.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(28, 15).addBox(-2.4F, -0.8F, -2.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -0.5F, -5.0F, 0.1F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(54, 0).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, -0.5F, -2.5F, -0.45F, 0.45F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(0, 10).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, -0.5F, -2.5F, -0.45F, -0.45F, 0.0F));
        PartDefinition p4 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(10, 10).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, 21.0F, -3.5F, 0.0F, 0.55F, 0.6267F));
        PartDefinition p5 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(22, 10).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, 21.0F, -3.5F, 0.0F, -0.55F, -0.6267F));
        PartDefinition p6 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(34, 10).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, 21.0F, -1.0F, 0.0F, -0.1F, 0.5265F));
        PartDefinition p7 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(46, 10).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, 21.0F, -1.0F, 0.0F, 0.1F, -0.5265F));
        PartDefinition p8 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(0, 15).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, 21.0F, 1.15F, 0.0F, -0.6F, 0.6508F));
        PartDefinition p9 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(12, 15).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, 21.0F, 1.15F, 0.0F, 0.6F, -0.6508F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition weevil() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.0F, -4.0F, -2.0F, 6.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(26, 0).addBox(-2.0F, -3.0F, -5.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(40, 0).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(10, 15).addBox(0.9F, -1.3F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(14, 15).addBox(-1.9F, -1.3F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -5.0F, 0.1F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(50, 0).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -1.0F, -1.5F, -0.45F, 0.45F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(0, 11).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -1.0F, -1.5F, -0.45F, -0.45F, 0.0F));
        PartDefinition p4 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(8, 11).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, 21.0F, -3.5F, 0.0F, 0.55F, 0.8229F));
        PartDefinition p5 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(18, 11).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, 21.0F, -3.5F, 0.0F, -0.55F, -0.8229F));
        PartDefinition p6 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(28, 11).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, 21.0F, -1.0F, 0.0F, -0.1F, 0.6792F));
        PartDefinition p7 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(38, 11).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, 21.0F, -1.0F, 0.0F, 0.1F, -0.6792F));
        PartDefinition p8 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(48, 11).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, 21.0F, 1.15F, 0.0F, -0.6F, 0.8591F));
        PartDefinition p9 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(0, 15).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, 21.0F, 1.15F, 0.0F, 0.6F, -0.8591F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition goliathBeetle() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-5.0F, -4.0F, -3.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-4.5F, -3.0F, -7.0F, 9.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(26, 14).addBox(-2.5F, -1.0F, -2.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 23).addBox(1.9F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(36, 23).addBox(-2.9F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -7.0F, 0.1F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(40, 14).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, -0.5F, -1.5F, -0.45F, 0.45F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(46, 14).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, -0.5F, -1.5F, -0.45F, -0.45F, 0.0F));
        PartDefinition p4 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(0, 21).addBox(0.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 21.0F, -5.0F, 0.0F, 0.55F, 0.4323F));
        PartDefinition p5 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(16, 21).addBox(-7.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 21.0F, -5.0F, 0.0F, -0.55F, -0.4323F));
        PartDefinition p6 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(32, 21).addBox(0.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 21.0F, -2.0F, 0.0F, -0.1F, 0.3671F));
        PartDefinition p7 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(48, 21).addBox(-7.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 21.0F, -2.0F, 0.0F, 0.1F, -0.3671F));
        PartDefinition p8 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(0, 23).addBox(0.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 21.0F, 1.0F, 0.0F, -0.6F, 0.4475F));
        PartDefinition p9 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(16, 23).addBox(-7.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 21.0F, 1.0F, 0.0F, 0.6F, -0.4475F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition tigerBeetle() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.0F, -3.0F, -2.5F, 6.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-2.5F, -2.0F, -4.5F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(46, 0).addBox(-3.0F, -1.0F, -2.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(60, 14).addBox(2.4F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(0, 16).addBox(-3.4F, -0.8F, -1.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -0.5F, -4.5F, 0.1F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("antenna_l", CubeListBuilder.create()
                .texOffs(26, 0).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, -0.5F, -1.5F, -0.45F, 0.45F, 0.0F));
        PartDefinition p3 = p1.addOrReplaceChild("antenna_r", CubeListBuilder.create()
                .texOffs(36, 0).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.5F, -0.5F, -1.5F, -0.45F, -0.45F, 0.0F));
        PartDefinition p4 = p1.addOrReplaceChild("mandible_l", CubeListBuilder.create()
                .texOffs(14, 10).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.2F, 0.3F, -2.0F, 0.0F, 0.4F, 0.0F));
        PartDefinition p5 = p1.addOrReplaceChild("mandible_r", CubeListBuilder.create()
                .texOffs(20, 10).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.2F, 0.3F, -2.0F, 0.0F, -0.4F, 0.0F));
        PartDefinition p6 = root.addOrReplaceChild("leg_l0", CubeListBuilder.create()
                .texOffs(32, 14).addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 21.0F, -3.5F, 0.0F, 0.55F, 0.5107F));
        PartDefinition p7 = root.addOrReplaceChild("leg_r0", CubeListBuilder.create()
                .texOffs(46, 14).addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, 21.0F, -3.5F, 0.0F, -0.55F, -0.5107F));
        PartDefinition p8 = root.addOrReplaceChild("leg_l1", CubeListBuilder.create()
                .texOffs(0, 14).addBox(0.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 21.0F, -1.5F, 0.0F, -0.1F, 0.3671F));
        PartDefinition p9 = root.addOrReplaceChild("leg_r1", CubeListBuilder.create()
                .texOffs(16, 14).addBox(-7.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, 21.0F, -1.5F, 0.0F, 0.1F, -0.3671F));
        PartDefinition p10 = root.addOrReplaceChild("leg_l2", CubeListBuilder.create()
                .texOffs(26, 10).addBox(0.0F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 21.0F, 0.65F, 0.0F, -0.6F, 0.3883F));
        PartDefinition p11 = root.addOrReplaceChild("leg_r2", CubeListBuilder.create()
                .texOffs(44, 10).addBox(-8.0F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, 21.0F, 0.65F, 0.0F, 0.6F, -0.3883F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition goldfish() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.0F, -2.5F, -3.0F, 4.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(40, 0).addBox(-1.5F, -2.0F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 11).addBox(1.2F, -1.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(20, 11).addBox(-2.2F, -1.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p0.addOrReplaceChild("tail_fin", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p3 = p2.addOrReplaceChild("tail_a", CubeListBuilder.create()
                .texOffs(20, 0).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.35F, 0.0F));
        PartDefinition p4 = p2.addOrReplaceChild("tail_b", CubeListBuilder.create()
                .texOffs(30, 0).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.35F, 0.0F));
        PartDefinition p5 = p0.addOrReplaceChild("dorsal_fin", CubeListBuilder.create()
                .texOffs(50, 0).addBox(0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -2.5F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p6 = p0.addOrReplaceChild("fin_l", CubeListBuilder.create()
                .texOffs(0, 11).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 1.5F, -1.5F, 0.0F, 0.0F, 0.7F));
        PartDefinition p7 = p0.addOrReplaceChild("fin_r", CubeListBuilder.create()
                .texOffs(8, 11).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, 1.5F, -1.5F, 0.0F, 0.0F, -0.7F));
        return LayerDefinition.create(mesh, 64, 16);
    }

    private static LayerDefinition betta() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(16, 0).addBox(-1.5F, -2.0F, -3.0F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(56, 0).addBox(-1.0F, -1.5F, -2.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 18).addBox(0.7F, -1.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(20, 18).addBox(-1.7F, -1.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p0.addOrReplaceChild("tail_fin", CubeListBuilder.create()
                .texOffs(0, 0).addBox(0.0F, -5.0F, 0.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p3 = p0.addOrReplaceChild("dorsal_fin", CubeListBuilder.create()
                .texOffs(46, 0).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.3F, 0.0F, 0.0F));
        PartDefinition p4 = p0.addOrReplaceChild("anal_fin", CubeListBuilder.create()
                .texOffs(34, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 2.0F, -1.0F, -0.3F, 0.0F, 0.0F));
        PartDefinition p5 = p0.addOrReplaceChild("fin_l", CubeListBuilder.create()
                .texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, 1.0F, -2.0F, 0.0F, 0.0F, 0.6F));
        PartDefinition p6 = p0.addOrReplaceChild("fin_r", CubeListBuilder.create()
                .texOffs(8, 18).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, 1.0F, -2.0F, 0.0F, 0.0F, -0.6F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static LayerDefinition neonTetra() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(22, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(0.4F, -0.8F, -0.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(36, 0).addBox(-1.4F, -0.8F, -0.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p0.addOrReplaceChild("tail_fin", CubeListBuilder.create()
                .texOffs(16, 0).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p3 = p0.addOrReplaceChild("dorsal_fin", CubeListBuilder.create()
                .texOffs(28, 0).addBox(0.0F, -1.0F, 0.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p4 = p0.addOrReplaceChild("fin_l", CubeListBuilder.create()
                .texOffs(40, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, 1.0F, -1.5F, 0.0F, 0.0F, 0.6F));
        PartDefinition p5 = p0.addOrReplaceChild("fin_r", CubeListBuilder.create()
                .texOffs(44, 0).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 1.0F, -1.5F, 0.0F, 0.0F, -0.6F));
        return LayerDefinition.create(mesh, 64, 16);
    }

    private static LayerDefinition angelfish() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-1.0F, -3.5F, -3.0F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(48, 0).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(0.4F, -1.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(60, 0).addBox(-1.4F, -1.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p0.addOrReplaceChild("tail_fin", CubeListBuilder.create()
                .texOffs(36, 0).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p3 = p0.addOrReplaceChild("dorsal_fin", CubeListBuilder.create()
                .texOffs(16, 0).addBox(0.0F, -6.0F, 0.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -3.5F, -1.0F, 0.55F, 0.0F, 0.0F));
        PartDefinition p4 = p0.addOrReplaceChild("anal_fin", CubeListBuilder.create()
                .texOffs(26, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 3.5F, -1.0F, -0.55F, 0.0F, 0.0F));
        PartDefinition p5 = p0.addOrReplaceChild("fin_l", CubeListBuilder.create()
                .texOffs(44, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, 2.5F, -2.0F, 0.4F, 0.0F, 0.3F));
        PartDefinition p6 = p0.addOrReplaceChild("fin_r", CubeListBuilder.create()
                .texOffs(46, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 2.5F, -2.0F, 0.4F, 0.0F, -0.3F));
        return LayerDefinition.create(mesh, 64, 16);
    }
}
