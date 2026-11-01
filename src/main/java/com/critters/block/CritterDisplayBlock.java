package com.critters.block;

import com.critters.registry.ModBlockEntities;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.Nullable;

/**
 * Shared logic for the Terrarium (land critters in Critter Jars) and the Aquarium (fish in buckets).
 * <ul>
 *   <li>Use a filled jar / fish bucket on it to put the critter inside.</li>
 *   <li>Sneak + use with an empty hand to take the last critter back out (in its jar / bucket).</li>
 *   <li>Use with an empty hand to see what lives inside.</li>
 * </ul>
 */
public abstract class CritterDisplayBlock extends BaseEntityBlock {

    protected CritterDisplayBlock(Properties properties) {
        super(properties);
    }

    /** true for the aquarium. */
    public abstract boolean isAquarium();

    public int capacity() {
        return isAquarium() ? 4 : 3;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CritterDisplayBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide
                ? createTickerHelper(type, ModBlockEntities.CRITTER_DISPLAY, CritterDisplayBlockEntity::clientTick)
                : null;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                              InteractionHand hand, BlockHitResult hit) {
        EntityType<?> type = CritterDisplayBlockEntity.entityTypeOf(stack);
        if (type == null || !(level.getBlockEntity(pos) instanceof CritterDisplayBlockEntity display)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        boolean aquaticItem = CritterDisplayBlockEntity.isAquaticItem(stack);
        if (aquaticItem != isAquarium()) {
            if (level.isClientSide) {
                player.displayClientMessage(Component.translatable(aquaticItem
                        ? "message.critters.display.not_aquarium"
                        : "message.critters.display.not_terrarium"), true);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (display.isFull()) {
            if (level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.critters.display.full"), true);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (!level.isClientSide) {
            // The jar/bucket goes inside with the critter and comes back out when it is removed.
            display.addCritter(stack.copyWithCount(1));
            stack.consume(1, player);
            level.playSound(null, pos, isAquarium() ? SoundEvents.BUCKET_EMPTY_FISH : SoundEvents.BOTTLE_EMPTY,
                    SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof CritterDisplayBlockEntity display)) {
            return InteractionResult.PASS;
        }
        if (player.isSecondaryUseActive() && !display.isEmpty()) {
            if (!level.isClientSide) {
                ItemStack out = display.removeLast();
                if (!player.getInventory().add(out)) {
                    player.drop(out, false);
                }
                level.playSound(null, pos, isAquarium() ? SoundEvents.BUCKET_FILL_FISH : SoundEvents.BOTTLE_FILL,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (level.isClientSide) {
            player.displayClientMessage(describe(display), true);
        }
        return InteractionResult.SUCCESS;
    }

    private Component describe(CritterDisplayBlockEntity display) {
        Component name = this.getName();
        List<ItemStack> critters = display.getCritters();
        if (critters.isEmpty()) {
            return Component.translatable("message.critters.display.empty", name);
        }
        MutableComponent names = Component.empty();
        for (int i = 0; i < critters.size(); i++) {
            if (i > 0) {
                names.append(", ");
            }
            EntityType<?> type = CritterDisplayBlockEntity.entityTypeOf(critters.get(i));
            names.append(type != null ? type.getDescription() : critters.get(i).getHoverName());
        }
        return Component.translatable("message.critters.display.contents", name, critters.size(), capacity(), names);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof CritterDisplayBlockEntity display) {
            for (ItemStack stack : display.getCritters()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack.copy());
            }
            display.clearCritters();
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(isAquarium() ? "tooltip.critters.display.aquarium" : "tooltip.critters.display.terrarium",
                capacity()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.critters.display.remove").withStyle(ChatFormatting.GRAY));
    }
}
