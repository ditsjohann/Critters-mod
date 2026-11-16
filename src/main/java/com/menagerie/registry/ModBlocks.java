package com.menagerie.registry;

import com.menagerie.Menagerie;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** The Wyvern Lair's blocks (Mo' Creatures' MoCBlockGrass, MoCBlockDirt, MoCBlockRock, MoCBlockLog, ...). */
public final class ModBlocks {
    public static final Block WYVERN_GRASS = register("wyvern_grass", new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)));
    public static final Block WYVERN_DIRT = register("wyvern_dirt", new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));
    public static final Block WYVERN_STONE = register("wyvern_stone", new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final Block WYVERN_LOG = register("wyvern_log", new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final Block WYVERN_PLANKS = register("wyvern_planks", new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final Block WYVERN_LEAVES = register("wyvern_leaves", new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final Block WYVERN_TALL_GRASS = register("wyvern_tall_grass", new TallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)));

    private ModBlocks() {
    }

    private static <T extends Block> T register(String name, T block) {
        T registered = Registry.register(BuiltInRegistries.BLOCK, Menagerie.id(name), block);
        ModItems.registerBlockItem(name, registered);
        return registered;
    }

    public static void init() {
        // class loading registers the blocks
    }
}
