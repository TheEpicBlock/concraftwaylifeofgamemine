package io.github.theepicblock.concraftwaylifeofgamemine;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

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

    public static BlockPos2D fromLong(long Long) {
        return new BlockPos2D(BlockPos.unpackLongX(Long),BlockPos.unpackLongZ(Long));
    }

    public static BlockPos2D from3D(BlockPos pos) {
        return new BlockPos2D(pos.getX(),pos.getY());
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
