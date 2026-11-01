package com.critters.item;

import com.critters.registry.CritterSpecies;
import com.critters.registry.ModItems;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * The empty Critter Jar catches any land critter (use it on the critter). Each critter has its own
 * filled jar item, which remembers the critter's data (name, health, ...). Use a filled jar on a
 * block to release the critter, or on a Terrarium to put it on display.
 */
public class CritterJarItem extends Item {
    private static final String CRITTER_KEY = "Critter";
    /** Entity data that should not travel with the critter. */
    private static final String[] STRIPPED = {"Pos", "Motion", "Rotation", "UUID", "FallDistance", "OnGround",
            "PortalCooldown", "Fire", "Leash"};

    @Nullable
    private final CritterSpecies species;

    public CritterJarItem(@Nullable CritterSpecies species, Properties properties) {
        super(properties);
        this.species = species;
    }

    /** The critter in this jar, or null for the empty jar. */
    @Nullable
    public CritterSpecies species() {
        return species;
    }

    // ------------------------------------------------------------------
    // Catching
    // ------------------------------------------------------------------

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (this.species != null) {
            return InteractionResult.PASS;
        }
        CritterSpecies caught = CritterSpecies.byType(target.getType());
        if (caught == null || caught.isAquatic() || !target.isAlive()) {
            if (player.level().isClientSide && target instanceof Mob && caught == null) {
                player.displayClientMessage(Component.translatable("message.critters.jar.cannot_catch"), true);
            }
            return InteractionResult.PASS;
        }
        Level level = player.level();
        if (!level.isClientSide) {
            ItemStack filled = new ItemStack(caught.container());
            CompoundTag data = new CompoundTag();
            target.saveWithoutId(data);
            for (String key : STRIPPED) {
                data.remove(key);
            }
            CompoundTag wrapper = new CompoundTag();
            wrapper.put(CRITTER_KEY, data);
            CustomData.set(DataComponents.CUSTOM_DATA, filled, wrapper);

            level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.BOTTLE_FILL,
                    SoundSource.PLAYERS, 1.0F, 1.3F);
            target.discard();
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, filled, false));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // ------------------------------------------------------------------
    // Releasing
    // ------------------------------------------------------------------

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (this.species == null) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }
        BlockPos clicked = context.getClickedPos();
        BlockState state = level.getBlockState(clicked);
        BlockPos pos = state.getCollisionShape(level, clicked).isEmpty() ? clicked : clicked.relative(context.getClickedFace());

        Entity entity = this.species.type().create(serverLevel);
        if (entity == null) {
            return InteractionResult.FAIL;
        }
        ItemStack stack = context.getItemInHand();
        CompoundTag data = critterData(stack);
        if (!data.isEmpty()) {
            entity.load(data);
        }
        float yaw = level.random.nextFloat() * 360.0F;
        entity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, yaw, 0.0F);
        if (entity instanceof Mob mob) {
            mob.setPersistenceRequired(); // released critters stay put
            mob.yHeadRot = yaw;
            mob.yBodyRot = yaw;
        }
        serverLevel.addFreshEntity(entity);
        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 1.0F, 1.2F);

        Player player = context.getPlayer();
        if (player == null || !player.getAbilities().instabuild) {
            ItemStack empty = new ItemStack(ModItems.CRITTER_JAR);
            stack.shrink(1);
            if (player != null) {
                if (stack.isEmpty()) {
                    player.setItemInHand(context.getHand(), empty);
                } else if (!player.getInventory().add(empty)) {
                    player.drop(empty, false);
                }
            }
        }
        return InteractionResult.CONSUME;
    }

    public static CompoundTag critterData(ItemStack stack) {
        CustomData custom = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return custom.copyTag().getCompound(CRITTER_KEY);
    }

    // ------------------------------------------------------------------
    // Tooltip
    // ------------------------------------------------------------------

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (this.species == null) {
            tooltip.add(Component.translatable("tooltip.critters.jar.empty").withStyle(ChatFormatting.GRAY));
            return;
        }
        CompoundTag data = critterData(stack);
        HolderLookup.Provider registries = context.registries();
        if (registries != null && data.contains("CustomName", 8)) {
            Component name = Component.Serializer.fromJson(data.getString("CustomName"), registries);
            if (name != null) {
                tooltip.add(Component.translatable("tooltip.critters.jar.name", name).withStyle(ChatFormatting.AQUA));
            }
        }
        tooltip.add(Component.translatable("tooltip.critters.jar.filled").withStyle(ChatFormatting.GRAY));
    }
}
