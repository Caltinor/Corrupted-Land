package com.dicemc.corruptedlands;

import java.util.Random;

import com.dicemc.corruptedlands.CorruptedLandMod.Core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class CorruptedBlock extends Block{

	protected CorruptedBlock(Properties builder) { super(builder);}

	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		double threshold = random.nextDouble();
		if (threshold >= Config.SPREAD_RATE.get()) Core.corruptNeighbors(pos, worldIn);	
	}
}
