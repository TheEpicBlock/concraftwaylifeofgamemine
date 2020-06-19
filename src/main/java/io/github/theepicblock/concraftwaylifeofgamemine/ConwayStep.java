package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.longs.LongList;
import jdk.nashorn.internal.ir.Block;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConwayStep {
    private final Map<World, Block2DbyLayer> aliveBlocks = new HashMap<>();
    private final List<ChunkPos> indexedChunks = new ArrayList<>();

    public void add(Chunk chunk, World world) {
        indexedChunks.add(chunk.getPos());

        AliveBlockHolder chunkAliveHolder = getAliveHolderFromChunk(chunk);
        Block2DbyLayer storedBlocks = aliveBlocks.get(world); //Get's the stored blocks in this world
        if (storedBlocks == null) {
            storedBlocks = new Block2DbyLayer();
            aliveBlocks.put(world, storedBlocks);
        }
        storedBlocks.putAll(chunkAliveHolder.getAliveBlocks());
    }

    public void doStuff() {
        System.out.println("DOING STUFF!!");
        aliveBlocks.forEach((world,blockLayerList) -> {
            System.out.println(world.getDimension() == DimensionType.getOverworldDimensionType() ? "overworld" : "some other dimension");
            blockLayerList.forEach((layer,list) -> {
                System.out.println(layer + ": " + list.size());
            });
        });
    }

    public static AliveBlockHolder getAliveHolderFromChunk(Chunk chunk) {
        return ComponentProvider.fromChunk(chunk).getComponent(ConwayMain.UPDATEHOLDER);
    }
}
