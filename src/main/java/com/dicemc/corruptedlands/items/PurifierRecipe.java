package com.dicemc.corruptedlands.items;

import com.dicemc.corruptedlands.Registration;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class PurifierRecipe extends CustomRecipe{

	public PurifierRecipe(ResourceLocation idIn) {super(idIn);}

	@Override
	public boolean matches(CraftingContainer inv, Level worldIn) {
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
	public ItemStack assemble(CraftingContainer inv) {
		ItemStack out = new ItemStack(Registration.PURIFIER.get());
		out.setDamageValue(19999);
		return out;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {return (width * height) == 9;}

	@Override
	public RecipeSerializer<?> getSerializer() {return SERIALIZER;}
	
	public static SimpleRecipeSerializer<PurifierRecipe> SERIALIZER;

}
