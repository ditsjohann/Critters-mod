package com.menagerie.event;

import com.menagerie.registry.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;

/**
 * Hearts drop from monsters slain by a player; they are brewed into the essences that transform horses.
 *
 * <p>In the original, hearts came from Mo' Creatures' own monsters (hostile horses, ogres, wraiths).
 * Until those are ported, vanilla monsters drop them: undead give hearts of undead, fiery mobs hearts
 * of fire, and creatures of the dark hearts of darkness.
 */
public final class HeartDrops {
    private HeartDrops() {
    }

    public static void init() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (!(source.getEntity() instanceof Player) || entity.isBaby()
                    || !entity.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                return;
            }
            Item heart = heartFor(entity);
            int chance = chanceFor(entity);
            if (heart != null && entity.getRandom().nextInt(100) < chance) {
                entity.spawnAtLocation(heart);
            }
        });
    }

    private static Item heartFor(LivingEntity entity) {
        EntityType<?> type = entity.getType();
        if (type == EntityType.BLAZE || type == EntityType.MAGMA_CUBE || type == EntityType.GHAST) {
            return ModItems.HEART_OF_FIRE;
        }
        if (type == EntityType.ENDERMAN || type == EntityType.PHANTOM || type == EntityType.WITHER_SKELETON) {
            return ModItems.HEART_OF_DARKNESS;
        }
        if (type.is(EntityTypeTags.UNDEAD)) {
            return ModItems.HEART_OF_UNDEAD;
        }
        return null;
    }

    private static int chanceFor(LivingEntity entity) {
        EntityType<?> type = entity.getType();
        if (type == EntityType.GHAST || type == EntityType.WITHER_SKELETON) {
            return 15;
        }
        if (type.is(EntityTypeTags.UNDEAD)) {
            return 4;
        }
        return 8;
    }
}
