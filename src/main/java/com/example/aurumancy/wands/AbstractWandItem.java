package com.example.aurumancy.wands;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.IForgeRegistryEntry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

public abstract class AbstractWandItem extends ShootableItem implements IForgeRegistryEntry<Item> {

    // Directly reference a log4j logger.
    protected static final Logger LOGGER = LogManager.getLogger();

    public AbstractWandItem(Properties properties, int cost, WandUsageType use) {
        super(properties);
        this.xpCost = cost;
        this.usage = use;
    }

    protected int xpCost = 0;

    protected WandUsageType usage = WandUsageType.INSTANT;

    protected void rightClickUsage(World world, PlayerEntity player, Hand hand) {
        LOGGER.debug("Wand using rightClickUsage.");
    }

    protected void chargedUsage(ItemStack stack, World world, PlayerEntity player) {
        LOGGER.debug("Wand using chargedUsage.");
    }

    protected void blockUsage(ItemUseContext context) {
        LOGGER.debug("Wand using blockUsage.");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        // If we have the mana
        if (player.experienceTotal >= xpCost) {
            // Start charging or do effect
            if (this.usage == WandUsageType.CHARGED) {
                LOGGER.debug("Player used wand charged action: " + this.toString());
                player.setActiveHand(hand);
                return ActionResult.resultSuccess(player.getHeldItem(hand));
            }
            else if (this.usage == WandUsageType.INSTANT) {
                LOGGER.debug("Player used wand right-click action: " + this.toString());
                player.giveExperiencePoints(-xpCost);
                this.rightClickUsage(world, player, hand);
                return ActionResult.resultSuccess(player.getHeldItem(hand));
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        // Deduct mana and do the effect if we have enough and this matches our usage
        if (context.getPlayer() != null
                && context.getPlayer().experienceTotal >= xpCost
                && this.usage == WandUsageType.BLOCK) {
            LOGGER.debug("Player used wand on block: " + this.toString());
            context.getPlayer().giveExperiencePoints(-xpCost);
            this.blockUsage(context);
            return ActionResultType.SUCCESS;
        }

        return super.onItemUse(context);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {
        // Cast to player
        if (!(entityLiving instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity)entityLiving;

        // Deduct mana and do the effect
        if (player.experienceTotal >= xpCost && timeLeft <= 71980) { // TODO do time calculation properly
            player.giveExperiencePoints(-xpCost);
            this.chargedUsage(stack, world, player);
        }
        LOGGER.debug("Player stopped using " + this.toString() + " with " + timeLeft + " time left.");
    }

    @Override
    public UseAction getUseAction(ItemStack stack) { return UseAction.BOW; }

    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate() { return null; }

    @Override
    public int getUseDuration(ItemStack stack) { return 72000; /* ticks (20Hz) */ }
}
