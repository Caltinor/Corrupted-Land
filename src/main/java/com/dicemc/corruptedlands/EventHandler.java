package com.dicemc.corruptedlands;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid=CorruptedLandMod.MOD_ID, bus=EventBusSubscriber.Bus.GAME)
public class EventHandler {

    @SubscribeEvent
    public static void onFleshExpire(ItemExpireEvent event) {
        if ((event.getEntity().getItem().getItem().equals(Items.ROTTEN_FLESH) && !event.getEntity().getItem().getOrDefault(Registration.PLAYER_THROWN, false))
                || CalyxCompat.corrupt(event.getEntity().getItem())) {
            BlockPos pos = event.getEntity().getOnPos().below();
            if (event.getEntity().level() instanceof ServerLevel level)
                CorruptedLandMod.Utils.attemptLandCorruption(pos, level);
        }
    }

    @SubscribeEvent
    public static void onCorruptibleItemDrop(EntityEvent.EntityConstructing event) {
        if (Config.FLESH_DESPAWN_TIME.get() >= 0) {
            if (event.getEntity() instanceof ItemEntity entity) {
                if (entity.getItem().getItem().equals(Items.ROTTEN_FLESH))
                    entity.lifespan = Config.FLESH_DESPAWN_TIME.get();
            }
        }
    }

    @SubscribeEvent
    public static void onRottenFleshDrop(ItemTossEvent event) {
        if (event.getEntity().getItem().getItem().equals(Items.ROTTEN_FLESH)) {
            event.getEntity().getItem().set(Registration.PLAYER_THROWN, true);
        }
    }

    //TODO add item pickup event to remove the thrown flag

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        if (ModList.get().isLoaded("immortuoscalyx")) {
            CalyxCompat.init();
            CorruptedLandMod.LOG.info("Calyx detected. Initializing support.");
        }
    }
}
