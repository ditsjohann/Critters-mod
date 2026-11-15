package com.menagerie.entity.horse;

import com.menagerie.registry.ModEntities;
import com.menagerie.registry.ModItems;
import com.menagerie.registry.ModSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * The Mo' Creatures horse: 44 breeds, from plain horses to unicorns, pegasi, nightmares, fairies,
 * ghosts and the undead.
 *
 * <p>Gameplay ported from {@code MoCEntityHorse} (DrZhark, GPL-3.0): breeds and stats, the breeding
 * table, essence transformations, fairy dyeing, undead decay, ghost horses rising from fallen magic
 * horses, breed-specific drops and sounds. Taming, saddles, chests, the inventory screen and armor
 * slots use vanilla's horse systems, so the horse behaves like any other horse in 1.21.
 */
public class MenagerieHorse extends AbstractChestedHorse {
    private static final EntityDataAccessor<Integer> DATA_BREED = SynchedEntityData.defineId(MenagerieHorse.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_STAYING = SynchedEntityData.defineId(MenagerieHorse.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_VANISH = SynchedEntityData.defineId(MenagerieHorse.class, EntityDataSerializers.INT);

    /** Undead horses rot into skeletons when this reaches {@link #UNDEAD_DECAY_AGE}. */
    private static final int UNDEAD_DECAY_AGE = 399;
    private int undeadAge;
    private int chargeCooldown;

    // client-side animation counters, read by the model (same meaning as in the original model)
    public int wingFlapCounter;
    public int mouthCounter;
    public int tailCounter;

    public MenagerieHorse(EntityType<? extends MenagerieHorse> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractHorse.createBaseHorseAttributes();
    }

    // ------------------------------------------------------------------ data

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BREED, HorseBreeds.PALE_BROWN);
        builder.define(DATA_STAYING, false);
        builder.define(DATA_VANISH, 0);
    }

    public int getBreed() {
        return this.entityData.get(DATA_BREED);
    }

    /** Sets the breed and the stats that come with it. Use {@link #transformInto} for in-game transformations. */
    public void setBreed(int breed) {
        if (!HorseBreeds.isValid(breed)) {
            breed = HorseBreeds.PALE_BROWN;
        }
        this.entityData.set(DATA_BREED, breed);
        applyBreedAttributes();
    }

    private void applyBreedAttributes() {
        int t = getBreed();
        setBase(getAttribute(Attributes.MAX_HEALTH), HorseBreeds.maxHealth(t));
        // original speed factors run 0.7-1.3; vanilla horses run 0.11-0.34
        setBase(getAttribute(Attributes.MOVEMENT_SPEED), 0.225 * HorseBreeds.speedFactor(t));
        // original jump factors run 0.35-0.6; x1.5 gives 1.8 to 4.4 block jumps
        setBase(getAttribute(Attributes.JUMP_STRENGTH), HorseBreeds.jumpFactor(t) * 1.5);
        if (getHealth() > getMaxHealth()) {
            setHealth(getMaxHealth());
        }
    }

    private static void setBase(@Nullable AttributeInstance attribute, double value) {
        if (attribute != null) {
            attribute.setBaseValue(value);
        }
    }

    public boolean isStaying() {
        return this.entityData.get(DATA_STAYING);
    }

    public void setStaying(boolean staying) {
        this.entityData.set(DATA_STAYING, staying);
    }

    /** 0 = visible; counts up to 100 while the horse fades away (after making a fairy foal). */
    public int getVanishTicks() {
        return this.entityData.get(DATA_VANISH);
    }

    public boolean isFlyer() {
        return HorseBreeds.isFlyer(getBreed());
    }

    public boolean isGhost() {
        return HorseBreeds.isGhost(getBreed());
    }

    /** In the air: flyers aloft, or a ghost (always floating). Drives the wing and leg poses. */
    public boolean isFloating() {
        return isGhost() || (isFlyer() && !onGround());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Breed", getBreed());
        tag.putBoolean("Staying", isStaying());
        tag.putInt("UndeadAge", this.undeadAge);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Breed")) {
            // breed spawn eggs merge "Breed" into a freshly spawned horse: keep it at full health
            boolean wasFullHealth = getHealth() >= getMaxHealth();
            setBreed(tag.getInt("Breed"));
            if (wasFullHealth) {
                setHealth(getMaxHealth());
            }
        }
        setStaying(tag.getBoolean("Staying"));
        this.undeadAge = tag.getInt("UndeadAge");
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.menagerie.horse." + HorseBreeds.key(getBreed()));
    }

    @Override
    public boolean fireImmune() {
        return HorseBreeds.isFireImmune(getBreed()) || super.fireImmune();
    }

    // ------------------------------------------------------------------ spawning and breeding

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                                        @Nullable SpawnGroupData spawnGroupData) {
        var biome = level.getBiome(this.blockPosition());
        boolean plains = biome.is(Biomes.PLAINS) || biome.is(Biomes.SUNFLOWER_PLAINS);
        boolean savanna = biome.is(BiomeTags.IS_SAVANNA); // zebras live on the savanna (the original used "sandy" biomes)
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        setBreed(HorseBreeds.wildBreed(level.getRandom(), plains, savanna));
        setHealth(getMaxHealth());
        return data;
    }

    @Override
    public int getMaxTemper() {
        return getBreed() == HorseBreeds.ZEBRA ? 200 : 100; // zebras are harder to tame
    }

    @Override
    public boolean canMate(Animal other) {
        return other != this && other instanceof MenagerieHorse mate
                && this.isTamed() && mate.isTamed()
                && this.isInLove() && mate.isInLove()
                && !this.isBaby() && !mate.isBaby()
                && this.getVanishTicks() == 0 && mate.getVanishTicks() == 0;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        MenagerieHorse foal = ModEntities.HORSE.create(level);
        if (foal == null || !(other instanceof MenagerieHorse mate)) {
            return foal;
        }
        int a = getBreed();
        int b = mate.getBreed();
        foal.setBreed(HorseBreeds.offspring(a, b, this.random));
        foal.setHealth(foal.getMaxHealth());
        if (getOwnerUUID() != null) {
            foal.setOwnerUUID(getOwnerUUID());
            foal.setTamed(true); // new: foals of tamed parents are born tame
        }
        if (HorseBreeds.makesFairy(a, b)) {
            // a unicorn and a pegasus give their magic to a fairy foal and fade away
            this.startVanishing();
            mate.startVanishing();
            playSound(ModSounds.MAGIC_APPEAR, 1.0F, 1.0F);
        }
        return foal;
    }

    private void startVanishing() {
        if (getVanishTicks() == 0) {
            this.entityData.set(DATA_VANISH, 1);
        }
    }

    // ------------------------------------------------------------------ interaction

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && !this.isVehicle() && getVanishTicks() == 0) {
            InteractionResult result = useItem(player, hand, stack);
            if (result != null) {
                return result;
            }
        }
        return super.mobInteract(player, hand);
    }

    /** The original's item rules. Returns null to fall through to vanilla horse handling (saddles, armor, riding). */
    @Nullable
    private InteractionResult useItem(Player player, InteractionHand hand, ItemStack stack) {
        boolean client = level().isClientSide;
        int t = getBreed();

        if (isTamed()) {
            if (stack.is(ModItems.ESSENCE_OF_UNDEAD) || stack.is(ModItems.ESSENCE_OF_FIRE)
                    || stack.is(ModItems.ESSENCE_OF_DARKNESS) || stack.is(ModItems.ESSENCE_OF_LIGHT)) {
                if (!client) {
                    drinkEssence(player, stack);
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                }
                this.mouthCounter = 1;
                return InteractionResult.sidedSuccess(client);
            }
            if (stack.is(ModItems.WHIP)) {
                if (!client) {
                    setStaying(!isStaying());
                    player.displayClientMessage(Component.translatable(isStaying()
                            ? "message.menagerie.horse.stay" : "message.menagerie.horse.follow"), true);
                }
                return InteractionResult.sidedSuccess(client);
            }
            if (stack.is(ModItems.HAYSTACK)) {
                if (!client) {
                    stack.consume(1, player);
                    setStaying(true);
                    if (HorseBreeds.eatsFood(t)) {
                        setHealth(getMaxHealth());
                    }
                    playSound(ModSounds.EATING, 1.0F, 1.0F);
                }
                this.mouthCounter = 1;
                return InteractionResult.sidedSuccess(client);
            }
        }

        if (t == HorseBreeds.FAIRY_WHITE && stack.getItem() instanceof DyeItem dye) {
            int fairy = HorseBreeds.fairyFromDye(dye.getDyeColor());
            if (fairy > 0 && fairy != t) {
                if (!client) {
                    stack.consume(1, player);
                    transformInto(fairy);
                }
                return InteractionResult.sidedSuccess(client);
            }
        }

        if (isTamed() && !isBaby() && isBodyArmorItem(stack) && !isWearingBodyArmor()) {
            if (!client) {
                equipBodyArmor(player, stack);
            }
            return InteractionResult.sidedSuccess(client);
        }

        if (stack.is(Items.CHEST) && !HorseBreeds.isBagger(t)) {
            return InteractionResult.PASS; // only donkeys, mules, zonkies, pegasi and fairies carry bags
        }

        if (HorseBreeds.eatsFood(t)) {
            return feed(player, hand, stack);
        }
        return null;
    }

    /** Food rules from the original: temper, healing and growth. Apples tame a horse outright. */
    @Nullable
    private InteractionResult feed(Player player, InteractionHand hand, ItemStack stack) {
        int temper;
        float heal;
        int growth;
        boolean tames = false;
        boolean breeding = false;
        if (stack.is(Items.WHEAT)) {
            temper = 25; heal = 5; growth = 20;
        } else if (stack.is(ModItems.SUGAR_LUMP)) {
            temper = 25; heal = 10; growth = 40;
        } else if (stack.is(Items.BREAD)) {
            temper = 100; heal = 20; growth = 60;
        } else if (stack.is(Items.APPLE) || stack.is(Items.GOLDEN_APPLE)) {
            temper = 0; heal = getMaxHealth(); growth = 20; tames = true;
        } else if (stack.is(Items.PUMPKIN) || stack.is(Items.MUSHROOM_STEW) || stack.is(Items.CAKE) || stack.is(Items.GOLDEN_CARROT)) {
            if (isBaby() || !isTamed()) {
                return null;
            }
            temper = 0; heal = getMaxHealth(); growth = 0; breeding = true;
        } else {
            return null;
        }

        boolean client = level().isClientSide;
        this.mouthCounter = 1;
        if (client) {
            return InteractionResult.SUCCESS;
        }
        if (stack.is(Items.MUSHROOM_STEW)) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BOWL)));
        } else {
            stack.consume(1, player);
        }
        if (temper > 0 && !isTamed()) {
            modifyTemper(temper);
        }
        heal(heal);
        if (isBaby() && growth > 0) {
            ageUp(growth);
        }
        if (tames && !isTamed()) {
            tameWithName(player);
        }
        if (breeding) {
            setInLove(player);
        }
        playSound(ModSounds.EATING, 1.0F, 1.0F);
        return InteractionResult.CONSUME;
    }

    /** Essence transformations, straight from the original. Magic horses also use essences to breed. */
    private void drinkEssence(Player player, ItemStack stack) {
        int t = getBreed();
        boolean readyToBreed = !isBaby() && getHealth() >= getMaxHealth();
        playSound(ModSounds.DRINKING, 1.0F, 1.0F);

        if (stack.is(ModItems.ESSENCE_OF_UNDEAD)) {
            if (HorseBreeds.isUndead(t) || HorseBreeds.isGhost(t)) {
                setHealth(getMaxHealth());
            }
            if (t == HorseBreeds.PEGASUS || t == HorseBreeds.BAT || t == HorseBreeds.DARK_PEGASUS) {
                transformInto(HorseBreeds.UNDEAD_PEGASUS);
            } else if (t == HorseBreeds.UNICORN || HorseBreeds.isFairy(t)) {
                transformInto(HorseBreeds.UNDEAD_UNICORN);
            } else if (HorseBreeds.isRegular(t) || t == HorseBreeds.ZEBRA || t == HorseBreeds.ZORSE) {
                transformInto(HorseBreeds.UNDEAD);
            }
        } else if (stack.is(ModItems.ESSENCE_OF_FIRE)) {
            if (t == HorseBreeds.NIGHTMARE) {
                if (readyToBreed) {
                    setInLove(player);
                }
                setHealth(getMaxHealth());
            } else if (t == HorseBreeds.ZORSE) {
                transformInto(HorseBreeds.NIGHTMARE);
            }
        } else if (stack.is(ModItems.ESSENCE_OF_DARKNESS)) {
            if (t == HorseBreeds.BAT) {
                if (readyToBreed) {
                    setInLove(player);
                }
                setHealth(getMaxHealth());
            } else if (t == HorseBreeds.ZORSE) {
                transformInto(HorseBreeds.BAT);
            } else if (t == HorseBreeds.PEGASUS) {
                transformInto(HorseBreeds.DARK_PEGASUS);
            }
        } else if (stack.is(ModItems.ESSENCE_OF_LIGHT)) {
            if (HorseBreeds.isMagic(t)) {
                if (readyToBreed) {
                    setInLove(player);
                }
                setHealth(getMaxHealth());
            }
            if (t == HorseBreeds.NIGHTMARE) {
                transformInto(HorseBreeds.UNICORN);
            } else if (t == HorseBreeds.BAT && getY() > 128.0) {
                transformInto(HorseBreeds.PEGASUS);   // a bat horse brought up into the sky becomes a pegasus
            } else if (HorseBreeds.isUndead(t) && !isBaby()) {
                this.undeadAge = 10;                   // essence of light reverses the decay
                if (t >= HorseBreeds.SKELETON) {
                    transformInto(t - 3);
                }
            }
        }
    }

    /** Changes breed with a burst of magic, keeping only the gear the new breed can use. */
    public void transformInto(int breed) {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }
        setBreed(breed);
        setHealth(getMaxHealth());
        if (!getBodyArmorItem().isEmpty() && !isBodyArmorItem(getBodyArmorItem())) {
            spawnAtLocation(getBodyArmorItem().copy());
            setBodyArmorItem(ItemStack.EMPTY);
        }
        if (hasChest() && !HorseBreeds.isBagger(breed)) {
            for (int i = 1; i < this.inventory.getContainerSize(); i++) {
                ItemStack item = this.inventory.removeItemNoUpdate(i);
                if (!item.isEmpty()) {
                    spawnAtLocation(item);
                }
            }
            spawnAtLocation(new ItemStack(Items.CHEST));
            setChest(false);
            createInventory();
        }
        server.sendParticles(magicParticle(breed), getX(), getY() + 1.0, getZ(), 40, 0.6, 0.8, 0.6, 0.05);
        playSound(ModSounds.MAGIC_APPEAR, 1.0F, 1.0F);
    }

    private static ParticleOptions magicParticle(int breed) {
        if (HorseBreeds.isUndead(breed)) return ParticleTypes.SOUL;
        if (breed == HorseBreeds.NIGHTMARE) return ParticleTypes.FLAME;
        if (breed == HorseBreeds.BAT || breed == HorseBreeds.DARK_PEGASUS) return ParticleTypes.WITCH;
        return ParticleTypes.END_ROD;
    }

    // ------------------------------------------------------------------ armor and bags

    @Override
    public boolean isBodyArmorItem(ItemStack stack) {
        int t = getBreed();
        if (stack.is(ModItems.CRYSTAL_HORSE_ARMOR)) {
            return HorseBreeds.wearsCrystalArmor(t);
        }
        return HorseBreeds.wearsMetalArmor(t)
                && (stack.is(Items.IRON_HORSE_ARMOR) || stack.is(Items.GOLDEN_HORSE_ARMOR) || stack.is(Items.DIAMOND_HORSE_ARMOR));
    }

    /** 0 none, 1 iron, 2 gold, 3 diamond, 4 crystal (texture suffixes of the original). */
    public int getArmorType() {
        ItemStack armor = getBodyArmorItem();
        if (armor.is(Items.IRON_HORSE_ARMOR)) return 1;
        if (armor.is(Items.GOLDEN_HORSE_ARMOR)) return 2;
        if (armor.is(Items.DIAMOND_HORSE_ARMOR)) return 3;
        if (armor.is(ModItems.CRYSTAL_HORSE_ARMOR)) return 4;
        return 0;
    }

    // ------------------------------------------------------------------ movement and flight

    @Override
    public void travel(Vec3 input) {
        if (isStaying() && !isVehicle()) {
            super.travel(Vec3.ZERO);
            return;
        }
        super.travel(input);
        int t = getBreed();
        if ((HorseBreeds.isFlyer(t) || HorseBreeds.isFloater(t)) && !onGround()) {
            Vec3 v = getDeltaMovement();
            if (v.y < 0.0) {
                setDeltaMovement(v.x, v.y * 0.6, v.z); // slow falling, as in the original
            }
        }
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        // New: a ridden flyer climbs while the rider holds jump, and glides when released.
        if (isFlyer() && !onGround() && player.jumping) {
            Vec3 v = getDeltaMovement();
            setDeltaMovement(v.x, Math.min(v.y + 0.14, 0.45), v.z);
            if (this.wingFlapCounter == 0) {
                this.wingFlapCounter = 1;
            }
        }
    }

    @Override
    public boolean canJump() {
        // on the ground the jump bar launches a flyer; in the air the jump key climbs instead
        return super.canJump() && (!isFlyer() || onGround());
    }

    @Override
    protected float getFlyingSpeed() {
        if (isFlyer() && getControllingPassenger() instanceof Player) {
            return getSpeed() * 0.35F;
        }
        return super.getFlyingSpeed();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        int t = getBreed();
        if (HorseBreeds.isFlyer(t) || HorseBreeds.isFloater(t)) {
            return false;
        }
        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    // ------------------------------------------------------------------ ticking

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide) {
            clientTick();
        } else {
            serverTick((ServerLevel) level());
        }
    }

    private void serverTick(ServerLevel level) {
        int t = getBreed();

        int vanish = getVanishTicks();
        if (vanish > 0) {
            this.entityData.set(DATA_VANISH, vanish + 1);
            if (vanish + 1 >= 100) {
                level.sendParticles(ParticleTypes.END_ROD, getX(), getY() + 1.0, getZ(), 30, 0.5, 0.8, 0.5, 0.05);
                playSound(ModSounds.VANISH, 1.0F, 1.0F);
                ejectPassengers();
                discard();
            }
            return;
        }

        if (isStaying() && !isVehicle()) {
            getNavigation().stop();
        }

        // undead horses slowly rot into skeletons
        if (HorseBreeds.isUndead(t) && t < HorseBreeds.SKELETON && !isBaby() && this.random.nextInt(20) == 0
                && this.random.nextInt(16) == 0 && ++this.undeadAge >= UNDEAD_DECAY_AGE) {
            this.undeadAge = 0;
            setBreed(t + 3);
        }

        if (this.chargeCooldown > 0) {
            this.chargeCooldown--;
        }
        if (getControllingPassenger() instanceof Player rider) {
            Vec3 v = getDeltaMovement();
            boolean galloping = v.horizontalDistanceSqr() > 0.04;
            if (t == HorseBreeds.NIGHTMARE) {
                nightmareTrail(level, rider);
            }
            if (galloping && HorseBreeds.hasHorn(t) && this.chargeCooldown == 0) {
                unicornCharge(level, rider);
            }
        }
    }

    /** New take on the original's NightmareEffect: a ridden nightmare sets nearby monsters alight. */
    private void nightmareTrail(ServerLevel level, Player rider) {
        if (this.random.nextInt(2) == 0) {
            level.sendParticles(ParticleTypes.FLAME, getX(), getY() + 0.2, getZ(), 2, 0.4, 0.1, 0.4, 0.01);
        }
        if (this.tickCount % 10 == 0) {
            for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(2.0),
                    e -> e instanceof Enemy && e != rider && !e.fireImmune())) {
                target.setRemainingFireTicks(60);
            }
        }
    }

    /** The original's "buckle": a galloping horned horse knocks monsters out of its way. */
    private void unicornCharge(ServerLevel level, Player rider) {
        Vec3 look = Vec3.directionFromRotation(0.0F, getYRot());
        var box = getBoundingBox().move(look.scale(1.2)).inflate(0.4);
        boolean hit = false;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, box, e -> e instanceof Enemy && e != rider)) {
            target.hurt(damageSources().mobAttack(this), 5.0F);
            target.knockback(1.2, -look.x, -look.z);
            hit = true;
        }
        if (hit) {
            this.chargeCooldown = 20;
            level.sendParticles(ParticleTypes.CRIT, getX() + look.x * 1.5, getY() + 1.2, getZ() + look.z * 1.5, 10, 0.3, 0.3, 0.3, 0.1);
        }
    }

    private void clientTick() {
        int t = getBreed();
        if (this.random.nextInt(200) == 0) {
            this.tailCounter = 1;
        }
        if (this.tailCounter > 0 && ++this.tailCounter > 30) {
            this.tailCounter = 0;
        }
        if (this.mouthCounter > 0 && ++this.mouthCounter > 30) {
            this.mouthCounter = 0;
        }

        if (HorseBreeds.isFlyer(t)) {
            if (!onGround() && this.wingFlapCounter == 0 && this.random.nextInt(isVehicle() ? 20 : 8) == 0) {
                this.wingFlapCounter = 1;
            }
            if (this.wingFlapCounter > 0 && ++this.wingFlapCounter > 20) {
                this.wingFlapCounter = 0;
            }
            if (this.wingFlapCounter == 5) {
                level().playLocalSound(getX(), getY(), getZ(), ModSounds.WING_FLAP, getSoundSource(), 0.6F, 1.0F, false);
            }
            if (this.wingFlapCounter != 0 && this.wingFlapCounter % 5 == 0 && HorseBreeds.isFairy(t)) {
                particle(ParticleTypes.END_ROD);
            }
        }
        if (t == HorseBreeds.NIGHTMARE && this.random.nextInt(50) == 0) {
            particle(ParticleTypes.LAVA);
        }
        if (t == HorseBreeds.UNICORN && !onGround()) {
            particle(ParticleTypes.END_ROD);
        }
        if (HorseBreeds.isUndead(t) && t < HorseBreeds.SKELETON && this.random.nextInt(40) == 0) {
            particle(ParticleTypes.SMOKE);
        }
    }

    private void particle(ParticleOptions type) {
        level().addParticle(type, getRandomX(0.6), getRandomY(), getRandomZ(0.6), 0.0, 0.02, 0.0);
    }

    // ------------------------------------------------------------------ death and drops

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!(level() instanceof ServerLevel server) || getVanishTicks() > 0) {
            return;
        }
        int t = getBreed();
        if (!isBaby() && server.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            dropBreedLoot(t);
        }
        // a tamed magic or pure-bred horse may come back as a ghost foal
        if (isTamed() && (HorseBreeds.isMagic(t) || HorseBreeds.isPureBreed(t)) && !HorseBreeds.isGhost(t)
                && this.random.nextInt(4) == 0) {
            MenagerieHorse ghost = ModEntities.HORSE.create(server);
            if (ghost != null) {
                ghost.moveTo(getX(), getY(), getZ(), getYRot(), 0.0F);
                ghost.setBreed(isFlyer() ? HorseBreeds.GHOST_WINGED : HorseBreeds.GHOST);
                ghost.setHealth(ghost.getMaxHealth());
                ghost.setAge(-24000);
                ghost.setOwnerUUID(getOwnerUUID());
                ghost.setTamed(true);
                server.addFreshEntity(ghost);
                ghost.playSound(ModSounds.MAGIC_APPEAR, 1.0F, 1.0F);
            }
        }
    }

    private void dropBreedLoot(int t) {
        boolean rare = this.random.nextInt(100) < 25;
        if (HorseBreeds.hasHorn(t) && !HorseBreeds.isUndead(t) && rare) {
            spawnAtLocation(ModItems.UNICORN_HORN);
        } else if (t == HorseBreeds.NIGHTMARE && rare) {
            spawnAtLocation(ModItems.HEART_OF_FIRE);
        } else if (t == HorseBreeds.BAT && rare) {
            spawnAtLocation(ModItems.HEART_OF_DARKNESS);
        } else if (t >= HorseBreeds.UNDEAD && t <= HorseBreeds.UNDEAD_PEGASUS && rare) {
            spawnAtLocation(ModItems.HEART_OF_UNDEAD);
        }
        int count = 1 + this.random.nextInt(2);
        if (t == HorseBreeds.PEGASUS || t == HorseBreeds.DARK_PEGASUS) {
            spawnAtLocation(new ItemStack(Items.FEATHER, count));
        } else if (t >= HorseBreeds.SKELETON && t <= HorseBreeds.SKELETON_PEGASUS) {
            spawnAtLocation(new ItemStack(Items.BONE, count));
        } else if (t >= HorseBreeds.UNDEAD && t <= HorseBreeds.UNDEAD_PEGASUS) {
            spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH, count));
        } else if (HorseBreeds.isGhost(t)) {
            spawnAtLocation(Items.GHAST_TEAR);
        } else {
            spawnAtLocation(new ItemStack(Items.LEATHER, count));
        }
    }

    // ------------------------------------------------------------------ sounds

    private enum Voice { HORSE, GHOST, UNDEAD, DONKEY, ZEBRA }

    private Voice voice() {
        int t = getBreed();
        if (HorseBreeds.isGhost(t)) return Voice.GHOST;
        if (HorseBreeds.isUndead(t)) return Voice.UNDEAD;
        if (t == HorseBreeds.DONKEY || t == HorseBreeds.MULE || t == HorseBreeds.ZONKY) return Voice.DONKEY;
        if (t == HorseBreeds.ZEBRA || t == HorseBreeds.ZORSE) return Voice.ZEBRA;
        return Voice.HORSE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        if (level().isClientSide) {
            this.mouthCounter = 1;
        }
        return switch (voice()) {
            case GHOST -> ModSounds.HORSE_GRUNT_GHOST;
            case UNDEAD -> ModSounds.HORSE_GRUNT_UNDEAD;
            case DONKEY -> ModSounds.DONKEY_GRUNT;
            case ZEBRA -> ModSounds.ZEBRA_GRUNT;
            case HORSE -> ModSounds.HORSE_GRUNT;
        };
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        super.getHurtSound(source);
        return switch (voice()) {
            case GHOST -> ModSounds.HORSE_HURT_GHOST;
            case UNDEAD -> ModSounds.HORSE_HURT_UNDEAD;
            case DONKEY -> ModSounds.DONKEY_HURT;
            case ZEBRA -> ModSounds.ZEBRA_HURT;
            case HORSE -> ModSounds.HORSE_HURT;
        };
    }

    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return switch (voice()) {
            case GHOST -> ModSounds.HORSE_DYING_GHOST;
            case UNDEAD -> ModSounds.HORSE_DYING_UNDEAD;
            case DONKEY -> ModSounds.DONKEY_DYING;
            case ZEBRA, HORSE -> ModSounds.HORSE_DYING;
        };
    }

    @Nullable
    @Override
    protected SoundEvent getAngrySound() {
        return switch (voice()) {
            case GHOST -> ModSounds.HORSE_MAD_GHOST;
            case UNDEAD -> ModSounds.HORSE_MAD_UNDEAD;
            case DONKEY -> ModSounds.DONKEY_GRUNT;
            case ZEBRA -> ModSounds.ZEBRA_GRUNT;
            case HORSE -> ModSounds.HORSE_MAD;
        };
    }

    /** Head bob used by the model's eating pose: staying put (haystack/whip) or grazing. */
    public boolean isHeadDown() {
        return (isStaying() && !isVehicle()) || isEating();
    }

    public float wingFlapProgress() {
        return Mth.clamp(this.wingFlapCounter / 20.0F, 0.0F, 1.0F);
    }
}
