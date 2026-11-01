package com.critters.registry;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

/**
 * Every critter in the mod. The id must match the model id in tools/critter_models.py
 * (the asset generator verifies this).
 */
public enum CritterSpecies {
    // --- Snails (climb walls, leave slime trails) ---
    GARDEN_SNAIL("garden_snail", Family.SNAIL, 0.4F, 0.35F, 4.0, 0.12, 0x8B5A2B, 0xC9B79C),
    BANDED_SNAIL("banded_snail", Family.SNAIL, 0.4F, 0.35F, 4.0, 0.12, 0xE8C547, 0x5A3416),
    MOSS_SNAIL("moss_snail", Family.SNAIL, 0.4F, 0.4F, 4.0, 0.11, 0x4A6B2A, 0x8FC24F),
    AMETHYST_SNAIL("amethyst_snail", Family.SNAIL, 0.4F, 0.4F, 5.0, 0.11, 0x7B4FB5, 0xC9A3F2),
    MAGMA_SNAIL("magma_snail", Family.SNAIL, 0.4F, 0.35F, 6.0, 0.13, 0x3B1A12, 0xFF7A1A),
    // --- Crabs (amphibious, scuttle sideways) ---
    FIDDLER_CRAB("fiddler_crab", Family.CRAB, 0.5F, 0.35F, 6.0, 0.2, 0x2F6F8F, 0xF2A33A),
    ROCK_CRAB("rock_crab", Family.CRAB, 0.55F, 0.35F, 8.0, 0.18, 0xB8322A, 0x5A1A16),
    // --- Small insects ---
    FIREFLY("firefly", Family.FLYER, 0.3F, 0.3F, 2.0, 0.2, 0x2A2420, 0xE8FF6A),
    CRICKET("cricket", Family.HOPPER, 0.3F, 0.3F, 3.0, 0.22, 0x5B3A1E, 0x8A5A30),
    ANT("ant", Family.CRAWLER, 0.3F, 0.2F, 2.0, 0.25, 0x1E1A1A, 0x5A2A1A),
    // --- Beetles ---
    LADYBUG("ladybug", Family.BEETLE, 0.3F, 0.2F, 2.0, 0.16, 0xD8261C, 0x141010),
    STAG_BEETLE("stag_beetle", Family.BEETLE, 0.4F, 0.3F, 5.0, 0.15, 0x3A2216, 0x8A3A1E),
    RHINOCEROS_BEETLE("rhinoceros_beetle", Family.BEETLE, 0.45F, 0.4F, 6.0, 0.14, 0x5A3418, 0x2A1A10),
    DUNG_BEETLE("dung_beetle", Family.BEETLE, 0.35F, 0.25F, 4.0, 0.17, 0x1A1A1E, 0x45464E),
    JEWEL_BEETLE("jewel_beetle", Family.BEETLE, 0.3F, 0.2F, 3.0, 0.18, 0x1FA05A, 0xE6C23A),
    SCARAB_BEETLE("scarab_beetle", Family.BEETLE, 0.35F, 0.25F, 4.0, 0.16, 0xD9A82C, 0x8A6414),
    BOMBARDIER_BEETLE("bombardier_beetle", Family.BEETLE, 0.3F, 0.2F, 3.0, 0.2, 0x1E2A4A, 0xE07A22),
    WEEVIL("weevil", Family.BEETLE, 0.3F, 0.25F, 3.0, 0.14, 0x6A5A48, 0xA89878),
    GOLIATH_BEETLE("goliath_beetle", Family.BEETLE, 0.5F, 0.35F, 8.0, 0.13, 0x6A3E1E, 0xEDE6D4),
    TIGER_BEETLE("tiger_beetle", Family.BEETLE, 0.3F, 0.2F, 3.0, 0.3, 0x2E7A3E, 0xEDE2B8),
    // --- Fish ---
    GOLDFISH("goldfish", Family.FISH, 0.5F, 0.35F, 3.0, 0, 0xF08A1C, 0xFFD07A),
    BETTA("betta", Family.FISH, 0.5F, 0.4F, 3.0, 0, 0x2A3FA8, 0xC8243A),
    NEON_TETRA("neon_tetra", Family.FISH, 0.35F, 0.25F, 3.0, 0, 0x2AD8FF, 0xE8243A),
    ANGELFISH("angelfish", Family.FISH, 0.45F, 0.6F, 3.0, 0, 0xD8D8D0, 0x202020);

    public enum Family {
        SNAIL, CRAB, FLYER, HOPPER, CRAWLER, BEETLE, FISH;

        public boolean isAquatic() {
            return this == FISH;
        }
    }

    private static final Map<EntityType<?>, CritterSpecies> BY_TYPE = new HashMap<>();

    public final String id;
    public final Family family;
    public final float width;
    public final float height;
    public final double health;
    public final double speed;
    public final int eggPrimary;
    public final int eggSecondary;

    // Filled in during registration.
    private EntityType<? extends Mob> type;
    private Item spawnEgg;
    /** The filled jar (land critters) or the fish bucket (fish). */
    private Item container;

    CritterSpecies(String id, Family family, float width, float height, double health, double speed,
                   int eggPrimary, int eggSecondary) {
        this.id = id;
        this.family = family;
        this.width = width;
        this.height = height;
        this.health = health;
        this.speed = speed;
        this.eggPrimary = eggPrimary;
        this.eggSecondary = eggSecondary;
    }

    public EntityType<? extends Mob> type() {
        return type;
    }

    public Item spawnEgg() {
        return spawnEgg;
    }

    /** The filled Critter Jar for land critters, or the "Bucket of ..." item for fish. */
    public Item container() {
        return container;
    }

    public boolean isAquatic() {
        return family.isAquatic();
    }

    /** Max fish in a school (fish only). */
    public int schoolSize() {
        return switch (this) {
            case GOLDFISH -> 4;
            case BETTA -> 1; // bettas are famously solitary
            case NEON_TETRA -> 12;
            case ANGELFISH -> 3;
            default -> 1;
        };
    }

    void bind(EntityType<? extends Mob> type, Item spawnEgg, Item container) {
        this.type = type;
        this.spawnEgg = spawnEgg;
        this.container = container;
        BY_TYPE.put(type, this);
    }

    @Nullable
    public static CritterSpecies byType(EntityType<?> type) {
        return BY_TYPE.get(type);
    }
}
