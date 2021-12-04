package com.dicemc.corruptedlands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;

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

    public static HashMap<ResourceLocation, Integer> getBiomesForResistance() {
        final String BiomeList = Config.BIOMERESIST.get();
        //split biome-resistance pairs
        String[] Biomeresist = BiomeList.split(",");
        HashMap<ResourceLocation, Integer> resistanceMap = new HashMap<>();
        try {
            for (String string : Biomeresist) {
                //split the biome-resistance pair
                    String[] resistanceSplit = string.split(";");
                    //protects against outofrangeexception from blank entry.
                    if(resistanceSplit.length == 2) {
                        ResourceLocation biome = new ResourceLocation(resistanceSplit[0]);
                        Integer resistance = Integer.valueOf(resistanceSplit[1]);
                        resistanceMap.put(biome, resistance);
                    }
            }
        }catch (NumberFormatException e){
            CorruptedLandMod.LOG.error("Corrupted Land: Resistances not parsed! Error in config: Invalid character where a number should be! See format guide in the common config for more information!");
        }
        return resistanceMap;
    }
}
