package com.critters.entity;

import com.critters.registry.CritterSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for every land critter. Used directly for crawling insects and beetles.
 */
public class CritterEntity extends PathfinderMob implements ActionHolder {
    private static final EntityDataAccessor<Byte> ACTION = SynchedEntityData.defineId(CritterEntity.class, EntityDataSerializers.BYTE);

    private final ActionController actions = new ActionController(this, ACTION);

    public CritterEntity(EntityType<? extends CritterEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public ActionController critterActions() {
        return actions;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTION, (byte) 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (ACTION.equals(key)) {
            actions.onSynced();
        }
    }

    /** May this critter start a special action right now? */
    protected boolean canStartAction() {
        return this.onGround() && this.getNavigation().isDone() && this.getTarget() == null
                && this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4 && this.hurtTime == 0;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            actions.serverTick(canStartAction());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            actionEffects();
        }
    }

    /** Particles that go with some special actions. */
    private void actionEffects() {
        CritterAction action = actions.current();
        int t = (int) actions.time(0.0F);
        switch (action) {
            case BOMBARDIER_SPRAY -> {
                if (t >= 10 && t <= 20) {
                    Vec3 back = Vec3.directionFromRotation(0.0F, this.yBodyRot).scale(-0.22);
                    this.level().addParticle(ParticleTypes.WHITE_SMOKE, this.getX() + back.x, this.getY() + 0.1,
                            this.getZ() + back.z, back.x * 0.3, 0.02, back.z * 0.3);
                }
            }
            case DUNG_DIG, CRAB_DIG -> {
                if (t % 3 == 0 && t > 8 && t < action.duration - 8) {
                    BlockState below = this.getBlockStateOn();
                    if (!below.isAir()) {
                        Vec3 front = Vec3.directionFromRotation(0.0F, this.yBodyRot).scale(0.15);
                        this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, below),
                                this.getX() + front.x, this.getY() + 0.05, this.getZ() + front.z,
                                (this.random.nextDouble() - 0.5) * 0.1, 0.12, (this.random.nextDouble() - 0.5) * 0.1);
                    }
                }
            }
            default -> {
            }
        }
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || actions.isBusy();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && !this.level().isClientSide) {
            actions.cancel();
        }
        return hurt;
    }

    public static AttributeSupplier.Builder createAttributes(CritterSpecies species) {
        AttributeSupplier.Builder builder = Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, species.health)
                .add(Attributes.MOVEMENT_SPEED, species.speed);
        if (species.family == CritterSpecies.Family.FLYER) {
            builder.add(Attributes.FLYING_SPEED, 0.45);
        }
        return builder;
    }

    /** Critters spawn on any block a mob could normally spawn on. */
    public static boolean checkCritterSpawnRules(EntityType<? extends Mob> type, LevelAccessor level,
                                                 MobSpawnType reason, BlockPos pos, RandomSource random) {
        return Mob.checkMobSpawnRules(type, level, reason, pos, random)
                && level.getFluidState(pos).isEmpty();
    }

    @Nullable
    public CritterSpecies species() {
        return CritterSpecies.byType(getType());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.6));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 4.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    /** Tiny critters don't get hurt by falling. */
    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 1.4F;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.05F, 1.6F);
    }

    @Override
    public int getMaxHeadXRot() {
        return 20;
    }
}
