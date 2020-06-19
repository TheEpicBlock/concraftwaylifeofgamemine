package io.github.theepicblock.concraftwaylifeofgamemine;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a list of blocks that need to be updated every conway tick.
 */
public class AliveBlockHolder implements CopyableComponent {
    private final Block2DbyLayer AliveBlocks = new Block2DbyLayer();

    public void markForUpdate(BlockPos pos) {
        AliveBlocks.put(pos);
    }

    public Int2ObjectArrayMap<List<BlockPos2D>> getAliveBlocks() {
        return AliveBlocks;
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        if (compoundTag.contains("conway_alive")) {
            long[] toUpdate = compoundTag.getLongArray("Conway_ToUpdate");
            AliveBlocks.putFromLongList(toUpdate);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        compoundTag.putLongArray("conway_alive", AliveBlocks.toLongList());
        return compoundTag;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return ConwayMain.UPDATEHOLDER;
    }
}
