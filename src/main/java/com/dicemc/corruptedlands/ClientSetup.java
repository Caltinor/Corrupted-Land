package com.dicemc.corruptedlands;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
	public static void init(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(Registration.CORRUPTED_GRASS_BLOCK.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(Registration.CORRUPTED_ICE_BLOCK.get(), RenderType.getTranslucent());
		
		event.enqueueWork(() -> {
			ItemModelsProperties.registerProperty(Registration.PURIFIER.get(), new ResourceLocation("damage"), (s,c,l) -> {return (float)s.getDamage();});
		});
	}
}
