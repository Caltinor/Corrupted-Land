package com.dicemc.corruptedlands.blocks;

import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.CorruptedLandMod.Core;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

public class CorruptedFallingBlock extends FallingBlock implements ICorrupted{

	public CorruptedFallingBlock(Properties properties) {super(properties);}

	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
		double threshold = random.nextDouble();
		if (threshold >= Config.SPREAD_RATE.get()) Core.corruptNeighbors(pos, worldIn);	
	}
	
	@Override
	protected void falling(FallingBlockEntity fallingEntity) {
		fallingEntity.dropItem = false;
	}
}
