package com.dicemc.corruptedlands;

import net.minecraft.block.DispenserBlock;
import net.minecraft.item.Items;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CorruptedLandMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonSetup {
	@SuppressWarnings("deprecation")
	public static void init(final FMLCommonSetupEvent event)
    {
        DeferredWorkQueue.runLater(
            () -> DispenserBlock.registerDispenseBehavior(
                Items.ROTTEN_FLESH,
                new Registration.DispenseFlesh()));
        
    }
}
