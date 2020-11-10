package com.dicemc.corruptedlands.recipes;

import blueduck.mysticalpumpkins.registry.InfusionTableRecipeRegistry;
import com.dicemc.corruptedlands.Registration;
import net.minecraft.item.ItemStack;

public class MysticalPumpkins {
	public static void init() {
		InfusionTableRecipeRegistry.addInfuserRecipe(
				new ItemStack(Registration.CORRUPTED_PUMPKIN_ITEM.get() /*base item*/),
				15 /*the amount of essence required to make a pure hearted pumpkin*/,
				new ItemStack(Registration.PURIFIER.get() /*change this to whatever you think makes sense*/),
				new ItemStack(Registration.PURE_HEARTED_PUMPKIN_ITEM.get())
		);
	}
}
