package com.dicemc.corruptedlands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;

public class CommonSetup {
    @SubscribeEvent
	public static void init(final FMLCommonSetupEvent event)
    {
        CorruptedLandMod.Utils.biomeResistance = getBiomesForResistance();

		event.enqueueWork(
            () -> DispenserBlock.registerBehavior(
                Items.ROTTEN_FLESH,
                new Registration.DispenseFlesh()));
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
                        ResourceLocation biome = ResourceLocation.parse(resistanceSplit[0]);
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
