package com.dicemc.corruptedlands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.cartoonishvillain.ImmortuosCalyx.Entity.InfectedEntity;
import com.cartoonishvillain.ImmortuosCalyx.Register;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.dicemc.corruptedlands.blocks.ICorrupted;
import com.dicemc.corruptedlands.items.PurifierItem;
import com.dicemc.corruptedlands.items.PurifierRecipe;
import com.cartoonishvillain.ImmortuosCalyx.Infection.InfectionManager;
import com.cartoonishvillain.ImmortuosCalyx.Infection.InfectionManagerCapability;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;


@Mod(CorruptedLandMod.MOD_ID)
public class CorruptedLandMod {
	public static final String MOD_ID = "dicemccl";
	public static final Logger LOG = LogManager.getLogger(MOD_ID);
	public static Random MASTER_RAND = new Random();
	public static MinecraftServer SERVER;
	public static Map<Block, Block> corruptionPair = new HashMap<Block, Block>();
	public static Capability<?> calyxCap = null;
	public static ArrayList<ResourceLocation> biomeResistance;

	public CorruptedLandMod() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
		Registration.init();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(RecipeSerializer.class, this::registerRecipeSerializers);
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
			//Immortuos eggs corrupting ground
			if (calyxCap != null && Config.CALYX_EGGS_CORRUPT_LAND.get() && event.getEntityItem().getItem().sameItem(new ItemStack(Register.IMMORTUOSCALYXEGGS.get()))){
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
				if (event.getEntityLiving() instanceof Player) {
					Player player = (Player) event.getEntityLiving();
					//purifier stone charge check
					if (player.getCommandSenderWorld().canSeeSky(player.blockPosition()) && player.getCommandSenderWorld().isDay())	{
						if (player.getMainHandItem().getItem() instanceof PurifierItem) player.getMainHandItem().hurtAndBreak(Config.PURIFIER_RECHARGE_RATE.get(), player, (p) -> {});
						if (player.getOffhandItem().getItem() instanceof PurifierItem) player.getOffhandItem().hurtAndBreak(Config.PURIFIER_RECHARGE_RATE.get(), player, (p) -> {});
					}
					//Corruption check
					BlockState bs = event.getEntityLiving().getCommandSenderWorld().getBlockState(event.getEntityLiving().blockPosition().below());
					if (!player.isCreative() && bs.getBlock() instanceof ICorrupted) {
						boolean calyxDetected = player.getCapability(calyxCap, null).map(cap -> {
							if (((InfectionManager) cap).getInfectionProgress() >= Config.CALYX_EFFECT_LEVEL.get()) {
								player.heal(Config.CORRUPTION_EFFECT_POWER.get());
								return true;}
							else if (player.getHealth() > Config.CORRUPTION_EFFECT_POWER.get()){
								player.hurt(new DamageSource(bs.getBlock().getRegistryName().toString()), Config.CORRUPTION_EFFECT_POWER.get());
								return true;
							}
							else return true;
						}).orElse(false);
						if (!calyxDetected && player.getHealth() > Config.CORRUPTION_EFFECT_POWER.get()) 
							player.hurt(new DamageSource(bs.getBlock().getRegistryName().toString()), Config.CORRUPTION_EFFECT_POWER.get());
					}
				}
				if (Config.DAMAGE_ANIMALS.get() && event.getEntityLiving() instanceof Animal) {
					BlockState bs = event.getEntityLiving().getCommandSenderWorld().getBlockState(event.getEntityLiving().blockPosition().below());
					if ((bs.getBlock() instanceof ICorrupted) && event.getEntityLiving().getHealth() > Config.CORRUPTION_EFFECT_POWER.get()) 
						event.getEntityLiving().hurt(new DamageSource(bs.getBlock().getRegistryName().toString()), Config.CORRUPTION_EFFECT_POWER.get());
				}
				if (Config.HEAL_MOBS.get() && event.getEntityLiving() instanceof Monster) {
					BlockState bs = event.getEntityLiving().getCommandSenderWorld().getBlockState(event.getEntityLiving().blockPosition().below());
					if (bs.getBlock() instanceof ICorrupted) {
						event.getEntityLiving().heal(Config.CORRUPTION_EFFECT_POWER.get());
					}
					if(calyxCap != null && event.getEntityLiving() instanceof InfectedEntity && bs.getBlock() instanceof ICorrupted){
						//Infected entities getting bonus effects if enabled in config.
						if(Config.CALYX_STRENGTHEN_INFECTED.get() > 0){event.getEntityLiving().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5, Config.CALYX_STRENGTHEN_INFECTED.get() - 1, false, false));}
						if(Config.CALYX_RESISTANCE_INFECTED.get() > 0){event.getEntityLiving().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5, Config.CALYX_RESISTANCE_INFECTED.get() - 1, false, false));}
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
			if (ModList.get().isLoaded("immortuoscalyx")) {
				CorruptedLandMod.calyxCap = InfectionManagerCapability.INSTANCE;
				LOG.info("Calyx detected. Initializing support.");
			}
		}
	}
	
	public static class Core {
		public static void corruptLand(BlockPos pos, ServerLevel serverWorld) {
			//pre-emptive boolean
			boolean resisted = false;
			// if Biome resistance is set for 0, no need to continue this check. Otherwise check if the current biome is in the list of resistant biomes.
			if(Config.BIOMERESISTAMOUNT.get() > 0 && biomeResistance.contains(serverWorld.getBiome(pos).getRegistryName())) {
				int chanceToResist = Config.BIOMERESISTAMOUNT.get();
				if(serverWorld.random.nextInt(100) <= chanceToResist) resisted = true;
			}
			//if the resistance check resulted in no resistance, continue as normal. Otherwise if resistance is successful, cancel the spread this time.
			if(!resisted) {
				BlockState current = serverWorld.getBlockState(pos);
				BlockState future = corruptionPair.getOrDefault(current.getBlock(), Blocks.AIR).defaultBlockState();
				if (!future.equals(Blocks.AIR.defaultBlockState())) {
					if (!MinecraftForge.EVENT_BUS.post(new BlockEvent.CropGrowEvent.Post(serverWorld, pos, current, future)))
						serverWorld.setBlock(pos, future, BlockFlags.BLOCK_UPDATE);
				}
			}
		}
		
		public static void corruptNeighbors(BlockPos pos, ServerLevel serverWorld) {
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
		
		private static boolean isNeighboredByAir(BlockPos pos, ServerLevel serverWorld) {
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
	
	public void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event)
    {
        event.getRegistry().registerAll(
                new SimpleRecipeSerializer<>(PurifierRecipe::new).setRegistryName("dicemccl:purifier")
        );
    }
}
