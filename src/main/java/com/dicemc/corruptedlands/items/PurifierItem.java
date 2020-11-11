package com.dicemc.corruptedlands.items;

import java.util.List;
import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.blocks.ICorrupted;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class PurifierItem extends Item{

	public PurifierItem(Properties properties) {
		super(properties);
	}
	
	@SuppressWarnings("resource")
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote && context.getWorld().getBlockState(context.getPos()).getBlock() instanceof ICorrupted) {
			//List<BlockPos> states = new ArrayList<BlockPos>(); 
			final AxisAlignedBB box = new AxisAlignedBB(context.getPos()).grow(Config.PURIFIER_RANGE.get());
			BlockPos p;
			BlockState s;
			outOfCharge:
			for(double x = box.minX; x < box.maxX; x++) {
				for(double y = box.minY; y < box.maxY; y++) {
					for(double z = box.minZ; z < box.maxZ; z++) {
						p = new BlockPos(x, y, z);
						s = context.getWorld().getBlockState(p);
						if (s.getBlock() instanceof ICorrupted) {
							if (context.getItem().getDamage() <= (context.getItem().getMaxDamage() - (Config.PURIFIER_DRAIN_RATE.get() * 2))) {
								List<ItemStack> drop = Block.getDrops(s, context.getPlayer().getServer().func_241755_D_(), p, null);
								context.getWorld().setBlockState(p, (drop.size() == 0 ? Blocks.AIR.getDefaultState() : Block.getBlockFromItem(drop.get(0).getItem()).getDefaultState()));
								context.getItem().damageItem(Config.PURIFIER_DRAIN_RATE.get(), context.getPlayer(), (player) -> {});
							}
							else break outOfCharge;
						}
					}
				}	
			}
			return ActionResultType.CONSUME;
		}
		return ActionResultType.FAIL;
	}
}
