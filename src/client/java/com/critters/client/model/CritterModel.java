package com.critters.client.model;

import com.critters.entity.SnailEntity;
import com.critters.registry.CritterSpecies.Family;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

/**
 * One model class for every critter. The geometry comes from {@link CritterLayers}; this class
 * animates the parts it finds by name (legs, antennae, wings, claws, fins, ...).
 */
public class CritterModel<T extends Entity> extends HierarchicalModel<T> {
    private static final float PI = (float) Math.PI;

    private record Leg(ModelPart part, int index, boolean left) {
    }

    private final ModelPart root;
    private final Family family;
    private final List<Leg> legs = new ArrayList<>();
    @Nullable private final ModelPart body, head, shell;
    @Nullable private final ModelPart antennaL, antennaR, eyestalkL, eyestalkR;
    @Nullable private final ModelPart wingL, wingR;
    @Nullable private final ModelPart hindlegL, hindlegR, shinL, shinR;
    @Nullable private final ModelPart clawL, clawR, pincerL, pincerR;
    @Nullable private final ModelPart tail, finL, finR, dorsal;

    public CritterModel(ModelPart root, Family family) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.family = family;
        for (int i = 0; i < 3; i++) {
            ModelPart l = find("leg_l" + i);
            ModelPart r = find("leg_r" + i);
            if (l != null) {
                legs.add(new Leg(l, i, true));
            }
            if (r != null) {
                legs.add(new Leg(r, i, false));
            }
        }
        body = find("body");
        head = find("head");
        shell = find("shell");
        antennaL = find("antenna_l");
        antennaR = find("antenna_r");
        eyestalkL = find("eyestalk_l");
        eyestalkR = find("eyestalk_r");
        wingL = find("wing_l");
        wingR = find("wing_r");
        hindlegL = find("hindleg_l");
        hindlegR = find("hindleg_r");
        shinL = find("shin_l");
        shinR = find("shin_r");
        clawL = find("claw_l");
        clawR = find("claw_r");
        pincerL = find("pincer_l");
        pincerR = find("pincer_r");
        tail = find("tail_fin");
        finL = find("fin_l");
        finR = find("fin_r");
        dorsal = find("dorsal_fin");
    }

    @Nullable
    private ModelPart find(String name) {
        return getAnyDescendantWithName(name).orElse(null);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);
        boolean airborne = !entity.onGround() && !entity.isInWater();
        float amount = Math.min(1.0F, limbSwingAmount);

        // ---- legs: alternating tripod gait ----
        float legSpeed = family == Family.CRAB ? 1.3F : 1.7F;
        for (Leg leg : legs) {
            float phase = (leg.index % 2 == 0 ? 0.0F : PI) + (leg.left ? 0.0F : PI);
            float cycle = limbSwing * legSpeed + phase;
            float swing = Mth.sin(cycle) * 0.45F * amount;
            float lift = Math.max(0.0F, Mth.cos(cycle)) * 0.35F * amount;
            leg.part.yRot += leg.left ? swing : -swing;
            leg.part.zRot += leg.left ? -lift : lift;
            if (family == Family.FLYER && airborne) {
                leg.part.zRot += leg.left ? 0.5F : -0.5F; // legs dangle while flying
            }
        }

        // ---- head looks around ----
        if (head != null && family != Family.FISH) {
            head.yRot += netHeadYaw * Mth.DEG_TO_RAD * 0.4F;
            head.xRot += headPitch * Mth.DEG_TO_RAD * 0.4F;
        }

        // ---- feelers ----
        if (antennaL != null && antennaR != null) {
            antennaL.yRot += Mth.sin(ageInTicks * 0.13F) * 0.12F;
            antennaL.xRot += Mth.sin(ageInTicks * 0.09F) * 0.1F;
            antennaR.yRot -= Mth.sin(ageInTicks * 0.13F + 1.3F) * 0.12F;
            antennaR.xRot += Mth.sin(ageInTicks * 0.09F + 1.3F) * 0.1F;
        }
        if (eyestalkL != null && eyestalkR != null) {
            eyestalkL.xRot += Mth.sin(ageInTicks * 0.07F) * 0.15F;
            eyestalkL.zRot += Mth.sin(ageInTicks * 0.05F) * 0.1F;
            eyestalkR.xRot += Mth.sin(ageInTicks * 0.07F + 2.0F) * 0.15F;
            eyestalkR.zRot -= Mth.sin(ageInTicks * 0.05F + 2.0F) * 0.1F;
        }

        // ---- wings ----
        if (wingL != null && wingR != null) {
            if (airborne) {
                float flap = -0.2F + Mth.sin(ageInTicks * 2.6F) * 0.65F;
                wingL.zRot += flap;
                wingR.zRot -= flap;
                wingL.yRot -= 0.35F;
                wingR.yRot += 0.35F;
            }
        }

        // ---- cricket jumping legs ----
        if (hindlegL != null && hindlegR != null) {
            if (airborne) {
                hindlegL.xRot -= 0.35F;
                hindlegR.xRot -= 0.35F;
                if (shinL != null && shinR != null) {
                    shinL.xRot += 1.1F;
                    shinR.xRot += 1.1F;
                }
            } else {
                float s = Mth.sin(limbSwing * 1.7F) * 0.2F * amount;
                hindlegL.xRot += s;
                hindlegR.xRot -= s;
            }
        }

        // ---- crab claws ----
        if (clawL != null && clawR != null) {
            clawL.yRot += Mth.sin(ageInTicks * 0.08F) * 0.08F;
            clawR.yRot -= Mth.sin(ageInTicks * 0.08F + 1.0F) * 0.08F;
            clawL.xRot += Mth.sin(limbSwing * 1.3F) * 0.25F * amount;
            clawR.xRot += Mth.sin(limbSwing * 1.3F + PI) * 0.25F * amount;
        }
        if (pincerL != null && pincerR != null) {
            float snip = Math.max(0.0F, Mth.sin(ageInTicks * 0.2F)) * 0.25F;
            pincerL.xRot -= snip;
            pincerR.xRot -= Math.max(0.0F, Mth.sin(ageInTicks * 0.2F + 2.0F)) * 0.25F;
        }

        // ---- fish ----
        if (family == Family.FISH) {
            float strength = entity.isInWater() ? 1.0F : 1.7F;
            if (tail != null) {
                tail.yRot = -strength * 0.45F * Mth.sin(0.6F * ageInTicks);
            }
            if (finL != null && finR != null) {
                finL.zRot += Mth.sin(ageInTicks * 0.35F) * 0.25F;
                finR.zRot -= Mth.sin(ageInTicks * 0.35F) * 0.25F;
            }
            if (dorsal != null) {
                dorsal.xRot += Mth.sin(ageInTicks * 0.25F) * 0.08F;
            }
        }

        // ---- snails: stretch while crawling, hide in the shell ----
        if (family == Family.SNAIL) {
            boolean hiding = entity instanceof SnailEntity snail && snail.isHiding();
            if (head != null) {
                head.visible = !hiding;
            }
            if (body != null) {
                body.visible = !hiding;
                body.zScale = 1.0F + Mth.sin(limbSwing * 0.8F) * 0.08F * amount;
            }
            if (shell != null) {
                if (hiding) {
                    shell.y += 2.0F;
                    shell.xRot = 0.0F;
                } else {
                    shell.y += Mth.sin(limbSwing * 0.8F) * 0.2F * amount;
                }
            }
        }
    }
}
