package com.dicemc.corruptedlands.blocks;

import java.util.Random;

import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.CorruptedLandMod.Core;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class CorruptedFallingBlock extends FallingBlock implements ICorrupted{

	public CorruptedFallingBlock(Properties properties) {super(properties);}

	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		double threshold = random.nextDouble();
		
		if (threshold >= Config.SPREAD_RATE.get()) {
			int range = 9;
			for (int y = -range; y <= range; y++) {
				for (int x = -(range-Math.abs(y)); x <= (range-Math.abs(y)); x++) {
					for (int z = -(range-Math.abs(y)); z <= (range-Math.abs(y)); z++) {
						Vector3d pos2 = new Vector3d(pos.getX(),pos.getY(),pos.getZ()).add(x, y, z).add(0,0,0);
						BlockPos pos3 = pos.add(x, y, z);
						if (worldIn.getBlockState(pos3).getBlock() instanceof IPreventCorruption) {
							IPreventCorruption preventor = ((IPreventCorruption)worldIn.getBlockState(pos3).getBlock());
							if (!preventor.allowSpread() && pos2.squareDistanceTo(pos3.getX(),pos3.getY(),pos3.getZ()) <= preventor.getRange()) {
								return;
							}
						}
					}
				}
			}
			
			Core.corruptNeighbors(pos, worldIn);
		}
	}
	
	@Override
	protected void onStartFalling(FallingBlockEntity fallingEntity) {
		fallingEntity.shouldDropItem = false;
	}
}
