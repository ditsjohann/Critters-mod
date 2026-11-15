package com.menagerie.client.render;

import com.menagerie.Menagerie;
import com.menagerie.client.model.MenagerieHorseModel;
import com.menagerie.client.model.ModModelLayers;
import com.menagerie.entity.horse.HorseBreeds;
import com.menagerie.entity.horse.MenagerieHorse;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Renders the horse with the original Mo' Creatures textures. Armor is part of the texture, as in
 * the original: e.g. {@code horsewhitediamond.png}, {@code horsepegasuscrystaline.png}.
 */
public class MenagerieHorseRenderer extends MobRenderer<MenagerieHorse, MenagerieHorseModel> {
    private static final Map<String, ResourceLocation> TEXTURES = new HashMap<>();

    public MenagerieHorseRenderer(EntityRendererProvider.Context context) {
        super(context, new MenagerieHorseModel(context.bakeLayer(ModModelLayers.HORSE)), 0.75F);
    }

    @Override
    public ResourceLocation getTextureLocation(MenagerieHorse horse) {
        String suffix = switch (horse.getArmorType()) {
            case 1 -> "metal";
            case 2 -> "gold";
            case 3 -> "diamond";
            case 4 -> "crystaline";
            default -> "";
        };
        String name = HorseBreeds.texture(horse.getBreed()) + suffix;
        return TEXTURES.computeIfAbsent(name, n -> Menagerie.id("textures/entity/" + n + ".png"));
    }
}
