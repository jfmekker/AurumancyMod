package com.jacobmekker.aurumancy.blocks;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.rituals.CirclePower;
import com.jacobmekker.aurumancy.rituals.Ritual;
import com.jacobmekker.aurumancy.rituals.RitualCircle;
import com.jacobmekker.aurumancy.rituals.Rituals;
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
        if (world.isRemote) return ActionResultType.PASS;

        CirclePower power = RitualCircle.GetCirclePower(world, pos);

        player.sendMessage(new StringTextComponent("Ritual circle power: " + power.toString()));

        for (Ritual r : Rituals.RITUAL_LIST) {
            if (r != null && r.doRitual(world, pos, player, power)) {
                Aurumancy.LOGGER.debug("Completed ritual: " + r.toString());
                return ActionResultType.SUCCESS;
            }
        }
        Aurumancy.LOGGER.debug("Failed to match any rituals.");

        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }
}
