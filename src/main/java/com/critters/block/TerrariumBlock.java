package com.critters.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class TerrariumBlock extends CritterDisplayBlock {
    public static final MapCodec<TerrariumBlock> CODEC = simpleCodec(TerrariumBlock::new);

    public TerrariumBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isAquarium() {
        return false;
    }
}
