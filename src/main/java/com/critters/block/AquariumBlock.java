package com.critters.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class AquariumBlock extends CritterDisplayBlock {
    public static final MapCodec<AquariumBlock> CODEC = simpleCodec(AquariumBlock::new);

    public AquariumBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isAquarium() {
        return true;
    }
}
