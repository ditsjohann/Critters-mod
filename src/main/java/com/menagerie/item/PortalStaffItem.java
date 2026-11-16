package com.menagerie.item;

import com.menagerie.Menagerie;
import com.menagerie.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * The Portal Staff: carries its user to the Wyvern Lair and back. Ported from Mo' Creatures'
 * {@code ItemStaffPortal} (DrZhark, GPL-3.0): the staff remembers where you left from and takes one
 * point of damage per trip into the Lair. Arrivals land on a quartz platform at the Lair's centre.
 *
 * <p>New: you can return from anywhere in the Lair, not only from the arrival platform.
 */
public class PortalStaffItem extends Item {
    public static final ResourceKey<Level> WYVERN_LAIR = ResourceKey.create(Registries.DIMENSION, Menagerie.id("wyvern_lair"));

    public PortalStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide || !(player instanceof ServerPlayer serverPlayer) || player.isPassenger() || player.isVehicle()) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
        MinecraftServer server = serverPlayer.server;
        if (level.dimension() != WYVERN_LAIR) {
            ServerLevel lair = server.getLevel(WYVERN_LAIR);
            if (lair == null) {
                player.displayClientMessage(Component.translatable("message.menagerie.portal_staff.no_lair"), true);
                return InteractionResultHolder.fail(stack);
            }
            CompoundTag tag = new CompoundTag();
            tag.putString("Dimension", level.dimension().location().toString());
            tag.putDouble("X", player.getX());
            tag.putDouble("Y", player.getY());
            tag.putDouble("Z", player.getZ());
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            BlockPos arrival = arrivalPlatform(lair);
            serverPlayer.teleportTo(lair, arrival.getX() + 0.5, arrival.getY(), arrival.getZ() + 0.5, player.getYRot(), player.getXRot());
            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        } else {
            CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            ServerLevel back = null;
            if (tag.contains("Dimension")) {
                ResourceLocation id = ResourceLocation.tryParse(tag.getString("Dimension"));
                if (id != null) {
                    back = server.getLevel(ResourceKey.create(Registries.DIMENSION, id));
                }
            }
            if (back == null) {
                // a staff that has never been used: go to the overworld spawn
                back = server.overworld();
                BlockPos top = back.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, back.getSharedSpawnPos());
                serverPlayer.teleportTo(back, top.getX() + 0.5, top.getY(), top.getZ() + 0.5, player.getYRot(), player.getXRot());
            } else {
                serverPlayer.teleportTo(back, tag.getDouble("X"), tag.getDouble("Y") + 0.1, tag.getDouble("Z"), player.getYRot(), player.getXRot());
            }
        }
        serverPlayer.serverLevel().playSound(null, serverPlayer.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.getCooldowns().addCooldown(this, 40);
        return InteractionResultHolder.sidedSuccess(stack, false);
    }

    /** Finds (or builds) the quartz arrival platform at the Lair's centre, like the original's portal structure. */
    private static BlockPos arrivalPlatform(ServerLevel lair) {
        lair.getChunk(0, 0); // make sure the centre is generated before looking at it
        BlockPos top = lair.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlockPos.ZERO);
        int y = top.getY() > lair.getMinBuildHeight() + 1 ? top.getY() : 70;
        BlockPos centre = new BlockPos(0, y, 0);
        if (!lair.getBlockState(centre.below()).is(Blocks.QUARTZ_BLOCK)) {
            for (int dx = -3; dx <= 3; dx++) {
                for (int dz = -3; dz <= 3; dz++) {
                    boolean rim = Math.abs(dx) == 3 || Math.abs(dz) == 3;
                    lair.setBlockAndUpdate(centre.offset(dx, -1, dz),
                            rim ? Blocks.QUARTZ_PILLAR.defaultBlockState() : Blocks.QUARTZ_BLOCK.defaultBlockState());
                    for (int dy = 0; dy < 3; dy++) {
                        lair.setBlockAndUpdate(centre.offset(dx, dy, dz), Blocks.AIR.defaultBlockState());
                    }
                }
            }
            for (int[] corner : new int[][]{{-3, -3}, {3, -3}, {-3, 3}, {3, 3}}) {
                for (int dy = 0; dy < 3; dy++) {
                    lair.setBlockAndUpdate(centre.offset(corner[0], dy, corner[1]), Blocks.QUARTZ_PILLAR.defaultBlockState());
                }
                lair.setBlockAndUpdate(centre.offset(corner[0], 3, corner[1]), ModBlocks.WYVERN_LEAVES.defaultBlockState());
            }
        }
        return centre;
    }
}
