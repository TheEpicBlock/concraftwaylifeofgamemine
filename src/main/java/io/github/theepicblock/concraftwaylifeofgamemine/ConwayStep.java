package io.github.theepicblock.concraftwaylifeofgamemine;

import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class ConwayStep {
    private final World world;
    private final Block2DbyLayer aliveBlocks = new Block2DbyLayer(10);
    private final List<ChunkPos> indexedChunks = new ArrayList<>();

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
        aliveBlocks.forEachBlock((layer, block) -> System.out.println(block.getX()+","+block.getZ()));
    }


    public static AliveBlockHolder getAliveHolderFromChunk(Chunk chunk) {
        return ComponentProvider.fromChunk(chunk).getComponent(ConwayMain.UPDATEHOLDER);
    }
}
