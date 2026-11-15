package com.menagerie.client.model;

import com.menagerie.client.model.geom.WyvernGeometry;
import com.menagerie.entity.wyvern.Wyvern;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

/**
 * The Mo' Creatures wyvern model. Geometry is converted from {@code MoCModelWyvern} (DrZhark, GPL-3.0);
 * {@link #setupAnim} ports its {@code setRotationAngles}, {@link #renderToBuffer} its {@code render}
 * (saddle and harness, chest, iron/gold/diamond armor plates, ghost transparency).
 */
public class WyvernModel extends EntityModel<Wyvern> {
    private static final float RAD = 57.29578F;
    private static final float PI = 3.141593F;

    private final ModelPart root;
    private final WyvernGeometry g;
    private final ModelPart[] iron;
    private final ModelPart[] gold;
    private final ModelPart[] diamond;

    private boolean ghost;
    private float alpha = 1.0F;
    private float yOffset;

    public WyvernModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.g = new WyvernGeometry(root);
        this.iron = new ModelPart[]{g.ironhelmethorn1, g.ironhelmethorn2, g.ironhelmet, g.ironhelmetsnout, g.ironrightlegarmor,
                g.ironleftlegarmor, g.ironchestarmor, g.ironrightshoulderpad, g.ironleftshoulderpad};
        this.gold = new ModelPart[]{g.goldleftshoulder, g.goldchestarmor, g.goldrightshoulder, g.goldleftlegarmor,
                g.goldrightlegarmor, g.goldhelmethorn1, g.goldhelmethorn2, g.goldhelmet, g.goldhelmetsnout};
        this.diamond = new ModelPart[]{g.diamondleftshoulder, g.diamondrightshoulder, g.diamondchestarmor, g.diamondleftlegarmor,
                g.diamondrightlegarmor, g.diamondhelmet, g.diamondhelmethorn2, g.diamondhelmethorn1, g.diamondhelmetsnout};
    }

    @Override
    public void setupAnim(Wyvern wyvern, float f, float f1, float f2, float f3, float f4) {
        int armor = wyvern.getArmorType();
        boolean isRidden = wyvern.isVehicle();
        boolean isChested = wyvern.hasChest();
        boolean isSaddled = wyvern.isSaddled();
        boolean flapwings = wyvern.wingFlapCounter != 0;
        boolean onAir = !wyvern.onGround() || wyvern.isFlying();
        boolean diving = isRidden && onAir && wyvern.getDeltaMovement().y < -0.3;
        boolean isSitting = wyvern.isInSittingPose();
        this.ghost = wyvern.isGhost();
        this.alpha = this.ghost ? wyvern.ghostAlpha(f2) : 1.0F;
        int openMouth = wyvern.mouthCounter;
        this.yOffset = isSitting ? 0.4F : 0F;

        // ---- render(): visibility
        g.storage.visible = isChested;
        g.saddle.visible = isSaddled;
        g.chestbelt.visible = isSaddled;
        g.stomachbelt.visible = isSaddled;
        g.mouthrod.visible = isSaddled;
        g.helmetstrap1.visible = isSaddled;
        g.helmetstrap2.visible = isSaddled;
        g.controlrope1.visible = isSaddled && isRidden;
        g.controlrope2.visible = isSaddled && isRidden;
        for (ModelPart p : this.iron) {
            p.visible = armor == 1;
        }
        for (ModelPart p : this.gold) {
            p.visible = armor == 2;
        }
        for (ModelPart p : this.diamond) {
            p.visible = armor == 3;
        }

        // ---- setRotationAngles
        float RLegXRot = Mth.cos((f * 0.6662F) + PI) * 0.8F * f1;
        float LLegXRot = Mth.cos(f * 0.6662F) * 0.8F * f1;

        f3 = f3 % 360F;
        float f10 = 60F;
        if (f3 > f10) {
            f3 = f10;
        }
        if (f3 < -f10) {
            f3 = -f10;
        }

        g.neck2.xRot = -66F / RAD + (f4 / 3F / RAD);
        g.neck1.xRot = 30F / RAD + (f4 * 2 / 3F / RAD);
        g.head.xRot = 45F / RAD;
        g.neck2.yRot = (f3 * 2 / 3F) / RAD;
        g.neck1.yRot = (f3 / 3F) / RAD;
        g.head.yRot = 0F;
        g.head.zRot = 0F;

        if (isRidden) {
            g.neck1.yRot = 0F;
            g.neck2.yRot = 0F;
            if (onAir) {
                g.neck1.xRot = 0F;
                g.neck2.xRot = 0F;
            } else {
                g.neck2.xRot = -66F / RAD + (RLegXRot / 60F);
                g.neck1.xRot = 30F / RAD + (RLegXRot * 2 / 60F);
            }
        }

        g.tail1.xRot = -19F / RAD;
        g.tail2.xRot = -16F / RAD;
        g.tail3.xRot = 7F / RAD;
        g.tail4.xRot = 11F / RAD;
        g.tail5.xRot = 8F / RAD;

        float t = f / 2;
        float A = 0.15F;
        float w = 0.9F;
        float k = 0.6F;
        int i = 0;
        g.tail1.yRot = A * Mth.sin(w * t - k * i++);
        g.tail2.yRot = A * Mth.sin(w * t - k * i++);
        g.tail3.yRot = A * Mth.sin(w * t - k * i++);
        g.tail4.yRot = A * Mth.sin(w * t - k * i++);
        g.tail5.yRot = A * Mth.sin(w * t - k * i);

        float WingSpread;
        if (flapwings && !this.ghost) {
            WingSpread = Mth.cos((f2 * 0.3F) + PI) * 1.2F;
        } else {
            WingSpread = Mth.cos(f * 0.5F) * 0.1F;
        }

        if (onAir || this.ghost) {
            float speedMov = this.ghost ? 0.5F : f1 * 0.5F;
            g.leftuparm.zRot = WingSpread * 2 / 3F;
            g.rightuparm.zRot = -WingSpread * 2 / 3F;
            g.leftlowarm.zRot = WingSpread * 0.1F;
            g.leftfing1a.zRot = WingSpread;
            g.leftfing2a.zRot = WingSpread * 0.8F;
            g.rightlowarm.zRot = -WingSpread * 0.1F;
            g.rightfing1a.zRot = -WingSpread;
            g.rightfing2a.zRot = -WingSpread * 0.8F;

            g.leftuparm.yRot = -10F / RAD - (WingSpread / 2.5F);
            g.leftlowarm.yRot = 15F / RAD + (WingSpread / 2.5F);
            g.leftfing1a.yRot = 70F / RAD;
            g.leftfing2a.yRot = 35F / RAD;
            g.leftfing3a.yRot = -5F / RAD;
            g.rightuparm.yRot = 10F / RAD + (WingSpread / 2.5F);
            g.rightlowarm.yRot = -15F / RAD - (WingSpread / 2.5F);
            g.rightfing1a.yRot = -70F / RAD;
            g.rightfing2a.yRot = -35F / RAD;
            g.rightfing3a.yRot = 5F / RAD;

            g.leftupleg.xRot = speedMov;
            g.leftmidleg.xRot = speedMov;
            g.leftfoot.xRot = 25F / RAD;
            g.lefttoe1.xRot = speedMov;
            g.lefttoe2.xRot = speedMov;
            g.lefttoe3.xRot = speedMov;
            g.rightfoot.xRot = 25F / RAD;
            g.rightupleg.xRot = speedMov;
            g.rightmidleg.xRot = speedMov;
            g.righttoe1.xRot = speedMov;
            g.righttoe2.xRot = speedMov;
            g.righttoe3.xRot = speedMov;
        } else {
            g.leftlowarm.zRot = 0F;
            g.leftfing1a.zRot = 0F;
            g.leftfing2a.zRot = 0F;
            g.rightlowarm.zRot = 0F;
            g.rightfing1a.zRot = 0F;
            g.rightfing2a.zRot = 0F;

            g.leftuparm.zRot = 30F / RAD;
            g.leftuparm.yRot = -60F / RAD + (LLegXRot / 5F);
            g.leftlowarm.yRot = 105F / RAD;
            g.leftfing1a.yRot = -20F / RAD;
            g.leftfing2a.yRot = -26F / RAD;
            g.leftfing3a.yRot = -32F / RAD;

            g.rightuparm.yRot = 60F / RAD - (RLegXRot / 5F);
            g.rightuparm.zRot = -30F / RAD;
            g.rightlowarm.yRot = -105F / RAD;
            g.rightfing1a.yRot = 16F / RAD;
            g.rightfing2a.yRot = 26F / RAD;
            g.rightfing3a.yRot = 32F / RAD;

            g.leftupleg.xRot = -25F / RAD + LLegXRot;
            g.rightupleg.xRot = -25F / RAD + RLegXRot;
            g.leftmidleg.xRot = 0F;
            g.leftlowleg.xRot = 0F;
            g.leftfoot.xRot = 25F / RAD - LLegXRot;
            g.lefttoe1.xRot = LLegXRot;
            g.lefttoe2.xRot = LLegXRot;
            g.lefttoe3.xRot = LLegXRot;
            g.rightmidleg.xRot = 0F;
            g.rightlowleg.xRot = 0F;
            g.rightfoot.xRot = 25F / RAD - RLegXRot;
            g.righttoe1.xRot = RLegXRot;
            g.righttoe2.xRot = RLegXRot;
            g.righttoe3.xRot = RLegXRot;
        }

        if (isSitting) {
            g.leftupleg.xRot = 45F / RAD + LLegXRot;
            g.rightupleg.xRot = 45F / RAD + RLegXRot;
            g.leftmidleg.xRot = 30F;
            g.rightmidleg.xRot = 30F;
            g.neck2.xRot = -36F / RAD + (f4 / 3F / RAD);
            g.neck1.xRot = 30F / RAD + (f4 * 2 / 3F / RAD);
        }

        if (diving) {
            g.leftuparm.zRot = -40F / RAD;
            g.rightuparm.zRot = 40F / RAD;
            g.leftlowarm.zRot = 0F;
            g.leftfing1a.zRot = 0F;
            g.leftfing2a.zRot = 0F;
            g.rightlowarm.zRot = 0F;
            g.rightfing1a.zRot = 0F;
            g.rightfing2a.zRot = 0F;
            g.leftuparm.yRot = -50F / RAD;
            g.leftlowarm.yRot = 30F / RAD;
            g.leftfing1a.yRot = 50F / RAD;
            g.leftfing2a.yRot = 30F / RAD;
            g.leftfing3a.yRot = 10F / RAD;
            g.rightuparm.yRot = 50F / RAD;
            g.rightlowarm.yRot = -30F / RAD;
            g.rightfing1a.yRot = -50F / RAD;
            g.rightfing2a.yRot = -30F / RAD;
            g.rightfing3a.yRot = -10F / RAD;
        }

        if (openMouth != 0) {
            float mouthMov = Mth.cos((openMouth - 15) * 0.11F) * 0.8F;
            g.jaw.xRot = (-10F / RAD) + mouthMov;
            g.leftearskin.yRot = mouthMov;
            g.rightearskin.yRot = -mouthMov;
        } else {
            g.jaw.xRot = -10F / RAD;
            g.leftearskin.yRot = 0F;
            g.rightearskin.yRot = 0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        poseStack.translate(0F, this.yOffset, 0F);
        if (this.ghost) {
            int a = (int) (((color >>> 24) & 0xFF) * Mth.clamp(this.alpha, 0.0F, 1.0F));
            int rgb = (int) (((color >> 16) & 0xFF) * 0.8F) << 16 | (int) (((color >> 8) & 0xFF) * 0.8F) << 8 | (int) ((color & 0xFF) * 0.8F);
            color = (a << 24) | rgb;
        }
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }
}
