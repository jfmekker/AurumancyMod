package com.example.aurumancy.blocks;

import javafx.geometry.Pos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TeleportCircleBlock extends Block {

    public static List<Tuple<DimensionType, BlockPos>> circles = new ArrayList<>();

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
        if (!isInCircleList(world, pos)) {
            LogManager.getLogger().debug("Adding circle to list.");
            circles.add(new Tuple<>(world.dimension.getType(), pos));
        }

        if (player.experienceTotal < xp_cost) {
            player.sendMessage(new StringTextComponent("You don't have enough mana to use this."));
            return ActionResultType.PASS;
        }

        List<TeleportCircleTileEntity> world_tc_te = new ArrayList<>();
        for (int i = 0 ; i < circles.size() ; i += 1) {
            if (world.dimension.getType() != circles.get(i).getA()) continue;
            if (!(world.getTileEntity(circles.get(i).getB()) instanceof TeleportCircleTileEntity)) {
                LogManager.getLogger().debug("Removing circle from list.");
                circles.remove(i);
                i -= 1;
            }
            else {
                world_tc_te.add((TeleportCircleTileEntity)world.getTileEntity(circles.get(i).getB()));
            }
        }

        List<TeleportCircleTileEntity> matching = new ArrayList<>();
        for (TeleportCircleTileEntity tc_te : world_tc_te) {
            if (here != tc_te) {
                if (tc_te.checkValidityAndColor() && tc_te.color == here.color) {
                    matching.add(tc_te);
                    LogManager.getLogger().debug("Matching " + tc_te.color + " circle found "
                            + here.getPos().manhattanDistance(tc_te.getPos())
                            + " blocks away.");
                }
                else if (tc_te.checkValidityAndColor()) {
                    LogManager.getLogger().debug("Non-matching " + tc_te.color + " circle found "
                            + here.getPos().manhattanDistance(tc_te.getPos())
                            + " blocks away.");
                }
                else {
                    LogManager.getLogger().debug("Invalid circle found "
                            + here.getPos().manhattanDistance(tc_te.getPos())
                            + " blocks away.");
                }
            }
        }

        if (matching.size() > 0) {
            TeleportCircleTileEntity dest = matching.get(world.rand.nextInt(matching.size()));
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

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isRemote) return;
        LogManager.getLogger().debug("Adding circle to list.");
        circles.add(new Tuple<>(worldIn.dimension.getType(), pos));
    }

    private boolean isInCircleList(World world, BlockPos pos) {
        for (Tuple<DimensionType,BlockPos> t : circles) {
            if (t.getA() == world.dimension.getType() && t.getB().equals(pos)) return true;
        }
        return false;
    }
}
