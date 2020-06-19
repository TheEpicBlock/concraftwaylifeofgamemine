package io.github.theepicblock.concraftwaylifeofgamemine;

import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.*;

import java.util.Random;

public class ConwayGameOfBlock extends Block {
    public ConwayGameOfBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient) {
            AliveBlockHolder updateHolder = getUpdateHolder(world,pos);
            if (updateHolder != null) {
                updateHolder.markForUpdate(pos);
            }
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        //TODO remove from Alive block list
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
