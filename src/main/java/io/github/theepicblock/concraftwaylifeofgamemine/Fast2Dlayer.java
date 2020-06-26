package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

public class Fast2Dlayer extends Int2ObjectArrayMap<IntCollection> {
    public Fast2Dlayer(Fast2Dlayer xyMap) {
        super(xyMap);
    }

    public Fast2Dlayer() {
        super();
    }

    public Fast2Dlayer(int size) {
        super(size);
    }

    public void put(int x, int z) {
        IntCollection zList = getOrPut(x);
        zList.add(z);
    }

    public void put(BlockPos2D pos) {
        this.put(pos.getX(),pos.getZ());
    }

    public void remove(int x, int z) {
        IntCollection zList = get(x);
        zList.rem(z);
    }

    public void remove(BlockPos2D pos) {
        this.remove(pos.getX(),pos.getZ());
    }

    public void removeAll(Fast2Dlayer R) {
        R.forEach((BiConsumer<? super Integer, ? super IntCollection>) (x, zListR) -> {
            IntCollection zList = get((int)x);
            if (zList == null) {
                return;
            }
            zList.removeAll(zListR);
        });
    }

    public boolean contains(int x,  int z) {
        IntCollection zList = get(x);
        if (zList == null) {
            return false;
        }
        return zList.contains(z);
    }

    public void forEachPos(PosConsumer consumer) {
        ObjectIterator<Entry<IntCollection>> iterator = this.int2ObjectEntrySet().fastIterator();
        while (iterator.hasNext()) {
            Entry<IntCollection> entry = iterator.next();
            int x = entry.getIntKey();
            IntCollection zList = entry.getValue();
            zList.forEach((IntConsumer) (z) -> consumer.accept(x,z));
        }
    }

    public IntCollection getOrPut(int x) {
        IntCollection zList = this.get(x);
        if (zList == null) {
            zList = getNewBacking();
            this.put(x, zList);
        }
        return zList;
    }

    protected IntCollection getNewBacking() {
        return new IntArrayList();
    }

    /**
     * get's all elements in a that aren't already in b
     * @param a
     * @param b
     * @return
     */
    public static Fast2Dlayer getMissing(Fast2Dlayer a, Fast2Dlayer b) {
        Fast2Dlayer out = new Fast2Dlayer(a.size());
        a.forEach((i, map) -> {
            IntArrayList newMap = new IntArrayList(map);
            IntCollection toRemove = b.get(i);
            if (toRemove != null) {
                newMap.removeAll(toRemove);
            }
            out.put(i,newMap);
        });
        return out;
    }
}
