package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.ints.IntCollection;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ConwayStep {
    private final World world;
    private final FastLookupBlock2DbyLayer aliveBlocks = new FastLookupBlock2DbyLayer(10);
    private final List<ChunkPos> indexedChunks = new ArrayList<>();
    private final List<ChunkPos> toBeLoaded = new ArrayList<>();

    public ConwayStep(World world) {
        this.world = world;
    }

    public void add(Chunk chunk) {
        indexedChunks.add(chunk.getPos());

        ConwayChunkInfo chunkInfo = getChunkInfo(chunk);
        chunkInfo.recalculateToLoad();
        aliveBlocks.putAll(chunkInfo.getAliveBlocks());
        toBeLoaded.addAll(chunkInfo.getToLoad());
    }

    public void add(ChunkPos pos) {
        add(world.getChunk(pos.getCenterBlockPos()));
    }

    public void doStuff() {
        //make sure all the chunks are loaded
        toBeLoaded.removeAll(indexedChunks);
        while (!toBeLoaded.isEmpty()) {
            add(toBeLoaded.get(0));
            toBeLoaded.removeAll(indexedChunks);
        }

        //Update layers
        aliveBlocks.forEach((layer,xyMap) -> {
            Fast2Dlayer newList = updateLayer(xyMap);

            Fast2Dlayer toRemove = Fast2Dlayer.getMissing(xyMap,newList);
            newList.removeAll(xyMap);

            toRemove.forEachPos((x, z) -> world.setBlockState(new BlockPos(x,layer,z), Blocks.AIR.getDefaultState()));
            newList.forEachPos((x, z) -> {
                BlockPos p = new BlockPos(x,layer,z);
                BlockState prev = world.getBlockState(p);
                if (!prev.isAir()) {
                    if (prev.isIn(BlockTags.WITHER_IMMUNE)) {
                        return; //can't break these :(
                    }
                    world.breakBlock(p,true);
                }
                world.setBlockState(p, ConwayMain.CONWAY_GAME_OF_BLOCK.getDefaultState());
            });
        });
    }

    public static Fast2Dlayer updateLayer(Fast2Dlayer alive) {
        Fast2Dlayer toUpdate = new SetBackedFast2Dlayer();
        Fast2Dlayer newAlive = new Fast2Dlayer();
        alive.forEachPos((PosConsumer) (x, z) -> BlockPos2D.addNeighbours(x,z,toUpdate));
        toUpdate.forEachPos((PosConsumer) (x, z) -> {
            int c = countNeighbours(alive, x, z);
            if (c == 3) {
                newAlive.put(x,z);
                return;
            }
            boolean isAlive = alive.contains(x,z);
            if (isAlive && c == 2) {
                newAlive.put(x,z);
            }
        });
        return newAlive;
    }

    public static int countNeighbours(Fast2Dlayer alive, int x, int z) {
        int i = 0;
        for (int xOff = -1; xOff <= 1; xOff++) {
            IntCollection zList = alive.get(x+xOff);
            if (zList == null) {
                continue;
            }
            for (int zOff = -1; zOff <= 1; zOff++) {
                if (xOff != 0 || zOff != 0) {
                    if (zList.contains(z+zOff)) {
                        i++;
                        if (i == 4) {
                            return i; //no point in counting further than 4
                        }
                    }
                }
            }
        }
        return i;
    }

    public static ConwayChunkInfo getChunkInfo(Chunk chunk) {
        return ComponentProvider.fromChunk(chunk).getComponent(ConwayMain.CHUNKINFO);
    }
}
