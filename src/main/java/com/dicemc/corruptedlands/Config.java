package com.dicemc.corruptedlands;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
	public static ForgeConfigSpec SERVER_CONFIG;
	
	public static ForgeConfigSpec.ConfigValue<Double> SPREAD_RATE;
	public static ForgeConfigSpec.ConfigValue<Boolean> HEAL_MOBS;
	public static ForgeConfigSpec.ConfigValue<Boolean> DAMAGE_ANIMALS;
	public static ForgeConfigSpec.ConfigValue<Integer> FLESH_DESPAWN_TIME;
	
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
		
		SERVER_CONFIG = SERVER_BUILDER.build();
	}
}
