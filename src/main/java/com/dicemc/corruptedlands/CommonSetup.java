package com.dicemc.corruptedlands;

import java.util.function.Predicate;

import com.dicemc.corruptedlands.blocks.ICorrupted;

import com.dicemc.corruptedlands.recipes.MysticalPumpkins;
import io.github.championash5357.paranoia.api.callback.SanityCallbacks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModList;
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
        if (ModList.get().isLoaded("paranoia")) {
        	CorruptedLandMod.LOG.info("Paranoia dectected! Initializing support.");
        	Predicate<ServerPlayerEntity> pred = player -> (
        			player.world.getBlockState(player.getPosition().down()).getBlock() instanceof ICorrupted);
        	SanityCallbacks.registerMultiplier(pred, Config.PARANOIA_MODIFIER.get());
        }
        
        if (ModList.get().isLoaded("mystical_pumpkins")) {
			MysticalPumpkins.init();
		}
    }
}
