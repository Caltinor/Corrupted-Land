package com.dicemc.corruptedlands;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ModReloadListener extends JsonReloadListener {

	public ModReloadListener(Gson p_i51536_1_, String p_i51536_2_) { super(p_i51536_1_, p_i51536_2_); }

	@Override
	protected Map<ResourceLocation, JsonElement> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) { return null; }

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
		// TODO Auto-generated method stub
		
	}
	
}
