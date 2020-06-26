package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
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
import java.util.Set;

public class ConwayStep {
    private final World world;
    private final Block2DbyLayer aliveBlocks = new Block2DbyLayer(10);
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
        aliveBlocks.forEach((layer,blockList) -> {
            Set<BlockPos2D> newList = updateLayer(blockList);
            List<BlockPos2D> toRemove = new ArrayList<>(blockList);
            toRemove.removeAll(newList);
            newList.removeAll(blockList);

            toRemove.forEach((pos) -> world.setBlockState(pos.to3D(layer), Blocks.AIR.getDefaultState()));
            newList.forEach((pos) -> {
                BlockPos p = pos.to3D(layer);
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

    public static Set<BlockPos2D> updateLayer(Set<BlockPos2D> alive) {
        Set<BlockPos2D> toUpdate = new ObjectArraySet<>();
        Set<BlockPos2D> newAlive = new ObjectArraySet<>();
        alive.forEach((pos) -> BlockPos2D.addNeighbours(pos,toUpdate));
        for (BlockPos2D pos : toUpdate) {
            //see https://en.wikipedia.org/wiki/Conway_game#Rules (the condensed rules)
            int c = countNeighbours(alive, pos);
            if (c == 3) {
                newAlive.add(pos);
                continue;
            }
            boolean isAlive = alive.contains(pos);
            if (isAlive && c == 2) {
                newAlive.add(pos);
            }
        }
        return newAlive;
    }

    public static int countNeighbours(Set<BlockPos2D> alive, BlockPos2D pos) {
        int i = 0;
        for (int xOff = -1; xOff <= 1; xOff++) {
            for (int zOff = -1; zOff <= 1; zOff++) {
                if (xOff != 0 || zOff != 0) {
                    BlockPos2D a = new BlockPos2D(pos.getX()+xOff,pos.getZ()+zOff);
                    if (alive.contains(a)) {
                        i++;
                        if (i == 4) {
                            return i; //no point in counting more then 4
                        }
                    }
                }
            }
        }
        return i;
        //TODO check if this is still slow
    }

    public static ConwayChunkInfo getChunkInfo(Chunk chunk) {
        return ComponentProvider.fromChunk(chunk).getComponent(ConwayMain.CHUNKINFO);
    }
}
