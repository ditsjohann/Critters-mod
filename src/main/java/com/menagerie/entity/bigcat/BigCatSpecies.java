package com.menagerie.entity.bigcat;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

/**
 * The big cat species and their variants, ported from Mo' Creatures' {@code MoCEntityLion},
 * {@code MoCEntityTiger}, {@code MoCEntityPanther}, {@code MoCEntityLeopard} and the hybrid classes
 * (DrZhark, GPL-3.0). Variant numbers match the original {@code getType()} values.
 *
 * <p>Stats are the original's: health, attack damage, attack range (how far a wild cat notices
 * prey), adult size (the original "max edad" / 100) and the largest prey it will hunt.
 */
public enum BigCatSpecies {
    //          id          health dmg range size  prey
    LION("lion", 30, 7, 8, 1.20F, 2.0F),
    TIGER("tiger", 35, 7, 8, 1.20F, 2.0F),
    PANTHER("panther", 25, 6, 8, 1.00F, 1.5F),
    LEOPARD("leopard", 25, 5, 6, 0.95F, 1.3F),
    LIGER("liger", 35, 8, 10, 1.35F, 2.0F),
    LITHER("lither", 25, 6, 8, 1.10F, 1.5F),
    LEOGER("leoger", 40, 7, 8, 1.30F, 2.0F),
    PANTHARD("panthard", 25, 6, 8, 1.00F, 1.5F),
    PANTHGER("panthger", 30, 7, 10, 1.15F, 1.5F),
    LIARD("liard", 30, 6, 8, 1.00F, 2.0F);

    // Lion variants
    public static final int LIONESS = 1, LION_MALE = 2, LION_WINGED = 3, WHITE_LIONESS = 6, WHITE_LION = 7, WHITE_LION_WINGED = 8;
    // Tiger variants
    public static final int TIGER_ORANGE = 1, TIGER_WHITE = 2, TIGER_WHITE_WINGED = 3;
    // Panther, liger: 1 normal, 2 winged. Leopard: 1 normal, 2 snow leopard.
    public static final int NORMAL = 1, WINGED = 2, SNOW = 2;

    public final String id;
    private final float health;
    private final double damage;
    private final double range;
    public final float adultSize;
    public final float maxPreySize;
    @Nullable
    private EntityType<BigCat> type;

    BigCatSpecies(String id, float health, double damage, double range, float adultSize, float maxPreySize) {
        this.id = id;
        this.health = health;
        this.damage = damage;
        this.range = range;
        this.adultSize = adultSize;
        this.maxPreySize = maxPreySize;
    }

    public EntityType<BigCat> type() {
        if (this.type == null) {
            throw new IllegalStateException("Big cat " + id + " is not registered yet");
        }
        return this.type;
    }

    public void bind(EntityType<BigCat> type) {
        this.type = type;
    }

    @Nullable
    public static BigCatSpecies of(EntityType<?> type) {
        for (BigCatSpecies s : values()) {
            if (s.type == type) {
                return s;
            }
        }
        return null;
    }

    public boolean isHybrid() {
        return ordinal() >= LIGER.ordinal();
    }

    /** Every variant of this species, for spawn eggs. */
    public int[] variants() {
        return switch (this) {
            case LION -> new int[]{LIONESS, LION_MALE, LION_WINGED, WHITE_LIONESS, WHITE_LION, WHITE_LION_WINGED};
            case TIGER -> new int[]{TIGER_ORANGE, TIGER_WHITE, TIGER_WHITE_WINGED};
            case PANTHER, LIGER -> new int[]{NORMAL, WINGED};
            case LEOPARD -> new int[]{NORMAL, SNOW};
            default -> new int[]{NORMAL};
        };
    }

    public boolean isValidVariant(int variant) {
        for (int v : variants()) {
            if (v == variant) {
                return true;
            }
        }
        return false;
    }

    /** Variant of a cat found in the wild. */
    public int wildVariant(RandomSource random, boolean snowy) {
        return switch (this) {
            case LION -> random.nextInt(20) == 0 ? WHITE_LIONESS + random.nextInt(2) : LIONESS + random.nextInt(2);
            case TIGER -> random.nextInt(20) == 0 ? TIGER_WHITE : TIGER_ORANGE;
            case LEOPARD -> snowy ? SNOW : NORMAL;
            default -> NORMAL;
        };
    }

    public String texture(int variant) {
        return switch (this) {
            case LION -> switch (variant) {
                case LIONESS -> "bcfemalelion";
                case WHITE_LIONESS, WHITE_LION, WHITE_LION_WINGED -> "bcwhitelion";
                default -> "bcmalelion";
            };
            case TIGER -> variant == TIGER_ORANGE ? "bctiger" : "bcwhitetiger";
            case PANTHER -> "bcpuma";
            case LEOPARD -> variant == SNOW ? "bcsnowleopard" : "bcleopard";
            default -> "bc" + id;
        };
    }

    /** Translation key suffix: entity.menagerie.&lt;species&gt;.&lt;suffix&gt;. */
    public String variantKey(int variant) {
        return switch (this) {
            case LION -> switch (variant) {
                case LIONESS -> "lioness";
                case LION_WINGED -> "winged";
                case WHITE_LIONESS -> "white_lioness";
                case WHITE_LION -> "white";
                case WHITE_LION_WINGED -> "white_winged";
                default -> "male";
            };
            case TIGER -> switch (variant) {
                case TIGER_WHITE -> "white";
                case TIGER_WHITE_WINGED -> "white_winged";
                default -> "orange";
            };
            case LEOPARD -> variant == SNOW ? "snow" : "common";
            default -> variant == WINGED ? "winged" : "common";
        };
    }

    public boolean isFemale(int variant) {
        return this == LION && (variant == LIONESS || variant == WHITE_LIONESS);
    }

    public boolean hasMane(int variant) {
        return this == LION && (variant == LION_MALE || variant == LION_WINGED || variant == WHITE_LION || variant == WHITE_LION_WINGED);
    }

    public boolean isFlyer(int variant) {
        return switch (this) {
            case LION -> variant == LION_WINGED || variant == WHITE_LION_WINGED;
            case TIGER -> variant == TIGER_WHITE_WINGED;
            case PANTHER, LIGER -> variant == WINGED;
            default -> false;
        };
    }

    public float maxHealth(int variant) {
        if (this == LION && !isFemale(variant)) {
            return 35;
        }
        if (this == TIGER && variant != TIGER_ORANGE) {
            return 40;
        }
        return health;
    }

    public double attackDamage(int variant) {
        if (this == TIGER && variant != TIGER_ORANGE) {
            return 8;
        }
        return damage;
    }

    /** Lionesses do the hunting: they notice prey from further away. */
    public double attackRange(int variant) {
        return isFemale(variant) ? 12 : range;
    }

    public float size(int variant) {
        if (isFemale(variant)) {
            return 1.10F;
        }
        if (this == TIGER && variant == TIGER_WHITE_WINGED) {
            return 1.30F;
        }
        return adultSize;
    }

    /** Light or darkness essence that gives this variant wings, and the winged variant, or 0. */
    public int wingedForm(int variant, boolean light) {
        if (light) {
            if (this == LION && variant == LION_MALE) return LION_WINGED;
            if (this == LION && variant == WHITE_LION) return WHITE_LION_WINGED;
            if (this == TIGER && variant == TIGER_WHITE) return TIGER_WHITE_WINGED;
            if (this == LIGER && variant == NORMAL) return WINGED;
        } else if (this == PANTHER && variant == NORMAL) {
            return WINGED;
        }
        return 0;
    }

    /** Winged cats and hybrids cannot breed. */
    public boolean canBreed(int variant) {
        return !isHybrid() && !isFlyer(variant);
    }

    /** The breeding table: which species a pairing produces, or null if the pair cannot breed. */
    @Nullable
    public static BigCatSpecies offspringSpecies(BigCatSpecies a, int va, BigCatSpecies b, int vb) {
        if (!a.canBreed(va) || !b.canBreed(vb)) {
            return null;
        }
        if (a == b) {
            return switch (a) {
                case LION -> a.isFemale(va) != b.isFemale(vb) ? LION : null;
                case LEOPARD -> va == vb ? LEOPARD : null;
                default -> a;
            };
        }
        // hybrids: the original needs a male lion, an orange tiger, a common leopard or a panther
        if (pair(a, va, b, vb, LION, LION_MALE, TIGER, -1)) return LIGER;
        if (pair(a, va, b, vb, LION, LION_MALE, LEOPARD, NORMAL)) return LIARD;
        if (pair(a, va, b, vb, LION, LION_MALE, PANTHER, NORMAL)) return LITHER;
        if (pair(a, va, b, vb, TIGER, TIGER_ORANGE, LEOPARD, NORMAL)) return LEOGER;
        if (pair(a, va, b, vb, TIGER, TIGER_ORANGE, PANTHER, NORMAL)) return PANTHGER;
        if (pair(a, va, b, vb, PANTHER, NORMAL, LEOPARD, NORMAL)) return PANTHARD;
        return null;
    }

    private static boolean pair(BigCatSpecies a, int va, BigCatSpecies b, int vb,
                                BigCatSpecies s1, int v1, BigCatSpecies s2, int v2) {
        return (a == s1 && (v1 < 0 || va == v1) && b == s2 && (v2 < 0 || vb == v2))
                || (b == s1 && (v1 < 0 || vb == v1) && a == s2 && (v2 < 0 || va == v2));
    }

    /** Variant of a cub; for lions this follows the original (two white parents give white cubs). */
    public static int offspringVariant(BigCatSpecies child, int va, int vb, RandomSource random) {
        if (child == LION) {
            boolean white = va >= WHITE_LIONESS && vb >= WHITE_LIONESS;
            return (white ? WHITE_LIONESS : LIONESS) + random.nextInt(2);
        }
        if (child == TIGER || child == LEOPARD) {
            return random.nextBoolean() ? va : vb;
        }
        return NORMAL;
    }
}
