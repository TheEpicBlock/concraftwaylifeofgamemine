package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConwayStep {
    private final World world;
    private final Block2DbyLayer aliveBlocks = new Block2DbyLayer(10);
    private final Block2DbyLayer extraBlocks = new Block2DbyLayer(10);
    private final List<ChunkPos> indexedChunks = new ArrayList<>();
    private final List<ChunkPos> indexedExtraChunks = new ArrayList<>();

    public ConwayStep(World world) {
        this.world = world;
    }

    public void add(Chunk chunk) {
        indexedChunks.add(chunk.getPos());

        AliveBlockHolder chunkAliveHolder = getAliveHolderFromChunk(chunk);
        aliveBlocks.putAll(chunkAliveHolder.getAliveBlocks());
    }

    public void addExtra(BlockPos pos) {
        Chunk chunk = world.getChunk(pos);

        indexedExtraChunks.add(chunk.getPos());

        AliveBlockHolder chunkAliveHolder = getAliveHolderFromChunk(chunk);
        extraBlocks.putAll(chunkAliveHolder.getAliveBlocks());
    }

    public void doStuff() {
        if(!(world.getDimension() == DimensionType.getOverworldDimensionType())) return;
        System.out.println("DOING STUFF!!");
        aliveBlocks.forEach((layer,blockList) -> {
            System.out.println("processing layer: " + layer);
            Set<BlockPos2D> newList = updateLayer(blockList);
            List<BlockPos2D> toRemove = new ArrayList<>(blockList);

            //Magic stuff to update the blocks
            toRemove.removeAll(newList);
            newList.removeAll(blockList);

            toRemove.forEach((pos) -> world.setBlockState(pos.to3D(layer), Blocks.AIR.getDefaultState()));
            newList.forEach((pos) -> {
                BlockPos p = pos.to3D(layer);
                if (world.getBlockState(p).isAir())
                world.setBlockState(p, ConwayMain.CONWAY_GAME_OF_BLOCK.getDefaultState());
            });
        });
    }

    private Set<BlockPos2D> updateLayer(Set<BlockPos2D> alive) {
        Set<BlockPos2D> toUpdate = new ObjectArraySet<>();
        Set<BlockPos2D> newAlive = new ObjectArraySet<>();
        alive.forEach((pos) -> BlockPos2D.addNeighbours(pos,toUpdate));



        //Handle conway stuff
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
        for (BlockPos2D p : BlockPos2D.getNeighbours(pos)) {
            if (alive.contains(p)) {
                i++;
            }
        }
        return i;
    }

    public static AliveBlockHolder getAliveHolderFromChunk(Chunk chunk) {
        return ComponentProvider.fromChunk(chunk).getComponent(ConwayMain.UPDATEHOLDER);
    }
}
