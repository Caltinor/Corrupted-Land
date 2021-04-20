package com.dicemc.corruptedlands.items;

import com.dicemc.corruptedlands.CorruptedLandMod;
import com.dicemc.corruptedlands.Registration;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class PurifierRecipe extends SpecialRecipe{

	public PurifierRecipe(ResourceLocation idIn) {super(idIn);}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		return (inv.getItem(0).getItem().equals(Items.FLINT) &&
				inv.getItem(1).getItem().equals(Items.GLASS_PANE) &&
				inv.getItem(2).getItem().equals(Items.FLINT) &&
				inv.getItem(3).getItem().equals(Items.GLASS_PANE) &&
				inv.getItem(4).getItem().equals(Items.ROTTEN_FLESH) &&
				inv.getItem(5).getItem().equals(Items.GLASS_PANE) &&
				inv.getItem(6).getItem().equals(Items.FLINT) &&
				inv.getItem(7).getItem().equals(Items.GLASS_PANE) &&
				inv.getItem(8).getItem().equals(Items.FLINT)); 
	}

	@Override
	public ItemStack assemble(CraftingInventory inv) {
		ItemStack out = new ItemStack(Registration.PURIFIER.get());
		out.setDamageValue(19999);
		return out;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {return (width * height) == 9;}

	@Override
	public IRecipeSerializer<?> getSerializer() {return SERIALIZER;}
	
	@ObjectHolder(CorruptedLandMod.MOD_ID+":purifier")
	public static SpecialRecipeSerializer<PurifierRecipe> SERIALIZER;

}
