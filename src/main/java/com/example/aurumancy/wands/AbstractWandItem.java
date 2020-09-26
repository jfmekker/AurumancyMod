package com.example.aurumancy.wands;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

/**
 * Base class for Aurumancy wands.
 */
public abstract class AbstractWandItem extends ShootableItem implements IForgeRegistryEntry<Item> {

    // Easier reference to logger
    protected static final Logger LOGGER = LogManager.getLogger();

    /**
     * Construct an AbstractWand. Although abstract, will not actually need to override or define any methods.
     * @param properties General Item properties object.
     * @param cost Cost to use the wand in xp. Can be positive, zero, or negative.
     * @param use How is the wand used? On a block, charged, or instant?
     */
    public AbstractWandItem(Properties properties, int cost, WandUsageType use) {
        super(properties.maxStackSize(1));
        this.xpCost = cost;
        this.usage = use;
    }

    /**
     * Cost to use the wand in xp. Can be positive, zero, or negative.
     */
    protected int xpCost;

    /**
     * How is the wand used? On a block, charged, or instant?
     */
    protected WandUsageType usage;

    /**
     * Action to do for an instant use (right-click).
     * @param world World event is in.
     * @param player Player triggering event.
     * @param hand Hand holding item.
     */
    protected void instantUsage(World world, PlayerEntity player, Hand hand) { LOGGER.debug("Wand using rightClickUsage."); }

    /**
     * Action to do for a charged use (held right-click).
     * @param stack Item stack of used item.
     * @param world World event is in.
     * @param player Player triggering event.
     */
    protected void chargedUsage(ItemStack stack, World world, PlayerEntity player) { LOGGER.debug("Wand using chargedUsage."); }

    /**
     * Action to do far a block use (right-click on a block).
     * @param context Context object of event. Contains block position, world, player, etc.
     */
    protected void blockUsage(ItemUseContext context) { LOGGER.debug("Wand using blockUsage."); }

    /**
     * Resolve Player right-clicking with a wand. Should not be overridden.
     * @param world World event is in.
     * @param player Player triggering event.
     * @param hand Hand holding item.
     * @return Result of using the Wand item.
     */
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
                this.instantUsage(world, player, hand);
                return ActionResult.resultSuccess(player.getHeldItem(hand));
            }
        }
        else {
            player.sendMessage(new StringTextComponent("Not enough mana to use this item!"));
        }

        return super.onItemRightClick(world, player, hand);
    }

    /**
     * Resolve player right-clicking a block with a wand.
     * @param context Context of item use. Contains all relevant information.
     * @return Result of the Wand item use.
     */
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        // Deduct mana and do the effect if we have enough and this matches our usage

        if (context.getPlayer() != null && this.usage == WandUsageType.BLOCK) {
            if (context.getPlayer().experienceTotal >= xpCost) {
                LOGGER.debug("Player used wand on block: " + this.toString());
                context.getPlayer().giveExperiencePoints(-xpCost);
                this.blockUsage(context);
                return ActionResultType.SUCCESS;
            }
            else {
                context.getPlayer().sendMessage(new StringTextComponent("Not enough mana to use this item!"));
            }
        }

        return super.onItemUse(context);
    }

    /**
     * Resolve Player stopping usage of a wand (un-holding right-click).
     * @param stack Item(s) being used.
     * @param world World event is in.
     * @param entityLiving Entity triggering event. Should be player.
     * @param timeLeft Duration subtracted by held time, in ticks.
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {
        // Cast to player
        if (!(entityLiving instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity)entityLiving;

        // Deduct mana and do the effect
        if (player.experienceTotal >= xpCost) {
            if (timeLeft <= 71980) { // TODO do time calculation properly
                player.giveExperiencePoints(-xpCost);
                this.chargedUsage(stack, world, player);
            }
        }
        else {
            player.sendMessage(new StringTextComponent("Not enough mana to use this item!"));
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
