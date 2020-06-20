package io.github.theepicblock.concraftwaylifeofgamemine;

import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class ConwayGameOfBlock extends Block {
    public ConwayGameOfBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient) {
            AliveBlockHolder updateHolder = getUpdateHolder(world,pos);
            if (updateHolder != null) {
                updateHolder.add(pos);
            }
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (!world.isClient()) {
            AliveBlockHolder updateHolder = getUpdateHolder(world,pos);
            if (updateHolder != null) {
                updateHolder.remove(pos);
            }
        }
    }

    private AliveBlockHolder getUpdateHolder(BlockView blockView, BlockPos pos) {
        return getChunkProvider(blockView, pos).getComponent(ConwayMain.UPDATEHOLDER);
    }

    private ComponentProvider getChunkProvider(BlockView blockView, BlockPos pos) {
        if (blockView instanceof CollisionView) {
            return ComponentProvider.fromChunk(((WorldView) blockView).getChunk(pos));
        }
        return null;
    }
}
