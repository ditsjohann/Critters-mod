package com.menagerie.client.model;

import com.menagerie.client.model.geom.BigCatGeometry;
import com.menagerie.entity.bigcat.BigCat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

/**
 * The Mo' Creatures big cat model. Geometry is converted from {@code MoCModelNewBigCat}
 * (DrZhark, GPL-3.0); {@link #setupAnim} ports its {@code setRotationAngles} and
 * {@code updateAnimationModifiers}, {@link #renderToBuffer} its {@code render}.
 */
public class BigCatModel extends EntityModel<BigCat> {
    private static final float RAD = 57.29578F;
    private static final float PI = 3.141593F;

    private final ModelPart root;
    private final BigCatGeometry g;
    private final ModelPart[] wings;
    private final ModelPart[] stinger;

    private boolean ghost;
    private float alpha = 1.0F;

    public BigCatModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.g = new BigCatGeometry(root);
        this.wings = new ModelPart[]{g.innerWing, g.midWing, g.outerWing, g.innerWingR, g.midWingR, g.outerWingR};
        this.stinger = new ModelPart[]{g.sTailRoot, g.sTail2, g.sTail3, g.sTail4, g.sTail5, g.stingerLump, g.stinger};
    }

    @Override
    public void setupAnim(BigCat cat, float f, float f1, float f2, float f3, float f4) {
        // ---- updateAnimationModifiers
        boolean isFlyer = cat.isFlyer();
        boolean isSaddled = cat.isSaddled();
        boolean flapwings = cat.wingFlapCounter != 0;
        boolean onAir = !cat.onGround();
        boolean floating = isFlyer && onAir;
        int openMouthCounter = cat.mouthCounter;
        boolean isRidden = cat.isVehicle();
        boolean hasMane = cat.hasMane();
        boolean isSitting = cat.isInSittingPose();
        boolean movingTail = cat.tailCounter != 0;
        boolean hasStinger = false;   // manticores only
        boolean poisoning = false;
        this.ghost = cat.isGhost();
        this.alpha = this.ghost ? cat.ghostAlpha(f2) : 1.0F;
        boolean isMovingVertically = onAir && Math.abs(cat.getDeltaMovement().y) > 0.01;

        // ---- render(): which parts show
        g.neckHarness.visible = isSaddled;
        g.harnessStick.visible = isSaddled;
        g.saddle.visible = isSaddled;
        g.leftHarness.visible = isSaddled && isRidden;
        g.rightHarness.visible = isSaddled && isRidden;
        g.mane.visible = hasMane;
        g.leftChinBeard.visible = hasMane;
        g.rightChinBeard.visible = hasMane;
        g.foreheadHair.visible = hasMane;
        g.neckHair.visible = hasMane;
        g.chinHair.visible = hasMane;
        g.collar.visible = cat.hasCollar();
        g.leftFang.visible = false;
        g.rightFang.visible = false;
        g.storageChest.visible = cat.hasChest();
        for (ModelPart p : wings) {
            p.visible = isFlyer;
        }
        for (ModelPart p : stinger) {
            p.visible = hasStinger;
        }

        // ---- setRotationAngles
        float RLegXRot = Mth.cos((f * 0.8F) + PI) * 0.8F * f1;
        float LLegXRot = Mth.cos(f * 0.8F) * 0.8F * f1;
        float gallopRLegXRot = Mth.cos((f * 0.6F) + PI) * 0.8F * f1;
        float gallopLLegXRot = Mth.cos(f * 0.6F) * 0.8F * f1;

        float stingYOffset;
        float stingZOffset;

        if (movingTail) {
            g.tail2.yRot = Mth.cos(f2 * 0.3F);
        } else {
            g.tail2.yRot = 0F;
        }

        if (isSitting) {
            stingYOffset = 17F;
            stingZOffset = -3F;
            g.chest.y = 14F;
            g.abdomen.xRot = -10F / RAD;
            g.chest.xRot = -45F / RAD;
            g.rightUpperLeg.xRot = 35F / RAD;
            g.rightLowerLeg.xRot = 5F / RAD;
            g.leftUpperLeg.xRot = 35F / RAD;
            g.leftLowerLeg.xRot = 5F / RAD;
            g.neckBase.xRot = 20F / RAD;
            g.rightHindUpperLeg.y = 1F;
            g.rightHindUpperLeg.xRot = -50F / RAD;
            g.leftHindUpperLeg.y = 1F;
            g.leftHindUpperLeg.xRot = -50F / RAD;
            g.rightHindFoot.xRot = 90F / RAD;
            g.leftHindFoot.xRot = 90F / RAD;
            g.tailRoot.xRot = 100F / RAD;
            g.tail2.xRot = 35F / RAD;
            g.tail3.xRot = 10F / RAD;
            g.neckHair.y = 2F;
            g.collar.xRot = 0F;
            g.collar.y = 7F;
            g.collar.z = -4F;
        } else {
            stingYOffset = 8F;
            stingZOffset = 0F;
            g.chest.y = 8F;
            g.abdomen.xRot = 0F;
            g.chest.xRot = 0F;
            g.neckBase.xRot = -14F / RAD;
            g.tailRoot.xRot = 87F / RAD;
            g.tail2.xRot = -30F / RAD;
            g.tail3.xRot = -17F / RAD;
            g.rightLowerLeg.xRot = -21.5F / RAD;
            g.leftLowerLeg.xRot = -21.5F / RAD;
            g.rightHindUpperLeg.y = 3F;
            g.leftHindUpperLeg.y = 3F;
            g.rightHindFoot.xRot = 27F / RAD;
            g.leftHindFoot.xRot = 27F / RAD;
            g.collar.z = -2F;
            g.neckHair.y = -0.5F;
            g.collar.y = hasMane ? 9F : 6F;
            g.collar.xRot = (20F / RAD) + Mth.cos(f * 0.8F) * 0.5F * f1;

            boolean galloping = f1 >= 0.97F;
            if (onAir || this.ghost) {
                if (this.ghost || (isFlyer && f1 > 0)) {
                    float speedMov = f1 * 0.5F;
                    g.rightUpperLeg.xRot = (45F / RAD) + speedMov;
                    g.leftUpperLeg.xRot = (45F / RAD) + speedMov;
                    g.rightHindUpperLeg.xRot = (10F / RAD) + speedMov;
                    g.leftHindUpperLeg.xRot = (10F / RAD) + speedMov;
                } else if (isMovingVertically) {
                    g.rightUpperLeg.xRot = -35F / RAD;
                    g.leftUpperLeg.xRot = -35F / RAD;
                    g.rightHindUpperLeg.xRot = 35F / RAD;
                    g.leftHindUpperLeg.xRot = 35F / RAD;
                }
            } else if (galloping) {
                g.rightUpperLeg.xRot = (15F / RAD) + gallopRLegXRot;
                g.leftUpperLeg.xRot = (15F / RAD) + gallopRLegXRot;
                g.rightHindUpperLeg.xRot = (-25F / RAD) + gallopLLegXRot;
                g.leftHindUpperLeg.xRot = (-25F / RAD) + gallopLLegXRot;
                g.abdomen.yRot = 0F;
            } else {
                g.rightUpperLeg.xRot = (15F / RAD) + RLegXRot;
                g.leftHindUpperLeg.xRot = (-25F / RAD) + RLegXRot;
                g.leftUpperLeg.xRot = (15F / RAD) + LLegXRot;
                g.rightHindUpperLeg.xRot = (-25F / RAD) + LLegXRot;
                if (!hasStinger) {
                    g.abdomen.yRot = Mth.cos(f * 0.3F) * 0.25F * f1;
                }
            }

            if (isRidden) {
                g.leftFootHarness.xRot = -60 / RAD;
                g.rightFootHarness.xRot = -60 / RAD;
            } else {
                g.leftFootHarness.xRot = RLegXRot / 3F;
                g.rightFootHarness.xRot = RLegXRot / 3F;
                g.leftFootHarness.zRot = RLegXRot / 5F;
                g.rightFootHarness.zRot = -RLegXRot / 5F;
            }

            float TailXRot = Mth.cos(f * 0.4F) * 0.15F * f1;
            g.tailRoot.xRot = (87F / RAD) + TailXRot;
            g.tail2.xRot = (-30F / RAD) + TailXRot;
            g.tail3.xRot = (-17F / RAD) + TailXRot;
            g.tail4.xRot = (21F / RAD) + TailXRot;
            g.tailTip.xRot = (21F / RAD) + TailXRot;
            g.tailTusk.xRot = (21F / RAD) + TailXRot;
        }

        float HeadXRot = f4 / RAD;
        g.headBack.xRot = 14F / RAD + HeadXRot;
        g.headBack.yRot = f3 / RAD;

        float mouthMov = 0F;
        if (openMouthCounter != 0) {
            if (openMouthCounter < 10) {
                mouthMov = 22 + (openMouthCounter * 3);
            } else if (openMouthCounter > 20) {
                mouthMov = 22 + (90 - (openMouthCounter * 3));
            } else {
                mouthMov = 55F;
            }
        }
        g.lowerJaw.xRot = mouthMov / RAD;

        if (isSaddled) {
            g.leftHarness.xRot = 25F / RAD + g.headBack.xRot;
            g.leftHarness.yRot = g.headBack.yRot;
            g.rightHarness.xRot = 25F / RAD + g.headBack.xRot;
            g.rightHarness.yRot = g.headBack.yRot;
        }

        if (isFlyer) {
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
                WingRot = 60 / RAD;
                g.outerWing.yRot = -90 / RAD;
                g.outerWingR.yRot = 90 / RAD;
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

            if (hasStinger) {
                if (!poisoning) {
                    setSting(g.sTailRoot, 33F, stingYOffset, stingZOffset);
                    setSting(g.sTail2, 54.5F, stingYOffset, stingZOffset);
                    setSting(g.sTail3, 95.1F, stingYOffset, stingZOffset);
                    setSting(g.sTail4, 141.8F, stingYOffset, stingZOffset);
                    setSting(g.sTail5, 173.9F, stingYOffset, stingZOffset);
                    setSting(g.stingerLump, 116.4F, stingYOffset, stingZOffset);
                    setSting(g.stinger, 69.5F, stingYOffset, stingZOffset);
                } else if (!isSitting) {
                    setSting(g.sTailRoot, 95.2F, 14.5F, 2F);
                    setSting(g.sTail2, 128.5F, 15F, 4F);
                    setSting(g.sTail3, 169F, 14F, 3.8F);
                    setSting(g.sTail4, 177F, 13.5F, -8.5F);
                    setSting(g.sTail5, 180F, 11.5F, -17F);
                    setSting(g.stingerLump, 35.4F, -4F, -28F);
                    setSting(g.stinger, 25.5F, 4F, -29F);
                }
            }
        }
    }

    private static void setSting(ModelPart part, float degrees, float y, float z) {
        part.xRot = degrees / RAD;
        part.y = y;
        part.z = z;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.ghost) {
            int a = (int) (((color >>> 24) & 0xFF) * Mth.clamp(this.alpha, 0.0F, 1.0F));
            int rgb = (int) (((color >> 16) & 0xFF) * 0.8F) << 16 | (int) (((color >> 8) & 0xFF) * 0.8F) << 8 | (int) ((color & 0xFF) * 0.8F);
            color = (a << 24) | rgb;
        }
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
