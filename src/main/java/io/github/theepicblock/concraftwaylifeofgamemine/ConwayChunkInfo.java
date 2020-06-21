package io.github.theepicblock.concraftwaylifeofgamemine;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a list of blocks that need to be updated every conway tick.
 */
public class ConwayChunkInfo implements CopyableComponent<ConwayChunkInfo> {
    private final Block2DbyLayer aliveBlocks = new Block2DbyLayer(0);
    private final List<ChunkPos> toLoad = new ArrayList<>(0);
    //TODO add a command to re sync this list with the actual blocks

    public void add(BlockPos pos) {
        aliveBlocks.put(pos);
        BlockPos2D.forChunkBorders(pos, toLoad::add);
    }

    public void remove(BlockPos pos) {
        aliveBlocks.remove(pos);
        if (BlockPos2D.isOnChunkBorder(pos)) {
            recalculateToLoad();
        }
    }

    public Block2DbyLayer getAliveBlocks() {
        return aliveBlocks;
    }

    public void recalculateToLoad() {
        toLoad.clear();
        aliveBlocks.forEachBlock((layer,pos) -> BlockPos2D.forChunkBorders(pos, toLoad::add));
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        if (compoundTag.contains("conway_alive")) {
            long[] toUpdate = compoundTag.getLongArray("conway_alive");
            aliveBlocks.put(toUpdate);
        }
        if (compoundTag.contains("conway_borders")) {
            long[] toUpdate = compoundTag.getLongArray("conway_borders");
            for (long l : toUpdate) {
                toLoad.add(new ChunkPos(l));
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        compoundTag.putLongArray("conway_alive", aliveBlocks.toLongList());
        long[] toLoadLong = new long[toLoad.size()];
        for (int i = 0; i < toLoad.size(); i++) {
            toLoadLong[i]  = toLoad.get(i).toLong();
        }
        compoundTag.putLongArray("conway_borders", toLoadLong);
        return compoundTag;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return ConwayMain.CHUNKINFO;
    }
}
