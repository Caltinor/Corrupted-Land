package com.dicemc.corruptedlands.blocks.mystical_pumpkins;

import blueduck.mysticalpumpkins.block.MysticalPumpkinBlock;
import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.CorruptedLandMod;
import com.dicemc.corruptedlands.Registration;
import com.dicemc.corruptedlands.blocks.ICorrupted;
import com.dicemc.corruptedlands.blocks.IPreventCorruption;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class CorruptedPumpkinBlock extends MysticalPumpkinBlock implements ICorrupted, IPreventCorruption {
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		double threshold = random.nextDouble();
		if (threshold >= Config.SPREAD_RATE.get()) CorruptedLandMod.Core.corruptNeighbors(pos, worldIn);
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if (!entityIn.isImmuneToFire() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn) && this.equals(Registration.CORRUPTED_OBSIDIAN_BLOCK.get())) {
			entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
		}
		super.onEntityWalk(worldIn, pos, entityIn);
	}
	
	@Override
	public boolean allowSpread() {
		return true;
	}
	
	@Override
	public double getRange() {
		return 16;
	}
}
