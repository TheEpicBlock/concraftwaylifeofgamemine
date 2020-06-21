package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;

/**
 * provides some functions so you don't have to directly interface with an Int2ObjectArrayMap<List<BlockPos2D>>
 */
public class Block2DbyLayer extends Int2ObjectArrayMap<Set<BlockPos2D>> {
    public Block2DbyLayer(final int capacity) {
        super(capacity);
    }

    public boolean put(BlockPos pos) {
        int y = pos.getY();
        Set<BlockPos2D> posList = this.getOrPut(y);
        return posList.add(BlockPos2D.from3D(pos));
    }

    public void put(long l) {
        int y = BlockPos.unpackLongY(l);
        Set<BlockPos2D> posList = this.getOrPut(y);
        posList.add(BlockPos2D.fromLong(l));
    }

    public void put(Set<BlockPos2D> blockList, int layer) {
        this.getOrPut(layer).addAll(blockList);
    }

    public void put(LongList list) {
        list.forEach((LongConsumer) this::put);
    }

    public void put(long[] list) {
        for (long l : list) {
            this.put(l);
        }
    }

    public void putAll(Block2DbyLayer list) {
        list.forEach((layer,blockList) -> this.put(blockList, layer));
    }

    public void remove(BlockPos pos) {
        int y = pos.getY();
        Set<BlockPos2D> posList = this.get(y);
        if (posList != null) {
            posList.remove(BlockPos2D.from3D(pos));
        }
    }

    public List<BlockPos> toBlockPosList() {
        List<BlockPos> l = new ArrayList<>();
        forEachBlock((layer,blockPos2D) -> l.add(blockPos2D.to3D(layer)));
        return l;
    }

    public Set<BlockPos2D> getOrPut(int layer) {
        Set<BlockPos2D> posList = this.get(layer);
        if (posList == null) {
            posList = new ObjectArraySet<>();
            this.put(layer, posList);
        }
        return posList;
    }

    public LongList toLongList() {
        LongList l = new LongArrayList();
        forEachBlock((layer,blockPos2D) -> l.add(blockPos2D.toLong(layer)));
        return l;
    }

    public void forEachBlock(BiConsumer<? super Integer, ? super BlockPos2D> action) {
        this.forEach((layer,Block2dList) -> Block2dList.forEach((blockPos2D) -> action.accept(layer, blockPos2D)));
    }
}