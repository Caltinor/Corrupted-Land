package com.dicemc.corruptedlands;

import java.util.Map;

import com.dicemc.corruptedlands.blocks.CorruptedBlock;
import com.dicemc.corruptedlands.blocks.CorruptedBreakableBlock;
import com.dicemc.corruptedlands.blocks.CorruptedFallingBlock;
import com.dicemc.corruptedlands.items.PurifierItem;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {
		public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CorruptedLandMod.MOD_ID);
		public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CorruptedLandMod.MOD_ID);
		
		public static void init() {
			BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
			ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		}
		
		public static final RegistryObject<Item> PURIFIER = ITEMS.register("purifier", () -> new PurifierItem(new Item.Properties().group(ItemGroup.MISC).maxDamage(20000)));
		
		public static final RegistryObject<Block> CORRUPTED_COBBLESTONE_BLOCK = BLOCKS.register("corrupted_cobblestone", () -> new CorruptedBlock(Block.Properties.from(Blocks.COBBLESTONE).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_COBBLESTONE_BLOCK_ITEM = ITEMS.register("corrupted_cobblestone", () -> new BlockItem(CORRUPTED_COBBLESTONE_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_OBSIDIAN_BLOCK = BLOCKS.register("corrupted_obsidian", () -> new CorruptedBlock(Block.Properties.from(Blocks.OBSIDIAN).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_OBSIDIAN_BLOCK_ITEM = ITEMS.register("corrupted_obsidian", () -> new BlockItem(CORRUPTED_OBSIDIAN_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_ICE_BLOCK = BLOCKS.register("corrupted_ice", () -> new CorruptedBreakableBlock(Block.Properties.from(Blocks.ICE).tickRandomly().noDrops()));
	    public static final RegistryObject<Item> CORRUPTED_ICE_BLOCK_ITEM = ITEMS.register("corrupted_ice", () -> new BlockItem(CORRUPTED_ICE_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_MOSSY_COBBLESTONE_BLOCK = BLOCKS.register("corrupted_mossy_cobblestone", () -> new CorruptedBlock(Block.Properties.from(Blocks.MOSSY_COBBLESTONE).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_MOSSY_COBBLESTONE_BLOCK_ITEM = ITEMS.register("corrupted_mossy_cobblestone", () -> new BlockItem(CORRUPTED_MOSSY_COBBLESTONE_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_RED_SANDSTONE_BLOCK = BLOCKS.register("corrupted_red_sandstone", () -> new CorruptedBlock(Block.Properties.from(Blocks.RED_SANDSTONE).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_RED_SANDSTONE_BLOCK_ITEM = ITEMS.register("corrupted_red_sandstone", () -> new BlockItem(CORRUPTED_RED_SANDSTONE_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_SANDSTONE_BLOCK = BLOCKS.register("corrupted_sandstone", () -> new CorruptedBlock(Block.Properties.from(Blocks.SANDSTONE).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_SANDSTONE_BLOCK_ITEM = ITEMS.register("corrupted_sandstone", () -> new BlockItem(CORRUPTED_SANDSTONE_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_GRANITE_BLOCK = BLOCKS.register("corrupted_granite", () -> new CorruptedBlock(Block.Properties.from(Blocks.GRANITE).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_GRANITE_BLOCK_ITEM = ITEMS.register("corrupted_granite", () -> new BlockItem(CORRUPTED_GRANITE_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_ANDESITE_BLOCK = BLOCKS.register("corrupted_andesite", () -> new CorruptedBlock(Block.Properties.from(Blocks.ANDESITE).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_ANDESITE_BLOCK_ITEM = ITEMS.register("corrupted_andesite", () -> new BlockItem(CORRUPTED_ANDESITE_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

	    public static final RegistryObject<Block> CORRUPTED_DIORITE_BLOCK = BLOCKS.register("corrupted_diorite", () -> new CorruptedBlock(Block.Properties.from(Blocks.DIORITE).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_DIORITE_BLOCK_ITEM = ITEMS.register("corrupted_diorite", () -> new BlockItem(CORRUPTED_DIORITE_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
		
	    public static final RegistryObject<Block> CORRUPTED_DIRT_BLOCK = BLOCKS.register("corrupted_dirt", () -> new CorruptedBlock(Block.Properties.from(Blocks.DIRT).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_DIRT_BLOCK_ITEM = ITEMS.register("corrupted_dirt", () -> new BlockItem(CORRUPTED_DIRT_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
		
	    public static final RegistryObject<Block> CORRUPTED_PODZOL_BLOCK = BLOCKS.register("corrupted_podzol", () -> new CorruptedBlock(Block.Properties.from(Blocks.DIRT).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_PODZOL_BLOCK_ITEM = ITEMS.register("corrupted_podzol", () -> new BlockItem(CORRUPTED_PODZOL_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
		
	    public static final RegistryObject<Block> CORRUPTED_COARSE_DIRT_BLOCK = BLOCKS.register("corrupted_coarse_dirt", () -> new CorruptedBlock(Block.Properties.from(Blocks.DIRT).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_COARSE_DIRT_BLOCK_ITEM = ITEMS.register("corrupted_coarse_dirt", () -> new BlockItem(CORRUPTED_COARSE_DIRT_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
			    
	    public static final RegistryObject<Block> CORRUPTED_GRASS_BLOCK = BLOCKS.register("corrupted_grass", () -> new CorruptedBlock(Block.Properties.from(Blocks.GRASS_BLOCK).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_GRASS_BLOCK_ITEM = ITEMS.register("corrupted_grass", () -> new BlockItem(CORRUPTED_GRASS_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_SAND_BLOCK = BLOCKS.register("corrupted_sand", () -> new CorruptedFallingBlock(Block.Properties.from(Blocks.SAND).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_SAND_BLOCK_ITEM = ITEMS.register("corrupted_sand", () -> new BlockItem(CORRUPTED_SAND_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_RED_SAND_BLOCK = BLOCKS.register("corrupted_red_sand", () -> new CorruptedFallingBlock(Block.Properties.from(Blocks.SAND).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_RED_SAND_BLOCK_ITEM = ITEMS.register("corrupted_red_sand", () -> new BlockItem(CORRUPTED_RED_SAND_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_GRAVEL_BLOCK = BLOCKS.register("corrupted_gravel", () -> new CorruptedFallingBlock(Block.Properties.from(Blocks.GRAVEL).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_GRAVEL_BLOCK_ITEM = ITEMS.register("corrupted_gravel", () -> new BlockItem(CORRUPTED_GRAVEL_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

	    public static void mapBlockPairs() {
	    	Map<Block, Block> map = CorruptedLandMod.corruptionPair;
	    	map.put(Blocks.STONE, Registration.CORRUPTED_COBBLESTONE_BLOCK.get());
	    	map.put(Blocks.GRANITE, Registration.CORRUPTED_GRANITE_BLOCK.get());
	    	map.put(Blocks.ANDESITE, Registration.CORRUPTED_ANDESITE_BLOCK.get());
	    	map.put(Blocks.DIORITE, Registration.CORRUPTED_DIORITE_BLOCK.get());
	    	map.put(Blocks.COBBLESTONE, Registration.CORRUPTED_COBBLESTONE_BLOCK.get());
	    	map.put(Blocks.DIRT, Registration.CORRUPTED_DIRT_BLOCK.get());
	    	map.put(Blocks.FARMLAND, Registration.CORRUPTED_DIRT_BLOCK.get());
	    	map.put(Blocks.GRASS_PATH, Registration.CORRUPTED_GRASS_BLOCK.get());
	    	map.put(Blocks.GRASS_BLOCK, Registration.CORRUPTED_GRASS_BLOCK.get());
	    	map.put(Blocks.SAND, Registration.CORRUPTED_SAND_BLOCK.get());
	    	map.put(Blocks.GRAVEL, Registration.CORRUPTED_GRAVEL_BLOCK.get());
	    	map.put(Blocks.COARSE_DIRT, Registration.CORRUPTED_COARSE_DIRT_BLOCK.get());
	    	map.put(Blocks.RED_SAND, Registration.CORRUPTED_RED_SAND_BLOCK.get());
	    	map.put(Blocks.PODZOL, Registration.CORRUPTED_PODZOL_BLOCK.get());
	    	map.put(Blocks.SANDSTONE, Registration.CORRUPTED_SANDSTONE_BLOCK.get());
	    	map.put(Blocks.RED_SANDSTONE, Registration.CORRUPTED_RED_SANDSTONE_BLOCK.get());
	    	map.put(Blocks.MOSSY_COBBLESTONE, Registration.CORRUPTED_MOSSY_COBBLESTONE_BLOCK.get());
	    	map.put(Blocks.LAVA, Registration.CORRUPTED_OBSIDIAN_BLOCK.get());
	    	map.put(Blocks.OBSIDIAN, Registration.CORRUPTED_OBSIDIAN_BLOCK.get());
	    	map.put(Blocks.CRYING_OBSIDIAN, Registration.CORRUPTED_OBSIDIAN_BLOCK.get());
	    	map.put(Blocks.ICE, Registration.CORRUPTED_ICE_BLOCK.get());
	    	map.put(Blocks.WATER, Registration.CORRUPTED_ICE_BLOCK.get());
	    	map.put(Blocks.BLUE_ICE, Registration.CORRUPTED_ICE_BLOCK.get());
	    	map.put(Blocks.PACKED_ICE, Registration.CORRUPTED_ICE_BLOCK.get());
	    	CorruptedLandMod.corruptionPair = map;
	    }
	    
 	    public static class DispenseFlesh implements IDispenseItemBehavior {

			@Override
			public ItemStack dispense(IBlockSource p_dispense_1_, ItemStack p_dispense_2_) {
				ItemStack itemstack = this.dispenseStack(p_dispense_1_, p_dispense_2_);
			    this.playDispenseSound(p_dispense_1_);
			    this.spawnDispenseParticles(p_dispense_1_, p_dispense_1_.getBlockState().get(DispenserBlock.FACING));
				itemstack.getOrCreateTag().putBoolean("playerthrown", true);
				return itemstack;
			}
			
			protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
				Direction direction = source.getBlockState().get(DispenserBlock.FACING);
				IPosition iposition = DispenserBlock.getDispensePosition(source);
				ItemStack itemstack = stack.split(1);
				doDispense(source.getWorld(), itemstack, 6, direction, iposition);
				return stack;
			}
	    	
			public static void doDispense(World worldIn, ItemStack stack, int speed, Direction facing, IPosition position) {
			    double d0 = position.getX();
			    double d1 = position.getY();
			    double d2 = position.getZ();
			    if (facing.getAxis() == Direction.Axis.Y) {
			       d1 = d1 - 0.125D;
			    } else {
			       d1 = d1 - 0.15625D;
			    }
			    ItemEntity itementity = new ItemEntity(worldIn, d0, d1, d2, stack);
			    double d3 = worldIn.rand.nextDouble() * 0.1D + 0.2D;
			    itementity.setMotion(worldIn.rand.nextGaussian() * (double)0.0075F * (double)speed + (double)facing.getXOffset() * d3, worldIn.rand.nextGaussian() * (double)0.0075F * (double)speed + (double)0.2F, worldIn.rand.nextGaussian() * (double)0.0075F * (double)speed + (double)facing.getZOffset() * d3);
			    worldIn.addEntity(itementity);
			}			
			protected void playDispenseSound(IBlockSource source) {source.getWorld().playEvent(1000, source.getBlockPos(), 0);}
			protected void spawnDispenseParticles(IBlockSource source, Direction facingIn) {source.getWorld().playEvent(2000, source.getBlockPos(), facingIn.getIndex());}
	    }
}
