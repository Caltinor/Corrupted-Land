package com.dicemc.corruptedlands.items.mystical_pumpkins;

import blueduck.mysticalpumpkins.block.MysticalPumpkinItem;
import com.dicemc.corruptedlands.Registration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class PureHeartedPumpkinItem extends MysticalPumpkinItem {
	public PureHeartedPumpkinItem(Block blockIn, Properties p_i48534_3_) {
		super(blockIn, p_i48534_3_);
	}
	
	public static PureHeartedPumpkinItem instance() {
		return new PureHeartedPumpkinItem(Registration.PURE_HEARTED_PUMPKIN_BLOCK.orElseGet(()->null),new Item.Properties().group(ItemGroup.MISC));
	}
}
