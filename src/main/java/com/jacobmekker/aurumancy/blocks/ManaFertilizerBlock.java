package com.jacobmekker.aurumancy.blocks;

import com.jacobmekker.aurumancy.blocks.tileentities.ManaFertilizerTileEntity;
import com.jacobmekker.aurumancy.data.BlockProperties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
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
        if (world.isRemote) return ActionResultType.PASS;

        int current_mana = 0;
        if (state.has(BlockProperties.stored_mana)) current_mana = state.get(BlockProperties.stored_mana);

        if (current_mana <= 81 - 9 && player.experienceTotal >= 9) {
            player.experienceTotal -= 9;
            current_mana += 9;

            world.setBlockState(pos, state.with(BlockProperties.stored_mana, current_mana));
        }

        player.sendMessage(new StringTextComponent("stored_mana=" + current_mana));
        return ActionResultType.SUCCESS;
    }
}
