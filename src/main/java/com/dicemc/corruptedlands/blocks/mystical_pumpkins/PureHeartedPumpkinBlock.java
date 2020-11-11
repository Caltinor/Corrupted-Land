package com.dicemc.corruptedlands.blocks.mystical_pumpkins;

import blueduck.mysticalpumpkins.block.MysticalPumpkinBlock;
import blueduck.mysticalpumpkins.block.MysticalPumpkinItem;
import com.dicemc.corruptedlands.blocks.IPreventCorruption;

public class PureHeartedPumpkinBlock extends MysticalPumpkinBlock implements IPreventCorruption {
	@Override
	public boolean allowSpread() {
		return false;
	}
	
	@Override
	public double getRange() {
		return 100;
	}
}
