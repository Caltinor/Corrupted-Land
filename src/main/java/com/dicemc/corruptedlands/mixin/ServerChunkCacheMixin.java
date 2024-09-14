package com.dicemc.corruptedlands.mixin;

import com.dicemc.corruptedlands.Config;
import com.dicemc.corruptedlands.CorruptedLandMod;
import com.dicemc.corruptedlands.Registration;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.chunk.LevelChunk;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkCache.class)
public class ServerChunkCacheMixin {
    @Inject(method = "tickChunks()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunk;getPos()Lnet/minecraft/world/level/ChunkPos;")
    )
    private void randomlyTickSpreadingCorruption(CallbackInfo ci, @Local LevelChunk chunk){
        ServerChunkCache cache = (ServerChunkCache)(Object)this;
        chunk.getData(Registration.CORRUPT).forEach(pos -> {
            cache.level.sendParticles(new DustParticleOptions(new Vector3f(168f, 0f, 255f), 1), pos.getX(), pos.getY(), pos.getZ(), 1, 0, 1, 0, 5);
            double threshold = cache.level.random.nextDouble();
            if (threshold >= Config.SPREAD_RATE.get())
                CorruptedLandMod.Utils.corruptNeighbors(pos, cache.level);
        });
        CorruptedLandMod.Utils.corruptLandFromQueue(cache.level);
    }
}
