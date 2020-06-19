package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

/**
 * Contains a list of blocks that need to be updated every conway tick.
 */
public class AliveBlockHolder implements CopyableComponent {
    private final LongList AliveBlocks = new LongArrayList();

    public void markForUpdate(BlockPos pos) {
        for(long l : AliveBlocks) {
            System.out.println(l);
        }
        AliveBlocks.add(pos.asLong());
    }

    public LongList getAlive() {
        return AliveBlocks;
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        if (compoundTag.contains("conway_alive")) {
            long[] toUpdate = compoundTag.getLongArray("Conway_ToUpdate");
            for (long i: toUpdate) {
                AliveBlocks.add(i);
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        compoundTag.putLongArray("conway_alive", AliveBlocks);
        return compoundTag;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return ConwayMain.UPDATEHOLDER;
    }
}
