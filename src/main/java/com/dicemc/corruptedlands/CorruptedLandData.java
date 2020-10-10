package com.dicemc.corruptedlands;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = CorruptedLandMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CorruptedLandData{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		if (event.includeServer()) {			
			gen.addProvider(new LootTables(gen));
		}
	}
	
	private static class LootTables extends LootTableProvider implements IDataProvider {
		
		public LootTables(DataGenerator dataGeneratorIn) { super(dataGeneratorIn); }
		
		private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables = ImmutableList.of(
                Pair.of(BlockTables::new, LootParameterSets.BLOCK)
        );

		@Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
            return tables;
        }
		
		@Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
            map.forEach((p_218436_2_, p_218436_3_) -> {
                LootTableManager.validateLootTable(validationtracker, p_218436_2_, p_218436_3_);
            });
        }
		
		public class BlockTables extends BlockLootTables{
			@Override
			protected void addTables() {
				this.registerDropping(Registration.CORRUPTED_COBBLESTONE_BLOCK.get(), Blocks.COBBLESTONE);
				this.registerDropping(Registration.CORRUPTED_DIRT_BLOCK.get(), Blocks.DIRT);
				this.registerDropping(Registration.CORRUPTED_GRASS_BLOCK.get(), Blocks.DIRT);
				this.registerDropping(Registration.CORRUPTED_SAND_BLOCK.get(), Blocks.SAND);
				this.registerDropping(Registration.CORRUPTED_GRAVEL_BLOCK.get(), Blocks.GRAVEL);
				this.registerDropping(Registration.CORRUPTED_GRANITE_BLOCK.get(), Blocks.GRANITE);
			}
			
			@Override
			protected Iterable<Block> getKnownBlocks() {
				return ForgeRegistries.BLOCKS.getValues().stream()
                        .filter(b -> b.getRegistryName().getNamespace().equals(CorruptedLandMod.MOD_ID))
                        .collect(Collectors.toList());
			}
		}
	}
}
