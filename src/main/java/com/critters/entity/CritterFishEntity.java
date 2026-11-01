package com.critters.entity;

import com.critters.registry.CritterSpecies;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Aquarium fish. Caught with a water bucket like vanilla fish. */
public class CritterFishEntity extends AbstractSchoolingFish implements ActionHolder {
    private static final EntityDataAccessor<Byte> ACTION = SynchedEntityData.defineId(CritterFishEntity.class, EntityDataSerializers.BYTE);

    private final ActionController actions = new ActionController(this, ACTION);

    public CritterFishEntity(EntityType<? extends CritterFishEntity> type, Level level) {
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

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            actions.serverTick(this.isInWater() && this.getNavigation().isDone() && this.hurtTime == 0);
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

    /** Deep-sea species: anywhere in water well below sea level. */
    public static boolean checkDeepWaterSpawnRules(EntityType<? extends net.minecraft.world.entity.Mob> type,
                                                   net.minecraft.world.level.LevelAccessor level,
                                                   net.minecraft.world.entity.MobSpawnType reason,
                                                   net.minecraft.core.BlockPos pos, net.minecraft.util.RandomSource random) {
        return level.getFluidState(pos).is(net.minecraft.tags.FluidTags.WATER)
                && level.getFluidState(pos.above()).is(net.minecraft.tags.FluidTags.WATER)
                && pos.getY() < level.getSeaLevel() - 14;
    }

    private CritterSpecies species() {
        CritterSpecies s = CritterSpecies.byType(getType());
        return s != null ? s : CritterSpecies.GOLDFISH;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(species().container());
    }

    @Override
    public int getMaxSchoolSize() {
        return species().schoolSize();
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.TROPICAL_FISH_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.TROPICAL_FISH_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.TROPICAL_FISH_HURT;
    }
}
