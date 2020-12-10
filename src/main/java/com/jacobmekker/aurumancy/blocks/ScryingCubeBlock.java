package com.jacobmekker.aurumancy.blocks;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.blocks.tileentities.ManaFertilizerTileEntity;
import com.jacobmekker.aurumancy.blocks.tileentities.ScryingCubeTileEntity;
import com.jacobmekker.aurumancy.data.BlockProperties;
import com.jacobmekker.aurumancy.utils.PlayerEntityHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ScryingCubeBlock extends Block {

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
    }

    public ScryingCubeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ScryingCubeTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isRemote)
            return ActionResultType.SUCCESS;

        TileEntity TE = world.getTileEntity(pos);
        if (TE instanceof ScryingCubeTileEntity) {
            ((ScryingCubeTileEntity)TE).startScry(player);
        }

        Aurumancy.LOGGER.debug("Scrying Cube used.");
        return ActionResultType.SUCCESS;
    }
}
