package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;

/**
 * A mess
 */
public class FastLookupBlock2DbyLayer extends Int2ObjectArrayMap<Fast2Dlayer> {
    public FastLookupBlock2DbyLayer(final int capacity) {
        super(capacity);
    }

    public void put(BlockPos pos) {
        this.put(pos.getX(),pos.getY(),pos.getZ());
    }

    public void put(long l) {
        this.put(BlockPos.unpackLongX(l),BlockPos.unpackLongY(l),BlockPos.unpackLongZ(l));
    }

    public void put(int layer, Collection<BlockPos2D> blockList) {
        Fast2Dlayer xzMap = this.getOrPut(layer);
        for (BlockPos2D block : blockList) {
            xzMap.put(block);
        }
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
        list.forEach(this::put);
        ObjectIterator<Entry<Set<BlockPos2D>>> iterator = list.int2ObjectEntrySet().fastIterator();
        while (iterator.hasNext()) {
            Entry<Set<BlockPos2D>> entry = iterator.next();
            this.put(entry.getIntKey(),entry.getValue());
        }
    }

    public void put(int x, int y, int z) {
        Fast2Dlayer xzMap = this.getOrPut(y);
        IntCollection zList = xzMap.getOrPut(x);
        zList.add(z);
    }

    public void remove(BlockPos pos) {
        this.remove(pos.getX(),pos.getY(),pos.getZ());
    }

    public void remove(int x, int y, int z) {
        Int2ObjectArrayMap<IntCollection> xzMap = this.get(y);
        IntCollection zList = xzMap.get(x);
        zList.rem(z);
    }

    public List<BlockPos> toBlockPosList() {
        List<BlockPos> l = new ArrayList<>();
        forEachBlock((layer,blockPos2D) -> l.add(blockPos2D.to3D(layer)));
        return l;
    }

    public Fast2Dlayer getOrPut(int layer) {
        Fast2Dlayer xzMap = this.get(layer);
        if (xzMap == null) {
            xzMap = new Fast2Dlayer();
            this.put(layer, xzMap);
        }
        return xzMap;
    }

    public LongList toLongList() {
        LongList l = new LongArrayList();
        forEachBlock((layer,blockPos2D) -> l.add(blockPos2D.toLong(layer)));
        return l;
    }

    public void forEachBlock(BiConsumer<? super Integer, ? super BlockPos2D> action) {
        this.forEach((y,xzMap) ->
                xzMap.forEachPos((PosConsumer) (x, z) ->
                        action.accept(y, new BlockPos2D(x, z))));
    }
}
