package com.critters.entity;

import com.critters.registry.CritterSpecies;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Aquarium fish. Caught with a water bucket like vanilla fish. */
public class CritterFishEntity extends AbstractSchoolingFish {

    public CritterFishEntity(EntityType<? extends CritterFishEntity> type, Level level) {
        super(type, level);
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
