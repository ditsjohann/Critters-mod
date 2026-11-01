package com.critters.block;

import com.critters.item.CritterJarItem;
import com.critters.registry.CritterSpecies;
import com.critters.registry.ModBlockEntities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

/**
 * Holds the critters living in a Terrarium or Aquarium. Critters are stored as the item they came
 * in (a filled Critter Jar or a fish bucket), so taking them out gives exactly that item back.
 * <p>
 * On the client it also keeps a small list of {@link DisplayCritter}s: fake entities that wander
 * around inside the glass and are drawn by the block entity renderer.
 */
public class CritterDisplayBlockEntity extends BlockEntity {
    private final List<ItemStack> critters = new ArrayList<>();

    /** Client only: animated stand-ins for the stored critters. */
    private final List<DisplayCritter> display = new ArrayList<>();
    private boolean displayDirty = true;

    public CritterDisplayBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRITTER_DISPLAY, pos, state);
    }

    // ------------------------------------------------------------------
    // Which items can go in
    // ------------------------------------------------------------------

    /** The entity type held by a filled jar or a water mob bucket, or null if the item holds no critter. */
    @Nullable
    public static EntityType<?> entityTypeOf(ItemStack stack) {
        if (stack.getItem() instanceof CritterJarItem jar && jar.species() != null) {
            return jar.species().type();
        }
        if (stack.getItem() instanceof MobBucketItem bucket && bucket.content == Fluids.WATER) {
            return bucket.type;
        }
        return null;
    }

    public static boolean isAquaticItem(ItemStack stack) {
        return stack.getItem() instanceof MobBucketItem;
    }

    // ------------------------------------------------------------------
    // Contents
    // ------------------------------------------------------------------

    public boolean isAquarium() {
        return getBlockState().getBlock() instanceof CritterDisplayBlock b && b.isAquarium();
    }

    public int capacity() {
        return getBlockState().getBlock() instanceof CritterDisplayBlock b ? b.capacity() : 3;
    }

    public List<ItemStack> getCritters() {
        return Collections.unmodifiableList(critters);
    }

    public boolean isEmpty() {
        return critters.isEmpty();
    }

    public boolean isFull() {
        return critters.size() >= capacity();
    }

    public void addCritter(ItemStack stack) {
        critters.add(stack);
        sync();
    }

    public ItemStack removeLast() {
        if (critters.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack out = critters.remove(critters.size() - 1);
        sync();
        return out;
    }

    public void clearCritters() {
        critters.clear();
        setChanged();
    }

    private void sync() {
        setChanged();
        displayDirty = true;
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    // ------------------------------------------------------------------
    // Saving / syncing
    // ------------------------------------------------------------------

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag list = new ListTag();
        for (ItemStack stack : critters) {
            if (!stack.isEmpty()) {
                list.add(stack.save(registries));
            }
        }
        tag.put("Critters", list);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        critters.clear();
        ListTag list = tag.getList("Critters", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            ItemStack.parse(registries, list.getCompound(i)).ifPresent(critters::add);
        }
        displayDirty = true;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // ------------------------------------------------------------------
    // Client-side animation
    // ------------------------------------------------------------------

    public List<DisplayCritter> getDisplay() {
        return display;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CritterDisplayBlockEntity be) {
        if (be.displayDirty) {
            be.rebuildDisplay(level);
        }
        boolean aquarium = be.isAquarium();
        for (DisplayCritter critter : be.display) {
            critter.tick(aquarium, pos, level.random);
        }
    }

    private void rebuildDisplay(Level level) {
        displayDirty = false;
        // Keep the critters that are still there (contents only change at the end of the list)
        int keep = 0;
        while (keep < display.size() && keep < critters.size()
                && display.get(keep).source().getItem() == critters.get(keep).getItem()) {
            keep++;
        }
        while (display.size() > keep) {
            display.remove(display.size() - 1);
        }
        for (int i = keep; i < critters.size(); i++) {
            ItemStack stack = critters.get(i);
            EntityType<?> type = entityTypeOf(stack);
            if (type == null) {
                continue;
            }
            Entity entity = type.create(level);
            if (entity == null) {
                continue;
            }
            if (entity instanceof Bucketable bucketable) {
                // restores variants, e.g. tropical fish patterns
                bucketable.loadFromBucketTag(stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).copyTag());
            }
            CritterSpecies species = CritterSpecies.byType(type);
            display.add(new DisplayCritter(stack, entity, species, level.random));
        }
    }
}
