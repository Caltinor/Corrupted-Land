package com.dicemc.corruptedlands.items;

import java.util.List;
import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.blocks.ICorrupted;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeHooks;

public class PurifierItem extends Item{

	public PurifierItem(Properties properties) {
		super(properties);
	}
	
	@SuppressWarnings("resource")
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		if (!context.getLevel().isClientSide && context.getLevel().getBlockState(context.getClickedPos()).getBlock() instanceof ICorrupted) {
			ServerPlayerEntity plyr = (ServerPlayerEntity) context.getPlayer();
			final AxisAlignedBB box = new AxisAlignedBB(context.getClickedPos()).inflate(Config.PURIFIER_RANGE.get());
			BlockPos p;
			BlockState s;	
			int currentDamage = context.getItemInHand().getDamageValue();
			for(double x = box.minX; x < box.maxX; x++) {
				for(double y = box.minY; y < box.maxY; y++) {
					for(double z = box.minZ; z < box.maxZ; z++) {
						p = new BlockPos(x, y, z);
						s = context.getLevel().getBlockState(p);
						if (s.getBlock() instanceof ICorrupted) {
							if (currentDamage < context.getItemInHand().getMaxDamage() - 1 - Config.PURIFIER_DRAIN_RATE.get()) {
								List<ItemStack> drop = Block.getDrops(s, context.getPlayer().getServer().overworld(), p, null);
								if (ForgeHooks.onBlockBreakEvent(context.getLevel(), plyr.gameMode.getGameModeForPlayer(), plyr, p) >= 0) {
									context.getLevel().setBlockAndUpdate(p, (drop.size() == 0 ? Blocks.AIR.defaultBlockState() : Block.byItem(drop.get(0).getItem()).defaultBlockState()));
									context.getItemInHand().hurtAndBreak(Config.PURIFIER_DRAIN_RATE.get(), context.getPlayer(), (player) -> {});
									currentDamage += Config.PURIFIER_DRAIN_RATE.get();
								}
							}
							else return ActionResultType.SUCCESS;
						}
					}
				}	
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}
}
