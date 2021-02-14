package com.dicemc.corruptedlands;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
	public static ForgeConfigSpec SERVER_CONFIG;
	public static ForgeConfigSpec COMMON_CONFIG;
	
	public static ForgeConfigSpec.ConfigValue<Double> SPREAD_RATE;
	public static ForgeConfigSpec.ConfigValue<Boolean> HEAL_MOBS;
	public static ForgeConfigSpec.ConfigValue<Boolean> DAMAGE_ANIMALS;
	public static ForgeConfigSpec.ConfigValue<Integer> FLESH_DESPAWN_TIME;
	public static ForgeConfigSpec.ConfigValue<Integer> PURIFIER_RANGE;
	public static ForgeConfigSpec.ConfigValue<Integer> PURIFIER_RECHARGE_RATE;
	public static ForgeConfigSpec.ConfigValue<Integer> PURIFIER_DRAIN_RATE;
	public static ForgeConfigSpec.ConfigValue<Integer> CALYX_EFFECT_LEVEL;
	public static ForgeConfigSpec.ConfigValue<Float> CORRUPTION_EFFECT_POWER;
	public static ForgeConfigSpec.ConfigValue<Double> PARANOIA_MODIFIER;
	
	static {
		ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
		ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
		
		SERVER_BUILDER.comment("Mod Settings").push("Server");
		SPREAD_RATE = SERVER_BUILDER.comment("The rate at which corruption should spread.  Higher = slower spread")
				.defineInRange("Spread_Rate", 0.0, 0, .99);
		HEAL_MOBS = SERVER_BUILDER.comment("Does corrupted land heal mobs")
				.define("Heal_Mobs", true);
		DAMAGE_ANIMALS = SERVER_BUILDER.comment("Does corrupted land damage animals")
				.define("Damage_Animals", true);
		FLESH_DESPAWN_TIME = SERVER_BUILDER.comment("The speed at which flesh despawns.  setting to -1 will use vanilla default rate")
				.define("Flesh_Despawn_Rate", -1);
		PURIFIER_RANGE = SERVER_BUILDER.comment("the AOE of the purifier")
				.define("Purifer Range", 5);
		PURIFIER_RECHARGE_RATE = SERVER_BUILDER.comment("Rate that sunlight recharges purifiers.  must be negative. the more negative the faster the charge.")
				.defineInRange("Purifier Recharge Rate", -1, -20000, 0);
		PURIFIER_DRAIN_RATE = SERVER_BUILDER.comment("How much damage per block the purifier takes when used. Default 20")
				.defineInRange("Purifier Drain Rate", 20, 0, Integer.MAX_VALUE);
		CORRUPTION_EFFECT_POWER = SERVER_BUILDER.comment("How much damage/healing entities take when on corrupted land")
				.define("Corruption_Effect_Power", 1F);
		SERVER_BUILDER.pop();
		
		SERVER_BUILDER.comment("Compat Settings").push("Compat");
		CALYX_EFFECT_LEVEL = SERVER_BUILDER.comment("The level at which Calyx Infection causes corrupted land to heal instead of poison")
				.define("Calyx_Level", 60);
		SERVER_BUILDER.pop();
		
		COMMON_BUILDER.comment("Compat Settings").push("Compat");
		PARANOIA_MODIFIER = SERVER_BUILDER.comment("value between -1 and 1 to define the rate at which corrupted land impacts paranoia.")
				.defineInRange("Paranoia_Modifier", 0.001, -1d, 1d);
		COMMON_BUILDER.pop();
		
		SERVER_CONFIG = SERVER_BUILDER.build();
		COMMON_CONFIG = COMMON_BUILDER.build();
	}
}
