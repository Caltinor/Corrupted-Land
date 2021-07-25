package com.dicemc.corruptedlands;

//import io.github.championash5357.paranoia.api.callback.SanityCallbacks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CorruptedLandMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonSetup {
	public static void init(final FMLCommonSetupEvent event)
    {
		event.enqueueWork(
            () -> DispenserBlock.registerBehavior(
                Items.ROTTEN_FLESH,
                new Registration.DispenseFlesh()));
        /*if (ModList.get().isLoaded("paranoia")) {
        	CorruptedLandMod.LOG.info("Paranoia detected! Initializing support.");
        	Predicate<ServerPlayer> pred = player -> (
        			player.level.getBlockState(player.blockPosition().below()).getBlock() instanceof ICorrupted);
        	SanityCallbacks.registerMultiplier(pred, Config.PARANOIA_MODIFIER.get());
        }*/
    }
}
