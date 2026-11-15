package com.menagerie.entity.wyvern;

import com.menagerie.registry.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** A wyvern egg. Use it on a block to set it down; after about half a minute it hatches. */
public class WyvernEggItem extends Item {
    public WyvernEggItem(Properties properties) {
        super(properties);
    }

    public static ItemStack stackOf(int kind) {
        ItemStack stack = new ItemStack(ModItems.WYVERN_EGG);
        CompoundTag tag = new CompoundTag();
        tag.putInt("Kind", kind);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    public static int kindOf(ItemStack stack) {
        int kind = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("Kind");
        return kind < 1 ? Wyvern.JUNGLE : Math.min(kind, Wyvern.KINDS);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.menagerie.wyvern_egg.kind",
                Component.translatable("entity.menagerie.wyvern." + Wyvern.kindKey(kindOf(stack))));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            Vec3 at = context.getClickLocation();
            WyvernEgg egg = WyvernEgg.create(level, kindOf(context.getItemInHand()), at.x, at.y, at.z);
            if (egg != null) {
                egg.setDeltaMovement(Vec3.ZERO);
                level.addFreshEntity(egg);
                context.getItemInHand().consume(1, context.getPlayer());
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
