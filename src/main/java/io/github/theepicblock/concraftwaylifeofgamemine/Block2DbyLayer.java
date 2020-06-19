package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * provides some functions so you don't have to directly interface with an Int2ObjectArrayMap<List<BlockPos2D>>
 */
public class Block2DbyLayer extends Int2ObjectArrayMap<List<BlockPos2D>> {
    public void put(BlockPos pos) {
        int y = pos.getY();
        List<BlockPos2D> posList = this.getOrDefault(y, new ArrayList<>());
        posList.add(BlockPos2D.from3D(pos));
    }

    public void put(long l) {
        int y = BlockPos.unpackLongY(l);
        List<BlockPos2D> posList = this.getOrDefault(y, new ArrayList<>());
        posList.add(BlockPos2D.fromLong(l));
    }

    public List<BlockPos> toBlockPosList() {
        List<BlockPos> l = new ArrayList<>();
        forEachBlock((layer,blockPos2D) -> {
            l.add(blockPos2D.to3D(layer));
        });
        return l;
    }

    public LongList toLongList() {
        LongList l = new LongArrayList();
        forEachBlock((layer,blockPos2D) -> {
            l.add(blockPos2D.toLong(layer));
        });
        return l;
    }

    public void forEachBlock(BiConsumer<? super Integer, ? super BlockPos2D> action) {
        this.forEach((layer,Block2dList) -> {
            Block2dList.forEach((blockPos2D) -> {
                action.accept(layer,blockPos2D);
            });
        });
    }

    public void putFromLongList(LongList list) {
        list.forEach((LongConsumer) this::put);
    }
    public void putFromLongList(long[] list) {
        for (long l : list) {
            this.put(l);
        }
    }
}