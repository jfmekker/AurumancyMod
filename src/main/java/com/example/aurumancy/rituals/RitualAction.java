package com.example.aurumancy.rituals;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for a Ritual's effect.
 */
public abstract class RitualAction {

    /**
     * Conduct the effect on the world or player.
     * @param world World to conduct effect in.
     * @param pos Position to center effect on.
     * @param player Player to add effect to.
     */
    public abstract void doAction(World world, BlockPos pos, PlayerEntity player);

}
