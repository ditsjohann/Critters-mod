package com.menagerie.entity.horse;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;

/**
 * Every horse breed, the rules attached to each one, and the breeding table.
 *
 * <p>Ported from Mo' Creatures' {@code MoCEntityHorse} (DrZhark, GPL-3.0). The numeric breed ids are
 * kept exactly as in the original, so the original breeding table and transformation rules carry over
 * unchanged.
 */
public final class HorseBreeds {
    // Regular breeds, tier 1 (1-5) to tier 4 (16-17)
    public static final int WHITE = 1, CREAMY = 2, BROWN = 3, DARK_BROWN = 4, BLACK = 5;
    public static final int BRIGHT_CREAMY = 6, SPECKLED = 7, PALE_BROWN = 8, GREY = 9;
    public static final int PINTO = 11, BRIGHT_PINTO = 12, PALE_SPECKLES = 13;
    public static final int SPOTTED = 16, COW = 17;
    // Ghosts, undead and skeletons
    public static final int GHOST_WINGED = 21, GHOST = 22;
    public static final int UNDEAD = 23, UNDEAD_UNICORN = 24, UNDEAD_PEGASUS = 25;
    public static final int SKELETON = 26, SKELETON_UNICORN = 27, SKELETON_PEGASUS = 28;
    // Magic breeds
    public static final int BUG = 30, BAT = 32, UNICORN = 36, NIGHTMARE = 38, PEGASUS = 39, DARK_PEGASUS = 40;
    // Fairy horses (48-59), made by dyeing a white fairy
    public static final int FAIRY_YELLOW = 48, FAIRY_PURPLE = 49, FAIRY_WHITE = 50, FAIRY_BLUE = 51, FAIRY_PINK = 52,
            FAIRY_LIGHT_GREEN = 53, FAIRY_BLACK = 54, FAIRY_RED = 55, FAIRY_DARK_BLUE = 56, FAIRY_CYAN = 57,
            FAIRY_GREEN = 58, FAIRY_ORANGE = 59;
    // Zebras and donkeys
    public static final int ZEBRA = 60, ZORSE = 61, DONKEY = 65, MULE = 66, ZONKY = 67;

    /** Breeds a spawn egg or the creative menu can produce. */
    public static final int[] ALL = {
            WHITE, CREAMY, BROWN, DARK_BROWN, BLACK, BRIGHT_CREAMY, SPECKLED, PALE_BROWN, GREY, PINTO, BRIGHT_PINTO,
            PALE_SPECKLES, SPOTTED, COW, GHOST_WINGED, GHOST, UNDEAD, UNDEAD_UNICORN, UNDEAD_PEGASUS, SKELETON,
            SKELETON_UNICORN, SKELETON_PEGASUS, BAT, UNICORN, NIGHTMARE, PEGASUS, DARK_PEGASUS, FAIRY_YELLOW,
            FAIRY_PURPLE, FAIRY_WHITE, FAIRY_BLUE, FAIRY_PINK, FAIRY_LIGHT_GREEN, FAIRY_BLACK, FAIRY_RED,
            FAIRY_DARK_BLUE, FAIRY_CYAN, FAIRY_GREEN, FAIRY_ORANGE, ZEBRA, ZORSE, DONKEY, MULE, ZONKY
    };

    private HorseBreeds() {
    }

    public static boolean isValid(int t) {
        for (int b : ALL) {
            if (b == t) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRegular(int t) {
        return t > 0 && t < 21;
    }

    public static boolean isPureBreed(int t) {
        return t > 10 && t < 21;
    }

    public static boolean isFairy(int t) {
        return t >= 45 && t < 60;
    }

    public static boolean isGhost(int t) {
        return t == GHOST_WINGED || t == GHOST;
    }

    public static boolean isUndead(int t) {
        return t >= UNDEAD && t <= SKELETON_PEGASUS;
    }

    public static boolean isFlyer(int t) {
        return t == PEGASUS || t == DARK_PEGASUS || isFairy(t) || t == BAT || t == GHOST_WINGED
                || t == UNDEAD_PEGASUS || t == SKELETON_PEGASUS;
    }

    /** Unicorns and wingless ghosts fall slowly but cannot fly. */
    public static boolean isFloater(int t) {
        return t == UNICORN || t == SKELETON_UNICORN || t == UNDEAD_UNICORN || t == GHOST;
    }

    public static boolean isMagic(int t) {
        return t == PEGASUS || t == UNICORN || t == BAT || t == DARK_PEGASUS || isFairy(t) || isGhost(t);
    }

    public static boolean hasHorn(int t) {
        return t == UNICORN || isFairy(t) || t == SKELETON_UNICORN || t == UNDEAD_UNICORN;
    }

    public static boolean hasMuleEars(int t) {
        return t == DONKEY || t == MULE || t == ZONKY;
    }

    /** Can carry saddle bags (a chest). */
    public static boolean isBagger(int t) {
        return t == MULE || t == DONKEY || t == ZONKY || t == PEGASUS || t == DARK_PEGASUS
                || t == UNDEAD_PEGASUS || t == SKELETON_PEGASUS || isFairy(t);
    }

    /** Wears iron, gold and diamond horse armor. Zebras and zorses are new: the original had the textures but not the rule. */
    public static boolean wearsMetalArmor(int t) {
        return isRegular(t) || t == ZEBRA || t == ZORSE;
    }

    /** Wears crystal horse armor. */
    public static boolean wearsCrystalArmor(int t) {
        return t == BAT || t == DARK_PEGASUS || isFairy(t) || isGhost(t) || t == PEGASUS || t == UNICORN;
    }

    public static boolean isFireImmune(int t) {
        return t == NIGHTMARE || t == DARK_PEGASUS;
    }

    /** Can be stored in an amulet (bone, ghost, fairy or pegasus). */
    public static boolean isAmuletHorse(int t) {
        return (t >= 48 && t < 60) || t == DARK_PEGASUS || t == PEGASUS || isGhost(t)
                || t == SKELETON || t == SKELETON_UNICORN || t == SKELETON_PEGASUS;
    }

    /** Food (wheat, apples, sugar...) works on these; magic and undead horses need essences instead. */
    public static boolean eatsFood(int t) {
        return !isMagic(t) && !isUndead(t);
    }

    public static String texture(int t) {
        return switch (t) {
            case WHITE -> "horsewhite";
            case CREAMY -> "horsecreamy";
            case BROWN -> "horsebrown";
            case DARK_BROWN -> "horsedarkbrown";
            case BLACK -> "horseblack";
            case BRIGHT_CREAMY -> "horsebrightcreamy";
            case SPECKLED -> "horsespeckled";
            case PALE_BROWN -> "horsepalebrown";
            case GREY -> "horsegrey";
            case PINTO -> "horsepinto";
            case BRIGHT_PINTO -> "horsebrightpinto";
            case PALE_SPECKLES -> "horsepalespeckles";
            case SPOTTED -> "horsespotted";
            case COW -> "horsecow";
            case GHOST_WINGED -> "horseghost";
            case GHOST -> "horseghostb";
            case UNDEAD -> "horseundead";
            case UNDEAD_UNICORN -> "horseundeadunicorn";
            case UNDEAD_PEGASUS -> "horseundeadpegasus";
            case SKELETON -> "horseskeleton";
            case SKELETON_UNICORN -> "horseunicornskeleton";
            case SKELETON_PEGASUS -> "horsepegasusskeleton";
            case BUG -> "horsebug";
            case BAT -> "horsebat";
            case UNICORN -> "horseunicorn";
            case NIGHTMARE -> "horsenightmare";
            case PEGASUS -> "horsepegasus";
            case DARK_PEGASUS -> "horsedarkpegasus";
            case FAIRY_YELLOW -> "horsefairyyellow";
            case FAIRY_PURPLE -> "horsefairypurple";
            case FAIRY_WHITE -> "horsefairywhite";
            case FAIRY_BLUE -> "horsefairyblue";
            case FAIRY_PINK -> "horsefairypink";
            case FAIRY_LIGHT_GREEN -> "horsefairylightgreen";
            case FAIRY_BLACK -> "horsefairyblack";
            case FAIRY_RED -> "horsefairyred";
            case FAIRY_DARK_BLUE -> "horsefairydarkblue";
            case FAIRY_CYAN -> "horsefairycyan";
            case FAIRY_GREEN -> "horsefairygreen";
            case FAIRY_ORANGE -> "horsefairyorange";
            case ZEBRA -> "horsezebra";
            case ZORSE -> "horsezorse";
            case DONKEY -> "horsedonkey";
            case MULE -> "horsemule";
            case ZONKY -> "horsezonky";
            default -> "horsebug";
        };
    }

    /** Translation key suffix for the breed name shown in the entity's name. */
    public static String key(int t) {
        return texture(t).substring("horse".length());
    }

    public static float maxHealth(int t) {
        if (t < 6) return 25;
        if (t < 11) return 30;
        if (t < 16) return 35;
        if (t < 21) return 40;
        if (t < 30) return 35;                // ghosts, undead, skeletons
        if (t < 40) return 50;                // magic
        if (t == DARK_PEGASUS) return 50;
        if (t < 60) return 40;                // fairies
        return 30;                            // zebras and donkeys
    }

    /** Original speed factor, 0.7 (donkey) to 1.3 (dark pegasus, fairies). */
    public static double speedFactor(int t) {
        if (t < 6) return 0.9;
        if (t < 11) return 1.0;
        if (t < 16) return 1.1;
        if (t < 21) return 1.2;
        if (t < 26) return 0.8;
        if (t < 30) return 1.0;
        if (t < 40) return 1.2;
        if (t < 60) return 1.3;
        if (t == ZEBRA || t == ZORSE) return 1.1;
        if (t == DONKEY) return 0.7;
        return 0.9;
    }

    /** Original jump factor, 0.35 (tier 1) to 0.6 (dark pegasus, fairies). */
    public static double jumpFactor(int t) {
        if (t < 6) return 0.35;
        if (t < 11) return 0.40;
        if (t < 16) return 0.45;
        if (t < 21) return 0.50;
        if (t < 26) return 0.45;
        if (t < 30) return 0.5;
        if (t < 40) return 0.55;
        if (t < 60) return 0.6;
        return 0.4;
    }

    /** Fairy color produced by feeding a dye to a white fairy horse, or -1. */
    public static int fairyFromDye(DyeColor dye) {
        return switch (dye) {
            case ORANGE -> FAIRY_ORANGE;
            case LIGHT_BLUE -> FAIRY_BLUE;
            case YELLOW -> FAIRY_YELLOW;
            case LIME -> FAIRY_LIGHT_GREEN;
            case PINK -> FAIRY_PINK;
            case CYAN -> FAIRY_CYAN;
            case PURPLE -> FAIRY_PURPLE;
            case BLUE -> FAIRY_DARK_BLUE;
            case GREEN -> FAIRY_GREEN;
            case RED -> FAIRY_RED;
            case BLACK -> FAIRY_BLACK;
            case WHITE -> FAIRY_WHITE;
            default -> -1;
        };
    }

    /** Breed of a horse found in the wild. Ported from {@code selectType} / {@code checkSpawningBiome}. */
    public static int wildBreed(RandomSource random, boolean plains, boolean sandy) {
        if (sandy || (plains && random.nextInt(3) == 0)) {
            return ZEBRA;
        }
        if (random.nextInt(20) == 0) {
            return DONKEY;                     // new: donkeys show up in the wild so mules and zonkies can be bred
        }
        int j = random.nextInt(100);
        if (j <= 33) return BRIGHT_CREAMY;
        if (j <= 66) return SPECKLED;
        if (j <= 95) return PALE_BROWN;
        return random.nextInt(5) + 1;          // new: an occasional tier 1 horse keeps the breeding table interesting
    }

    /**
     * The original breeding table ({@code HorseGenetics}). Returns the foal's breed.
     * Unicorn + pegasus gives a fairy and both parents vanish; the caller handles that.
     */
    public static int offspring(int a, int b, RandomSource random) {
        if (a == b) return a;
        if ((a == ZEBRA && b < 21) || (b == ZEBRA && a < 21)) return ZORSE;
        if ((a == DONKEY && b < 21) || (b == DONKEY && a < 21)) return MULE;
        if ((a == ZEBRA && b == DONKEY) || (b == ZEBRA && a == DONKEY)) return ZONKY;
        if ((a > 20 && b < 21) || (b > 20 && a < 21)) return Math.min(a, b);
        if ((a == UNICORN && b == PEGASUS) || (b == UNICORN && a == PEGASUS)) return FAIRY_WHITE;
        if ((a == UNICORN && b == DARK_PEGASUS) || (b == UNICORN && a == DARK_PEGASUS)) return FAIRY_BLACK;
        if (a > 20 && b > 20) return random.nextInt(5) + 1;

        int roll = random.nextInt(4);
        if (roll == 0) return a;
        if (roll == 1) return b;
        int lo = Math.min(a, b), hi = Math.max(a, b);
        int mix = MIX[lo][hi];
        return mix != 0 ? mix : a;
    }

    /** True when breeding these two makes a fairy, and both parents vanish. */
    public static boolean makesFairy(int a, int b) {
        return (a == UNICORN || b == UNICORN) && (a == PEGASUS || b == PEGASUS || a == DARK_PEGASUS || b == DARK_PEGASUS);
    }

    /** MIX[lo][hi] = foal breed for two different regular breeds (from the original table), 0 = not listed. */
    private static final int[][] MIX = new int[21][21];

    private static void mix(int a, int b, int child) {
        MIX[Math.min(a, b)][Math.max(a, b)] = child;
    }

    static {
        mix(1, 2, 6); mix(1, 3, 2); mix(1, 4, 7); mix(1, 5, 9); mix(1, 7, 12); mix(1, 8, 7); mix(1, 9, 13);
        mix(1, 11, 12); mix(1, 12, 13); mix(1, 17, 16);
        mix(2, 4, 3); mix(2, 5, 4); mix(2, 7, 8); mix(2, 8, 3); mix(2, 12, 6); mix(2, 16, 13); mix(2, 17, 12);
        mix(3, 4, 8); mix(3, 5, 8); mix(3, 6, 2); mix(3, 7, 11); mix(3, 9, 8); mix(3, 12, 11); mix(3, 16, 11);
        mix(3, 17, 11);
        mix(4, 6, 3); mix(4, 7, 8); mix(4, 9, 7); mix(4, 11, 7); mix(4, 12, 7); mix(4, 13, 7); mix(4, 16, 13);
        mix(4, 17, 5);
        mix(5, 6, 4); mix(5, 7, 4); mix(5, 8, 4); mix(5, 11, 17); mix(5, 12, 13); mix(5, 13, 16); mix(5, 16, 17);
        mix(6, 8, 2); mix(6, 17, 7);
        mix(7, 16, 13);
        mix(8, 11, 7); mix(8, 12, 7); mix(8, 13, 7); mix(8, 16, 7); mix(8, 17, 7);
        mix(9, 16, 13);
        mix(11, 16, 13); mix(11, 17, 7);
        mix(12, 16, 13);
        mix(13, 17, 9);
    }
}
