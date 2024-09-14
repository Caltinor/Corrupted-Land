package com.dicemc.corruptedlands;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.Position;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;

public class Registration {
		public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(CorruptedLandMod.MOD_ID);
		public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CorruptedLandMod.MOD_ID);
		public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CorruptedLandMod.MOD_ID);

		
		public static final DeferredHolder<Item, PurifierItem> PURIFIER = ITEMS.register("purifier", () -> new PurifierItem(new Item.Properties().durability(20000)));
		public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> PLAYER_THROWN = COMPONENTS
				.register("player_thrown", () -> DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());
		public static final DeferredHolder<AttachmentType<?>, AttachmentType<HashSet<BlockPos>>> CORRUPT = ATTACHMENTS
				.register("corrupt", () -> AttachmentType.builder(() -> new HashSet<BlockPos>())
					.serialize(BlockPos.CODEC.listOf().xmap(HashSet::new, ArrayList::new)).build());
	    
 	    public static class DispenseFlesh implements DispenseItemBehavior {

			@Override
			public ItemStack dispense(BlockSource p_dispense_1_, ItemStack p_dispense_2_) {
				ItemStack itemstack = this.dispenseStack(p_dispense_1_, p_dispense_2_);
			    this.playDispenseSound(p_dispense_1_);
			    this.spawnDispenseParticles(p_dispense_1_, p_dispense_1_.blockEntity().getBlockState().getValue(DispenserBlock.FACING));
				itemstack.set(PLAYER_THROWN, true);
				return itemstack;
			}
			
			protected ItemStack dispenseStack(BlockSource source, ItemStack stack) {
				Direction direction = source.blockEntity().getBlockState().getValue(DispenserBlock.FACING);
				Position iposition = DispenserBlock.getDispensePosition(source);
				ItemStack itemstack = stack.split(1);
				doDispense(source.level(), itemstack, 6, direction, iposition);
				return stack;
			}
	    	
			public static void doDispense(Level worldIn, ItemStack stack, int speed, Direction facing, Position position) {
			    double d0 = position.x();
			    double d1 = position.y();
			    double d2 = position.z();
			    if (facing.getAxis() == Direction.Axis.Y) {
			       d1 = d1 - 0.125D;
			    } else {
			       d1 = d1 - 0.15625D;
			    }
			    ItemEntity itementity = new ItemEntity(worldIn, d0, d1, d2, stack);
			    double d3 = worldIn.random.nextDouble() * 0.1D + 0.2D;
			    itementity.setDeltaMovement(worldIn.random.nextGaussian() * (double)0.0075F * (double)speed + (double)facing.getStepX() * d3, worldIn.random.nextGaussian() * (double)0.0075F * (double)speed + (double)0.2F, worldIn.random.nextGaussian() * (double)0.0075F * (double)speed + (double)facing.getStepZ() * d3);
			    worldIn.addFreshEntity(itementity);
			}			
			protected void playDispenseSound(BlockSource source) {source.level().levelEvent(1000, source.pos(), 0);}
			protected void spawnDispenseParticles(BlockSource source, Direction facingIn) {source.level().levelEvent(2000, source.pos(), facingIn.get3DDataValue());}
	    }
}
