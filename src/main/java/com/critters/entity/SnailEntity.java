package com.critters.entity;

import com.critters.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Snails crawl slowly, climb up walls like spiders, leave a slime trail behind them and hide in
 * their shell when hurt.
 */
public class SnailEntity extends CritterEntity {
    private static final EntityDataAccessor<Byte> FLAGS = SynchedEntityData.defineId(SnailEntity.class, EntityDataSerializers.BYTE);
    private static final int FLAG_CLIMBING = 1;
    private static final int FLAG_HIDING = 2;

    private int hideTicks;
    private BlockPos lastTrailPos = BlockPos.ZERO;

    public SnailEntity(EntityType<? extends SnailEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLAGS, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.02F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 4.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            setFlag(FLAG_CLIMBING, this.horizontalCollision);
            if (this.hideTicks > 0) {
                this.hideTicks--;
            }
            setFlag(FLAG_HIDING, this.hideTicks > 0);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && !isHiding() && this.tickCount % 4 == 0) {
            leaveTrail();
        }
    }

    private void leaveTrail() {
        BlockPos pos = this.blockPosition();
        if (pos.equals(this.lastTrailPos)) {
            return;
        }
        BlockState current = this.level().getBlockState(pos);
        if (!current.isAir() && !current.is(ModBlocks.SLIME_TRAIL)) {
            return;
        }
        Direction face = null;
        if (this.onGround()) {
            face = Direction.DOWN;
        } else if (isClimbing()) {
            Direction facing = this.getDirection();
            if (attachable(pos, facing)) {
                face = facing;
            } else {
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    if (attachable(pos, dir)) {
                        face = dir;
                        break;
                    }
                }
            }
        }
        if (face == null) {
            return;
        }
        BlockState trail = ModBlocks.SLIME_TRAIL.getStateForPlacement(current, this.level(), pos, face);
        if (trail != null) {
            this.level().setBlock(pos, trail, 3);
            this.lastTrailPos = pos;
        }
    }

    private boolean attachable(BlockPos pos, Direction dir) {
        BlockPos other = pos.relative(dir);
        return MultifaceBlock.canAttachTo(this.level(), dir, other, this.level().getBlockState(other));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isHiding()) {
            amount *= 0.5F; // the shell protects it
        }
        boolean hurt = super.hurt(source, amount);
        if (hurt && !this.level().isClientSide && this.isAlive()) {
            this.hideTicks = 80 + this.random.nextInt(60);
            this.getNavigation().stop();
            setFlag(FLAG_HIDING, true);
        }
        return hurt;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || isHiding();
    }

    @Override
    public boolean onClimbable() {
        return isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(FLAGS) & FLAG_CLIMBING) != 0;
    }

    public boolean isHiding() {
        return (this.entityData.get(FLAGS) & FLAG_HIDING) != 0;
    }

    private void setFlag(int flag, boolean value) {
        byte b = this.entityData.get(FLAGS);
        byte n = value ? (byte) (b | flag) : (byte) (b & ~flag);
        if (n != b) {
            this.entityData.set(FLAGS, n);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SLIME_HURT_SMALL;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH_SMALL;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SLIME_SQUISH_SMALL, 0.04F, 1.5F);
    }
}
