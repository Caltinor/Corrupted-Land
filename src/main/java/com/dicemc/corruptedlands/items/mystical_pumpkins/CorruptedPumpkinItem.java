package com.dicemc.corruptedlands.items.mystical_pumpkins;

import blueduck.mysticalpumpkins.block.MysticalPumpkinItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class CorruptedPumpkinItem extends MysticalPumpkinItem {
	public CorruptedPumpkinItem(Block blockIn, Properties p_i48534_3_) {
		super(blockIn, p_i48534_3_);
	}
	
	public static CorruptedPumpkinItem instance() {
		return new CorruptedPumpkinItem(Blocks.CARVED_PUMPKIN, new Item.Properties().group(ItemGroup.MISC));
	}
}
