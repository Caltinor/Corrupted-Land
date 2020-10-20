package com.dicemc.corruptedlands;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
	public static ForgeConfigSpec SERVER_CONFIG;
	
	public static ForgeConfigSpec.ConfigValue<Double> SPREAD_RATE;
	public static ForgeConfigSpec.ConfigValue<Boolean> HEAL_MOBS;
	public static ForgeConfigSpec.ConfigValue<Boolean> DAMAGE_ANIMALS;
	public static ForgeConfigSpec.ConfigValue<Integer> FLESH_DESPAWN_TIME;
	public static ForgeConfigSpec.ConfigValue<Integer> CALYX_EFFECT_LEVEL;
	
	static {
		ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
		
		SERVER_BUILDER.comment("Mod Settings").push("Server");
		SPREAD_RATE = SERVER_BUILDER.comment("The rate at which corruption should spread.  Higher = slower spread")
				.defineInRange("Spread_Rate", 0.0, 0, .99);
		HEAL_MOBS = SERVER_BUILDER.comment("Does corrupted land heal mobs")
				.define("Heal_Mobs", true);
		DAMAGE_ANIMALS = SERVER_BUILDER.comment("Does corrupted land damage animals")
				.define("Damage_Animals", true);
		FLESH_DESPAWN_TIME = SERVER_BUILDER.comment("The speed at which flesh despawns.  setting to -1 will use vanilla default rate")
				.define("Flesh_Despawn_Rate", -1);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.comment("Immortuos Calyx Compat Settings").push("Compat");
		CALYX_EFFECT_LEVEL = SERVER_BUILDER.comment("The level at which Calyx Infection causes corrupted land to heal instead of poison")
				.define("Calyx_Level", 60);
		
		SERVER_CONFIG = SERVER_BUILDER.build();
	}
}
