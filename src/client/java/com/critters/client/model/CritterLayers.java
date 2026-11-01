// GENERATED FILE - DO NOT EDIT BY HAND.
// Produced by tools/generate_assets.py from tools/critter_models.py.
package com.critters.client.model;

import com.critters.client.model.layers.GardenSnailLayer;
import com.critters.client.model.layers.BandedSnailLayer;
import com.critters.client.model.layers.MossSnailLayer;
import com.critters.client.model.layers.AmethystSnailLayer;
import com.critters.client.model.layers.MagmaSnailLayer;
import com.critters.client.model.layers.FiddlerCrabLayer;
import com.critters.client.model.layers.RockCrabLayer;
import com.critters.client.model.layers.FireflyLayer;
import com.critters.client.model.layers.CricketLayer;
import com.critters.client.model.layers.AntLayer;
import com.critters.client.model.layers.LadybugLayer;
import com.critters.client.model.layers.StagBeetleLayer;
import com.critters.client.model.layers.RhinocerosBeetleLayer;
import com.critters.client.model.layers.DungBeetleLayer;
import com.critters.client.model.layers.JewelBeetleLayer;
import com.critters.client.model.layers.ScarabBeetleLayer;
import com.critters.client.model.layers.BombardierBeetleLayer;
import com.critters.client.model.layers.WeevilLayer;
import com.critters.client.model.layers.GoliathBeetleLayer;
import com.critters.client.model.layers.TigerBeetleLayer;
import com.critters.client.model.layers.GoldfishLayer;
import com.critters.client.model.layers.BettaLayer;
import com.critters.client.model.layers.NeonTetraLayer;
import com.critters.client.model.layers.AngelfishLayer;
import java.util.Set;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public final class CritterLayers {
    /** Critters that have an emissive "_glow" texture. */
    public static final Set<String> GLOWING = Set.of("amethyst_snail", "magma_snail", "firefly", "neon_tetra");

    private CritterLayers() {
    }

    public static LayerDefinition create(String id) {
        return switch (id) {
            case "garden_snail" -> GardenSnailLayer.create();
            case "banded_snail" -> BandedSnailLayer.create();
            case "moss_snail" -> MossSnailLayer.create();
            case "amethyst_snail" -> AmethystSnailLayer.create();
            case "magma_snail" -> MagmaSnailLayer.create();
            case "fiddler_crab" -> FiddlerCrabLayer.create();
            case "rock_crab" -> RockCrabLayer.create();
            case "firefly" -> FireflyLayer.create();
            case "cricket" -> CricketLayer.create();
            case "ant" -> AntLayer.create();
            case "ladybug" -> LadybugLayer.create();
            case "stag_beetle" -> StagBeetleLayer.create();
            case "rhinoceros_beetle" -> RhinocerosBeetleLayer.create();
            case "dung_beetle" -> DungBeetleLayer.create();
            case "jewel_beetle" -> JewelBeetleLayer.create();
            case "scarab_beetle" -> ScarabBeetleLayer.create();
            case "bombardier_beetle" -> BombardierBeetleLayer.create();
            case "weevil" -> WeevilLayer.create();
            case "goliath_beetle" -> GoliathBeetleLayer.create();
            case "tiger_beetle" -> TigerBeetleLayer.create();
            case "goldfish" -> GoldfishLayer.create();
            case "betta" -> BettaLayer.create();
            case "neon_tetra" -> NeonTetraLayer.create();
            case "angelfish" -> AngelfishLayer.create();
            default -> throw new IllegalArgumentException("Unknown critter model: " + id);
        };
    }

    /** Scale the renderer applies to the model (models are authored at high resolution). */
    public static float renderScale(String id) {
        return switch (id) {
            case "garden_snail" -> 0.2F;
            case "banded_snail" -> 0.2F;
            case "moss_snail" -> 0.2F;
            case "amethyst_snail" -> 0.2F;
            case "magma_snail" -> 0.2F;
            case "fiddler_crab" -> 0.2F;
            case "rock_crab" -> 0.2F;
            case "firefly" -> 0.2F;
            case "cricket" -> 0.2F;
            case "ant" -> 0.2F;
            case "ladybug" -> 0.2F;
            case "stag_beetle" -> 0.2F;
            case "rhinoceros_beetle" -> 0.2F;
            case "dung_beetle" -> 0.2F;
            case "jewel_beetle" -> 0.2F;
            case "scarab_beetle" -> 0.2F;
            case "bombardier_beetle" -> 0.2F;
            case "weevil" -> 0.2F;
            case "goliath_beetle" -> 0.2F;
            case "tiger_beetle" -> 0.2F;
            case "goldfish" -> 0.2F;
            case "betta" -> 0.2F;
            case "neon_tetra" -> 0.2F;
            case "angelfish" -> 0.2F;
            default -> 1.0F;
        };
    }
}
