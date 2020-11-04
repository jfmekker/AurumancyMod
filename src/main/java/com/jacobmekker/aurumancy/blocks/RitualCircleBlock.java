package com.jacobmekker.aurumancy.blocks;

import com.jacobmekker.aurumancy.rituals.CirclePower;
import com.jacobmekker.aurumancy.rituals.RitualCircle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class RitualCircleBlock extends Block {

    public RitualCircleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote) return ActionResultType.PASS;

        CirclePower power = RitualCircle.GetCirclePower(world, pos);
        if (power == null || (power.total() == 0)) return ActionResultType.PASS;

        player.sendMessage(new StringTextComponent("Ritual circle power: " + power.toString()));

        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }
}
