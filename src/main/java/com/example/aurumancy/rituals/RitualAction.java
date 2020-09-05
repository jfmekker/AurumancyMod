package com.example.aurumancy.rituals;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RitualAction {

    public abstract void doAction(World world, BlockPos pos, PlayerEntity player);

}
