package com.critters.client.model;

import com.critters.entity.ActionController;
import com.critters.entity.ActionHolder;
import com.critters.entity.CritterAction;
import com.critters.entity.SnailEntity;
import com.critters.registry.CritterSpecies;
import com.critters.registry.CritterSpecies.Family;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/**
 * One animated model class for every critter. Geometry comes from the generated layer classes;
 * this class animates parts by name. Every critter has
 * <ul>
 *   <li>a <b>walk</b> (or swim / flight) - unique per species, except that all snails share one,</li>
 *   <li>a looping <b>idle</b> - unique per species,</li>
 *   <li>two to four <b>special actions</b> ({@link CritterAction}) played now and then while resting.</li>
 * </ul>
 * Whole-body motion (rearing, rolling over, lunging, bobbing) is done by {@link CritterPoses} in the renderer.
 */
public class CritterModel<T extends Entity> extends HierarchicalModel<T> {
    private static final float PI = (float) Math.PI;
    /** Stand-in for parts a critter doesn't have, so animation code needs no null checks. */
    private static final ModelPart NONE = new ModelPart(List.of(), Map.of());

    private record Leg(ModelPart root, ModelPart femur, ModelPart tibia, int index, boolean left) {
        float side() {
            return left ? 1.0F : -1.0F;
        }
    }

    private final ModelPart root;
    private final CritterSpecies species;
    private final Family family;
    private final List<Leg> legs = new ArrayList<>();
    private final ModelPart body, head, shell, abdomen, sprout;
    private final ModelPart antennaL, antennaR, eyestalkL, eyestalkR, tentacleL, tentacleR;
    private final ModelPart mandibleL, mandibleR, horn;
    private final ModelPart wingL, wingR, elytronL, elytronR, tegmenL, tegmenR, cercusL, cercusR;
    private final ModelPart hindlegL, hindlegR, shinL, shinR;
    private final ModelPart clawL, clawR, handL, handR, pincerL, pincerR;
    private final ModelPart tailBase, tail, finL, finR, dorsal, anal, pelvicL, pelvicR;
    private final ModelPart jaw, lure, nose, earL, earR, rhinophoreL, rhinophoreR, gills, footL, footR;
    private final List<ModelPart> chainSegs = new ArrayList<>();    // tail_base_N / lure segments
    private final List<ModelPart> arms = new ArrayList<>();         // octopus arm roots
    private final List<List<ModelPart>> starArms = new ArrayList<>();
    private final List<ModelPart> tentacles = new ArrayList<>();    // jellyfish tentacles + oral arms
    private final List<ModelPart> spines = new ArrayList<>();
    private final List<ModelPart> gillList = new ArrayList<>();

    public CritterModel(ModelPart root, CritterSpecies species) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.species = species;
        this.family = species.family;
        for (int i = 0; i < 4; i++) {
            for (boolean left : new boolean[]{true, false}) {
                String s = (left ? "l" : "r") + i;
                ModelPart leg = find("leg_" + s);
                if (leg != NONE) {
                    legs.add(new Leg(leg, find("femur_" + s), find("tibia_" + s), i, left));
                }
            }
        }
        body = find("body");
        head = find("head");
        shell = find("shell");
        abdomen = find("abdomen");
        sprout = find("sprout");
        antennaL = find("antenna_l");
        antennaR = find("antenna_r");
        eyestalkL = find("eyestalk_l");
        eyestalkR = find("eyestalk_r");
        tentacleL = find("tentacle_l");
        tentacleR = find("tentacle_r");
        mandibleL = find("mandible_l");
        mandibleR = find("mandible_r");
        horn = find("horn");
        wingL = find("wing_l");
        wingR = find("wing_r");
        elytronL = find("elytron_l");
        elytronR = find("elytron_r");
        tegmenL = find("tegmen_l");
        tegmenR = find("tegmen_r");
        cercusL = find("cercus_l");
        cercusR = find("cercus_r");
        hindlegL = find("hindleg_l");
        hindlegR = find("hindleg_r");
        shinL = find("shin_l");
        shinR = find("shin_r");
        clawL = find("claw_l");
        clawR = find("claw_r");
        handL = find("hand_l");
        handR = find("hand_r");
        pincerL = find("pincer_l");
        pincerR = find("pincer_r");
        tailBase = find("tail_base");
        tail = find("tail_fin");
        finL = find("fin_l");
        finR = find("fin_r");
        dorsal = find("dorsal_fin");
        anal = find("anal_fin");
        pelvicL = find("pelvic_l");
        pelvicR = find("pelvic_r");
        jaw = find("jaw");
        lure = find("lure");
        nose = find("nose");
        earL = find("ear_l");
        earR = find("ear_r");
        rhinophoreL = find("rhinophore_l");
        rhinophoreR = find("rhinophore_r");
        gills = find("gills");
        footL = find("foot_l");
        footR = find("foot_r");
        for (int i = 1; i < 10; i++) {
            ModelPart seg = find("tail_base_" + i);
            if (seg != NONE) {
                chainSegs.add(seg);
            }
        }
        for (int i = 0; i < 8; i++) {
            ModelPart arm = find("arm_" + i);
            if (arm == NONE) {
                break;
            }
            arms.add(arm);
            List<ModelPart> segs = new ArrayList<>();
            for (int k = 0; k < 4; k++) {
                ModelPart seg = find("arm_" + i + "_" + k);
                if (seg != NONE) {
                    segs.add(seg);
                }
            }
            starArms.add(segs);
        }
        for (int i = 0; i < 16; i++) {
            ModelPart t = find("tentacle_" + i);
            if (t != NONE) {
                tentacles.add(t);
            }
        }
        for (int i = 0; i < 4; i++) {
            ModelPart o = find("oral_" + i);
            if (o != NONE) {
                tentacles.add(o);
            }
        }
        for (int i = 0; i < 10; i++) {
            ModelPart sp = find("spine_" + i);
            if (sp != NONE) {
                spines.add(sp);
            }
        }
        for (int i = 0; i < 8; i++) {
            ModelPart g = find("gill_" + i);
            if (g != NONE) {
                gillList.add(g);
            }
        }
    }

    private ModelPart find(String name) {
        return getAnyDescendantWithName(name).orElse(NONE);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    private static float sin(float x) {
        return Mth.sin(x);
    }

    private static float cos(float x) {
        return Mth.cos(x);
    }

    /** 0 -> 1 -> 0 envelope over an action, easing in and out over `fade` ticks. */
    static float env(float t, float duration, float fade) {
        return Mth.clamp(Math.min(t / fade, (duration - t) / fade), 0.0F, 1.0F);
    }

    /** Tripod (insects) or wave (crabs) gait. */
    private void gait(float limbSwing, float amount, float speed, float swing, float lift, boolean wave, float hindLift) {
        for (Leg leg : legs) {
            float phase = wave ? leg.index * 1.1F + (leg.left ? 0.0F : PI) : (leg.index % 2 == 0 ? 0.0F : PI) + (leg.left ? 0.0F : PI);
            float cycle = limbSwing * speed + phase;
            float l = Math.max(0.0F, cos(cycle)) * lift * amount * (leg.index == 2 ? hindLift : 1.0F);
            leg.root.yRot += leg.side() * sin(cycle) * swing * amount;
            leg.femur.zRot -= leg.side() * l;
            leg.tibia.zRot -= leg.side() * l * 0.4F;
        }
    }

    /** Raise and reach a pair of legs forward (positive forward = towards the head). */
    private void legPose(int index, float raise, float forward, float bend) {
        for (Leg leg : legs) {
            if (leg.index == index) {
                leg.femur.zRot -= leg.side() * raise;
                leg.root.yRot += leg.side() * forward;
                leg.tibia.zRot -= leg.side() * bend;
            }
        }
    }

    /** Antenna pose: x = lower (+) / raise (-), out = spread outwards. */
    private void antennae(float x, float out, float xr, float outR) {
        antennaL.xRot += x;
        antennaL.yRot -= out;
        antennaR.xRot += xr;
        antennaR.yRot += outR;
    }

    private void look(float yaw, float pitch) {
        head.yRot += yaw;
        head.xRot += pitch;
    }

    // ------------------------------------------------------------------
    // entry point
    // ------------------------------------------------------------------

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);
        float move = Mth.clamp(limbSwingAmount * 1.6F, 0.0F, 1.0F);
        float idle = 1.0F - move;
        boolean airborne = !entity.onGround() && !entity.isInWater();
        CritterAction action = CritterAction.NONE;
        float at = 0.0F;
        if (entity instanceof ActionHolder holder) {
            ActionController ac = holder.critterActions();
            action = ac.current();
            at = ac.time(age - (int) age);
        }
        float e = action == CritterAction.NONE ? 0.0F : env(at, action.duration, 8.0F);
        if (family != Family.FISH && family != Family.CRAB) {
            look(netHeadYaw * Mth.DEG_TO_RAD * 0.35F * (1.0F - e), headPitch * Mth.DEG_TO_RAD * 0.35F * (1.0F - e));
        }
        switch (family) {
            case SNAIL -> snail(entity, limbSwing, move, idle, age, action, at, e);
            case CRAB -> crab(limbSwing, move, idle, age, action, at, e);
            case FLYER -> firefly(limbSwing, move, idle, age, airborne, action, at, e);
            case HOPPER -> cricket(limbSwing, move, idle, age, airborne, action, at, e);
            case CRAWLER -> ant(limbSwing, move, idle, age, action, at, e);
            case BEETLE -> beetle(limbSwing, move, idle, age, action, at, e);
            case FISH -> {
                fish(entity, limbSwing, move, idle, age, action, at, e);
                seaFish(entity, move, idle, age, action, at, e);
            }
            case SWIMMER -> swimmer(move, idle, age, action, at, e);
            case SEA_CRAWLER -> seaCrawler(limbSwing, move, idle, age, action, at, e);
            case FROG -> frog(entity, limbSwing, move, idle, age, action, at, e);
            case AMPHIBIAN -> salamander(limbSwing, move, idle, age, action, at, e);
        }
    }

    // ------------------------------------------------------------------
    // SNAILS: shared crawl, unique idles
    // ------------------------------------------------------------------

    private void snail(T entity, float ls, float move, float idle, float age, CritterAction action, float at, float e) {
        // crawl (shared by all snails): a muscular wave runs down the foot, the head leads, stalks point ahead
        float wave = sin(ls * 0.8F);
        body.zScale = 1.0F + wave * 0.09F * move;
        head.z -= wave * 0.8F * move;
        head.xRot += cos(ls * 0.8F) * 0.05F * move;
        shell.y += wave * 0.6F * move;
        shell.xRot += cos(ls * 0.8F) * 0.03F * move;
        eyestalkL.xRot -= 0.3F * move;
        eyestalkR.xRot -= 0.3F * move;
        tentacleL.xRot += 0.25F * move + sin(ls * 1.6F) * 0.1F * move;
        tentacleR.xRot += 0.25F * move + sin(ls * 1.6F + 1.0F) * 0.1F * move;

        // unique idles
        switch (species) {
            case GARDEN_SNAIL -> {
                // eyestalks take turns pulling in and stretching back out
                eyestalkL.yScale = 1.0F - 0.4F * Math.max(0.0F, sin(age * 0.05F)) * idle;
                eyestalkR.yScale = 1.0F - 0.4F * Math.max(0.0F, sin(age * 0.05F + PI)) * idle;
                eyestalkL.zRot += sin(age * 0.07F) * 0.1F * idle;
                eyestalkR.zRot -= sin(age * 0.07F) * 0.1F * idle;
            }
            case BANDED_SNAIL -> {
                // swings its head side to side, flicking the lower tentacles
                head.yRot += sin(age * 0.045F) * 0.32F * idle;
                tentacleL.xRot += sin(age * 0.35F) * 0.25F * idle;
                tentacleR.xRot += sin(age * 0.35F + 1.5F) * 0.25F * idle;
                eyestalkL.xRot += sin(age * 0.045F) * 0.12F * idle;
                eyestalkR.xRot -= sin(age * 0.045F) * 0.12F * idle;
            }
            case MOSS_SNAIL -> {
                // drowsy: head low, stalks drooping, the moss sprout swaying, slow breathing
                head.xRot += 0.25F * idle;
                eyestalkL.yScale = 1.0F - 0.3F * idle;
                eyestalkR.yScale = 1.0F - 0.3F * idle;
                eyestalkL.xRot += (0.35F + sin(age * 0.03F) * 0.1F) * idle;
                eyestalkR.xRot += (0.35F + sin(age * 0.03F + 0.7F) * 0.1F) * idle;
                sprout.zRot += sin(age * 0.06F) * 0.25F;
                sprout.xRot += sin(age * 0.045F) * 0.12F;
                shell.yScale = 1.0F + sin(age * 0.05F) * 0.02F * idle;
            }
            case AMETHYST_SNAIL -> {
                // gazes up and around, eyestalks twirling in little circles
                head.xRot -= 0.15F * idle;
                head.yRot += sin(age * 0.03F) * 0.45F * idle;
                eyestalkL.zRot += cos(age * 0.09F) * 0.22F * idle;
                eyestalkL.xRot += sin(age * 0.09F) * 0.22F * idle;
                eyestalkR.zRot -= cos(age * 0.09F + 1.0F) * 0.22F * idle;
                eyestalkR.xRot += sin(age * 0.09F + 1.0F) * 0.22F * idle;
            }
            case MAGMA_SNAIL -> {
                // twitchy and restless, the shell heaving as if it were bubbling
                head.yRot += sin(age * 0.9F) * sin(age * 0.13F) * 0.18F * idle;
                shell.y += sin(age * 0.25F) * 0.7F * idle;
                shell.zRot += sin(age * 0.17F) * 0.03F * idle;
                eyestalkL.xRot += sin(age * 0.7F) * 0.12F * idle;
                eyestalkR.xRot += sin(age * 0.7F + 2.0F) * 0.12F * idle;
            }
            default -> {
            }
        }

        // actions
        switch (action) {
            case SNAIL_LOOK_UP -> {
                head.xRot -= 0.55F * e;
                eyestalkL.yScale += 0.35F * e;
                eyestalkR.yScale += 0.35F * e;
                eyestalkL.xRot -= 0.35F * e;
                eyestalkR.xRot -= 0.35F * e;
                head.yRot += sin(at * 0.1F) * 0.3F * e;
            }
            case SNAIL_TASTE -> {
                head.xRot += 0.5F * e;
                head.z -= 1.5F * e;
                tentacleL.xRot += (0.3F + sin(at * 0.5F) * 0.3F) * e;
                tentacleR.xRot += (0.3F + sin(at * 0.5F + 1.2F) * 0.3F) * e;
                eyestalkL.xRot += 0.35F * e;
                eyestalkR.xRot += 0.35F * e;
            }
            case SNAIL_STRETCH -> {
                float s = sin(Mth.clamp(at / action.duration, 0.0F, 1.0F) * PI);
                body.zScale += 0.2F * s;
                head.z -= 3.5F * s;
                head.xRot -= 0.15F * s;
                shell.z += 0.8F * s;
            }
            default -> {
            }
        }

        // hiding in the shell (when hurt)
        boolean hiding = entity instanceof SnailEntity snail && snail.isHiding();
        head.visible = !hiding;
        body.visible = !hiding;
        if (hiding) {
            shell.y += 6.0F;
            shell.xRot = 0.0F;
        }
    }

    // ------------------------------------------------------------------
    // CRABS
    // ------------------------------------------------------------------

    private void crab(float ls, float move, float idle, float age, CritterAction action, float at, float e) {
        boolean fiddler = species == CritterSpecies.FIDDLER_CRAB;
        // sideways scuttle with a wave gait (the renderer turns the crab 90 degrees)
        gait(ls, move, fiddler ? 2.0F : 1.6F, 0.35F, 0.45F, true, 1.0F);
        if (fiddler) {
            // the big claw is carried high and bobs with each step
            clawR.xRot -= (0.45F + sin(ls * 2.0F) * 0.12F) * move;
            clawL.xRot += sin(ls * 2.0F + 1.0F) * 0.1F * move;
            // idle: the small claw keeps scooping food to the mouth
            float scoop = Math.max(0.0F, sin(age * 0.28F));
            clawL.xRot -= scoop * 0.65F * idle;
            clawL.yRot += scoop * 0.3F * idle;
            pincerL.xRot -= Math.max(0.0F, sin(age * 0.56F)) * 0.4F * idle;
            eyestalkL.zRot += sin(age * 0.05F) * 0.08F;
            eyestalkR.zRot -= sin(age * 0.05F + 1.0F) * 0.08F;
        } else {
            // claws held low and forward while walking, the body rolling side to side (renderer)
            clawL.xRot += 0.15F * move;
            clawR.xRot += 0.15F * move;
            clawL.yRot += sin(ls * 1.6F) * 0.12F * move;
            clawR.yRot += sin(ls * 1.6F + PI) * 0.12F * move;
            // idle: claws slowly open and close, eyestalks swivel
            pincerL.xRot -= Math.max(0.0F, sin(age * 0.12F)) * 0.45F * idle;
            pincerR.xRot -= Math.max(0.0F, sin(age * 0.12F + 1.8F)) * 0.45F * idle;
            clawL.yRot -= sin(age * 0.04F) * 0.1F * idle;
            clawR.yRot += sin(age * 0.04F + 1.0F) * 0.1F * idle;
            eyestalkL.yRot += sin(age * 0.08F) * 0.35F * idle;
            eyestalkR.yRot += sin(age * 0.08F + 2.0F) * 0.35F * idle;
        }

        switch (action) {
            case CRAB_WAVE -> {
                // fiddler courtship wave: big claw raised high and swept up and down
                float w = sin(at * 0.32F);
                clawR.xRot -= (1.0F + w * 0.45F) * e;
                clawR.zRot -= (0.3F + w * 0.2F) * e;
                pincerR.xRot -= Math.max(0.0F, w) * 0.3F * e;
            }
            case CRAB_THREAT -> {
                clawL.xRot -= 0.95F * e;
                clawR.xRot -= 0.95F * e;
                clawL.yRot -= 0.45F * e;
                clawR.yRot += 0.45F * e;
                pincerL.xRot -= 0.55F * e;
                pincerR.xRot -= 0.55F * e;
                eyestalkL.xRot -= 0.2F * e;
                eyestalkR.xRot -= 0.2F * e;
            }
            case CRAB_DIG -> {
                for (Leg leg : legs) {
                    leg.root.yRot += sin(at * 1.3F + leg.index) * 0.35F * e;
                    leg.femur.zRot += leg.side() * 0.15F * e;
                }
                clawL.xRot += 0.3F * e;
                clawR.xRot += 0.3F * e;
            }
            case CRAB_EYES_DOWN -> {
                eyestalkL.xRot += 1.35F * e;
                eyestalkR.xRot += 1.35F * e;
                clawL.yRot += 0.3F * e;
                clawR.yRot -= 0.3F * e;
            }
            default -> {
            }
        }
    }

    // ------------------------------------------------------------------
    // INSECTS
    // ------------------------------------------------------------------

    private void groom(float at, float e) {
        // front legs come up to the head and rub over the face and antennae
        float rub = sin(at * 0.7F);
        legPose(0, (0.75F + rub * 0.15F) * e, (0.55F + rub * 0.2F) * e, 0.3F * e);
        head.xRot += 0.25F * e;
        antennae(Math.max(0.0F, rub) * 0.5F * e, 0.0F, Math.max(0.0F, -rub) * 0.5F * e, 0.0F);
    }

    private void lookAround(float at, float e) {
        float turn = sin(at * 0.13F);
        head.yRot += turn * 0.55F * e;
        antennae(-0.15F * e, turn * 0.2F * e, -0.15F * e, -turn * 0.2F * e);
    }

    private void antennaClean(float at, float duration, float e) {
        // pulls one antenna down through the mouthparts, then the other
        boolean first = at < duration / 2.0F;
        float pull = sin((at % (duration / 2.0F)) / (duration / 2.0F) * PI) * e;
        head.xRot += 0.3F * e;
        head.zRot += (first ? 0.15F : -0.15F) * e;
        if (first) {
            antennaL.xRot += 1.1F * pull;
            antennaL.yRot += 0.4F * pull;
        } else {
            antennaR.xRot += 1.1F * pull;
            antennaR.yRot -= 0.4F * pull;
        }
        legPose(0, 0.4F * e, 0.4F * e, 0.2F * e);
    }

    private void firefly(float ls, float move, float idle, float age, boolean airborne, CritterAction action, float at, float e) {
        if (airborne) {
            // flight: wing covers lifted, hind wings a blur, legs dangling
            elytronL.zRot -= 0.85F;
            elytronR.zRot += 0.85F;
            elytronL.xRot -= 0.25F;
            elytronR.xRot -= 0.25F;
            float flap = -0.2F + sin(age * 2.6F) * 0.65F;
            wingL.zRot += flap;
            wingR.zRot -= flap;
            wingL.yRot -= 0.35F;
            wingR.yRot += 0.35F;
            for (Leg leg : legs) {
                leg.femur.zRot += leg.side() * 0.6F;
                leg.root.yRot += leg.side() * sin(age * 0.2F + leg.index) * 0.1F;
            }
            antennae(-0.2F, 0.15F, -0.2F, 0.15F);
        } else {
            // slow, careful crawl
            gait(ls, move, 1.6F, 0.4F, 0.35F, false, 1.0F);
            head.xRot += sin(ls * 3.2F) * 0.06F * move;
            elytronL.zRot -= 0.06F * move;
            elytronR.zRot += 0.06F * move;
        }
        // idle: antennae trace slow circles, wing covers tick
        antennae(sin(age * 0.1F) * 0.18F * idle, cos(age * 0.1F) * 0.15F * idle,
                sin(age * 0.1F + 1.5F) * 0.18F * idle, cos(age * 0.1F + 1.5F) * 0.15F * idle);
        elytronL.zRot -= Math.max(0.0F, sin(age * 0.07F) - 0.9F) * 1.5F * idle;
        elytronR.zRot += Math.max(0.0F, sin(age * 0.07F) - 0.9F) * 1.5F * idle;

        switch (action) {
            case FIREFLY_WINGS -> {
                elytronL.zRot -= 0.9F * e;
                elytronR.zRot += 0.9F * e;
                float f = sin(at * 0.6F) * 0.35F;
                wingL.zRot += (-0.3F + f) * e;
                wingR.zRot -= (-0.3F + f) * e;
                wingL.yRot -= 0.5F * e;
                wingR.yRot += 0.5F * e;
            }
            case FIREFLY_FLASH -> antennae(-0.3F * e, 0.2F * e, -0.3F * e, 0.2F * e);
            case GROOM -> groom(at, e);
            default -> {
            }
        }
    }

    private void cricket(float ls, float move, float idle, float age, boolean airborne, CritterAction action, float at, float e) {
        if (airborne) {
            // mid-hop: jumping legs kicked out straight
            hindlegL.xRot -= 0.35F;
            hindlegR.xRot -= 0.35F;
            shinL.xRot += 1.15F;
            shinR.xRot += 1.15F;
            antennae(-0.25F, 0.2F, -0.25F, 0.2F);
        } else {
            gait(ls, move, 1.9F, 0.45F, 0.35F, false, 1.0F);
            // jumping legs pump alternately as it walks
            float pump = sin(ls * 1.9F) * 0.22F * move;
            hindlegL.xRot += pump;
            hindlegR.xRot -= pump;
            shinL.xRot -= pump * 0.8F;
            shinR.xRot += pump * 0.8F;
        }
        // idle: the long antennae make big slow sweeps, cerci twitch
        antennae(sin(age * 0.05F) * 0.12F * idle, sin(age * 0.04F) * 0.35F * idle,
                sin(age * 0.05F + 2.0F) * 0.12F * idle, sin(age * 0.04F + 2.5F) * 0.35F * idle);
        cercusL.yRot += sin(age * 0.3F) * Math.max(0.0F, sin(age * 0.05F)) * 0.25F;
        cercusR.yRot -= sin(age * 0.3F + 1.0F) * Math.max(0.0F, sin(age * 0.05F)) * 0.25F;

        switch (action) {
            case CRICKET_CHIRP -> {
                // wings raised and rubbed together (stridulation)
                float buzz = sin(at * 3.1F) * 0.07F;
                tegmenL.zRot -= (0.45F + buzz) * e;
                tegmenR.zRot += (0.45F - buzz) * e;
                tegmenL.xRot -= 0.2F * e;
                tegmenR.xRot -= 0.2F * e;
            }
            case ANTENNA_CLEAN -> antennaClean(at, action.duration, e);
            case LOOK_AROUND -> lookAround(at, e);
            default -> {
            }
        }
    }

    private void ant(float ls, float move, float idle, float age, CritterAction action, float at, float e) {
        // quick, busy gait with antennae tapping the ground ahead
        gait(ls, move, 2.5F, 0.42F, 0.4F, false, 1.0F);
        antennae(sin(ls * 1.25F) * 0.3F * move, 0.0F, sin(ls * 1.25F + PI) * 0.3F * move, 0.0F);
        abdomen.xRot += sin(ls * 2.5F) * 0.06F * move;
        // idle: rapid alternating antenna taps, head turning, jaws working
        antennae(Math.max(0.0F, sin(age * 0.6F)) * 0.45F * idle, sin(age * 0.11F) * 0.15F * idle,
                Math.max(0.0F, sin(age * 0.6F + PI)) * 0.45F * idle, sin(age * 0.11F) * 0.15F * idle);
        head.yRot += sin(age * 0.07F) * 0.2F * idle;
        mandibleL.yRot -= Math.max(0.0F, sin(age * 0.2F)) * 0.25F * idle;
        mandibleR.yRot += Math.max(0.0F, sin(age * 0.2F)) * 0.25F * idle;

        switch (action) {
            case ANT_ALARM -> {
                abdomen.xRot -= 0.65F * e;
                mandibleL.yRot -= 0.45F * e;
                mandibleR.yRot += 0.45F * e;
                antennae(-0.45F * e, 0.3F * e, -0.45F * e, 0.3F * e);
                head.xRot -= 0.25F * e;
            }
            case ANTENNA_CLEAN -> antennaClean(at, action.duration, e);
            case LOOK_AROUND -> lookAround(at, e);
            default -> {
            }
        }
    }

    // ------------------------------------------------------------------
    // BEETLES: every species walks and idles in its own way
    // ------------------------------------------------------------------

    private void beetle(float ls, float move, float idle, float age, CritterAction action, float at, float e) {
        switch (species) {
            case LADYBUG -> {
                gait(ls, move, 2.8F, 0.32F, 0.3F, false, 1.0F);               // quick little scurry
                antennae(sin(ls * 2.8F) * 0.1F * move, 0, sin(ls * 2.8F) * 0.1F * move, 0);
                antennae(sin(age * 0.5F) * Math.max(0.0F, sin(age * 0.07F)) * 0.25F * idle, 0.0F,
                        sin(age * 0.5F + 1.0F) * Math.max(0.0F, sin(age * 0.07F)) * 0.25F * idle, 0.0F);
            }
            case STAG_BEETLE -> {
                gait(ls, move, 1.1F, 0.5F, 0.35F, false, 1.0F);               // heavy, deliberate
                head.xRot += sin(ls * 2.2F) * 0.07F * move;
                mandibleL.yRot += sin(ls * 1.1F) * 0.08F * move;
                mandibleR.yRot += sin(ls * 1.1F) * 0.08F * move;
                // idle: antlers slowly flex open and shut
                float jaw = (0.15F + 0.15F * sin(age * 0.06F)) * idle;
                mandibleL.yRot -= jaw;
                mandibleR.yRot += jaw;
                head.xRot -= 0.08F * idle;
            }
            case RHINOCEROS_BEETLE -> {
                gait(ls, move, 1.0F, 0.45F, 0.4F, false, 1.0F);               // lumbering
                head.xRot += sin(ls * 2.0F) * 0.09F * move;
                head.xRot += sin(age * 0.05F) * 0.14F * idle;                 // idle: slow horn nod
            }
            case DUNG_BEETLE -> {
                gait(ls, move, 1.4F, 0.5F, 0.3F, false, 1.7F);                // head down, high back legs
                head.xRot += 0.25F * move;
                for (Leg leg : legs) {                                         // idle: front legs scrape
                    if (leg.index == 0) {
                        leg.root.yRot += leg.side() * sin(age * 0.3F + (leg.left ? 0 : PI)) * 0.25F * idle;
                    }
                }
                head.xRot += 0.15F * idle;
            }
            case JEWEL_BEETLE -> {
                gait(ls, move, 1.8F, 0.35F, 0.15F, false, 1.0F);              // smooth glide
                antennae(0.25F * move, 0.25F * move, 0.25F * move, 0.25F * move);
                antennae(0.0F, sin(age * 0.07F) * 0.35F * idle, 0.0F, sin(age * 0.07F + 0.5F) * 0.35F * idle);
            }
            case SCARAB_BEETLE -> {
                gait(ls, move, 1.4F, 0.4F, 0.35F, false, 1.0F);               // stately, head bobbing
                head.xRot += sin(ls * 2.8F) * 0.1F * move;
                for (Leg leg : legs) {                                         // idle: front legs tap in turn
                    if (leg.index == 0) {
                        leg.femur.zRot -= leg.side() * Math.max(0.0F, sin(age * 0.2F + (leg.left ? 0 : PI))) * 0.35F * idle;
                    }
                }
            }
            case BOMBARDIER_BEETLE -> {
                float twitch = ls + 0.6F * sin(ls * 0.9F);                    // stop-start, twitchy
                gait(twitch, move, 2.2F, 0.4F, 0.3F, false, 1.0F);
                antennae(sin(age * 0.8F) * 0.12F, sin(age * 0.6F) * 0.12F, sin(age * 0.8F + 1.0F) * 0.12F, sin(age * 0.6F + 1.0F) * 0.12F);
            }
            case WEEVIL -> {
                gait(ls, move, 0.9F, 0.35F, 0.55F, false, 1.0F);              // slow, high steps
                head.xRot += sin(ls * 1.8F) * 0.1F * move;
                head.xRot += sin(age * 0.08F) * 0.15F * idle;                 // idle: snout bobbing
                antennae(sin(age * 0.06F) * 0.1F * idle, 0, sin(age * 0.06F + 1.0F) * 0.1F * idle, 0);
            }
            case GOLIATH_BEETLE -> {
                gait(ls, move, 0.9F, 0.5F, 0.45F, false, 1.0F);               // heavy giant
                head.yRot += sin(age * 0.04F) * 0.12F * idle;                 // idle: horn sway (+ breathing)
                horn.xRot += sin(age * 0.06F) * 0.05F * idle;
            }
            case TIGER_BEETLE -> {
                gait(ls, move, 3.2F, 0.55F, 0.3F, false, 1.0F);               // very fast, long strides
                // idle: alert, snapping head turns and jaw clicks
                float snap = Mth.clamp(sin(age * 0.05F) * 4.0F, -1.0F, 1.0F);
                head.yRot += snap * 0.3F * idle;
                float click = Math.max(0.0F, sin(age * 0.45F)) * Math.max(0.0F, sin(age * 0.04F));
                mandibleL.yRot -= click * 0.3F * idle;
                mandibleR.yRot += click * 0.3F * idle;
            }
            default -> {
            }
        }

        switch (action) {
            case GROOM -> groom(at, e);
            case LOOK_AROUND -> lookAround(at, e);
            case PUSH_UP -> legPose(1, -0.15F * e, 0.0F, -0.1F * e);
            case LADYBUG_PLAY_DEAD -> {
                // on its back (renderer), legs pulled in and twitching
                for (Leg leg : legs) {
                    leg.femur.zRot += leg.side() * 0.7F * e;
                    leg.root.yRot += leg.side() * sin(at * 1.6F + leg.index) * 0.25F * e;
                }
                antennae(0.5F * e, 0, 0.5F * e, 0);
            }
            case STAG_THREAT -> {
                // rears up, antlers flung wide... then snapped shut
                float open = at < 34 ? e : e * Mth.clamp(1.0F - (at - 34) / 4.0F, 0.0F, 1.0F);
                mandibleL.yRot -= 0.55F * open;
                mandibleR.yRot += 0.55F * open;
                mandibleL.xRot -= 0.2F * e;
                mandibleR.xRot -= 0.2F * e;
                head.xRot -= 0.25F * e;
                legPose(0, 0.6F * e, 0.4F * e, 0.2F * e);
            }
            case RHINO_TOSS -> {
                float toss = Math.max(0.0F, sin(at / action.duration * PI * 2.0F));
                head.xRot -= 0.65F * toss;
            }
            case DUNG_DIG -> {
                head.xRot += 0.35F * e;
                for (Leg leg : legs) {
                    if (leg.index == 0) {
                        leg.root.yRot += leg.side() * sin(at * 1.5F + (leg.left ? 0 : PI)) * 0.5F * e;
                        leg.femur.zRot += leg.side() * 0.2F * e;
                    }
                }
            }
            case JEWEL_SHIMMER -> antennae(-0.2F * e, 0.2F * e, -0.2F * e, 0.2F * e);
            case SCARAB_PRAY -> {
                legPose(0, 0.95F * e, 0.6F * e, 0.4F * e);
                head.xRot -= 0.2F * e;
                antennae(-0.3F * e, 0.2F * e, -0.3F * e, 0.2F * e);
            }
            case BOMBARDIER_SPRAY -> {
                antennae(0.4F * e, 0.3F * e, 0.4F * e, 0.3F * e);
                head.xRot += 0.2F * e;
            }
            case WEEVIL_DRILL -> head.xRot += (0.4F + sin(at * 0.9F) * 0.2F) * e;
            case GOLIATH_REAR -> {
                legPose(0, (0.85F + sin(at * 0.4F) * 0.2F) * e, 0.5F * e, 0.4F * e);
                head.xRot -= 0.3F * e;
            }
            case TIGER_POUNCE -> {
                float snap = at > 8 && at < 14 ? 1.0F : 0.0F;
                mandibleL.yRot -= 0.35F * (e - snap) ;
                mandibleR.yRot += 0.35F * (e - snap);
                head.xRot += 0.2F * e;
            }
            default -> {
            }
        }
    }

    // ------------------------------------------------------------------
    // FISH
    // ------------------------------------------------------------------

    private void fish(T entity, float ls, float move, float idle, float age, CritterAction action, float at, float e) {
        boolean water = entity.isInWater();
        float freq;
        float amp;
        switch (species) {
            case GOLDFISH -> {
                freq = 0.45F;
                amp = 0.5F;
            }
            case BETTA -> {
                freq = 0.3F;
                amp = 0.35F;
            }
            case NEON_TETRA -> {
                freq = 1.1F;
                amp = 0.35F;
            }
            case CLOWNFISH -> {
                freq = 0.75F;
                amp = 0.45F;
            }
            case LIONFISH -> {
                freq = 0.22F;
                amp = 0.2F;
            }
            case SEAHORSE -> {
                freq = 0.2F;
                amp = 0.08F;
            }
            case BLOBFISH -> {
                freq = 0.18F;
                amp = 0.25F;
            }
            case ANGLERFISH -> {
                freq = 0.3F;
                amp = 0.3F;
            }
            default -> {
                freq = 0.35F;
                amp = 0.3F;
            }
        }
        if (!water) {
            freq = 1.4F;
            amp = 0.7F;
        }
        float swim = Math.max(move, water ? 0.0F : 1.0F);
        float sway = sin(age * freq);
        tailBase.yRot += -sway * amp * 0.5F * swim;
        tail.yRot += -sin(age * freq - 0.8F) * amp * 0.75F * swim;
        finL.zRot += sin(age * freq * 1.3F) * 0.2F * swim;
        finR.zRot -= sin(age * freq * 1.3F) * 0.2F * swim;

        // unique hovering idles
        switch (species) {
            case GOLDFISH -> {
                finL.yRot += sin(age * 0.9F) * 0.4F * idle;                   // pectorals fanning
                finR.yRot -= sin(age * 0.9F) * 0.4F * idle;
                tail.yRot += sin(age * 0.12F) * 0.15F * idle;
                dorsal.xRot += sin(age * 0.1F) * 0.06F * idle;
            }
            case BETTA -> {
                float b = sin(age * 0.1F);                                     // veils billowing
                tail.yRot += b * 0.25F * idle;
                tail.xRot += sin(age * 0.07F) * 0.08F * idle;
                dorsal.xRot += sin(age * 0.1F + 1.0F) * 0.14F * idle;
                anal.xRot -= sin(age * 0.1F + 2.0F) * 0.14F * idle;
            }
            case NEON_TETRA -> {
                float burst = sin(age * 0.1F) > 0.6F ? 1.0F : 0.2F;           // twitchy little flicks
                tail.yRot += sin(age * 1.4F) * 0.3F * burst * idle;
                finL.zRot += sin(age * 1.1F) * 0.3F * idle;
                finR.zRot -= sin(age * 1.1F) * 0.3F * idle;
            }
            case ANGELFISH -> {
                pelvicL.xRot += sin(age * 0.07F) * 0.12F;                      // trailing filaments sway
                pelvicR.xRot += sin(age * 0.07F + 0.6F) * 0.12F;
                finL.yRot += sin(age * 0.2F) * 0.25F * idle;
                finR.yRot -= sin(age * 0.2F) * 0.25F * idle;
                dorsal.xRot += sin(age * 0.06F) * 0.05F * idle;
                anal.xRot -= sin(age * 0.06F) * 0.05F * idle;
            }
            default -> {
            }
        }

        switch (action) {
            case FISH_DART -> {
                tailBase.yRot += -sin(at * 2.2F) * 0.5F * e;
                tail.yRot += -sin(at * 2.2F - 0.8F) * 0.8F * e;
                finL.zRot -= 0.4F * e;
                finR.zRot += 0.4F * e;
            }
            case FISH_NIBBLE -> {
                finL.yRot += sin(at * 0.8F) * 0.3F * e;
                finR.yRot -= sin(at * 0.8F) * 0.3F * e;
            }
            case BETTA_FLARE -> {
                dorsal.xRot -= 0.35F * e;
                anal.xRot += 0.35F * e;
                tail.yScale = 1.0F + 0.15F * e;
                tail.zScale = 1.0F + 0.1F * e;
                finL.yRot += 0.6F * e;
                finR.yRot -= 0.6F * e;
                head.xScale = 1.0F + 0.15F * e;                                // gill covers flared
                tail.yRot += sin(at * 0.5F) * 0.15F * e;
            }
            case ANGEL_GLIDE -> {
                dorsal.xRot -= 0.12F * e;
                anal.xRot += 0.12F * e;
                tail.yRot += sin(at * 0.15F) * 0.2F * e;
            }
            default -> {
            }
        }
    }

    // ------------------------------------------------------------------
    // SECOND WAVE: more fish
    // ------------------------------------------------------------------

    private void seaFish(T entity, float move, float idle, float age, CritterAction action, float at, float e) {
        switch (species) {
            case CLOWNFISH -> {
                // busy little waggle while hovering, pectorals buzzing
                tailBase.yRot += sin(age * 0.6F) * 0.15F * idle;
                finL.yRot += sin(age * 1.3F) * 0.45F;
                finR.yRot -= sin(age * 1.3F) * 0.45F;
            }
            case LIONFISH -> {
                // hangs in the water slowly opening and closing its huge fans
                float fan = sin(age * 0.05F);
                finL.yRot += fan * 0.3F;
                finR.yRot -= fan * 0.3F;
                for (int i = 0; i < spines.size(); i++) {
                    spines.get(i).xRot += sin(age * 0.07F + i * 0.4F) * 0.05F;
                }
            }
            case SEAHORSE -> {
                // upright, dorsal fin a blur, tail slowly coiling and uncoiling
                dorsal.yRot += sin(age * 2.6F) * 0.45F;
                finL.yRot += sin(age * 2.2F) * 0.4F;
                finR.yRot -= sin(age * 2.2F) * 0.4F;
                head.yRot += sin(age * 0.04F) * 0.3F * idle;
                for (ModelPart seg : chainSegs) {
                    seg.xRot += sin(age * 0.05F) * 0.06F;
                }
            }
            case BLOBFISH -> {
                // a sagging, wobbling blob that slowly "breathes"
                float breathe = sin(age * 0.05F);
                body.yScale = 1.0F + breathe * 0.03F * idle;
                body.xScale = 1.0F - breathe * 0.02F * idle;
                nose.xRot += sin(age * 0.09F) * 0.06F;
                jaw.y += Math.max(0.0F, breathe) * 0.5F;
            }
            case ANGLERFISH -> {
                // the glowing lure dangles and sways in front of the gaping mouth
                lure.xRot += sin(age * 0.08F) * 0.15F;
                lure.zRot += sin(age * 0.05F) * 0.12F;
                jaw.xRot += (0.1F + Math.max(0.0F, sin(age * 0.06F)) * 0.12F) * idle;
            }
            default -> {
            }
        }
        switch (action) {
            case CLOWN_WIGGLE -> {
                tailBase.yRot += sin(at * 1.6F) * 0.5F * e;
                tail.yRot += sin(at * 1.6F - 0.8F) * 0.6F * e;
            }
            case LIONFISH_FAN -> {
                finL.yRot += 0.45F * e;
                finR.yRot -= 0.45F * e;
                finL.xRot -= 0.2F * e;
                finR.xRot -= 0.2F * e;
                for (ModelPart spine : spines) {
                    spine.xRot -= 0.3F * e;
                }
            }
            case SEAHORSE_CURL -> {
                for (ModelPart seg : chainSegs) {
                    seg.xRot -= 0.25F * e;
                }
                head.xRot += 0.15F * e;
            }
            case SEAHORSE_BOW -> head.xRot += Math.abs(sin(at * 0.14F)) * 0.45F * e;
            case BLOB_SIGH -> {
                float sag = sin(Mth.clamp(at / action.duration, 0.0F, 1.0F) * PI);
                body.yScale -= 0.12F * sag;
                body.xScale += 0.08F * sag;
                body.zScale += 0.04F * sag;
                nose.xRot += 0.35F * sag;
                jaw.y += 1.0F * sag;
            }
            case ANGLER_LURE -> {
                lure.xRot += sin(at * 0.3F) * 0.4F * e;
                lure.zRot += cos(at * 0.3F) * 0.25F * e;
                jaw.xRot += 0.2F * e;
            }
            case ANGLER_SNAP -> {
                float open = at < 14 ? Mth.clamp(at / 8.0F, 0.0F, 1.0F) : Mth.clamp(1.0F - (at - 14) / 2.0F, 0.0F, 1.0F);
                jaw.xRot += 0.95F * open;
                head.xRot -= 0.15F * open;
            }
            default -> {
            }
        }
    }

    // ------------------------------------------------------------------
    // SWIMMERS: jellyfish, dumbo octopus
    // ------------------------------------------------------------------

    private void swimmer(float move, float idle, float age, CritterAction action, float at, float e) {
        if (species == CritterSpecies.JELLYFISH) {
            float rate = 0.18F + 0.17F * move + (action == CritterAction.JELLY_PULSE ? 0.25F * e : 0.0F);
            float strength = 0.12F + (action == CritterAction.JELLY_PULSE ? 0.12F * e : 0.0F);
            float pulse = Math.max(0.0F, sin(age * rate));
            body.yScale = 1.0F - pulse * strength;
            body.xScale = 1.0F + pulse * strength * 0.6F;
            body.zScale = 1.0F + pulse * strength * 0.6F;
            for (int i = 0; i < tentacles.size(); i++) {
                ModelPart t = tentacles.get(i);
                t.xRot += sin(age * 0.07F + i * 0.7F) * 0.12F + pulse * 0.1F;
                t.zRot += cos(age * 0.06F + i * 0.5F) * 0.1F;
            }
            if (action == CritterAction.JELLY_SPIN) {
                body.yRot += at * 0.05F * e;
            }
        } else {
            // dumbo octopus: flaps its "ears" to swim, arms trailing like a little skirt
            float flapRate = 0.2F + 0.3F * move + (action == CritterAction.OCTO_FLAP ? 0.35F * e : 0.0F);
            float flap = sin(age * flapRate) * (0.45F + 0.25F * move);
            earL.zRot += flap;
            earR.zRot -= flap;
            for (int i = 0; i < arms.size(); i++) {
                ModelPart arm = arms.get(i);
                arm.xRot += (sin(age * 0.08F + i * 0.8F) * 0.12F - 0.25F * move) ;
                if (action == CritterAction.OCTO_UMBRELLA) {
                    arm.xRot += 0.75F * e;      // arms flared up like an umbrella
                }
            }
            body.xRot += 0.15F * move;
        }
    }

    // ------------------------------------------------------------------
    // SEA FLOOR CRAWLERS: sea star, nudibranch
    // ------------------------------------------------------------------

    private void seaCrawler(float ls, float move, float idle, float age, CritterAction action, float at, float e) {
        if (species == CritterSpecies.SEA_STAR) {
            for (int i = 0; i < starArms.size(); i++) {
                List<ModelPart> segs = starArms.get(i);
                // walking: a slow ripple of tube feet down each arm; idle: arm tips lift one by one
                float lift = Math.max(0.0F, sin(age * 0.03F + i * 1.26F)) * 0.12F * idle;
                for (int k = 0; k < segs.size(); k++) {
                    ModelPart seg = segs.get(k);
                    seg.xRot -= lift * (k > 0 ? 1.0F : 0.0F);
                    seg.xRot += sin(ls * 0.6F + i + k * 0.5F) * 0.04F * move;
                    if (action == CritterAction.STAR_CURL && k > 0) {
                        seg.xRot -= 0.22F * e;
                    }
                    if (action == CritterAction.STAR_WAVE && i == 0) {
                        seg.xRot -= (0.3F + sin(at * 0.3F) * 0.12F) * e;
                    }
                }
            }
        } else {
            // nudibranch: muscular crawl like a snail, sensory horns swaying, gill plume breathing
            body.zScale = 1.0F + sin(ls * 0.8F) * 0.07F * move;
            rhinophoreL.xRot += sin(age * 0.07F) * 0.15F;
            rhinophoreR.xRot += sin(age * 0.07F + 1.2F) * 0.15F;
            rhinophoreL.zRot += cos(age * 0.05F) * 0.1F * idle;
            rhinophoreR.zRot -= cos(age * 0.05F + 0.5F) * 0.1F * idle;
            float breathe = 1.0F + sin(age * 0.12F) * 0.08F;
            gills.xScale = breathe;
            gills.yScale = breathe;
            gills.zScale = breathe;
            if (action == CritterAction.SLUG_RHINO) {
                float tuck = sin(Mth.clamp(at / action.duration, 0.0F, 1.0F) * PI);
                rhinophoreL.yScale = 1.0F - 0.6F * tuck;
                rhinophoreR.yScale = 1.0F - 0.6F * tuck;
            }
            if (action == CritterAction.SLUG_GILLS) {
                for (int i = 0; i < gillList.size(); i++) {
                    gillList.get(i).zRot += (i - 3) * 0.12F * e;
                    gillList.get(i).xRot += sin(at * 0.5F + i) * 0.15F * e;
                }
            }
        }
    }

    // ------------------------------------------------------------------
    // AMPHIBIANS
    // ------------------------------------------------------------------

    private void frog(T entity, float ls, float move, float idle, float age, CritterAction action, float at, float e) {
        boolean airborne = !entity.onGround() && !entity.isInWater();
        boolean swimming = entity.isInWater();
        if (species == CritterSpecies.RAIN_FROG) {
            // waddles on its stubby legs (the body rocks in the renderer), slow round breathing
            gait(ls, move, 2.2F, 0.35F, 0.0F, false, 1.0F);
            float breathe = 1.0F + sin(age * 0.08F) * 0.025F * idle;
            body.xScale = breathe;
            body.zScale = breathe;
        } else {
            gait(ls, move, 2.0F, 0.35F, 0.3F, false, 1.0F);
            float push = sin(ls * 2.0F) * 0.2F * move;
            hindlegL.yRot += push;
            hindlegR.yRot += push;
            if (airborne || swimming) {
                // legs kicked straight out behind for the leap / stroke
                float kick = swimming ? (0.5F + 0.5F * sin(age * 0.5F)) : 1.0F;
                hindlegL.yRot += 0.75F * kick;
                hindlegR.yRot -= 0.75F * kick;
                shinL.yRot -= 0.5F * kick;
                shinR.yRot += 0.5F * kick;
                footL.yRot += 0.5F * kick;
                footR.yRot -= 0.5F * kick;
            }
            // idle: rapid throat breathing
            head.yScale = 1.0F + Math.max(0.0F, sin(age * 0.7F)) * 0.03F * idle;
        }
        switch (action) {
            case FROG_CROAK -> {
                float sac = Math.max(0.0F, sin(at * 0.45F)) * e;
                head.yScale += 0.2F * sac;
                head.xScale += 0.1F * sac;
                head.xRot -= 0.1F * e;
            }
            case FROG_BLINK -> head.yScale -= 0.12F * sin(Mth.clamp(at / action.duration, 0.0F, 1.0F) * PI);
            case RAIN_FROG_PUFF -> {
                float puff = sin(Mth.clamp(at / action.duration, 0.0F, 1.0F) * PI);
                body.xScale += 0.22F * puff;
                body.yScale += 0.18F * puff;
                body.zScale += 0.2F * puff;
            }
            case LOOK_AROUND -> lookAround(at, e);
            default -> {
            }
        }
    }

    private void salamander(float ls, float move, float idle, float age, CritterAction action, float at, float e) {
        // walks with the lizard-like side-to-side wave of the whole body
        gait(ls, move, 1.4F, 0.5F, 0.35F, false, 1.0F);
        float wave = sin(ls * 1.4F) * 0.16F * move;
        body.yRot += wave;
        head.yRot -= wave * 1.3F;
        for (int k = 0; k < chainSegs.size(); k++) {
            chainSegs.get(k).yRot += sin(ls * 1.4F - (k + 1) * 0.7F) * 0.2F * move;
        }
        tailBase.yRot += sin(ls * 1.4F - 0.5F) * 0.2F * move;
        // idle: the tail tip curls lazily, throat pumping
        if (!chainSegs.isEmpty()) {
            chainSegs.get(chainSegs.size() - 1).yRot += sin(age * 0.06F) * 0.3F * idle;
            chainSegs.get(chainSegs.size() - 2).yRot += sin(age * 0.06F - 0.6F) * 0.2F * idle;
        }
        head.yScale = 1.0F + Math.max(0.0F, sin(age * 0.5F)) * 0.025F * idle;
        switch (action) {
            case SALAMANDER_STRETCH -> {
                legPose(0, -0.25F * e, 0.15F * e, -0.15F * e);
                legPose(1, -0.25F * e, -0.15F * e, -0.15F * e);
                head.xRot -= 0.25F * e;
            }
            case SALAMANDER_TAIL -> {
                tailBase.yRot += sin(at * 0.5F) * 0.35F * e;
                for (int k = 0; k < chainSegs.size(); k++) {
                    chainSegs.get(k).yRot += sin(at * 0.5F - (k + 1) * 0.6F) * 0.3F * e;
                }
            }
            case LOOK_AROUND -> lookAround(at, e);
            default -> {
            }
        }
    }
}
