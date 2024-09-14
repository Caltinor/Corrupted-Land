package com.dicemc.corruptedlands;

import java.util.List;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.common.CommonHooks;

public class PurifierItem extends Item{

	public PurifierItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getLevel() instanceof ServerLevel level && level.getChunk(context.getClickedPos()).getData(Registration.CORRUPT).contains(context.getClickedPos())) {
			ServerPlayer plyr = (ServerPlayer) context.getPlayer();
			final AABB box = new AABB(context.getClickedPos()).inflate(Config.PURIFIER_RANGE.get());
			BlockPos p;
			BlockState s;	
			int currentDamage = context.getItemInHand().getDamageValue();
			for(double x = box.minX; x < box.maxX; x++) {
				for(double y = box.minY; y < box.maxY; y++) {
					for(double z = box.minZ; z < box.maxZ; z++) {
						p = new BlockPos((int) x, (int) y, (int) z);
						s = context.getLevel().getBlockState(p);
						if (level.getChunk(p).getData(Registration.CORRUPT).contains(p)) {
							if (currentDamage < context.getItemInHand().getMaxDamage() - 1 - Config.PURIFIER_DRAIN_RATE.get()) {
								level.getChunk(p).getData(Registration.CORRUPT).remove(p);
								context.getItemInHand().hurtAndBreak(Config.PURIFIER_DRAIN_RATE.get(), context.getPlayer(), context.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
								currentDamage += Config.PURIFIER_DRAIN_RATE.get();
							}
							else return InteractionResult.SUCCESS;
						}
					}
				}	
			}
			//Purifier used within range of Infected entities can harm and debuff
			if(CalyxCompat.calyxCap != null && Config.CALYX_HARMED_BY_PURIFIER.get()){
				CalyxCompat.purifierHarm(box, context.getLevel());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player
				&& (player.getMainHandItem().equals(stack) || player.getOffhandItem().equals(stack))
				&& level.isDay() && level.canSeeSky(entity.getOnPos())) {
			stack.hurtAndBreak(Config.PURIFIER_RECHARGE_RATE.get(), player, player.getEquipmentSlotForItem(stack));
		}
		super.inventoryTick(stack, level, entity, slotId, isSelected);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}
}
