package io.github.theepicblock.concraftwaylifeofgamemine;

import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
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
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            AliveBlockHolder updateHolder = getUpdateHolder(world,pos);
            if (updateHolder != null) {
                updateHolder.markForUpdate(pos);
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
