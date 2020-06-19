package io.github.theepicblock.concraftwaylifeofgamemine.mixin;

import io.github.theepicblock.concraftwaylifeofgamemine.AliveBlockHolder;
import io.github.theepicblock.concraftwaylifeofgamemine.ConwayMain;
import io.github.theepicblock.concraftwaylifeofgamemine.ConwayStep;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nerdhub.cardinal.components.api.component.ComponentProvider.fromChunk;

@Mixin(ServerChunkManager.class)
public class ServerChunkManagerMixin {
    private ConwayStep ConwayCurrentStep;
    @Shadow @Final private ServerWorld world;

    @Inject(method = "tickChunks()V", at = @At("HEAD"))
    public void startTick(CallbackInfo ci){
        if (ConwayMain.isTickConway(world.getServer().getTicks())) {
            ConwayCurrentStep = new ConwayStep();
        }
    }

    /**
     * Get's called for every chunk, every tick.
     */
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "method_20801", at = @At("HEAD"))
    public void chunkUpdateInject(long something, boolean something2, SpawnHelper.Info spawnInfo, boolean something3, int nothing, ChunkHolder chunkHolder, CallbackInfo ci) {
        if (ConwayMain.isTickConway(world.getServer().getTicks())) {
            Chunk chunk = chunkHolder.getCompletedChunk();
            if (chunk != null) {
                ConwayCurrentStep.add(chunk, this.world);
            }
        }
    }

    /**
     * Gets called at the end of a tick
     */
    @Inject(method = "tickChunks()V", at = @At("TAIL"))
    public void endTick(CallbackInfo ci){
        if (ConwayMain.isTickConway(world.getServer().getTicks()))
        ConwayCurrentStep.doStuff();
    }
}
