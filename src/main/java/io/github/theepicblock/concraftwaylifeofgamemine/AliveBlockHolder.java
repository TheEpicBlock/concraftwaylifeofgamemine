package io.github.theepicblock.concraftwaylifeofgamemine;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a list of blocks that need to be updated every conway tick.
 */
public class AliveBlockHolder implements CopyableComponent<AliveBlockHolder> {
    private final Block2DbyLayer aliveBlocks = new Block2DbyLayer(0);
    private final List<ChunkPos> toLoad = new ArrayList<>(0);
    //TODO add a command to re sync this list with the actual blocks

    public void add(BlockPos pos) {
        aliveBlocks.put(pos);
        int xRel = pos.getX() & 15;
        int zRel = pos.getZ() & 15;
        if (xRel == 0 | xRel == 15 | zRel == 0 | zRel == 15) {
            System.out.println("BORDER");
        }
    }

    public void remove(BlockPos pos) {
        aliveBlocks.remove(pos);
    }

    public Block2DbyLayer getAliveBlocks() {
        return aliveBlocks;
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        if (compoundTag.contains("conway_alive")) {
            long[] toUpdate = compoundTag.getLongArray("conway_alive");
            aliveBlocks.put(toUpdate);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        compoundTag.putLongArray("conway_alive", aliveBlocks.toLongList());
        return compoundTag;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return ConwayMain.UPDATEHOLDER;
    }
}
