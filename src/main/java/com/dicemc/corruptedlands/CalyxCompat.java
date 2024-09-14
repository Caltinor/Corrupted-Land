package com.dicemc.corruptedlands;

//import java.util.ArrayList;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import com.cartoonishvillain.immortuoscalyx.Register;
//import com.cartoonishvillain.immortuoscalyx.damage.InternalOrganDamage;
//import com.cartoonishvillain.immortuoscalyx.entities.InfectedEntity;
//import com.cartoonishvillain.immortuoscalyx.infection.InfectionManager;
//import com.cartoonishvillain.immortuoscalyx.infection.InfectionManagerCapability;
//import com.dicemc.corruptedlands.Config;
//import com.dicemc.corruptedlands.blocks.ICorrupted;
//
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
//import net.minecraftforge.common.capabilities.Capability;

public class CalyxCompat {
	public static Object calyxCap = null;
//
	public static void init() {
//		calyxCap = InfectionManagerCapability.INSTANCE;
	}
//
	public static boolean calyxDetected(Player player) { return false;
//		return player.getCapability(calyxCap, null).map(cap -> {
//			if (((InfectionManager) cap).getInfectionProgress() >= Config.CALYX_EFFECT_LEVEL.get()) {
//				player.heal(Config.CORRUPTION_EFFECT_POWER.get());
//				return true;}
//			else if (player.getHealth() > Config.CORRUPTION_EFFECT_POWER.get()){
//				player.hurt(new DamageSource(blockID.toString()), Config.CORRUPTION_EFFECT_POWER.get());
//				return true;
//			}
//			else return true;
//		}).orElse(false);
	}
//
	public static boolean corrupt(ItemStack item) {return false;
//		return calyxCap != null && Config.CALYX_EGGS_CORRUPT_LAND.get() && item.sameItem(new ItemStack(Register.IMMORTUOSCALYXEGGS.get()));
	}
//
	public static boolean isInfected(LivingEntity entity) {return false;
//		return calyxCap != null && entity instanceof InfectedEntity;
	}
//
	public static void purifierHarm(AABB box, Level level) {
//		//increases radius a bit
//		box.setMaxY(box.maxY + 3);
//		box.setMinY(box.minY - 3);
//		box.setMaxX(box.maxX + 3);
//		box.setMinX(box.minX - 3);
//		box.setMaxZ(box.maxZ + 3);
//		box.setMinZ(box.minZ - 3);
//		ArrayList<Entity> entities = (ArrayList<Entity>) level.getEntities(null, box);
//		ArrayList<LivingEntity> LivingEntities = new ArrayList<>();
//		//Grab all suitably infected entities.
//		for(Entity entity : entities){
//			if(entity instanceof InfectedEntity) LivingEntities.add((LivingEntity) entity);
//			else if (entity instanceof LivingEntity){
//				AtomicInteger infectionRate = new AtomicInteger(0);
//				entity.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{infectionRate.set(h.getInfectionProgress());});
//				if(infectionRate.get() >= Config.CALYX_EFFECT_LEVEL.get()) LivingEntities.add((LivingEntity) entity);
//			}
//		}
//		//Negatively Effect them.
//		for(LivingEntity livingEntity : LivingEntities){
//			livingEntity.hurt(InternalOrganDamage.causeInternalDamage(livingEntity), (livingEntity.getMaxHealth()/5));
//			livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 90*20, 1, true, true));
//			livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 90*20, 1, true, true));
//		}
	}
}
