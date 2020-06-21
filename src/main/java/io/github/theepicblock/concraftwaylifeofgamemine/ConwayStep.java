package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.Arrays;
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

        AliveBlockHolder chunkAliveHolder = getAliveHolderFromChunk(chunk);
        aliveBlocks.putAll(chunkAliveHolder.getAliveBlocks());
    }

    public void doStuff() {
        if(!(world.getDimension() == DimensionType.getOverworldDimensionType())) return;
        System.out.println("DOING STUFF!!");
        aliveBlocks.forEach((layer,blockList) -> {
            System.out.println("processing layer: " + layer);
            Set<BlockPos2D> newList = updateLayer(blockList);
            List<BlockPos2D> toRemove = new ArrayList<>(blockList);
            toRemove.removeAll(newList);
            newList.removeAll(blockList);

            toRemove.forEach((pos) -> world.setBlockState(pos.to3D(layer), Blocks.AIR.getDefaultState()));
            newList.forEach((pos) -> world.setBlockState(pos.to3D(layer), ConwayMain.CONWAY_GAME_OF_BLOCK.getDefaultState()));
        });
    }

    public static Set<BlockPos2D> updateLayer(Set<BlockPos2D> alive) {
        Set<BlockPos2D> toUpdate = new ObjectArraySet<>();
        Set<BlockPos2D> newAlive = new ObjectArraySet<>();
        alive.forEach((pos) -> toUpdate.addAll(Arrays.asList(getNeighbours(pos))));
        toUpdate.forEach((pos) -> {
            //see https://en.wikipedia.org/wiki/Conway_game#Rules (the condensed rules)
            int c = countNeighbours(alive, pos);
            if (c == 3) {
                newAlive.add(pos);
                return;
            }
            boolean isAlive = alive.contains(pos);
            if (isAlive && c == 2) {
                newAlive.add(pos);
            }
        });
        return newAlive;
    }

    public static int countNeighbours(Set<BlockPos2D> alive, BlockPos2D pos) {
        int i = 0;
        for (BlockPos2D p : getNeighbours(pos)) {
            if (alive.contains(p)) {
                i++;
            }
        }
        return i;
    }

    public static BlockPos2D[] getNeighbours(BlockPos2D c) {
        BlockPos2D[] arr = new BlockPos2D[8];
        int t = 0;
        for (int xOff = -1; xOff <= 1; xOff++) {
            for (int zOff = -1; zOff <= 1; zOff++) {
                if (xOff != 0 || zOff != 0) {
                    arr[t] = new BlockPos2D(c.getX()+xOff,c.getZ()+zOff);
                    t++;
                }
            }
        }
        return arr;
    }

    public static AliveBlockHolder getAliveHolderFromChunk(Chunk chunk) {
        return ComponentProvider.fromChunk(chunk).getComponent(ConwayMain.UPDATEHOLDER);
    }
}
