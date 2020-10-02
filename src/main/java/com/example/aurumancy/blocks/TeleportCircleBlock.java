package com.example.aurumancy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TeleportCircleBlock extends Block {

    private static int xp_cost = 9;

    public TeleportCircleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TeleportCircleTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (world.isRemote) return ActionResultType.PASS;

        TeleportCircleTileEntity here = (TeleportCircleTileEntity) world.getTileEntity(pos);
        if (here == null || !here.checkValidityAndColor()) return ActionResultType.FAIL;

        if (player.experienceTotal < xp_cost) {
            player.sendMessage(new StringTextComponent("You don't have enough mana to use this."));
            return ActionResultType.PASS;
        }

        List<TeleportCircleTileEntity> filtered = new ArrayList<>();
        for (TileEntity te : world.loadedTileEntityList) {
            if (te instanceof TeleportCircleTileEntity && here != te) {
                TeleportCircleTileEntity tc_te = (TeleportCircleTileEntity) te;
                if (tc_te.checkValidityAndColor() && tc_te.color == here.color) {
                    filtered.add((TeleportCircleTileEntity)te);
                    LogManager.getLogger().debug("Matching " + tc_te.color + " circle found "
                            + here.getPos().manhattanDistance(te.getPos())
                            + " blocks away.");
                }
                else if (tc_te.checkValidityAndColor()) {
                    LogManager.getLogger().debug("Non-matching " + tc_te.color + " circle found "
                            + here.getPos().manhattanDistance(te.getPos())
                            + " blocks away.");
                }
                else {
                    LogManager.getLogger().debug("Invalid circle found "
                            + here.getPos().manhattanDistance(te.getPos())
                            + " blocks away.");
                }
            }
        }

        if (filtered.size() > 0) {
            TeleportCircleTileEntity dest = filtered.get(world.rand.nextInt(filtered.size()));
            player.attemptTeleport(
                    dest.getPos().getX() + 0.5,
                    dest.getPos().getY() + 1,
                    dest.getPos().getZ() + 0.5,
                    false);
            player.experienceTotal -= xp_cost;
        }
        else {
            player.sendMessage(new StringTextComponent(
                    "No matching " + here.color + " teleport circles in this dimension."));
        }

        return ActionResultType.SUCCESS;
    }
}
