package com.dicemc.corruptedlands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;


@Mod(CorruptedLandMod.MOD_ID)
public class CorruptedLandMod {
	public static final String MOD_ID = "dicemccl";
	public static final Logger LOG = LogManager.getLogger(MOD_ID);

	public CorruptedLandMod(IEventBus bus, ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
		container.registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
		Registration.ITEMS.register(bus);
		Registration.COMPONENTS.register(bus);
		Registration.ATTACHMENTS.register(bus);
		bus.addListener(CommonSetup::init);
	}

	public static class Utils {
		public static HashMap<ResourceLocation, Integer> biomeResistance = new HashMap<>();
		public static final List<BlockPos> corruptionQueue = new ArrayList<>();

		public static void corruptLandFromQueue(ServerLevel level) {
			corruptionQueue.forEach(pos -> {
				level.getChunk(pos).getData(Registration.CORRUPT).add(pos);
			});
			corruptionQueue.clear();
		}

		public static void attemptLandCorruption(BlockPos pos, ServerLevel serverWorld) {
			boolean resisted = false;
			// if Biome resistance is set for 0, no need to continue this check. Otherwise, check if the current biome is in the list of resistant biomes.
			if(biomeResistance.containsKey(serverWorld.getBiome(pos).unwrapKey().get().location())) {
				int chanceToResist = biomeResistance.get(serverWorld.getBiome(pos).unwrapKey().get().location());
				if(serverWorld.random.nextInt(100) <= chanceToResist) resisted = true;
			}
			//if the resistance check resulted in no resistance, continue as normal. Otherwise, if resistance is successful, cancel the spread this time.
			if(!resisted) {
				corruptionQueue.add(pos);
			}
		}
		
		public static void corruptNeighbors(BlockPos pos, ServerLevel serverWorld) {
			BlockPos neighbor = getRandomNeighbor(pos, serverWorld.random);
			if (!serverWorld.getChunk(neighbor).getData(Registration.CORRUPT).contains(neighbor)
					&& isNeighboredByAir(neighbor, serverWorld)
					&& !serverWorld.getBlockState(neighbor).isAir()) {
				attemptLandCorruption(neighbor, serverWorld);
			}
		}
		
		private static BlockPos getRandomNeighbor(BlockPos pos, RandomSource rand) {
			int next = rand.nextInt(3);
			int x = (next == 2 ? pos.getX() + 1 : (next == 1 ? pos.getX() : pos.getX() - 1));
			next = rand.nextInt(3);
			int y = (next == 2 ? pos.getY() + 1 : (next == 1 ? pos.getY() : pos.getY() - 1));
			next = rand.nextInt(3);
			int z = (next == 2 ? pos.getZ() + 1 : (next == 1 ? pos.getZ() : pos.getZ() - 1));
			return new BlockPos(x, y, z);
		}
		
		private static boolean isNeighboredByAir(BlockPos pos, ServerLevel serverWorld) {
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
