package com.dicemc.corruptedlands;

//import io.github.championash5357.paranoia.api.callback.SanityCallbacks;
import com.cartoonishvillain.ImmortuosCalyx.ImmortuosCalyx;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = CorruptedLandMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonSetup {
	public static void init(final FMLCommonSetupEvent event)
    {
        CorruptedLandMod.biomeResistance = getBiomesForResistance();

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

    public static ArrayList<ResourceLocation> getBiomesForResistance() {
        final String BiomeList = Config.BIOMERESIST.get();
        String[] Biomeresist = BiomeList.split(",");
        int resistLength = Biomeresist.length;
        ArrayList<ResourceLocation> finalBiomeresist = new ArrayList<>();
        int counter = 0;
        for (String i : Biomeresist) {
            ResourceLocation newResource = new ResourceLocation(i);
            finalBiomeresist.add(newResource);
            counter++;
        }
        return finalBiomeresist;
    }
}
