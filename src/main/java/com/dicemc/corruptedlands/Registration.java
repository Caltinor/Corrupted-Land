package com.dicemc.corruptedlands;

import java.util.Map;

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
			ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());;
		}
		
		public static final RegistryObject<Block> CORRUPTED_COBBLESTONE_BLOCK = BLOCKS.register("corrupted_cobblestone", () -> new CorruptedBlock(Block.Properties.from(Blocks.COBBLESTONE).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_COBBLESTONE_BLOCK_ITEM = ITEMS.register("corrupted_cobblestone", () -> new BlockItem(CORRUPTED_COBBLESTONE_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
		
	    public static final RegistryObject<Block> CORRUPTED_DIRT_BLOCK = BLOCKS.register("corrupted_dirt", () -> new CorruptedBlock(Block.Properties.from(Blocks.DIRT).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_DIRT_BLOCK_ITEM = ITEMS.register("corrupted_dirt", () -> new BlockItem(CORRUPTED_DIRT_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
		
	    public static final RegistryObject<Block> CORRUPTED_GRASS_BLOCK = BLOCKS.register("corrupted_grass", () -> new CorruptedBlock(Block.Properties.from(Blocks.GRASS_BLOCK).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_GRASS_BLOCK_ITEM = ITEMS.register("corrupted_grass", () -> new BlockItem(CORRUPTED_GRASS_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_SAND_BLOCK = BLOCKS.register("corrupted_sand", () -> new CorruptedFallingBlock(Block.Properties.from(Blocks.SAND).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_SAND_BLOCK_ITEM = ITEMS.register("corrupted_sand", () -> new BlockItem(CORRUPTED_SAND_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	    
	    public static final RegistryObject<Block> CORRUPTED_GRAVEL_BLOCK = BLOCKS.register("corrupted_gravel", () -> new CorruptedFallingBlock(Block.Properties.from(Blocks.GRAVEL).tickRandomly()));
	    public static final RegistryObject<Item> CORRUPTED_GRAVEL_BLOCK_ITEM = ITEMS.register("corrupted_gravel", () -> new BlockItem(CORRUPTED_GRAVEL_BLOCK.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

	    public static void mapBlockPairs() {
	    	Map<Block, Block> map = CorruptedLandMod.corruptionPair;
	    	map.put(Blocks.STONE, Registration.CORRUPTED_COBBLESTONE_BLOCK.get());
	    	map.put(Blocks.COBBLESTONE, Registration.CORRUPTED_COBBLESTONE_BLOCK.get());
	    	map.put(Blocks.DIRT, Registration.CORRUPTED_DIRT_BLOCK.get());
	    	map.put(Blocks.FARMLAND, Registration.CORRUPTED_DIRT_BLOCK.get());
	    	map.put(Blocks.GRASS_BLOCK, Registration.CORRUPTED_GRASS_BLOCK.get());
	    	map.put(Blocks.SAND, Registration.CORRUPTED_SAND_BLOCK.get());
	    	map.put(Blocks.GRAVEL, Registration.CORRUPTED_GRAVEL_BLOCK.get());
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
