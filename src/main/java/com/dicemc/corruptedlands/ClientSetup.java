package com.dicemc.corruptedlands;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
	public static void init(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(Registration.CORRUPTED_GRASS_BLOCK.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(Registration.CORRUPTED_ICE_BLOCK.get(), RenderType.translucent());
		
		event.enqueueWork(() -> {
			ItemProperties.register(Registration.PURIFIER.get(), new ResourceLocation("damage"), (s,l,e,i) -> {return (float)s.getDamageValue();});
		});
	}
}
