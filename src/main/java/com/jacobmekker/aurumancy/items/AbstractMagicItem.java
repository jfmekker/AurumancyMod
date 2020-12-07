package com.jacobmekker.aurumancy.items;

import com.jacobmekker.aurumancy.Aurumancy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Predicate;

/**
 * Base class for Aurumancy wands.
 */
public abstract class AbstractMagicItem extends ShootableItem implements IForgeRegistryEntry<Item> {

    /**
     * Construct an AbstractWand. Although abstract, will not actually need to override or define any methods.
     * @param properties General Item properties object.
     * @param cost Cost to use the wand in xp. Can be positive, zero, or negative.
     * @param use How is the wand used? On a block, charged, or instant?
     */
    public AbstractMagicItem(Properties properties, int cost, ItemUsageType use, int cooldown) {
        super(properties.group(Aurumancy.ITEM_GROUP).maxStackSize(1).maxDamage(cooldown));
        this.xpCost = cost;
        this.usage = use;
        this.cooldownTime = cooldown;
    }

    /**
     * Cool-down time of the wand in ticks
     */
    protected int cooldownTime;

    /**
     * Cost to use the wand in xp. Can be positive, zero, or negative.
     */
    protected int xpCost;

    /**
     * How is the wand used? On a block, charged, or instant?
     */
    protected ItemUsageType usage;

    /**
     * Action to do for an instant use (right-click).
     * @param world World event is in.
     * @param player Player triggering event.
     * @param hand Hand holding item.
     */
    protected void instantUsage(World world, PlayerEntity player, Hand hand) { Aurumancy.LOGGER.trace("Wand using rightClickUsage."); }

    /**
     * Action to do for a charged use (held right-click).
     * @param stack Item stack of used item.
     * @param world World event is in.
     * @param player Player triggering event.
     */
    protected void chargedUsage(ItemStack stack, World world, PlayerEntity player) { Aurumancy.LOGGER.trace("Wand using chargedUsage."); }

    /**
     * Action to do far a block use (right-click on a block).
     * @param context Context object of event. Contains block position, world, player, etc.
     */
    protected void blockUsage(ItemUseContext context) { Aurumancy.LOGGER.trace("Wand using blockUsage."); }

    /**
     * Resolve Player right-clicking with a wand. Should not be overridden.
     * @param world World event is in.
     * @param player Player triggering event.
     * @param hand Hand holding item.
     * @return Result of using the Wand item.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        // Get the item stack
        ItemStack stack = player.getHeldItem(hand);

        // Do we have the mana?
        if (player.experienceTotal >= xpCost) {
            // Is cooldown done?
            if (!stack.isDamaged()) {
                // Start charging or do effect
                if (this.usage == ItemUsageType.CHARGED) {
                    Aurumancy.LOGGER.trace("Player using wand charged action: " + this.toString());
                    // Deduct mana and start cooldown when done
                    player.setActiveHand(hand);
                    return ActionResult.resultSuccess(stack);
                }
                else if (this.usage == ItemUsageType.INSTANT) {
                    Aurumancy.LOGGER.trace("Player used wand right-click action: " + this.toString());
                    stack.attemptDamageItem(cooldownTime, player.getRNG(), null);
                    player.giveExperiencePoints(-xpCost);
                    this.instantUsage(world, player, hand);
                    return ActionResult.resultSuccess(stack);
                }
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
        // Get player and item stack
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getItem();

        // Does this match our usage?
        if (player != null && this.usage == ItemUsageType.BLOCK) {
            // Do we have the mana?
            if (player.experienceTotal >= xpCost) {
                // Is cooldown finished?
                if (!stack.isDamaged()) {
                    Aurumancy.LOGGER.trace("Player used wand on block: " + this.toString());
                    stack.attemptDamageItem(cooldownTime, player.getRNG(), null);
                    player.giveExperiencePoints(-xpCost);
                    this.blockUsage(context);
                    return ActionResultType.SUCCESS;
                }
            }
            else {
                player.sendMessage(new StringTextComponent("Not enough mana to use this item!"));
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
                stack.attemptDamageItem(cooldownTime, player.getRNG(), null);
                player.giveExperiencePoints(-xpCost);
                this.chargedUsage(stack, world, player);
            }
        }
        else {
            player.sendMessage(new StringTextComponent("Not enough mana to use this item!"));
        }
        Aurumancy.LOGGER.trace("Player stopped using " + this.toString() + " with " + timeLeft + " time left.");
    }

    @Override
    public UseAction getUseAction(ItemStack stack) { return UseAction.BOW; }

    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate() { return null; }

    @Override
    public int getUseDuration(ItemStack stack) { return 72000; /* ticks (20Hz) */ }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (stack.isDamaged()) stack.setDamage(stack.getDamage() - 1);
    }
}
