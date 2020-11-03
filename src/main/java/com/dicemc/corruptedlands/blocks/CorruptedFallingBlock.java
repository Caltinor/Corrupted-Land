package com.dicemc.corruptedlands.blocks;

import java.util.Random;

import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.CorruptedLandMod.Core;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class CorruptedFallingBlock extends FallingBlock implements ICorrupted{

	public CorruptedFallingBlock(Properties properties) {super(properties);}

	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		double threshold = random.nextDouble();
		if (threshold >= Config.SPREAD_RATE.get()) Core.corruptNeighbors(pos, worldIn);	
	}
	
	@Override
	protected void onStartFalling(FallingBlockEntity fallingEntity) {
		fallingEntity.shouldDropItem = false;
	}
}
