package com.menagerie.entity.wyvern;

import com.menagerie.Menagerie;
import com.menagerie.registry.ModItems;
import com.menagerie.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
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
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Wyverns: flying dragons that hatch from eggs and become loyal, rideable mounts.
 *
 * <p>Ported from Mo' Creatures' {@code MoCEntityWyvern} (DrZhark, GPL-3.0): 12 kinds of wyvern,
 * wild ones that take to the air at random, hunt small animals and attack players with a
 * poisonous bite; eggs that drop from slain wyverns and hatch into a wyvern tamed to the nearest
 * player; growth from hatchling to adult; saddle, horse armor (drawn as real armor plates),
 * a chest; essence of light makes a tamed wyvern lay an egg; a mother wyvern transforms into an
 * undead, light or dark wyvern with the matching essence.
 *
 * <p>New in Menagerie: ridden wyverns fly where the rider looks (hold forward), climb with jump
 * and glide when the keys are released; wild wyverns live in the overworld biome matching their
 * kind until the Wyvern Lair arrives.
 */
public class Wyvern extends TamableAnimal implements InventoryCarrier {
    // Kinds (the original's type numbers)
    public static final int JUNGLE = 1, SWAMP = 2, DESERT = 3, SUN = 4, MOTHER = 5, UNDEAD = 6, LIGHT = 7, DARK = 8,
            ARCTIC = 9, CAVE = 10, MOUNTAIN = 11, SEA = 12;
    public static final int KINDS = 12;
    private static final String[] TEXTURES = {"", "wyvernjungle", "wyvernmix", "wyvernsand", "wyvernsun", "wyvernmother",
            "wyvernundead", "wyvernlight", "wyverndark", "wyvernarctic", "wyverncave", "wyvernmountain", "wyvernsea"};
    private static final String[] KEYS = {"", "jungle", "swamp", "desert", "sun", "mother", "undead", "light", "dark",
            "arctic", "cave", "mountain", "sea"};

    /** Adults are rideable above this age (the original's "edad" > 90). */
    public static final int RIDEABLE_AGE = 90;
    private static final int HATCHLING_AGE = 30;

    private static final EntityDataAccessor<Integer> DATA_KIND = SynchedEntityData.defineId(Wyvern.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_AGE = SynchedEntityData.defineId(Wyvern.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ARMOR = SynchedEntityData.defineId(Wyvern.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_FLAGS = SynchedEntityData.defineId(Wyvern.class, EntityDataSerializers.BYTE);
    private static final int FLAG_SADDLED = 1, FLAG_CHESTED = 2, FLAG_FLYING = 4, FLAG_GHOST = 8;
    private static final byte EVENT_OPEN_MOUTH = 64;
    private static final ResourceLocation ARMOR_MODIFIER = Menagerie.id("wyvern_armor");

    private final SimpleContainer inventory = new SimpleContainer(9);
    private int transformTo;
    private int transformTicks;
    private int biteCooldown;
    @Nullable
    private Vec3 flightTarget;

    // client animation counters, read by the model
    public int mouthCounter;
    public int wingFlapCounter;

    public Wyvern(EntityType<? extends Wyvern> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    // ------------------------------------------------------------------ data

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_KIND, JUNGLE);
        builder.define(DATA_AGE, 120);
        builder.define(DATA_ARMOR, 0);
        builder.define(DATA_FLAGS, (byte) 0);
    }

    public int getKind() {
        return this.entityData.get(DATA_KIND);
    }

    public void setKind(int kind) {
        this.entityData.set(DATA_KIND, Mth.clamp(kind, 1, KINDS));
        setBase(getAttribute(Attributes.MAX_HEALTH), maxHealth(getKind()));
        setBase(getAttribute(Attributes.ATTACK_DAMAGE), attackDamage(getKind()));
        if (getHealth() > getMaxHealth()) {
            setHealth(getMaxHealth());
        }
    }

    public static String texture(int kind) {
        return TEXTURES[Mth.clamp(kind, 1, KINDS)];
    }

    public static String kindKey(int kind) {
        return KEYS[Mth.clamp(kind, 1, KINDS)];
    }

    private static final int[] EGG_COLORS = {0xFFFFFF, 0x8FD06A, 0x9DB585, 0xEAD7A4, 0xF2B24C, 0xD6A0F0, 0xA4B894,
            0xFFF6C8, 0x7A6A8E, 0xDDF3FF, 0xA8A8A8, 0xC2B49A, 0x86CFE0};

    /** New: eggs are tinted by kind (the original used one plain egg texture for all of them). */
    public static int eggColor(int kind) {
        return EGG_COLORS[Mth.clamp(kind, 1, KINDS)];
    }

    private static double maxHealth(int kind) {
        if (kind == MOTHER) return 80;
        if (kind == UNDEAD || kind == LIGHT || kind == DARK) return 60;
        return 40;
    }

    private static double attackDamage(int kind) {
        if (kind == MOTHER) return 12;
        if (kind == UNDEAD || kind == LIGHT || kind == DARK) return 8;
        return 5;
    }

    /** Fully grown age: 180 for mothers, 160 for the magic wyverns, 120 otherwise. */
    public static int maxAge(int kind) {
        if (kind == MOTHER) return 180;
        if (kind == UNDEAD || kind == LIGHT || kind == DARK) return 160;
        return 120;
    }

    private static void setBase(@Nullable AttributeInstance attribute, double value) {
        if (attribute != null) {
            attribute.setBaseValue(value);
        }
    }

    /** The original's "edad": 30 for a hatchling, up to {@link #maxAge}. Size is age / 100. */
    public int getGrowth() {
        return this.entityData.get(DATA_AGE);
    }

    public void setGrowth(int age) {
        this.entityData.set(DATA_AGE, age);
    }

    public float sizeFactor() {
        return getGrowth() * 0.01F;
    }

    public boolean isGrownUp() {
        return getGrowth() > RIDEABLE_AGE;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_AGE.equals(key)) {
            refreshDimensions();
        }
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return super.getDefaultDimensions(pose).scale(sizeFactor());
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
    public boolean isGhost() { return flag(FLAG_GHOST); }

    /** Wild wyverns switch between walking and flying; while flying they ignore gravity. */
    public boolean isFlying() {
        return flag(FLAG_FLYING);
    }

    private void setFlying(boolean flying) {
        setFlag(FLAG_FLYING, flying);
        setNoGravity(flying);
        this.flightTarget = null;
    }

    /** 0 none, 1 iron, 2 gold, 3 diamond (drawn as armor plates on the model). */
    public int getArmorType() {
        return this.entityData.get(DATA_ARMOR);
    }

    private void setArmorType(int type) {
        this.entityData.set(DATA_ARMOR, type);
        AttributeInstance armor = getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.removeModifier(ARMOR_MODIFIER);
            int value = switch (type) {
                case 1 -> 5;
                case 2 -> 7;
                case 3 -> 11;
                default -> 0;
            };
            if (value > 0) {
                armor.addOrUpdateTransientModifier(new AttributeModifier(ARMOR_MODIFIER, value, AttributeModifier.Operation.ADD_VALUE));
            }
        }
    }

    private static int armorTypeOf(ItemStack stack) {
        if (stack.is(Items.IRON_HORSE_ARMOR)) return 1;
        if (stack.is(Items.GOLDEN_HORSE_ARMOR)) return 2;
        if (stack.is(Items.DIAMOND_HORSE_ARMOR)) return 3;
        return 0;
    }

    private static ItemStack armorItem(int type) {
        return switch (type) {
            case 1 -> new ItemStack(Items.IRON_HORSE_ARMOR);
            case 2 -> new ItemStack(Items.GOLDEN_HORSE_ARMOR);
            case 3 -> new ItemStack(Items.DIAMOND_HORSE_ARMOR);
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Kind", getKind());
        tag.putInt("Growth", getGrowth());
        tag.putInt("ArmorType", getArmorType());
        tag.putByte("WyvernFlags", this.entityData.get(DATA_FLAGS));
        writeInventoryToTag(tag, registryAccess());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        boolean wasFullHealth = getHealth() >= getMaxHealth();
        if (tag.contains("Kind")) {
            setKind(tag.getInt("Kind"));
        }
        if (tag.contains("Growth")) {
            setGrowth(tag.getInt("Growth"));
        }
        if (wasFullHealth) {
            setHealth(getMaxHealth());
        }
        setArmorType(tag.getInt("ArmorType"));
        this.entityData.set(DATA_FLAGS, tag.getByte("WyvernFlags"));
        setNoGravity(isFlying());
        readInventoryFromTag(tag, registryAccess());
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.menagerie.wyvern." + kindKey(getKind()));
    }

    // ------------------------------------------------------------------ AI

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0, 10.0F, 3.0F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                target -> !isTame() && isGrownUp() && level().getDifficulty() != Difficulty.PEACEFUL));
        // the original's hunt: small animals only
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Animal.class, 40, true, false,
                target -> !isTame() && !(target instanceof Wyvern) && target.getBbHeight() <= 1.0F && target.getBbWidth() <= 1.0F));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false; // wyverns are not bred with food: they lay eggs (essence of light)
    }

    @Override
    public boolean canMate(net.minecraft.world.entity.animal.Animal other) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null;
    }

    // ------------------------------------------------------------------ spawning

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                                        @Nullable SpawnGroupData spawnGroupData) {
        setKind(kindForBiome(level.getBiome(blockPosition()), level.getRandom().nextInt(100)));
        // most wild wyverns are adults; one in six is still young, as in the original
        setGrowth(this.random.nextInt(6) == 0 ? 50 + this.random.nextInt(40) : 92 + this.random.nextInt(maxAge(getKind()) - 91));
        setHealth(getMaxHealth());
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    /** New: each kind lives in the biome its colours suggest. */
    public static int kindForBiome(Holder<Biome> biome, int roll) {
        if (biome.is(BiomeTags.IS_JUNGLE)) return JUNGLE;
        if (biome.is(Biomes.SWAMP) || biome.is(Biomes.MANGROVE_SWAMP)) return SWAMP;
        if (biome.is(Biomes.DESERT)) return DESERT;
        if (biome.is(BiomeTags.IS_SAVANNA) || biome.is(BiomeTags.IS_BADLANDS)) return SUN;
        if (biome.is(BiomeTags.IS_BEACH) || biome.is(BiomeTags.IS_OCEAN)) return SEA;
        if (biome.is(Biomes.FROZEN_PEAKS) || biome.is(Biomes.SNOWY_SLOPES) || biome.is(Biomes.ICE_SPIKES)) return ARCTIC;
        if (biome.is(Biomes.DRIPSTONE_CAVES) || biome.is(Biomes.LUSH_CAVES) || biome.is(Biomes.DEEP_DARK)) return CAVE;
        if (biome.is(BiomeTags.IS_MOUNTAIN) || biome.is(BiomeTags.IS_HILL)) return MOUNTAIN;
        // anywhere else (e.g. the Wyvern Lair): the original's mix, mothers included
        if (roll < 20) return MOTHER;
        int[] common = {JUNGLE, SWAMP, DESERT, SUN, ARCTIC, CAVE, MOUNTAIN, SEA};
        return common[roll % common.length];
    }

    // ------------------------------------------------------------------ interaction

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean client = level().isClientSide;
        if (!isTame() || !isOwnedBy(player)) {
            return super.mobInteract(player, hand);
        }
        int kind = getKind();

        if (stack.is(ModItems.WHIP)) {
            if (!client) {
                setOrderedToSit(!isOrderedToSit());
                setFlying(false);
                this.navigation.stop();
                setTarget(null);
            }
            return InteractionResult.sidedSuccess(client);
        }
        if ((stack.is(ItemTags.MEAT) || stack.is(ItemTags.FISHES)) && getHealth() < getMaxHealth()) {
            if (!client) {
                stack.consume(1, player);
                heal(10.0F);
                playSound(ModSounds.EATING, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(client);
        }
        if (isGrownUp()) {
            if (stack.is(Items.SADDLE) && !isSaddled()) {
                if (!client) {
                    stack.consume(1, player);
                    setFlag(FLAG_SADDLED, true);
                    playSound(SoundEvents.HORSE_SADDLE, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(client);
            }
            int armor = armorTypeOf(stack);
            if (armor > 0 && armor != getArmorType()) {
                if (!client) {
                    spawnAtLocation(armorItem(getArmorType()));
                    stack.consume(1, player);
                    setArmorType(armor);
                    playSound(ModSounds.ARMOR_PUT, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(client);
            }
            if (stack.is(Items.CHEST) && !hasChest()) {
                if (!client) {
                    stack.consume(1, player);
                    setFlag(FLAG_CHESTED, true);
                    playSound(SoundEvents.DONKEY_CHEST, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(client);
            }
            // essence of light: a tamed wyvern lays an egg of its own kind
            if (stack.is(ModItems.ESSENCE_OF_LIGHT) && kind < MOTHER && !isGhost()) {
                if (!client) {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    WyvernEgg egg = WyvernEgg.create(level(), kind, player.getX(), player.getY(), player.getZ());
                    if (egg != null) {
                        level().addFreshEntity(egg);
                    }
                    playSound(SoundEvents.CHICKEN_EGG, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(client);
            }
            // a mother wyvern becomes an undead, light or dark wyvern
            if (kind == MOTHER && this.transformTicks == 0 && !isGhost()) {
                int target = stack.is(ModItems.ESSENCE_OF_UNDEAD) ? UNDEAD
                        : stack.is(ModItems.ESSENCE_OF_LIGHT) ? LIGHT
                        : stack.is(ModItems.ESSENCE_OF_DARKNESS) ? DARK : 0;
                if (target != 0) {
                    if (!client) {
                        player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                        this.transformTo = target;
                        this.transformTicks = 1;
                        playSound(ModSounds.TRANSFORM, 1.0F, 1.0F);
                    }
                    return InteractionResult.sidedSuccess(client);
                }
            }
        }
        if (hasChest() && player.isSecondaryUseActive()) {
            if (!client) {
                player.openMenu(new SimpleMenuProvider((id, playerInventory, p) ->
                        new ChestMenu(MenuType.GENERIC_9x1, id, playerInventory, this.inventory, 1), getDisplayName()));
            }
            return InteractionResult.sidedSuccess(client);
        }
        if (stack.isEmpty() && isSaddled() && isGrownUp() && !isVehicle() && !player.isSecondaryUseActive()) {
            if (!client) {
                setOrderedToSit(false);
                player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(client);
        }
        if (stack.isEmpty()) {
            if (!client) {
                setOrderedToSit(!isOrderedToSit());
                setFlying(false);
                this.navigation.stop();
            }
            return InteractionResult.sidedSuccess(client);
        }
        return super.mobInteract(player, hand);
    }

    // ------------------------------------------------------------------ riding and flight

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
        if (player.jumping && this.wingFlapCounter == 0) {
            this.wingFlapCounter = 1;
        }
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        return new Vec3(player.xxa * 0.5F, 0.0, player.zza);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return (float) getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public void travel(Vec3 input) {
        if (getControllingPassenger() instanceof Player rider && (!onGround() || rider.jumping)) {
            ridenFlight(rider, input);
            return;
        }
        if (isFlying() && !isVehicle()) {
            // wild flight: steer towards the current flight target (set in aiStep)
            Vec3 v = getDeltaMovement().scale(flyerFriction());
            setDeltaMovement(v);
            move(MoverType.SELF, v);
            return;
        }
        super.travel(input);
    }

    /** New: a ridden wyvern flies where its rider looks. Jump climbs; no keys means a slow glide down. */
    private void ridenFlight(Player rider, Vec3 input) {
        Vec3 look = rider.getLookAngle();
        double thrust = 0.6 * 0.08; // the original's flyerThrust, per tick
        Vec3 push = Vec3.ZERO;
        if (input.z > 0) {
            push = look.scale(thrust * input.z);
        } else if (input.z < 0) {
            push = look.scale(thrust * input.z * 0.4);
        }
        if (input.x != 0) {
            Vec3 side = new Vec3(look.z, 0, -look.x).normalize();
            push = push.add(side.scale(thrust * input.x));
        }
        if (rider.jumping) {
            push = push.add(0, 0.06, 0);
        } else if (input.z == 0) {
            push = push.add(0, -0.01, 0);
        }
        Vec3 v = getDeltaMovement().scale(flyerFriction()).add(push);
        if (v.y > 0.5) {
            v = new Vec3(v.x, 0.5, v.z);
        }
        setDeltaMovement(v);
        move(MoverType.SELF, v);
        this.fallDistance = 0;
    }

    /** Air friction: big wyverns glide further (the original's flyerFriction). */
    private float flyerFriction() {
        int kind = getKind();
        return (kind == MOTHER || kind == UNDEAD || kind == LIGHT || kind == DARK || isGhost()) ? 0.96F : 0.94F;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
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
            boolean inAir = !onGround() || isFlying();
            if (inAir && this.wingFlapCounter == 0 && this.random.nextInt(isVehicle() ? 12 : 6) == 0) {
                this.wingFlapCounter = 1;
            }
            if (this.wingFlapCounter > 0 && ++this.wingFlapCounter > 20) {
                this.wingFlapCounter = 0;
            }
            if (this.wingFlapCounter == 5) {
                level().playLocalSound(getX(), getY(), getZ(), ModSounds.WYVERN_WING_FLAP, getSoundSource(), 0.8F, 1.0F, false);
            }
            if (this.transformTicks > 0) {
                this.transformTicks++;
            }
            return;
        }
        serverTick((ServerLevel) level());
    }

    private void serverTick(ServerLevel level) {
        // growth: hatchlings grow up over about twenty minutes
        if (getGrowth() < maxAge(getKind()) && this.random.nextInt(300) == 0) {
            setGrowth(getGrowth() + 1);
        }
        if (this.random.nextInt(300) == 0 && getHealth() < getMaxHealth()) {
            heal(1.0F);
        }
        if (this.biteCooldown > 0) {
            this.biteCooldown--;
        }

        // transformation (mother -> undead / light / dark), 100 ticks of flickering as in the original
        if (this.transformTicks > 0) {
            if (this.transformTicks % 10 == 0) {
                level.sendParticles(ParticleTypes.WITCH, getX(), getY() + getBbHeight() * 0.6, getZ(), 6, 0.8, 0.8, 0.8, 0.02);
            }
            if (++this.transformTicks > 100) {
                this.transformTicks = 0;
                setKind(this.transformTo);
                setHealth(getMaxHealth());
                level.sendParticles(ParticleTypes.END_ROD, getX(), getY() + 1.0, getZ(), 40, 1.0, 1.0, 1.0, 0.05);
            }
        }

        boolean ceased = isVehicle() || isOrderedToSit();
        if (ceased) {
            if (isFlying()) {
                setFlying(false);
            }
            return;
        }

        // wild wyverns take off and land at random, and take off when they have a target
        if (!isTame() && isGrownUp() && this.random.nextInt(300) == 0) {
            setFlying(!isFlying());
        }
        if (getTarget() != null && !isTame() && !isFlying() && this.random.nextInt(20) == 0) {
            setFlying(true);
        }
        // tamed wyverns fly back to an owner who is far away
        if (isTame() && getOwner() != null && distanceToSqr(getOwner()) > 400 && !isFlying()) {
            setFlying(true);
        }
        if (isFlying()) {
            flyTick(level);
        }
    }

    private void flyTick(ServerLevel level) {
        LivingEntity target = getTarget();
        Vec3 goal;
        if (target != null && target.isAlive()) {
            goal = target.position().add(0, target.getBbHeight() * 0.5, 0);
            if (distanceToSqr(target) < Math.max(4.0, getBbWidth() * getBbWidth()) && this.biteCooldown == 0) {
                doHurtTarget(target);
                this.biteCooldown = 20;
            }
        } else if (isTame() && getOwner() != null) {
            LivingEntity owner = getOwner();
            goal = owner.position().add(0, 3, 0);
            if (distanceToSqr(owner) < 36) {
                setFlying(false); // land next to the owner
                return;
            }
        } else {
            if (this.flightTarget == null || position().distanceToSqr(this.flightTarget) < 4.0 || this.random.nextInt(200) == 0) {
                this.flightTarget = pickFlightTarget(level);
            }
            goal = this.flightTarget;
        }
        Vec3 dir = goal.subtract(position());
        double dist = dir.length();
        if (dist > 0.01) {
            Vec3 accel = dir.scale(0.04 / dist);
            Vec3 v = getDeltaMovement().add(accel);
            if (v.length() > 0.45) {
                v = v.normalize().scale(0.45);
            }
            setDeltaMovement(v);
            float yaw = (float) (Mth.atan2(v.z, v.x) * (180.0 / Math.PI)) - 90.0F;
            setYRot(Mth.rotLerp(0.2F, getYRot(), yaw));
            this.yBodyRot = getYRot();
        }
    }

    private Vec3 pickFlightTarget(ServerLevel level) {
        int maxHeight = isTame() ? 5 : 18; // the original's maxFlyingHeight
        double x = getX() + (this.random.nextDouble() - 0.5) * 32;
        double z = getZ() + (this.random.nextDouble() - 0.5) * 32;
        int ground = level.getHeight(Heightmap.Types.MOTION_BLOCKING, Mth.floor(x), Mth.floor(z));
        double y = ground + 3 + this.random.nextInt(maxHeight);
        return new Vec3(x, y, z);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit) {
            level().broadcastEntityEvent(this, EVENT_OPEN_MOUTH);
            // the original's poisonous bite
            if (target instanceof Player player && this.random.nextInt(3) == 0) {
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0), this);
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

    /** Transformation progress for the flickering texture, 0 when not transforming. */
    public int transformTicks() {
        return this.transformTicks;
    }

    // ------------------------------------------------------------------ death

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!(level() instanceof ServerLevel)) {
            return;
        }
        if (isSaddled()) {
            spawnAtLocation(Items.SADDLE);
        }
        spawnAtLocation(armorItem(getArmorType()));
        if (hasChest()) {
            spawnAtLocation(Items.CHEST);
            for (ItemStack item : this.inventory.removeAllItems()) {
                spawnAtLocation(item);
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        // the original drops an egg of the wyvern's own kind; mothers drop them more often
        int chance = getKind() == MOTHER ? 33 : 10;
        if (!isGhost() && isGrownUp() && this.random.nextInt(100) < chance) {
            spawnAtLocation(WyvernEggItem.stackOf(getKind()));
        }
    }

    // ------------------------------------------------------------------ sounds

    @Override
    protected SoundEvent getAmbientSound() {
        level().broadcastEntityEvent(this, EVENT_OPEN_MOUTH);
        return ModSounds.WYVERN_GRUNT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        level().broadcastEntityEvent(this, EVENT_OPEN_MOUTH);
        return ModSounds.WYVERN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.WYVERN_DYING;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 400;
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F + sizeFactor() * 0.4F;
    }

    public float ghostAlpha(float ageInTicks) {
        return 0.35F + 0.2F * Mth.sin(ageInTicks * 0.07F + getId());
    }

    /** Spawns a freshly hatched wyvern of the given kind, tamed to the given player. */
    public static void hatch(ServerLevel level, BlockPos pos, int kind, @Nullable Player owner) {
        Wyvern wyvern = com.menagerie.registry.ModEntities.WYVERN.create(level);
        if (wyvern == null) {
            return;
        }
        wyvern.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.getRandom().nextFloat() * 360.0F, 0.0F);
        wyvern.setKind(kind);
        wyvern.setGrowth(HATCHLING_AGE);
        wyvern.setHealth(wyvern.getMaxHealth());
        if (owner != null) {
            wyvern.tame(owner);
        }
        level.addFreshEntity(wyvern);
    }
}
