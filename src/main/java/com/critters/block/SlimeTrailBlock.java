package com.critters.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Thin, glossy slime left behind by snails. Like glow lichen it can sit on the floor and on
 * walls (any face). It slowly dries up and disappears.
 */
public class SlimeTrailBlock extends MultifaceBlock {
    public static final MapCodec<SlimeTrailBlock> CODEC = simpleCodec(SlimeTrailBlock::new);
    /** How long a trail lasts before drying up (ticks). */
    private static final int MIN_LIFETIME = 900;
    private static final int RANDOM_LIFETIME = 900;

    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public SlimeTrailBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<SlimeTrailBlock> codec() {
        return CODEC;
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return spreader;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!oldState.is(this)) {
            level.scheduleTick(pos, this, MIN_LIFETIME + level.random.nextInt(RANDOM_LIFETIME));
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.removeBlock(pos, false);
    }
}
