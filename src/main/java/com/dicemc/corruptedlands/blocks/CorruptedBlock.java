package com.dicemc.corruptedlands.blocks;

import java.util.Random;

import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.Registration;
import com.dicemc.corruptedlands.CorruptedLandMod.Core;

import com.dicemc.corruptedlands.items.mystical_pumpkins.CorruptedPumpkinItem;
import com.dicemc.corruptedlands.items.mystical_pumpkins.PureHeartedPumpkinItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CorruptedBlock extends Block implements ICorrupted{

	public CorruptedBlock(Properties builder) { super(builder);}

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
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if(entityIn instanceof LivingEntity) {
			if (
					((LivingEntity)entityIn).getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof CorruptedPumpkinItem ||
							((LivingEntity)entityIn).getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof PureHeartedPumpkinItem
			) {
				super.onEntityWalk(worldIn, pos, entityIn);
				return;
			}
		}
		
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
		
		if (!entityIn.isImmuneToFire() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn) && this.equals(Registration.CORRUPTED_OBSIDIAN_BLOCK.get())) {
			entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
		}
		super.onEntityWalk(worldIn, pos, entityIn);
 	}
}
