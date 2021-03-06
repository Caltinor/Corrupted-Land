package com.dicemc.corruptedlands;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Loader;

import com.dicemc.corruptedlands.blocks.ICorrupted;
import com.dicemc.corruptedlands.items.PurifierItem;
import com.dicemc.corruptedlands.items.PurifierRecipe;
import com.jedijoe.ImmortuosCalyx.Infection.InfectionManagerCapability;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(CorruptedLandMod.MOD_ID)
public class CorruptedLandMod {
	public static final String MOD_ID = "dicemccl";
	public static final Logger LOG = LogManager.getLogger(MOD_ID);
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
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, this::registerRecipeSerializers);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Mod.EventBusSubscriber
	public static class EventHandler {
		
		@SubscribeEvent
		public static void onFleshExpire(ItemExpireEvent event) {
			if (event.getEntityItem().getItem().sameItem(new ItemStack(Items.ROTTEN_FLESH)) && !event.getEntityItem().getItem().getOrCreateTag().getBoolean("playerthrown")) {
				BlockPos pos = new BlockPos(event.getEntityItem().getX(), event.getEntityItem().getY()-1, event.getEntityItem().getZ());
				Core.corruptLand(pos, event.getEntityItem().getServer().getLevel(event.getEntityItem().getCommandSenderWorld().dimension()));
			}
		}
		
		@SubscribeEvent
		public static void onCorruptibleItemDrop(EntityJoinWorldEvent event) {
			if (Config.FLESH_DESPAWN_TIME.get() >= 0) {
				if (event.getEntity() instanceof ItemEntity) {
					if (((ItemEntity) event.getEntity()).getItem().sameItem(new ItemStack(Items.ROTTEN_FLESH))) 
						((ItemEntity)event.getEntity()).lifespan = Config.FLESH_DESPAWN_TIME.get();					
				}	
			}
		}
		
		@SuppressWarnings("resource")
		@SubscribeEvent
		public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
			if (!event.getEntity().getCommandSenderWorld().isClientSide && (event.getEntity().getCommandSenderWorld().getGameTime() % 10) == 0) {
				if (event.getEntityLiving() instanceof PlayerEntity) {
					PlayerEntity player = (PlayerEntity) event.getEntityLiving();
					//purifier stone charge check
					if (player.getCommandSenderWorld().canSeeSky(player.blockPosition()) && player.getCommandSenderWorld().isDay())	{
						if (player.getMainHandItem().getItem() instanceof PurifierItem) player.getMainHandItem().hurtAndBreak(Config.PURIFIER_RECHARGE_RATE.get(), player, (p) -> {});
						if (player.getOffhandItem().getItem() instanceof PurifierItem) player.getOffhandItem().hurtAndBreak(Config.PURIFIER_RECHARGE_RATE.get(), player, (p) -> {});
					}
					//Corruption check
					BlockState bs = event.getEntityLiving().getCommandSenderWorld().getBlockState(event.getEntityLiving().blockPosition().below());
					if (!player.isCreative() && bs.getBlock() instanceof ICorrupted && player.getHealth() > Config.CORRUPTION_EFFECT_POWER.get())
						player.getCapability(InfectionManagerCapability.INSTANCE, null).ifPresent(cap -> {
							if (cap.getInfectionProgress() >= Config.CALYX_EFFECT_LEVEL.get()) {
								player.heal(Config.CORRUPTION_EFFECT_POWER.get());}
							else {
								player.hurt(new DamageSource(bs.getBlock().getRegistryName().toString()), Config.CORRUPTION_EFFECT_POWER.get());
							}
						});							
				}
				if (Config.DAMAGE_ANIMALS.get() && event.getEntityLiving() instanceof AnimalEntity) {
					BlockState bs = event.getEntityLiving().getCommandSenderWorld().getBlockState(event.getEntityLiving().blockPosition().below());
					if ((bs.getBlock() instanceof ICorrupted) && event.getEntityLiving().getHealth() > Config.CORRUPTION_EFFECT_POWER.get()) 
						event.getEntityLiving().hurt(new DamageSource(bs.getBlock().getRegistryName().toString()), Config.CORRUPTION_EFFECT_POWER.get());
				}
				if (Config.HEAL_MOBS.get() && event.getEntityLiving() instanceof MonsterEntity) {
					BlockState bs = event.getEntityLiving().getCommandSenderWorld().getBlockState(event.getEntityLiving().blockPosition().below());
					if (bs.getBlock() instanceof ICorrupted) {
						event.getEntityLiving().heal(Config.CORRUPTION_EFFECT_POWER.get());
					}
				}
			}
		}		
		
		@SubscribeEvent
		public static void onRottenFleshDrop(ItemTossEvent event) {
			if (event.getEntityItem().getItem().sameItem(new ItemStack(Items.ROTTEN_FLESH))) {
				event.getEntityItem().getItem().getOrCreateTag().putBoolean("playerthrown", true);
			}
		}
		
		@SubscribeEvent
		public static void onServerStart(FMLServerStartingEvent event ) {
			CorruptedLandMod.SERVER = event.getServer();
			Registration.mapBlockPairs();
			if (Loader.isClassAvailable("com.jedijoe.ImmortuosCalyx.Infection.InfectionManager")) {
				CorruptedLandMod.installedCalyx = true;
				LOG.info("Calyx detected. Initializing support.");
			}
		}
	}
	
	public static class Core {
		public static void corruptLand(BlockPos pos, ServerWorld serverWorld) {
			BlockState current = serverWorld.getBlockState(pos);
			BlockState future = corruptionPair.getOrDefault(current.getBlock(), Blocks.AIR.getBlock()).defaultBlockState();
			if (!future.equals(Blocks.AIR.defaultBlockState())) {
		        if (!MinecraftForge.EVENT_BUS.post(new BlockEvent.CropGrowEvent.Post(serverWorld, pos, current, future)))
		        	serverWorld.setBlock(pos, future, BlockFlags.BLOCK_UPDATE);
			}
		}
		
		public static void corruptNeighbors(BlockPos pos, ServerWorld serverWorld) {
			BlockPos neighbor = getRandomNeighbor(pos);
			if (isNeighboredByAir(neighbor, serverWorld)) { corruptLand(neighbor, serverWorld);}			
		}
		
		private static BlockPos getRandomNeighbor(BlockPos pos) {
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
	
	public void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        event.getRegistry().registerAll(
                new SpecialRecipeSerializer<>(PurifierRecipe::new).setRegistryName("dicemccl:purifier")
        );
    }
}
