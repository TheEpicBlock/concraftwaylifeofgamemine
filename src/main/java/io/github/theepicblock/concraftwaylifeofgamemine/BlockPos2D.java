package io.github.theepicblock.concraftwaylifeofgamemine;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;
import java.util.function.Consumer;

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

    public static BlockPos2D fromLong(long Long) {
        return new BlockPos2D(BlockPos.unpackLongX(Long),BlockPos.unpackLongZ(Long));
    }

    public static BlockPos2D from3D(BlockPos pos) {
        return new BlockPos2D(pos.getX(),pos.getY());
    }

    //GENERAL UTILS
    public static void forChunkBorders(BlockPos pos, Consumer<ChunkPos> consumer) {
        int xRel = getChunkPos(pos.getX());
        int zRel = getChunkPos(pos.getZ());
        if (isOnChunkBorder(xRel,zRel)) {
            forChunkBorders(xRel,zRel,new ChunkPos(pos),consumer);
        }
    }

    public static void forChunkBorders(BlockPos2D pos, Consumer<ChunkPos> consumer) {
        int xRel = getChunkPos(pos.getX());
        int zRel = getChunkPos(pos.getZ());
        if (isOnChunkBorder(xRel,zRel)) {
            forChunkBorders(xRel,zRel,pos.toChunkPos(),consumer);
        }
    }

    public static void forChunkBorders(int x, int z, ChunkPos center, Consumer<ChunkPos> consumer) {
        System.out.println("BORDER");
        if (x == 0) {
            consumer.accept(new ChunkPos(center.x-1,center.z));
        } else if (x == 15) {
            consumer.accept(new ChunkPos(center.x+1,center.z));
        }
        if (z == 0) {
            consumer.accept(new ChunkPos(center.x,center.z-1));
        } else if (z == 15) {
            consumer.accept(new ChunkPos(center.x,center.z+1));
        }
    }

    public static boolean isOnChunkBorder(int xRel, int zRel) {
        return xRel == 0 | xRel == 15 | zRel == 0 | zRel == 15;
    }

    public static boolean isOnChunkBorder(BlockPos pos) {
        int xRel = getChunkPos(pos.getX());
        int zRel = getChunkPos(pos.getZ());
        return isOnChunkBorder(xRel,zRel);
    }

    public static boolean isOnChunkBorder(BlockPos2D pos) {
        int xRel = getChunkPos(pos.getX());
        int zRel = getChunkPos(pos.getZ());
        return isOnChunkBorder(xRel,zRel);
    }

    public static int getChunkPos(int i) {
        return i & 15;
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
