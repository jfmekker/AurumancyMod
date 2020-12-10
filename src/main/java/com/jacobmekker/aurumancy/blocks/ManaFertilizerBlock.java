package com.jacobmekker.aurumancy.blocks;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.blocks.tileentities.ManaFertilizerTileEntity;
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

public class ManaFertilizerBlock extends Block {

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BlockProperties.stored_mana);
    }

    public ManaFertilizerBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(BlockProperties.stored_mana, 0));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ManaFertilizerTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isRemote) return ActionResultType.SUCCESS;

        if (!player.getHeldItem(hand).isEmpty()) return ActionResultType.PASS;

        int current_mana = state.get(BlockProperties.stored_mana);
        int needed_mana = Math.min(81 - current_mana, 9);

        if (needed_mana > 0 && PlayerEntityHelper.GetActualExperienceTotal(player) >= needed_mana) {
            PlayerEntityHelper.AddActualExperienceTotal(player, -needed_mana);
            current_mana += needed_mana;
            world.playEvent(2005, pos.add(0,1,0), 0);
            world.setBlockState(pos, state.with(BlockProperties.stored_mana, current_mana));
        }

        Aurumancy.LOGGER.trace("ManaFertilizer stored_mana=" + current_mana);
        return ActionResultType.SUCCESS;
    }
}
