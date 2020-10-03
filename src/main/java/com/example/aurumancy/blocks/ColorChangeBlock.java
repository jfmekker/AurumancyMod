package com.example.aurumancy.blocks;

import com.example.aurumancy.Aurumancy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class ColorChangeBlock extends Block {

    public static final BooleanProperty IS_GREEN = BooleanProperty.create("is_green");

    public ColorChangeBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(IS_GREEN, true));
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);

        if (worldIn.isRemote) return;
        Aurumancy.LOGGER.debug("ColorChangeBlock landed at: " + pos.toString());
        Aurumancy.LOGGER.debug("IS_GREEN = " + worldIn.getBlockState(pos).get(IS_GREEN));

        if (fallDistance > 0.75) {
            worldIn.setBlockState(pos, worldIn.getBlockState(pos).with(IS_GREEN, !worldIn.getBlockState(pos).get(IS_GREEN)));
            Aurumancy.LOGGER.debug("IS_GREEN = " + worldIn.getBlockState(pos).get(IS_GREEN));

        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(IS_GREEN);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Random rand = new Random();

        return super.getStateForPlacement(context);
    }
}
