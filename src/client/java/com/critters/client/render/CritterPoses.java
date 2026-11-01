package com.critters.client.render;

import com.critters.entity.ActionController;
import com.critters.entity.ActionHolder;
import com.critters.entity.CritterAction;
import com.critters.registry.CritterSpecies;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

/**
 * Whole-body motion that can't be done by moving individual parts: rearing up, rolling onto the
 * back, lunging, bobbing, waddling. Applied by the renderer around the critter's middle.
 */
public final class CritterPoses {
    private static final float PI = (float) Math.PI;

    private CritterPoses() {
    }

    private static float env(float t, float duration, float fade) {
        return Mth.clamp(Math.min(t / fade, (duration - t) / fade), 0.0F, 1.0F);
    }

    public static void apply(PoseStack pose, Mob entity, CritterSpecies species, float partialTick) {
        float ls = entity.walkAnimation.position(partialTick);
        float move = Mth.clamp(entity.walkAnimation.speed(partialTick) * 1.6F, 0.0F, 1.0F);
        float idle = 1.0F - move;
        float age = entity.tickCount + partialTick;
        CritterAction action = CritterAction.NONE;
        float at = 0.0F;
        if (entity instanceof ActionHolder holder) {
            ActionController ac = holder.critterActions();
            action = ac.current();
            at = ac.time(partialTick);
        }
        float e = action == CritterAction.NONE ? 0.0F : env(at, action.duration, 8.0F);

        float rise = 0.0F;      // blocks
        float pitch = 0.0F;     // degrees, + = nose down
        float roll = 0.0F;      // degrees
        float forward = 0.0F;   // blocks

        // ---- per-species walk body motion and idles ----
        switch (species) {
            case ROCK_CRAB -> roll += Mth.sin(ls * 1.6F) * 5.0F * move;
            case FIDDLER_CRAB -> rise += Math.abs(Mth.sin(ls * 2.0F)) * 0.008F * move;
            case CRICKET -> rise += Math.abs(Mth.sin(ls * 1.9F)) * 0.006F * move;
            case LADYBUG -> roll += Mth.sin(age * 0.1F) * 2.0F * idle;
            case STAG_BEETLE -> roll += Mth.sin(ls * 1.1F) * 3.0F * move;
            case RHINOCEROS_BEETLE -> {
                roll += Mth.sin(ls * 1.0F) * 4.5F * move;
                rise += Math.abs(Mth.sin(ls * 1.0F)) * 0.006F * move;
            }
            case DUNG_BEETLE -> pitch += 9.0F * move;
            case BOMBARDIER_BEETLE -> pitch += 5.0F * idle;
            case WEEVIL -> rise += Math.abs(Mth.sin(ls * 0.9F)) * 0.005F * move;
            case GOLIATH_BEETLE -> {
                roll += Mth.sin(ls * 0.9F) * 4.0F * move;
                rise += Mth.sin(age * 0.06F) * 0.004F * idle;
            }
            case TIGER_BEETLE -> rise += 0.012F;
            case FIREFLY -> {
                if (!entity.onGround()) {
                    rise += Mth.sin(age * 0.2F) * 0.03F;
                    pitch -= 12.0F;
                }
            }
            case GOLDFISH -> rise += Mth.sin(age * 0.08F) * 0.012F * idle;
            case BETTA -> roll += Mth.sin(age * 0.05F) * 3.0F * idle;
            case ANGELFISH -> pitch += Mth.sin(age * 0.04F) * 3.0F * idle;
            case SEAHORSE -> rise += Mth.sin(age * 0.07F) * 0.015F;
            case BLOBFISH -> rise += Mth.sin(age * 0.05F) * 0.008F * idle;
            case JELLYFISH -> rise += Mth.sin(age * 0.18F) * 0.02F;
            case DUMBO_OCTOPUS -> {
                rise += Mth.sin(age * 0.1F) * 0.02F;
                roll += Mth.sin(age * 0.06F) * 4.0F * idle;
            }
            case RAIN_FROG -> roll += Mth.sin(ls * 2.2F) * 7.0F * move;
            case DART_FROG -> pitch -= 6.0F;
            case FIRE_SALAMANDER -> roll += Mth.sin(ls * 1.4F) * 3.0F * move;
            default -> {
            }
        }

        // ---- special actions ----
        switch (action) {
            case PUSH_UP -> rise += Math.abs(Mth.sin(at * 0.28F)) * 0.03F * e;
            case LADYBUG_PLAY_DEAD -> {
                float flip = Mth.clamp(Math.min(at / 10.0F, (action.duration - at) / 10.0F), 0.0F, 1.0F);
                roll += 180.0F * flip;
                rise += 0.06F * Mth.sin(flip * PI);
            }
            case STAG_THREAT -> {
                pitch -= 18.0F * e;
                rise += 0.02F * e;
            }
            case RHINO_TOSS -> pitch -= 8.0F * Math.max(0.0F, Mth.sin(at / action.duration * PI * 2.0F));
            case DUNG_DIG -> {
                pitch += 10.0F * e;
                roll += Mth.sin(at * 0.9F) * 3.0F * e;
            }
            case JEWEL_SHIMMER -> roll += Mth.sin(at * 0.25F) * 14.0F * e;
            case SCARAB_PRAY -> pitch -= 13.0F * e;
            case BOMBARDIER_SPRAY -> {
                pitch += 20.0F * e;
                roll += (at > 10 && at < 20 ? Mth.sin(at * 2.5F) * 3.0F : 0.0F);
            }
            case WEEVIL_DRILL -> pitch += (12.0F + Mth.sin(at * 0.9F) * 4.0F) * e;
            case GOLIATH_REAR -> {
                pitch -= 26.0F * e;
                rise += 0.03F * e;
            }
            case TIGER_POUNCE -> {
                float lunge = at < 6 ? at / 6.0F : Mth.clamp(1.0F - (at - 6) / 14.0F, 0.0F, 1.0F);
                forward += 0.14F * Mth.sin(lunge * PI / 2.0F);
                pitch += 6.0F * lunge;
            }
            case ANT_ALARM -> rise += 0.012F * e;
            case CRAB_THREAT -> rise += 0.03F * e;
            case CRAB_DIG -> {
                rise -= 0.025F * e;
                roll += Mth.sin(at * 1.1F) * 6.0F * e;
            }
            case FIREFLY_FLASH -> pitch += 8.0F * e;
            case FISH_DART -> forward += 0.08F * Mth.sin(Mth.clamp(at / action.duration, 0.0F, 1.0F) * PI);
            case FISH_NIBBLE -> pitch += (22.0F + Mth.sin(at * 0.8F) * 6.0F) * e;
            case ANGEL_GLIDE -> roll += Mth.sin(at * 0.1F) * 12.0F * e;
            case SNAIL_LOOK_UP -> pitch -= 6.0F * e;
            case CLOWN_WIGGLE -> roll += Mth.sin(at * 1.6F) * 10.0F * e;
            case SEAHORSE_BOW -> pitch += Math.abs(Mth.sin(at * 0.14F)) * 12.0F * e;
            case BLOB_SIGH -> rise -= 0.02F * Mth.sin(Mth.clamp(at / action.duration, 0.0F, 1.0F) * PI);
            case ANGLER_SNAP -> forward += at < 14 ? 0.0F : 0.06F * Mth.clamp(1.0F - (at - 14) / 10.0F, 0.0F, 1.0F);
            case JELLY_PULSE -> rise += 0.04F * e;
            case OCTO_FLAP -> rise += Math.abs(Mth.sin(at * 0.3F)) * 0.05F * e;
            case OCTO_UMBRELLA -> rise += 0.03F * e;
            case RAIN_FROG_PUFF -> rise += 0.015F * Mth.sin(Mth.clamp(at / action.duration, 0.0F, 1.0F) * PI);
            case FROG_CROAK -> pitch -= 4.0F * e;
            case SALAMANDER_STRETCH -> rise += 0.025F * e;
            default -> {
            }
        }

        if (rise == 0 && pitch == 0 && roll == 0 && forward == 0) {
            return;
        }
        float mid = entity.getBbHeight() * 0.45F;
        pose.translate(0.0F, rise, -forward);
        pose.translate(0.0F, mid, 0.0F);
        if (pitch != 0) {
            pose.mulPose(Axis.XP.rotationDegrees(-pitch));
        }
        if (roll != 0) {
            pose.mulPose(Axis.ZP.rotationDegrees(roll));
        }
        pose.translate(0.0F, -mid, 0.0F);
    }
}
