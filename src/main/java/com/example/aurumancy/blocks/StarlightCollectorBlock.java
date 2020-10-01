package com.example.aurumancy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class StarlightCollectorBlock extends Block {

    public StarlightCollectorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new StarlightCollectorTileEntity();
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entityIn, float fallDistance) {
        super.onFallenUpon(world, pos, entityIn, fallDistance);
        if (!world.isRemote) {
            StarlightCollectorTileEntity tile = (StarlightCollectorTileEntity) world.getTileEntity(pos);
            if (tile == null) return;
            entityIn.sendMessage(new StringTextComponent("Starlight: " + tile.stored_starlight));
        }
    }
}
