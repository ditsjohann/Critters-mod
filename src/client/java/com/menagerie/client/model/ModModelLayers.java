package com.menagerie.client.model;

import com.menagerie.Menagerie;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class ModModelLayers {
    public static final ModelLayerLocation HORSE = new ModelLayerLocation(Menagerie.id("horse"), "main");
    public static final ModelLayerLocation BIG_CAT = new ModelLayerLocation(Menagerie.id("big_cat"), "main");
    public static final ModelLayerLocation WYVERN = new ModelLayerLocation(Menagerie.id("wyvern"), "main");
    public static final ModelLayerLocation EGG = new ModelLayerLocation(Menagerie.id("egg"), "main");

    private ModModelLayers() {
    }
}
