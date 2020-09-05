package com.example.aurumancy.rituals;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualActionPotionEffect extends RitualAction {

    private EffectInstance effect;

    public RitualActionPotionEffect(EffectInstance effectInstance) {
        effect = effectInstance;
    }

    @Override
    public void doAction(World world, BlockPos pos, PlayerEntity player) {
        player.addPotionEffect(effect);
    }
}
