package com.dicemc.corruptedlands;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
	public static void init(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(Registration.CORRUPTED_GRASS_BLOCK.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(Registration.CORRUPTED_ICE_BLOCK.get(), RenderType.getTranslucent());
	}
}
