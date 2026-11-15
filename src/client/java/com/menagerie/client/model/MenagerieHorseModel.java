package com.menagerie.client.model;

import com.menagerie.client.model.geom.NewHorseGeometry;
import com.menagerie.entity.horse.HorseBreeds;
import com.menagerie.entity.horse.MenagerieHorse;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

/**
 * The Mo' Creatures horse model. Geometry is converted from {@code MoCModelNewHorse} (DrZhark, GPL-3.0);
 * {@link #setupAnim} is a line-by-line port of its {@code setRotationAngles}, and {@link #renderToBuffer}
 * ports the original {@code render} (which parts show, ghost and fairy-wing transparency).
 */
public class MenagerieHorseModel extends EntityModel<MenagerieHorse> {
    private static final float DEG = 57.29578F;
    private static final float PI = 3.141593F;

    private final ModelPart root;
    private final NewHorseGeometry g;
    private final ModelPart[] saddleParts;
    private final ModelPart[] wingParts;

    // render state captured in setupAnim
    private float alpha = 1.0F;
    private boolean ghostly;
    private boolean butterflies;
    private float butterflyAlpha = 1.0F;

    public MenagerieHorseModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.g = new NewHorseGeometry(root);
        this.saddleParts = new ModelPart[]{g.headSaddle, g.saddle, g.saddleB, g.saddleC, g.saddleL, g.saddleL2,
                g.saddleR, g.saddleR2, g.saddleMouthL, g.saddleMouthR, g.saddleMouthLine, g.saddleMouthLineR};
        this.wingParts = new ModelPart[]{g.midWing, g.innerWing, g.outerWing, g.innerWingR, g.midWingR, g.outerWingR};
    }

    @Override
    public void setupAnim(MenagerieHorse horse, float f, float f1, float f2, float f3, float f4) {
        int type = horse.getBreed();
        boolean ghost = horse.isGhost();
        int vanish = horse.getVanishTicks();
        boolean flapwings = horse.wingFlapCounter != 0;
        boolean shuffling = false;
        boolean saddled = horse.isSaddled();
        boolean wings = horse.isFlyer() && !ghost && type < 45;
        boolean eating = horse.isHeadDown();
        boolean standing = horse.isStanding() && !horse.isPassenger();
        boolean openMouth = horse.mouthCounter != 0;
        boolean moveTail = horse.tailCounter != 0;
        boolean rider = horse.isVehicle();
        boolean floating = horse.isFloating();

        // ---- which parts are drawn (original render())
        this.ghostly = ghost || vanish != 0;
        if (vanish != 0) {
            this.alpha = 1.0F - vanish / 100.0F;
        } else if (ghost) {
            this.alpha = ghostAlpha(f2);
        } else {
            this.alpha = 1.0F;
        }
        for (ModelPart p : saddleParts) {
            p.visible = saddled;
        }
        g.saddleMouthLine.visible = saddled && rider;
        g.saddleMouthLineR.visible = saddled && rider;
        boolean muleEars = HorseBreeds.hasMuleEars(type) && !this.ghostly;
        g.muleEarL.visible = muleEars;
        g.muleEarR.visible = muleEars;
        g.ear1.visible = !muleEars;
        g.ear2.visible = !muleEars;
        g.uMouth.visible = !openMouth;
        g.lMouth.visible = !openMouth;
        g.uMouth2.visible = openMouth;
        g.lMouth2.visible = openMouth;
        g.unicorn.visible = HorseBreeds.hasHorn(type) && !this.ghostly;
        boolean bags = horse.hasChest() && !this.ghostly;
        g.bag1.visible = bags;
        g.bag2.visible = bags;
        boolean featherWings = this.ghostly
                ? (type == HorseBreeds.PEGASUS || type == HorseBreeds.DARK_PEGASUS || type == HorseBreeds.SKELETON_PEGASUS)
                : (horse.isFlyer() && type < 45);
        for (ModelPart p : wingParts) {
            p.visible = featherWings;
        }
        this.butterflies = HorseBreeds.isFairy(type) || type == HorseBreeds.GHOST_WINGED;
        if (type == HorseBreeds.GHOST_WINGED) {
            float wingAlpha = flapwings ? 1.0F - horse.wingFlapCounter / 25.0F : 0.0F;
            this.butterflyAlpha = Math.min(wingAlpha, this.alpha);
        } else {
            this.butterflyAlpha = this.ghostly ? this.alpha : 0.7F;
        }
        g.butterflyL.visible = false; // drawn separately in renderToBuffer
        g.butterflyR.visible = false;

        // ---- animation (original setRotationAngles)
        float RLegXRot = Mth.cos((f * 0.6662F) + PI) * 0.8F * f1;
        float LLegXRot = Mth.cos(f * 0.6662F) * 0.8F * f1;
        float HeadXRot = (f4 / DEG);
        if (f3 > 20F) {
            f3 = 20F;
        }
        if (f3 < -20F) {
            f3 = -20F;
        }

        if (shuffling) {
            HeadXRot = HeadXRot + (Mth.cos(f2 * 0.4F) * 0.15F);
        } else if (f1 > 0.2F && !floating) {
            HeadXRot = HeadXRot + (Mth.cos(f * 0.4F) * 0.15F * f1);
        }

        g.head.y = 4.0F;
        g.head.z = -10F;
        g.head.xRot = 0.5235988F + HeadXRot;
        g.head.yRot = f3 / DEG;
        g.tailA.y = 3F;
        g.tailB.z = 14F;
        g.bag2.y = 3F;
        g.bag2.z = 10F;
        g.body.xRot = 0F;

        if (standing && !shuffling) {
            g.head.y = -6F;
            g.head.z = -1F;
            g.head.xRot = (15 / DEG) + HeadXRot;
            g.head.yRot = f3 / DEG;
            g.tailA.y = 9F;
            g.tailB.z = 18F;
            g.bag2.y = 5.5F;
            g.bag2.z = 15F;
            g.body.xRot = -45 / DEG;
        } else if (eating && !shuffling) {
            g.head.y = 11.0F;
            g.head.z = -10F;
            g.head.xRot = 2.18166F;
            g.head.yRot = 0.0F;
        }

        for (ModelPart p : new ModelPart[]{g.ear1, g.ear2, g.muleEarL, g.muleEarR, g.neck, g.uMouth, g.uMouth2,
                g.lMouth, g.lMouth2, g.mane, g.unicorn}) {
            p.y = g.head.y;
            p.z = g.head.z;
            p.xRot = g.head.xRot;
            p.yRot = g.head.yRot;
        }
        g.uMouth2.xRot = g.head.xRot - 0.0872664F;
        g.lMouth2.xRot = g.head.xRot + 0.261799F;
        // mule ears keep their sideways tilt from the geometry (zRot is never animated)

        g.bag1.xRot = RLegXRot / 5F;
        g.bag2.xRot = -RLegXRot / 5F;

        if (wings) {
            for (ModelPart p : wingParts) {
                p.xRot = g.body.xRot;
            }
            if (standing) {
                g.innerWing.y = -5F;
                g.innerWing.z = 4F;
            } else {
                g.innerWing.y = 3F;
                g.innerWing.z = -6F;
            }

            float WingRot;
            if (flapwings) {
                WingRot = Mth.cos((f2 * 0.3F) + PI) * 1.2F;
            } else {
                WingRot = Mth.cos(f * 0.5F) * 0.1F;
            }

            if (floating) {
                g.outerWing.yRot = -0.3228859F + (WingRot / 2F);
                g.outerWingR.yRot = 0.3228859F - (WingRot / 2F);
            } else {
                WingRot = 60 / DEG;
                g.outerWing.yRot = -90 / DEG;
                g.outerWingR.yRot = 90 / DEG;
            }

            g.innerWingR.y = g.innerWing.y;
            g.innerWingR.z = g.innerWing.z;
            g.outerWing.x = g.innerWing.x + (Mth.cos(WingRot) * 12F);
            g.outerWingR.x = g.innerWingR.x - (Mth.cos(WingRot) * 12F);
            g.midWing.y = g.innerWing.y;
            g.midWingR.y = g.innerWing.y;
            g.outerWing.y = g.innerWing.y + (Mth.sin(WingRot) * 12F);
            g.outerWingR.y = g.innerWingR.y + (Mth.sin(WingRot) * 12F);
            g.midWing.z = g.innerWing.z;
            g.midWingR.z = g.innerWing.z;
            g.outerWing.z = g.innerWing.z;
            g.outerWingR.z = g.innerWing.z;
            g.midWing.zRot = WingRot;
            g.innerWing.zRot = WingRot;
            g.outerWing.zRot = WingRot;
            g.innerWingR.zRot = -WingRot;
            g.midWingR.zRot = -WingRot;
            g.outerWingR.zRot = -WingRot;
        }

        if (this.butterflies) {
            float f2a = f2 % 100F;
            float WingRot = 0F;
            if (type != HorseBreeds.GHOST_WINGED) {
                if (flapwings) {
                    WingRot = Mth.cos(f2 * 0.9F) * 0.9F;
                } else if (floating) {
                    WingRot = Mth.cos(f2 * 0.6662F) * 0.5F;
                } else if (f2a > 40 && f2a < 60) {
                    WingRot = Mth.cos(f2 * 0.15F) * 1.20F;
                }
            } else {
                WingRot = Mth.cos(f2 * 0.1F);
            }
            if (standing) {
                g.butterflyL.y = -2.5F;
                g.butterflyL.z = 6.5F;
            } else {
                g.butterflyL.y = 3F;
                g.butterflyL.z = -2F;
            }
            g.butterflyR.y = g.butterflyL.y;
            g.butterflyR.z = g.butterflyL.z;
            g.butterflyL.xRot = g.body.xRot;
            g.butterflyR.xRot = g.body.xRot;
            float baseAngle = type == HorseBreeds.GHOST_WINGED ? 0F : 0.52359F;
            g.butterflyL.zRot = -baseAngle + WingRot;
            g.butterflyR.zRot = baseAngle - WingRot;
        }

        // knee joints: Leg1 and Leg4 use LLegXRot, Leg2 and Leg3 use RLegXRot
        float RLegXRotB = RLegXRot;
        float LLegXRotB = LLegXRot;
        float RLegXRotC = RLegXRot;
        float LLegXRotC = LLegXRot;

        if (floating) {
            RLegXRot = 15 / DEG;
            LLegXRot = RLegXRot;
            RLegXRotB = 45 / DEG;
            RLegXRotC = RLegXRotB;
            LLegXRotB = RLegXRotB;
            LLegXRotC = RLegXRotB;
        }

        if (standing) {
            g.leg3A.y = -2F;
            g.leg3A.z = -2F;
            g.leg4A.y = g.leg3A.y;
            g.leg4A.z = g.leg3A.z;

            RLegXRot = (-60 / DEG) + Mth.cos((f2 * 0.4F) + PI);
            LLegXRot = (-60 / DEG) + Mth.cos(f2 * 0.4F);
            RLegXRotB = 45 / DEG;
            LLegXRotB = RLegXRotB;
            RLegXRotC = -15 / DEG;
            LLegXRotC = 15 / DEG;

            g.leg3B.y = g.leg3A.y + (Mth.sin((90 / DEG) + RLegXRot) * 7F);
            g.leg3B.z = g.leg3A.z + (Mth.cos((270 / DEG) + RLegXRot) * 7F);
            g.leg4B.y = g.leg4A.y + (Mth.sin((90 / DEG) + LLegXRot) * 7F);
            g.leg4B.z = g.leg4A.z + (Mth.cos((270 / DEG) + LLegXRot) * 7F);
            g.leg1B.y = g.leg1A.y + (Mth.sin((90 / DEG) + RLegXRotC) * 7F);
            g.leg1B.z = g.leg1A.z + (Mth.cos((270 / DEG) + RLegXRotC) * 7F);
            g.leg2B.y = g.leg1B.y;
            g.leg2B.z = g.leg1B.z;

            g.leg1A.xRot = RLegXRotC;
            g.leg1B.xRot = LLegXRotC;
            g.leg1C.xRot = g.leg1B.xRot;
            g.leg2A.xRot = RLegXRotC;
            g.leg2B.xRot = LLegXRotC;
            g.leg2C.xRot = g.leg2B.xRot;
            g.leg3A.xRot = RLegXRot;
            g.leg3B.xRot = RLegXRotB;
            g.leg3C.xRot = RLegXRotB;
            g.leg4A.xRot = LLegXRot;
            g.leg4B.xRot = LLegXRotB;
            g.leg4C.xRot = LLegXRotB;
        } else {
            g.leg3A.y = 9F;
            g.leg3A.z = -8F;
            g.leg4A.y = g.leg3A.y;
            g.leg4A.z = g.leg3A.z;

            if (!floating && f1 > 0.2F) {
                float RLegXRot2 = Mth.cos(((f + 0.1F) * 0.6662F) + PI) * 0.8F * f1;
                float LLegXRot2 = Mth.cos((f + 0.1F) * 0.6662F) * 0.8F * f1;
                if (RLegXRot > RLegXRot2) {
                    RLegXRotB = RLegXRot + (55 / DEG);
                }
                if (RLegXRot < RLegXRot2) {
                    RLegXRotC = RLegXRot + (15 / DEG);
                }
                if (LLegXRot > LLegXRot2) {
                    LLegXRotB = LLegXRot + (55 / DEG);
                }
                if (LLegXRot < LLegXRot2) {
                    LLegXRotC = LLegXRot + (15 / DEG);
                }
            }

            g.leg1B.y = g.leg1A.y + (Mth.sin((90 / DEG) + LLegXRot) * 7F);
            g.leg1B.z = g.leg1A.z + (Mth.cos((270 / DEG) + LLegXRot) * 7F);
            g.leg2B.y = g.leg2A.y + (Mth.sin((90 / DEG) + RLegXRot) * 7F);
            g.leg2B.z = g.leg2A.z + (Mth.cos((270 / DEG) + RLegXRot) * 7F);
            g.leg3B.y = g.leg3A.y + (Mth.sin((90 / DEG) + RLegXRot) * 7F);
            g.leg3B.z = g.leg3A.z + (Mth.cos((270 / DEG) + RLegXRot) * 7F);
            g.leg4B.y = g.leg4A.y + (Mth.sin((90 / DEG) + LLegXRot) * 7F);
            g.leg4B.z = g.leg4A.z + (Mth.cos((270 / DEG) + LLegXRot) * 7F);

            g.leg1A.xRot = LLegXRot;
            g.leg1B.xRot = LLegXRotC;
            g.leg1C.xRot = LLegXRotC;
            g.leg2A.xRot = RLegXRot;
            g.leg2B.xRot = RLegXRotC;
            g.leg2C.xRot = RLegXRotC;
            g.leg3A.xRot = RLegXRot;
            g.leg3B.xRot = RLegXRotB;
            g.leg3C.xRot = RLegXRotB;
            g.leg4A.xRot = LLegXRot;
            g.leg4B.xRot = LLegXRotB;
            g.leg4C.xRot = LLegXRotB;
        }

        g.leg1C.y = g.leg1B.y;
        g.leg1C.z = g.leg1B.z;
        g.leg2C.y = g.leg2B.y;
        g.leg2C.z = g.leg2B.z;
        g.leg3C.y = g.leg3B.y;
        g.leg3C.z = g.leg3B.z;
        g.leg4C.y = g.leg4B.y;
        g.leg4C.z = g.leg4B.z;

        if (saddled) {
            if (standing) {
                g.saddle.y = 0.5F;
                g.saddle.z = 11F;
            } else {
                g.saddle.y = 2F;
                g.saddle.z = 2F;
            }
            for (ModelPart p : new ModelPart[]{g.saddleB, g.saddleC, g.saddleL, g.saddleR, g.saddleL2, g.saddleR2}) {
                p.y = g.saddle.y;
                p.z = g.saddle.z;
            }
            g.bag1.y = g.bag2.y;
            g.bag1.z = g.bag2.z;
            g.saddle.xRot = g.body.xRot;
            g.saddleB.xRot = g.body.xRot;
            g.saddleC.xRot = g.body.xRot;

            for (ModelPart p : new ModelPart[]{g.saddleMouthLine, g.saddleMouthLineR, g.headSaddle, g.saddleMouthL, g.saddleMouthR}) {
                p.y = g.head.y;
                p.z = g.head.z;
                p.yRot = g.head.yRot;
            }
            g.saddleMouthLine.xRot = HeadXRot;
            g.saddleMouthLineR.xRot = HeadXRot;
            g.headSaddle.xRot = g.head.xRot;
            g.saddleMouthL.xRot = g.head.xRot;
            g.saddleMouthR.xRot = g.head.xRot;

            if (rider) {
                for (ModelPart p : new ModelPart[]{g.saddleL, g.saddleL2, g.saddleR, g.saddleR2}) {
                    p.xRot = -60 / DEG;
                    p.zRot = 0F;
                }
            } else {
                g.saddleL.xRot = RLegXRot / 3F;
                g.saddleL2.xRot = RLegXRot / 3F;
                g.saddleR.xRot = RLegXRot / 3F;
                g.saddleR2.xRot = RLegXRot / 3F;
                g.saddleL.zRot = RLegXRot / 5F;
                g.saddleL2.zRot = RLegXRot / 5F;
                g.saddleR.zRot = -RLegXRot / 5F;
                g.saddleR2.zRot = -RLegXRot / 5F;
            }
        } else {
            g.bag1.y = g.bag2.y;
            g.bag1.z = g.bag2.z;
        }

        float tailMov = -1.3089F + (f1 * 1.5F);
        if (tailMov > 0) {
            tailMov = 0;
        }
        if (moveTail) {
            g.tailA.yRot = Mth.cos(f2 * 0.7F);
            tailMov = 0;
        } else {
            g.tailA.yRot = 0F;
        }
        g.tailB.yRot = g.tailA.yRot;
        g.tailC.yRot = g.tailA.yRot;
        g.tailB.y = g.tailA.y;
        g.tailC.y = g.tailA.y;
        g.tailB.z = g.tailA.z;
        g.tailC.z = g.tailA.z;
        g.tailA.xRot = tailMov;
        g.tailB.xRot = tailMov;
        g.tailC.xRot = -0.2618F + tailMov;
    }

    /** Ghosts flicker gently between 0.4 and 0.8 opacity (the original's tFloat()). */
    private static float ghostAlpha(float ageInTicks) {
        return 0.6F + 0.2F * Mth.sin(ageInTicks * 0.05F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.ghostly) {
            // ghosts and fading horses: pale, see-through and a little wider, as in the original
            poseStack.pushPose();
            poseStack.scale(1.3F, 1.0F, 1.3F);
            this.root.render(poseStack, buffer, packedLight, packedOverlay, tint(color, 0.8F, this.alpha));
            poseStack.popPose();
        } else {
            this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
        }
        if (this.butterflies && this.butterflyAlpha > 0.01F) {
            g.butterflyL.visible = true;
            g.butterflyR.visible = true;
            poseStack.pushPose();
            poseStack.scale(1.3F, 1.0F, 1.3F);
            int wingColor = tint(color, this.ghostly ? 0.8F : 1.0F, this.butterflyAlpha);
            g.butterflyL.render(poseStack, buffer, packedLight, packedOverlay, wingColor);
            g.butterflyR.render(poseStack, buffer, packedLight, packedOverlay, wingColor);
            poseStack.popPose();
            g.butterflyL.visible = false;
            g.butterflyR.visible = false;
        }
    }

    /** Multiplies an ARGB color's RGB by {@code brightness} and its alpha by {@code alpha}. */
    private static int tint(int color, float brightness, float alpha) {
        int a = (int) (((color >>> 24) & 0xFF) * Mth.clamp(alpha, 0.0F, 1.0F));
        int r = (int) (((color >> 16) & 0xFF) * brightness);
        int gr = (int) (((color >> 8) & 0xFF) * brightness);
        int b = (int) ((color & 0xFF) * brightness);
        return (a << 24) | (r << 16) | (gr << 8) | b;
    }
}
