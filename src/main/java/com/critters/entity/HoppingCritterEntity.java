package com.critters.entity;

import com.critters.registry.CritterSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/** Crickets and frogs: walk around and frequently hop forward (the round rain frog only waddles). */
public class HoppingCritterEntity extends CritterEntity {

    public HoppingCritterEntity(EntityType<? extends HoppingCritterEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.onGround() && species() != CritterSpecies.RAIN_FROG) {
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

    private boolean isFrog() {
        CritterSpecies s = species();
        return s != null && s.family == CritterSpecies.Family.FROG;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return isFrog() && this.random.nextInt(3) == 0 ? SoundEvents.FROG_AMBIENT : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return isFrog() ? SoundEvents.FROG_HURT : super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return isFrog() ? SoundEvents.FROG_DEATH : super.getDeathSound();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (isFrog()) {
            this.playSound(SoundEvents.FROG_STEP, 0.08F, 1.5F);
        } else {
            super.playStepSound(pos, state);
        }
    }
}
