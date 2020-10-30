package com.dicemc.corruptedlands;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.core.util.Loader;

import com.jedijoe.ImmortuosCalyx.Infection.InfectionManagerCapability;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CorruptedLandMod.MOD_ID)
public class CorruptedLandMod {
	public static final String MOD_ID = "dicemccl";
	public static Random MASTER_RAND = new Random();
	public static MinecraftServer SERVER;
	public static Map<Block, Block> corruptionPair = new HashMap<Block, Block>();
	public static boolean installedCalyx = false;

	public CorruptedLandMod() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
		Registration.init();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Mod.EventBusSubscriber
	public static class EventHandler {
		
		@SubscribeEvent
		public static void onFleshExpire(ItemExpireEvent event) {
			if (event.getEntityItem().getItem().isItemEqual(new ItemStack(Items.ROTTEN_FLESH)) && !event.getEntityItem().getItem().getOrCreateTag().getBoolean("playerthrown")) {
				BlockPos pos = new BlockPos(event.getEntityItem().getPosX(), event.getEntityItem().getPosY()-1, event.getEntityItem().getPosZ());
				Core.corruptLand(pos, event.getEntityItem().getServer().getWorld(event.getEntityItem().getEntityWorld().getDimensionKey()));
			}
		}
		
		@SubscribeEvent
		public static void onCorruptibleItemDrop(EntityJoinWorldEvent event) {
			if (Config.FLESH_DESPAWN_TIME.get() >= 0) {
				if (event.getEntity() instanceof ItemEntity) {
					if (((ItemEntity) event.getEntity()).getItem().isItemEqual(new ItemStack(Items.ROTTEN_FLESH))) 
						((ItemEntity)event.getEntity()).lifespan = Config.FLESH_DESPAWN_TIME.get();					
				}	
			}
		}
		
		@SuppressWarnings("resource")
		@SubscribeEvent
		public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
			if (!event.getEntity().getEntityWorld().isRemote) {
				if (event.getEntityLiving() instanceof PlayerEntity) {
					PlayerEntity player = (PlayerEntity) event.getEntityLiving();
					BlockState bs = event.getEntityLiving().getEntityWorld().getBlockState(event.getEntityLiving().getPosition().down());
					if (!player.isCreative() && bs.getBlock() instanceof ICorrupted)
						if (!installedCalyx && !player.isPotionActive(Effects.POISON)) player.addPotionEffect(new EffectInstance(Effects.POISON, 25, 0));
						else {
							player.getCapability(InfectionManagerCapability.INSTANCE, null).ifPresent(cap -> {
								if (cap.getInfectionProgress() >= Config.CALYX_EFFECT_LEVEL.get()) {
									player.addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 20, 0));}
								else if (!player.isPotionActive(Effects.POISON)){
									player.addPotionEffect(new EffectInstance(Effects.POISON, 25, 0));
								}
							});							
						}
				}
				if (Config.DAMAGE_ANIMALS.get() && event.getEntityLiving() instanceof AnimalEntity) {
					BlockState bs = event.getEntityLiving().getEntityWorld().getBlockState(event.getEntityLiving().getPosition().down());
					if ((bs.getBlock() instanceof ICorrupted) && !event.getEntityLiving().isPotionActive(Effects.POISON)) 
						event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.POISON, 25, 0));
				}
				if (Config.HEAL_MOBS.get() && event.getEntityLiving() instanceof MonsterEntity) {
					BlockState bs = event.getEntityLiving().getEntityWorld().getBlockState(event.getEntityLiving().getPosition().down());
					if (event.getEntityLiving() instanceof ZombieEntity || event.getEntityLiving() instanceof AbstractSkeletonEntity) {
						if (bs.getBlock() instanceof ICorrupted) 
							event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.INSTANT_DAMAGE, 20, 0));
					}
					else {
						if (bs.getBlock() instanceof ICorrupted) 
							event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 20, 0));
					}
				}
			}
		}		
		
		@SubscribeEvent
		public static void onRottenFleshDrop(ItemTossEvent event) {
			if (event.getEntityItem().getItem().isItemEqual(new ItemStack(Items.ROTTEN_FLESH))) {
				event.getEntityItem().getItem().getOrCreateTag().putBoolean("playerthrown", true);
			}
		}
		
		@SubscribeEvent
		public static void onServerStart(FMLServerStartingEvent event ) {
			CorruptedLandMod.SERVER = event.getServer();
			Registration.mapBlockPairs();
			if (Loader.isClassAvailable("com.jedijoe.ImmortuosCalyx.Infection.InfectionManager")) {
				CorruptedLandMod.installedCalyx = true;
				System.out.println("Calyx Detected");
			}
		}
	}
	
	public static class Core {
		public static void corruptLand(BlockPos pos, ServerWorld serverWorld) {
			BlockState current = serverWorld.getBlockState(pos);
			BlockState future = corruptionPair.getOrDefault(current.getBlock(), Blocks.AIR.getBlock()).getDefaultState();
			if (!future.equals(Blocks.AIR.getDefaultState())) serverWorld.setBlockState(pos, future, BlockFlags.BLOCK_UPDATE);
		}
		
		public static void corruptNeighbors(BlockPos pos, ServerWorld serverWorld) {
			BlockPos neighbor = getRandomNeighbor(pos, serverWorld);
			if (isNeighboredByAir(neighbor, serverWorld)) { corruptLand(neighbor, serverWorld);}			
		}
		
		private static BlockPos getRandomNeighbor(BlockPos pos, ServerWorld serverWorld) {
			int next = CorruptedLandMod.MASTER_RAND.nextInt(3);
			int x = (next == 2 ? pos.getX() + 1 : (next == 1 ? pos.getX() : pos.getX() - 1));
			next = CorruptedLandMod.MASTER_RAND.nextInt(3);
			int y = (next == 2 ? pos.getY() + 1 : (next == 1 ? pos.getY() : pos.getY() - 1));
			next = CorruptedLandMod.MASTER_RAND.nextInt(3);
			int z = (next == 2 ? pos.getZ() + 1 : (next == 1 ? pos.getZ() : pos.getZ() - 1));
			return new BlockPos(x, y, z);
		}
		
		private static boolean isNeighboredByAir(BlockPos pos, ServerWorld serverWorld) {
			BlockState current = serverWorld.getBlockState(pos);
			if (corruptionPair.get(current.getBlock()) == null) return false;
			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					for (int z = -1; z < 2; z++) {
						BlockPos checkPos = new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z);
						BlockState bs = serverWorld.getBlockState(checkPos);
						if (bs.getBlock().equals(Blocks.AIR) || bs.getBlock().equals(Blocks.CAVE_AIR)) {return true;}
					}
				}
			}
			return false;
		}
	}
}
