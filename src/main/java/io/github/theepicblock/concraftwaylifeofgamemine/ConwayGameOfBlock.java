package io.github.theepicblock.concraftwaylifeofgamemine;

import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class ConwayGameOfBlock extends Block {
    public ConwayGameOfBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient) {
            ConwayChunkInfo updateHolder = getUpdateHolder(world,pos);
            if (updateHolder != null) {
                updateHolder.add(pos);
            }
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClient() && newState.getBlock() != ConwayMain.CONWAY_GAME_OF_BLOCK) {
            ConwayChunkInfo updateHolder = getUpdateHolder(world,pos);
            if (updateHolder != null) {
                updateHolder.remove(pos);
            }
        }
    }

    private ConwayChunkInfo getUpdateHolder(BlockView blockView, BlockPos pos) {
        return getChunkProvider(blockView, pos).getComponent(ConwayMain.CHUNKINFO);
    }

    private ComponentProvider getChunkProvider(BlockView blockView, BlockPos pos) {
        if (blockView instanceof CollisionView) {
            return ComponentProvider.fromChunk(((WorldView) blockView).getChunk(pos));
        }
        return null;
    }
}
