package com.dicemc.corruptedlands.blocks;

import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.CorruptedLandMod.Core;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

public class CorruptedBreakableBlock extends HalfTransparentBlock implements ICorrupted{

	public CorruptedBreakableBlock(Properties properties) {super(properties);}
	
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
		double threshold = random.nextDouble();
		if (threshold >= Config.SPREAD_RATE.get()) Core.corruptNeighbors(pos, worldIn);	
	}

}
