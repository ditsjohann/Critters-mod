package com.critters.entity;

import com.critters.registry.CritterSpecies;
import java.util.List;

/**
 * One-shot "special" animations a critter performs now and then while standing still
 * (on top of its looping idle and walk/swim/fly animations, which are driven by the model).
 */
public enum CritterAction {
    NONE(0),
    // snails
    SNAIL_LOOK_UP(60),
    SNAIL_TASTE(60),
    SNAIL_STRETCH(50),
    // crabs
    CRAB_WAVE(70),
    CRAB_THREAT(45),
    CRAB_DIG(60),
    CRAB_EYES_DOWN(45),
    // insects and beetles (shared)
    GROOM(60),
    LOOK_AROUND(50),
    PUSH_UP(35),
    // insects and beetles (signature moves)
    FIREFLY_FLASH(40),
    FIREFLY_WINGS(45),
    CRICKET_CHIRP(70),
    ANTENNA_CLEAN(50),
    ANT_ALARM(40),
    LADYBUG_PLAY_DEAD(90),
    STAG_THREAT(55),
    RHINO_TOSS(35),
    DUNG_DIG(60),
    JEWEL_SHIMMER(50),
    SCARAB_PRAY(55),
    BOMBARDIER_SPRAY(35),
    WEEVIL_DRILL(55),
    GOLIATH_REAR(55),
    TIGER_POUNCE(25),
    // fish
    FISH_DART(18),
    FISH_NIBBLE(35),
    BETTA_FLARE(55),
    ANGEL_GLIDE(60);

    private static final CritterAction[] VALUES = values();

    /** Length in ticks. */
    public final int duration;

    CritterAction(int duration) {
        this.duration = duration;
    }

    public static CritterAction byId(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : NONE;
    }

    /** The special actions each critter can perform. */
    public static List<CritterAction> forSpecies(CritterSpecies species) {
        return switch (species) {
            case GARDEN_SNAIL, BANDED_SNAIL, MOSS_SNAIL, AMETHYST_SNAIL, MAGMA_SNAIL ->
                    List.of(SNAIL_LOOK_UP, SNAIL_TASTE, SNAIL_STRETCH);
            case FIDDLER_CRAB -> List.of(CRAB_WAVE, CRAB_THREAT, CRAB_DIG, CRAB_EYES_DOWN);
            case ROCK_CRAB -> List.of(CRAB_THREAT, CRAB_DIG, CRAB_EYES_DOWN);
            case FIREFLY -> List.of(FIREFLY_FLASH, FIREFLY_WINGS, GROOM);
            case CRICKET -> List.of(CRICKET_CHIRP, ANTENNA_CLEAN, LOOK_AROUND);
            case ANT -> List.of(ANT_ALARM, ANTENNA_CLEAN, LOOK_AROUND);
            case LADYBUG -> List.of(LADYBUG_PLAY_DEAD, GROOM, PUSH_UP);
            case STAG_BEETLE -> List.of(STAG_THREAT, GROOM, LOOK_AROUND);
            case RHINOCEROS_BEETLE -> List.of(RHINO_TOSS, PUSH_UP, LOOK_AROUND);
            case DUNG_BEETLE -> List.of(DUNG_DIG, GROOM, PUSH_UP);
            case JEWEL_BEETLE -> List.of(JEWEL_SHIMMER, GROOM, LOOK_AROUND);
            case SCARAB_BEETLE -> List.of(SCARAB_PRAY, PUSH_UP, GROOM);
            case BOMBARDIER_BEETLE -> List.of(BOMBARDIER_SPRAY, GROOM, LOOK_AROUND);
            case WEEVIL -> List.of(WEEVIL_DRILL, LOOK_AROUND, PUSH_UP);
            case GOLIATH_BEETLE -> List.of(GOLIATH_REAR, PUSH_UP, GROOM);
            case TIGER_BEETLE -> List.of(TIGER_POUNCE, LOOK_AROUND, GROOM);
            case GOLDFISH -> List.of(FISH_NIBBLE, FISH_DART);
            case BETTA -> List.of(BETTA_FLARE, FISH_DART);
            case NEON_TETRA -> List.of(FISH_DART, FISH_NIBBLE);
            case ANGELFISH -> List.of(ANGEL_GLIDE, FISH_NIBBLE);
        };
    }
}
