package io.github.theepicblock.concraftwaylifeofgamemine;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

import static io.github.theepicblock.concraftwaylifeofgamemine.BlockPos2D.getChunkPos;

/**
 * Contains a list of blocks that need to be updated every conway tick.
 */
public class ConwayChunkInfo implements CopyableComponent<ConwayChunkInfo> {
    private final Block2DbyLayer aliveBlocks = new Block2DbyLayer(0);
    private final List<ChunkPos> toLoad = new ArrayList<>(0);
    //TODO add a command to re sync this list with the actual blocks

    public void add(BlockPos pos) {
        boolean c = aliveBlocks.put(pos);
        if (c) this.addToLoads(pos);
    }

    public void remove(BlockPos pos) {
        aliveBlocks.remove(pos);

        int xRel = getChunkPos(pos.getX());
        int zRel = getChunkPos(pos.getZ());
        if (xRel >= 14 | xRel <= 1 | zRel >= 14 | zRel <= 1) {
            recalculateToLoad();
        }
    }

    public Block2DbyLayer getAliveBlocks() {
        return aliveBlocks;
    }

    public void recalculateToLoad() {
        toLoad.clear();
        aliveBlocks.forEachBlock((layer,pos) -> this.addToLoads(pos));
    }

    private void addToLoads(BlockPos pos) {
        int xRel = getChunkPos(pos.getX());
        int zRel = getChunkPos(pos.getZ());
        int xCPos = pos.getX() >> 4;
        int zCPos = pos.getZ() >> 4;
        if (xRel >= 14) {
            toLoad.add(new ChunkPos(xCPos+1,zCPos));
        }
        if (xRel <= 1) {
            toLoad.add(new ChunkPos(xCPos-1,zCPos));
        }
        if (zRel >= 14) {
            toLoad.add(new ChunkPos(xCPos,zCPos+1));
        }
        if (zRel <= 1) {
            toLoad.add(new ChunkPos(xCPos,zCPos-1));
        }
    }

    private void addToLoads(BlockPos2D pos) {
        int xRel = getChunkPos(pos.getX());
        int zRel = getChunkPos(pos.getZ());
        if (xRel >= 14) {
            toLoad.add(new ChunkPos(pos.getX()+1,pos.getZ()));
        }
        if (xRel <= 1) {
            toLoad.add(new ChunkPos(pos.getX()-1,pos.getZ()));
        }
        if (zRel >= 14) {
            toLoad.add(new ChunkPos(pos.getX(),pos.getZ()+1));
        }
        if (zRel <= 1) {
            toLoad.add(new ChunkPos(pos.getX(),pos.getZ()-1));
        }
    }

    /*
    Saving and loading is *not* needed.
    The game already calls the onBlockAdded function on every block when a chunk is loaded
    This will already add all of the stuff into the chunk
     */
    @Override
    public void fromTag(CompoundTag compoundTag) {
//        if (compoundTag.contains("conway_alive")) {
//            long[] toUpdate = compoundTag.getLongArray("conway_alive");
//            aliveBlocks.put(toUpdate);
//        }
//        if (compoundTag.contains("conway_borders")) {
//            long[] toUpdate = compoundTag.getLongArray("conway_borders");
//            for (long l : toUpdate) {
//                toLoad.add(new ChunkPos(l));
//            }
//        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
//        compoundTag.putLongArray("conway_alive", aliveBlocks.toLongList());
//        long[] toLoadLong = new long[toLoad.size()];
//        for (int i = 0; i < toLoad.size(); i++) {
//            toLoadLong[i]  = toLoad.get(i).toLong();
//        }
//        compoundTag.putLongArray("conway_borders", toLoadLong);
        return compoundTag;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return ConwayMain.CHUNKINFO;
    }
}
