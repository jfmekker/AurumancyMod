package com.example.aurumancy.rituals;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Ritual action to add a potion effect to a player.
 */
public class RitualActionPotionEffect extends RitualAction {

    /**
     * Potion effect instance to add.
     */
    private EffectInstance effect;

    /**
     * Construct a new Ritual potion effect.
     * @param effectInstance Potion effect to add.
     */
    public RitualActionPotionEffect(EffectInstance effectInstance) {
        effect = effectInstance;
    }

    /**
     * Add a potion effect to the player.
     * @param world World to conduct effect in.
     * @param pos Position to center effect on.
     * @param player Player to add effect to.
     */
    @Override
    public void doAction(World world, BlockPos pos, PlayerEntity player) {
        player.addPotionEffect(effect);
    }
}
