package com.menagerie.entity.wyvern;

import com.menagerie.registry.ModEntities;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * A wyvern egg lying in the world. Ported from Mo' Creatures' {@code MoCEntityEgg} (DrZhark, GPL-3.0):
 * out of water, a 1-in-20 chance each tick advances the hatch counter; it wobbles every 5 steps and
 * hatches at 30 (about half a minute), and the wyvern is tamed to the nearest player within 24 blocks.
 * Right-click picks the egg back up; hitting it pops it off as an item.
 */
public class WyvernEgg extends Entity {
    private static final EntityDataAccessor<Integer> DATA_KIND = SynchedEntityData.defineId(WyvernEgg.class, EntityDataSerializers.INT);
    private static final byte EVENT_WOBBLE = 70;
    private int hatchCounter;
    /** Client: ticks left of the wobble animation. */
    public int wobbleTicks;

    public WyvernEgg(EntityType<? extends WyvernEgg> type, Level level) {
        super(type, level);
    }

    @Nullable
    public static WyvernEgg create(Level level, int kind, double x, double y, double z) {
        WyvernEgg egg = ModEntities.WYVERN_EGG.create(level);
        if (egg != null) {
            egg.setKind(kind);
            egg.setPos(x, y, z);
            egg.setDeltaMovement((level.random.nextFloat() - 0.5F) * 0.2F, 0.1F, (level.random.nextFloat() - 0.5F) * 0.2F);
        }
        return egg;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_KIND, Wyvern.JUNGLE);
    }

    public int getKind() {
        return this.entityData.get(DATA_KIND);
    }

    public void setKind(int kind) {
        this.entityData.set(DATA_KIND, kind);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setKind(tag.getInt("Kind"));
        this.hatchCounter = tag.getInt("Hatch");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Kind", getKind());
        tag.putInt("Hatch", this.hatchCounter);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 v = getDeltaMovement();
        if (!isNoGravity()) {
            v = v.add(0.0, -0.04, 0.0);
        }
        move(MoverType.SELF, v);
        v = getDeltaMovement().scale(0.98);
        if (onGround()) {
            v = v.multiply(0.6, -0.5, 0.6);
        }
        setDeltaMovement(v);

        if (this.wobbleTicks > 0) {
            this.wobbleTicks--;
        }
        if (level() instanceof ServerLevel server && !isInWater() && this.random.nextInt(20) == 0) {
            this.hatchCounter++;
            if (this.hatchCounter % 5 == 0) {
                server.broadcastEntityEvent(this, EVENT_WOBBLE);
                playSound(SoundEvents.TURTLE_EGG_CRACK, 0.6F, 1.2F);
            }
            if (this.hatchCounter >= 30) {
                Player owner = server.getNearestPlayer(this, 24.0);
                server.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, WyvernEggItem.stackOf(getKind())),
                        getX(), getY() + 0.3, getZ(), 15, 0.2, 0.2, 0.2, 0.05);
                playSound(SoundEvents.TURTLE_EGG_HATCH, 1.0F, 1.0F);
                Wyvern.hatch(server, blockPosition(), getKind(), owner);
                discard();
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_WOBBLE) {
            this.wobbleTicks = 10;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!level().isClientSide) {
            ItemStack egg = WyvernEggItem.stackOf(getKind());
            if (!player.getInventory().add(egg)) {
                spawnAtLocation(egg);
            }
            discard();
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        }
        if (!level().isClientSide && isAlive()) {
            spawnAtLocation(WyvernEggItem.stackOf(getKind()));
            discard();
        }
        return true;
    }

    @Override
    public boolean isPickable() {
        return !isRemoved();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public ItemStack getPickResult() {
        return WyvernEggItem.stackOf(getKind());
    }
}
