package io.github.theepicblock.concraftwaylifeofgamemine;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

import java.util.Collection;
import java.util.Objects;

/**
 * A 2d BlockPos (top-down)
 * Also contains utility functions for working with block positions in general
 */
public class BlockPos2D {
    private final int x;
    private final int z;
    private static final int SIZE_BITS_X;
    private static final int SIZE_BITS_Z;
    private static final int SIZE_BITS_Y;
    private static final long BITS_X;
    private static final long BITS_Y;
    private static final long BITS_Z;
    private static final int BIT_SHIFT_Z;
    private static final int BIT_SHIFT_X;

    public BlockPos2D(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public long toLong() {
        long l = 0L;
        l |= ((long)x & BITS_X) << BIT_SHIFT_X;
        l |= ((long)z & BITS_Z) << BIT_SHIFT_Z;
        return l;
    }

    public long toLong(int y) {
        long l = 0L;
        l |= ((long)x & BITS_X) << BIT_SHIFT_X;
        l |= ((long)y & BITS_Y);
        l |= ((long)z & BITS_Z) << BIT_SHIFT_Z;
        return l;
    }

    public BlockPos to3D(int y) {
        return new BlockPos(x,y,z);
    }

    public ChunkPos toChunkPos() {
        return new ChunkPos(this.x >> 4, this.z >> 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockPos2D that = (BlockPos2D) o;
        return x == that.x &&
               z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    public String toString() {
        return x + "," + z;
    }

    public static BlockPos2D fromLong(long Long) {
        return new BlockPos2D(BlockPos.unpackLongX(Long),BlockPos.unpackLongZ(Long));
    }

    public static BlockPos2D from3D(BlockPos pos) {
        return new BlockPos2D(pos.getX(),pos.getZ());
    }

    //GENERAL UTILS
    public static int getChunkPos(int i) {
        return i & 15;
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

    public static void addNeighbours(BlockPos2D center, Collection<BlockPos2D> toAddTo) {
        for (int xOff = -1; xOff <= 1; xOff++) {
            for (int zOff = -1; zOff <= 1; zOff++) {
                if (xOff != 0 || zOff != 0) {
                    toAddTo.add(new BlockPos2D(center.getX()+xOff,center.getZ()+zOff));
                }
            }
        }
    }

    public static void addNeighbours(int x, int z, Fast2Dlayer toAddTo) {
        for (int xOff = -1; xOff <= 1; xOff++) {
            for (int zOff = -1; zOff <= 1; zOff++) {
                if (xOff != 0 || zOff != 0) {
                    toAddTo.put(x+xOff,z+zOff);
                }
            }
        }
    }

    static {
        SIZE_BITS_X = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
        SIZE_BITS_Z = SIZE_BITS_X;
        SIZE_BITS_Y = 64 - SIZE_BITS_X - SIZE_BITS_Z;
        BITS_X = (1L << SIZE_BITS_X) - 1L;
        BITS_Y = (1L << SIZE_BITS_Y) - 1L;
        BITS_Z = (1L << SIZE_BITS_Z) - 1L;
        BIT_SHIFT_Z = SIZE_BITS_Y;
        BIT_SHIFT_X = SIZE_BITS_Y + SIZE_BITS_Z;
    }
}
