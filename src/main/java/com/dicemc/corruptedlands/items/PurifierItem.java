package com.dicemc.corruptedlands.items;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.cartoonishvillain.ImmortuosCalyx.Entity.InfectedEntity;
import com.cartoonishvillain.ImmortuosCalyx.Infection.InfectionManagerCapability;
import com.cartoonishvillain.ImmortuosCalyx.InternalOrganDamage;
import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.CorruptedLandMod;
import com.dicemc.corruptedlands.blocks.ICorrupted;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraftforge.common.ForgeHooks;

import net.minecraft.world.item.Item.Properties;

public class PurifierItem extends Item{

	public PurifierItem(Properties properties) {
		super(properties);
	}
	
	@SuppressWarnings("resource")
	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (!context.getLevel().isClientSide && context.getLevel().getBlockState(context.getClickedPos()).getBlock() instanceof ICorrupted) {
			ServerPlayer plyr = (ServerPlayer) context.getPlayer();
			final AABB box = new AABB(context.getClickedPos()).inflate(Config.PURIFIER_RANGE.get());
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
							else return InteractionResult.SUCCESS;
						}
					}
				}	
			}
			//Purifier used within range of Infected entities can harm and debuff
			if(CorruptedLandMod.calyxCap != null && Config.CALYX_HARMED_BY_PURIFIER.get()){
				//increases radius a bit
				box.setMaxY(box.maxY + 3);
				box.setMinY(box.minY - 3);
				box.setMaxX(box.maxX + 3);
				box.setMinX(box.minX - 3);
				box.setMaxZ(box.maxZ + 3);
				box.setMinZ(box.minZ - 3);
				ArrayList<Entity> entities = (ArrayList<Entity>) context.getLevel().getEntities(null, box);
				ArrayList<LivingEntity> LivingEntities = new ArrayList<>();
				//Grab all suitably infected entities.
				for(Entity entity : entities){
					if(entity instanceof InfectedEntity) LivingEntities.add((LivingEntity) entity);
					else if (entity instanceof LivingEntity){
						AtomicInteger infectionRate = new AtomicInteger(0);
						entity.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{infectionRate.set(h.getInfectionProgress());});
						if(infectionRate.get() >= Config.CALYX_EFFECT_LEVEL.get()) LivingEntities.add((LivingEntity) entity);
					}
				}
				//Negatively Effect them.
				for(LivingEntity livingEntity : LivingEntities){
					livingEntity.hurt(InternalOrganDamage.causeInternalDamage(livingEntity), (livingEntity.getMaxHealth()/5));
					livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 90*20, 1, true, true));
					livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 90*20, 1, true, true));
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}
}
