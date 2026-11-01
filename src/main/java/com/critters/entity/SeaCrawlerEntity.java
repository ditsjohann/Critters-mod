package com.critters.entity;

import com.critters.registry.CritterSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Slow sea-floor crawlers (sea star, nudibranch). They breathe underwater, creep along the bottom and
 * can be scooped up with a water bucket like fish, so they can live in an aquarium.
 */
public class SeaCrawlerEntity extends CrabEntity implements Bucketable {
    private boolean fromBucket;

    public SeaCrawlerEntity(EntityType<? extends SeaCrawlerEntity> type, Level level) {
        super(type, level);
    }

    /** Spawns on a solid sea floor, under water. */
    public static boolean checkSeaFloorSpawnRules(EntityType<? extends Mob> type, LevelAccessor level, MobSpawnType reason,
                                                  BlockPos pos, RandomSource random) {
        BlockPos below = pos.below();
        return level.getFluidState(pos).is(FluidTags.WATER)
                && level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)
                && pos.getY() < level.getSeaLevel();
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public boolean fromBucket() {
        return fromBucket;
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.fromBucket = fromBucket;
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
    }

    @Override
    public ItemStack getBucketItemStack() {
        CritterSpecies species = CritterSpecies.byType(getType());
        return species != null ? new ItemStack(species.container()) : ItemStack.EMPTY;
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("FromBucket", fromBucket);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        fromBucket = tag.getBoolean("FromBucket");
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || fromBucket;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return !fromBucket && !hasCustomName();
    }

    @Override
    protected boolean canStartAction() {
        return this.getNavigation().isDone() && this.hurtTime == 0;
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
    }
}
