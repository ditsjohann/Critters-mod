package com.critters.registry;

import com.critters.CrittersMod;
import com.critters.block.AquariumBlock;
import com.critters.block.SlimeTrailBlock;
import com.critters.block.TerrariumBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class ModBlocks {
    public static final SlimeTrailBlock SLIME_TRAIL = register("slime_trail", new SlimeTrailBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.NONE)
            .replaceable()
            .noCollission()
            .instabreak()
            .noLootTable()
            .sound(SoundType.HONEY_BLOCK)
            .pushReaction(PushReaction.DESTROY)));

    public static final TerrariumBlock TERRARIUM = register("terrarium", new TerrariumBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(0.6F)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .isValidSpawn((state, level, pos, type) -> false)
            .isRedstoneConductor((state, level, pos) -> false)
            .isSuffocating((state, level, pos) -> false)
            .isViewBlocking((state, level, pos) -> false)));

    public static final AquariumBlock AQUARIUM = register("aquarium", new AquariumBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WATER)
            .strength(0.6F)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .isValidSpawn((state, level, pos, type) -> false)
            .isRedstoneConductor((state, level, pos) -> false)
            .isSuffocating((state, level, pos) -> false)
            .isViewBlocking((state, level, pos) -> false)));

    private ModBlocks() {
    }

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, CrittersMod.id(name), block);
    }

    public static void init() {
        // class loading registers the blocks
    }
}
