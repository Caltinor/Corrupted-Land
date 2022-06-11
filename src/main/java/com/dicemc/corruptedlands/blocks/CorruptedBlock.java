package com.dicemc.corruptedlands.blocks;

import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.Registration;
import com.dicemc.corruptedlands.CorruptedLandMod.Core;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

public class CorruptedBlock extends Block implements ICorrupted{

	public CorruptedBlock(Properties builder) { super(builder);}

	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
		double threshold = random.nextDouble();
		if (threshold >= Config.SPREAD_RATE.get()) Core.corruptNeighbors(pos, worldIn);	
	}
	
	@Override
	public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
		if (!entityIn.fireImmune() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn) && this.equals(Registration.CORRUPTED_OBSIDIAN_BLOCK.get())) {
			entityIn.hurt(DamageSource.HOT_FLOOR, 1.0F);
		}
		super.stepOn(worldIn, pos, state, entityIn);
 	}
}
