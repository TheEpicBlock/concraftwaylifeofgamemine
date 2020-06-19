package io.github.theepicblock.concraftwaylifeofgamemine.mixin;

import io.github.theepicblock.concraftwaylifeofgamemine.ConwayMain;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkManager.class)
public class ServerChunkManagerMixin {
    @Shadow @Final private ServerWorld world;

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "method_20801", at = @At("HEAD"))
    public void chunkUpdateInject(long something, boolean something2, SpawnHelper.Info spawnInfo, boolean something3, int nothing, ChunkHolder chunk, CallbackInfo ci) {
        int ticks = world.getServer().getTicks();
        if (ticks % ConwayMain.getConwayTickTime() == 0) {
            System.out.println("DOING THINGY: " + chunk.isTicking());
        }
    }
}
