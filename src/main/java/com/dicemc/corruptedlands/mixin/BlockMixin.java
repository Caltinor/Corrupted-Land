package com.dicemc.corruptedlands.mixin;

import com.dicemc.corruptedlands.CalyxCompat;
import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method="stepOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/Entity;)V",
            at = @At(value="HEAD"))
    public void corruptionOnStepCheck(Level level, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity living && level.getChunk(pos).getData(Registration.CORRUPT).contains(pos)) {
            DamageSource source = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.HOT_FLOOR));
            if (living instanceof Player player) {
                if (!player.isCreative()) {
                    boolean calyxDetected = CalyxCompat.calyxDetected(player);
                    if (!calyxDetected && player.getHealth() > Config.CORRUPTION_EFFECT_POWER.get())
                        player.hurt(source, Config.CORRUPTION_EFFECT_POWER.get());
                }
            }
            if (Config.DAMAGE_ANIMALS.get() && living instanceof Animal animal && animal.getHealth() > Config.CORRUPTION_EFFECT_POWER.get()) {
                animal.hurt(source, Config.CORRUPTION_EFFECT_POWER.get());
            }
            if (Config.HEAL_MOBS.get() && living instanceof Monster monster) {
                monster.heal(Config.CORRUPTION_EFFECT_POWER.get());
                if (CalyxCompat.isInfected(monster)) {
                    //Infected entities getting bonus effects if enabled in config.
                    if (Config.CALYX_STRENGTHEN_INFECTED.get() > 0) {
                        monster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5, Config.CALYX_STRENGTHEN_INFECTED.get() - 1, false, false));
                    }
                    if (Config.CALYX_RESISTANCE_INFECTED.get() > 0) {
                        monster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5, Config.CALYX_RESISTANCE_INFECTED.get() - 1, false, false));
                    }
                }
            }
        }
    }
}
