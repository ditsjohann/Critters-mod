package com.menagerie.entity.bigcat;

import com.menagerie.registry.ModItems;
import com.menagerie.registry.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Lions, tigers, panthers, leopards and their hybrids.
 *
 * <p>Ported from Mo' Creatures' {@code MoCEntityBigCat} and its species classes (DrZhark, GPL-3.0):
 * wild adults stalk players and hunt smaller animals; cubs can be raised by feeding them meat and
 * then giving them a medallion; tamed cats wear a collar, can be ridden with a saddle, carry a chest,
 * sit on command with the whip, and may return as a ghost cub when they die. Lions, tigers and
 * ligers grow wings from essence of light, panthers from essence of darkness. Different species
 * cross-breed into ligers, liards, lithers, leogers, panthgers and panthards.
 */
public class BigCat extends TamableAnimal implements InventoryCarrier {
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(BigCat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_FLAGS = SynchedEntityData.defineId(BigCat.class, EntityDataSerializers.BYTE);
    private static final int FLAG_SADDLED = 1, FLAG_CHESTED = 2, FLAG_COLLAR = 4, FLAG_GHOST = 8, FLAG_HUNTING = 16, FLAG_FED = 32;
    private static final byte EVENT_OPEN_MOUTH = 64;

    private final SimpleContainer inventory = new SimpleContainer(18);

    // client animation counters, read by the model
    public int mouthCounter;
    public int tailCounter;
    public int wingFlapCounter;

    public BigCat(EntityType<? extends BigCat> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.FOLLOW_RANGE, 8.0)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    public BigCatSpecies species() {
        BigCatSpecies s = BigCatSpecies.of(getType());
        return s != null ? s : BigCatSpecies.LION;
    }

    // ------------------------------------------------------------------ data

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, 1);
        builder.define(DATA_FLAGS, (byte) 0);
    }

    public int getVariant() {
        return this.entityData.get(DATA_VARIANT);
    }

    public void setVariant(int variant) {
        if (!species().isValidVariant(variant)) {
            variant = species().variants()[0];
        }
        this.entityData.set(DATA_VARIANT, variant);
        BigCatSpecies s = species();
        setBase(getAttribute(Attributes.MAX_HEALTH), s.maxHealth(variant));
        setBase(getAttribute(Attributes.ATTACK_DAMAGE), s.attackDamage(variant));
        setBase(getAttribute(Attributes.FOLLOW_RANGE), s.attackRange(variant));
        if (getHealth() > getMaxHealth()) {
            setHealth(getMaxHealth());
        }
    }

    private static void setBase(@Nullable AttributeInstance attribute, double value) {
        if (attribute != null) {
            attribute.setBaseValue(value);
        }
    }

    private boolean flag(int f) {
        return (this.entityData.get(DATA_FLAGS) & f) != 0;
    }

    private void setFlag(int f, boolean on) {
        byte b = this.entityData.get(DATA_FLAGS);
        this.entityData.set(DATA_FLAGS, (byte) (on ? b | f : b & ~f));
    }

    public boolean isSaddled() { return flag(FLAG_SADDLED); }
    public boolean hasChest() { return flag(FLAG_CHESTED); }
    public boolean hasCollar() { return flag(FLAG_COLLAR); }
    public boolean isGhost() { return flag(FLAG_GHOST); }
    public boolean isHunting() { return flag(FLAG_HUNTING); }
    private boolean hasEaten() { return flag(FLAG_FED); }

    public boolean isFlyer() {
        return species().isFlyer(getVariant());
    }

    public boolean hasMane() {
        return species().hasMane(getVariant()) && !isBaby();
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", getVariant());
        tag.putByte("CatFlags", this.entityData.get(DATA_FLAGS));
        writeInventoryToTag(tag, registryAccess());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Variant")) {
            boolean wasFullHealth = getHealth() >= getMaxHealth();
            setVariant(tag.getInt("Variant"));
            if (wasFullHealth) {
                setHealth(getMaxHealth());
            }
        }
        this.entityData.set(DATA_FLAGS, tag.getByte("CatFlags"));
        readInventoryFromTag(tag, registryAccess());
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.menagerie." + species().id + "." + species().variantKey(getVariant()));
    }

    // ------------------------------------------------------------------ AI

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3, true));
        this.goalSelector.addGoal(3, new EatDroppedMeatGoal(this));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        // wild adults stalk players (the original's EntityAINearestAttackableTargetMoC)
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                target -> canHunt() && !isTame() && level().getDifficulty() != Difficulty.PEACEFUL));
        // and hunt smaller animals when hungry (the original's EntityAIHunt)
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Animal.class, 10, true, false,
                target -> canHunt() && isHunting() && isPrey(target)));
    }

    /** Cubs never attack; they must be 80% grown, as in the original. */
    private boolean canHunt() {
        return !isBaby() && !isVehicle() && !isGhost();
    }

    private boolean isPrey(LivingEntity target) {
        if (target instanceof BigCat cat && cat.species() == species()) {
            return false;
        }
        if (target instanceof TamableAnimal pet && pet.isTame()) {
            return false;
        }
        float max = species().maxPreySize;
        return target.getBbWidth() < max && target.getBbHeight() < max;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return isMeat(stack);
    }

    public static boolean isMeat(ItemStack stack) {
        return stack.is(ItemTags.MEAT) || stack.is(ItemTags.FISHES);
    }

    // ------------------------------------------------------------------ spawning and breeding

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                                        @Nullable SpawnGroupData spawnGroupData) {
        boolean snowy = level.getBiome(blockPosition()).is(BiomeTags.SPAWNS_SNOW_FOXES)
                || level.getBiome(blockPosition()).is(BiomeTags.IS_MOUNTAIN);
        setVariant(species().wildVariant(level.getRandom(), snowy));
        setHealth(getMaxHealth());
        if (spawnType != MobSpawnType.BREEDING && level.getRandom().nextInt(4) == 0) {
            setAge(-24000); // a quarter of wild cats are cubs, as in the original
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public boolean canMate(Animal other) {
        return other != this && other instanceof BigCat mate && isTame() && mate.isTame()
                && isInLove() && mate.isInLove() && !isGhost() && !mate.isGhost()
                && BigCatSpecies.offspringSpecies(species(), getVariant(), mate.species(), mate.getVariant()) != null;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        if (!(other instanceof BigCat mate)) {
            return null;
        }
        BigCatSpecies childSpecies = BigCatSpecies.offspringSpecies(species(), getVariant(), mate.species(), mate.getVariant());
        if (childSpecies == null) {
            return null;
        }
        BigCat cub = childSpecies.type().create(level);
        if (cub != null) {
            cub.setVariant(BigCatSpecies.offspringVariant(childSpecies, getVariant(), mate.getVariant(), this.random));
            cub.setHealth(cub.getMaxHealth());
            if (getOwnerUUID() != null) {
                cub.setOwnerUUID(getOwnerUUID());
                cub.setTame(true, true);
                cub.setFlag(FLAG_COLLAR, true);
            }
        }
        return cub;
    }

    // ------------------------------------------------------------------ interaction

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean client = level().isClientSide;
        BigCatSpecies s = species();
        int v = getVariant();

        // Taming: a cub that has eaten accepts a medallion and becomes yours.
        if (stack.is(ModItems.MEDALLION) && !hasCollar() && (isTame() ? isOwnedBy(player) : (isBaby() && hasEaten()))) {
            if (!client) {
                stack.consume(1, player);
                if (!isTame()) {
                    tame(player);
                    this.navigation.stop();
                    setTarget(null);
                    level().broadcastEntityEvent(this, (byte) 7); // hearts
                }
                setFlag(FLAG_COLLAR, true);
            }
            return InteractionResult.sidedSuccess(client);
        }

        // Wild cubs eat meat from your hand (new; the original only let them eat meat from the ground).
        if (!isTame() && isBaby() && isMeat(stack) && !hasEaten()) {
            if (!client) {
                stack.consume(1, player);
                eatMeat();
            }
            return InteractionResult.sidedSuccess(client);
        }

        if (!isTame() || !isOwnedBy(player)) {
            return super.mobInteract(player, hand);
        }

        // --- owner only ---
        if (stack.is(ModItems.ESSENCE_OF_LIGHT) || stack.is(ModItems.ESSENCE_OF_DARKNESS)) {
            int winged = s.wingedForm(v, stack.is(ModItems.ESSENCE_OF_LIGHT));
            if (winged != 0) {
                if (!client) {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    setVariant(winged);
                    setHealth(getMaxHealth());
                    ((ServerLevel) level()).sendParticles(ParticleTypes.END_ROD, getX(), getY() + 1.0, getZ(), 40, 0.6, 0.6, 0.6, 0.05);
                    playSound(ModSounds.MAGIC_APPEAR, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(client);
            }
        }
        if (stack.is(ModItems.WHIP)) {
            if (!client) {
                setOrderedToSit(!isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                setTarget(null);
            }
            return InteractionResult.sidedSuccess(client);
        }
        if (isMeat(stack) && getHealth() < getMaxHealth()) {
            if (!client) {
                stack.consume(1, player);
                eatMeat();
            }
            return InteractionResult.sidedSuccess(client);
        }
        if (stack.is(Items.SADDLE) && !isSaddled() && !isBaby()) {
            if (!client) {
                stack.consume(1, player);
                setFlag(FLAG_SADDLED, true);
                playSound(SoundEvents.HORSE_SADDLE, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(client);
        }
        if (stack.is(Items.CHEST) && !hasChest() && !isBaby()) {
            if (!client) {
                stack.consume(1, player);
                setFlag(FLAG_CHESTED, true);
                playSound(SoundEvents.DONKEY_CHEST, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(client);
        }
        if (hasChest() && player.isSecondaryUseActive()) {
            if (!client) {
                player.openMenu(new SimpleMenuProvider((id, playerInventory, p) -> new ChestMenu(MenuType.GENERIC_9x2, id, playerInventory, this.inventory, 2),
                        getDisplayName()));
            }
            return InteractionResult.sidedSuccess(client);
        }

        InteractionResult result = super.mobInteract(player, hand); // breeding food and love mode
        if (result.consumesAction()) {
            return result;
        }
        if (stack.isEmpty() && isSaddled() && !isBaby() && !isVehicle() && !player.isSecondaryUseActive()) {
            if (!client) {
                setOrderedToSit(false);
                player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(client);
        }
        if (stack.isEmpty()) {
            if (!client) {
                setOrderedToSit(!isOrderedToSit());
                this.navigation.stop();
                setTarget(null);
            }
            return InteractionResult.sidedSuccess(client);
        }
        return result;
    }

    void eatMeat() {
        heal(getMaxHealth());
        setFlag(FLAG_FED, true);
        setFlag(FLAG_HUNTING, false);
        playSound(ModSounds.EATING, 1.0F, 1.0F);
        level().broadcastEntityEvent(this, EVENT_OPEN_MOUTH);
    }

    boolean wantsToEat() {
        return !isTame() || getHealth() < getMaxHealth();
    }

    // ------------------------------------------------------------------ riding

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (isSaddled() && getFirstPassenger() instanceof Player player) {
            return player;
        }
        return super.getControllingPassenger();
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = getYRot();
        if (player.jumping) {
            if (onGround()) {
                jumpFromGround();
            } else if (isFlyer()) {
                Vec3 v = getDeltaMovement();
                setDeltaMovement(v.x, Math.min(v.y + 0.14, 0.45), v.z);
                if (this.wingFlapCounter == 0) {
                    this.wingFlapCounter = 1;
                }
            }
        }
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float strafe = player.xxa * 0.5F;
        float forward = player.zza;
        if (forward <= 0.0F) {
            forward *= 0.25F;
        }
        return new Vec3(strafe, 0.0, forward);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        // the original: 0.18 walking, 0.37 sprinting
        return (float) getAttributeValue(Attributes.MOVEMENT_SPEED) * (player.isSprinting() ? 1.5F : 1.0F);
    }

    @Override
    protected float getFlyingSpeed() {
        if (isFlyer() && getControllingPassenger() instanceof Player) {
            return getSpeed() * 0.35F;
        }
        return super.getFlyingSpeed();
    }

    @Override
    public void travel(Vec3 input) {
        super.travel(input);
        if (isFlyer() && !onGround()) {
            Vec3 v = getDeltaMovement();
            if (v.y < 0.0) {
                setDeltaMovement(v.x, v.y * 0.6, v.z);
            }
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (isFlyer()) {
            return false;
        }
        // the original halves fall damage for big cats
        return super.causeFallDamage(fallDistance, multiplier * 0.5F, source);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        // riders can't hurt their own mount
        return (isVehicle() && source.getEntity() != null && hasPassenger(source.getEntity())) || super.isInvulnerableTo(source);
    }

    // ------------------------------------------------------------------ ticking

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide) {
            if (this.mouthCounter > 0 && ++this.mouthCounter > 30) {
                this.mouthCounter = 0;
            }
            if (this.random.nextInt(250) == 0) {
                this.tailCounter = 1;
            }
            if (this.tailCounter > 0 && ++this.tailCounter > 10 && this.random.nextInt(15) == 0) {
                this.tailCounter = 0;
            }
            if (isFlyer()) {
                if (!onGround() && this.wingFlapCounter == 0 && this.random.nextInt(isVehicle() ? 20 : 8) == 0) {
                    this.wingFlapCounter = 1;
                }
                if (this.wingFlapCounter > 0 && ++this.wingFlapCounter > 20) {
                    this.wingFlapCounter = 0;
                }
                if (this.wingFlapCounter == 5) {
                    level().playLocalSound(getX(), getY(), getZ(), ModSounds.WING_FLAP, getSoundSource(), 0.6F, 1.0F, false);
                }
            }
            return;
        }
        // wild adults go hunting at random (the original's 1-in-500 roll); tamed cats hunt when hurt
        if (!isHunting() && !isBaby() && this.random.nextInt(500) == 0 && (!isTame() || getHealth() < getMaxHealth() * 0.5F)) {
            setFlag(FLAG_HUNTING, true);
        }
        if (isVehicle()) {
            setTarget(null);
        }
        if (this.random.nextInt(300) == 0 && getHealth() < getMaxHealth()) {
            heal(1.0F);
        }
        setSprinting(getTarget() != null);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit) {
            level().broadcastEntityEvent(this, EVENT_OPEN_MOUTH);
            if (!target.isAlive()) {
                setFlag(FLAG_HUNTING, false); // made a kill
            }
        }
        return hit;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_OPEN_MOUTH) {
            this.mouthCounter = 1;
        } else {
            super.handleEntityEvent(id);
        }
    }

    // ------------------------------------------------------------------ death

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!(level() instanceof ServerLevel server)) {
            return;
        }
        if (isSaddled()) {
            spawnAtLocation(Items.SADDLE);
        }
        if (hasChest()) {
            spawnAtLocation(Items.CHEST);
            for (ItemStack item : this.inventory.removeAllItems()) {
                spawnAtLocation(item);
            }
        }
        if (hasCollar()) {
            spawnAtLocation(ModItems.MEDALLION);
        }
        // a tamed cat may come back as a ghost cub
        if (isTame() && !isGhost() && this.random.nextInt(4) == 0) {
            BigCat ghost = species().type().create(server);
            if (ghost != null) {
                ghost.moveTo(getX(), getY(), getZ(), getYRot(), 0.0F);
                ghost.setVariant(getVariant());
                ghost.setHealth(ghost.getMaxHealth());
                ghost.setAge(-24000);
                ghost.setOwnerUUID(getOwnerUUID());
                ghost.setTame(true, false);
                ghost.setFlag(FLAG_GHOST, true);
                server.addFreshEntity(ghost);
                ghost.playSound(ModSounds.MAGIC_APPEAR, 1.0F, 1.0F);
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        if (!isBaby() && !isGhost()) {
            spawnAtLocation(new ItemStack(ModItems.BIG_CAT_CLAW, 1 + this.random.nextInt(2)));
        }
    }

    // ------------------------------------------------------------------ sounds

    @Override
    protected SoundEvent getAmbientSound() {
        level().broadcastEntityEvent(this, EVENT_OPEN_MOUTH);
        return isBaby() ? ModSounds.CUB_GRUNT : ModSounds.LION_GRUNT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return isBaby() ? ModSounds.CUB_HURT : ModSounds.LION_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return isBaby() ? ModSounds.CUB_DYING : ModSounds.LION_DEATH;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 400;
    }

    /** Ghost cats flicker between 15% and 55% opacity, as in the original. */
    public float ghostAlpha(float ageInTicks) {
        return 0.35F + 0.2F * (float) Math.sin(ageInTicks * 0.07F + getId());
    }

    /** Called from {@link EatDroppedMeatGoal}. */
    List<ItemEntity> nearbyMeat(double range) {
        return level().getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(range), item -> isMeat(item.getItem()));
    }
}
