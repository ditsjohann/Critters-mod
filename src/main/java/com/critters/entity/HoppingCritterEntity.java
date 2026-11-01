package com.critters.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Crickets: walk around and frequently hop forward. */
public class HoppingCritterEntity extends CritterEntity {

    public HoppingCritterEntity(EntityType<? extends HoppingCritterEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.onGround()) {
            boolean moving = !this.getNavigation().isDone();
            if ((moving && this.random.nextInt(10) == 0) || this.random.nextInt(240) == 0) {
                this.getJumpControl().jump();
            }
        }
    }

    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        Vec3 forward = Vec3.directionFromRotation(0.0F, this.getYRot()).scale(0.22);
        this.setDeltaMovement(this.getDeltaMovement().add(forward.x, 0.0, forward.z));
    }

    @Override
    protected float getJumpPower() {
        return super.getJumpPower() * 1.15F;
    }
}
