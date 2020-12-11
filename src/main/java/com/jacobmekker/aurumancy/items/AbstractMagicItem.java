package com.jacobmekker.aurumancy.items;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.utils.PlayerEntityHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Predicate;

/**
 * Base class for Aurumancy magic items.
 */
public abstract class AbstractMagicItem extends ShootableItem implements IForgeRegistryEntry<Item> {

    /**
     * Construct an AbstractMagicItem. Will need to override onMagicItemUse (no behavior needed).
     * @param properties General Item properties object.
     * @param cost Cost to use the magic item in xp. Can be positive, zero, or negative.
     * @param use How is the magic item used? On a block, charged, or instant?
     */
    public AbstractMagicItem(Properties properties, int cost, ItemUsageType use, int cooldown) {
        super(properties.group(Aurumancy.ITEM_GROUP).maxStackSize(1).maxDamage(cooldown));
        this.xpCost = cost;
        this.usage = use;
        this.cooldownTime = cooldown;
    }

    /**
     * Cool-down time of the magic item in ticks
     */
    protected int cooldownTime;

    /**
     * Cost to use the magic item in xp. Can be positive, zero, or negative.
     */
    protected int xpCost;

    /**
     * How is the magic item used? On a block, charged, or instant?
     */
    protected ItemUsageType usage;

    /**
     * Action to do when the magic item is used. Method is called dependant on the item's ItemUsageType.
     * @param player Player entity using the item.
     * @param hand The hand that is holding the item.
     * @param pos The position of the event (the selected block for a block usage, position of the player otherwise).
     */
    protected abstract void onMagicItemUse(PlayerEntity player, Hand hand, BlockPos pos);

    /**
     * Resolve Player right-clicking with a magic item.
     * @param world World event is in.
     * @param player Player triggering event.
     * @param hand Hand holding item.
     * @return Result of using the magic item.
     */
    @Override
    public final ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        // Get the item stack
        ItemStack stack = player.getHeldItem(hand);

        // Do we have the mana?
        if (PlayerEntityHelper.GetActualExperienceTotal(player) >= xpCost) {
            // Is cooldown done?
            if (!stack.isDamaged()) {
                // Start charging or do effect
                if (this.usage == ItemUsageType.CHARGED) {
                    Aurumancy.LOGGER.trace("Player using item charged action: " + this.toString());
                    // Deduct mana and start cooldown when done
                    player.setActiveHand(hand);
                    return ActionResult.resultSuccess(stack);
                }
                else if (this.usage == ItemUsageType.INSTANT) {
                    Aurumancy.LOGGER.trace("Player used item right-click action: " + this.toString());
                    stack.attemptDamageItem(cooldownTime, player.getRNG(), null);
                    PlayerEntityHelper.AddActualExperienceTotal(player, -xpCost);
                    this.onMagicItemUse(player, hand, player.getPosition());
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
     * Resolve player right-clicking a block with a magic item.
     * @param context Context of item use. Contains all relevant information.
     * @return Result of the magic item use.
     */
    @Override
    public final ActionResultType onItemUse(ItemUseContext context) {
        // Get player and item stack
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getItem();

        // Does this match our usage?
        if (player != null && this.usage == ItemUsageType.BLOCK) {
            // Do we have the mana?
            if (PlayerEntityHelper.GetActualExperienceTotal(player) >= xpCost) {
                // Is cooldown finished?
                if (!stack.isDamaged()) {
                    Aurumancy.LOGGER.trace("Player used wand on block: " + this.toString());
                    stack.attemptDamageItem(cooldownTime, player.getRNG(), null);
                    PlayerEntityHelper.AddActualExperienceTotal(player, -xpCost);
                    this.onMagicItemUse(player, context.getHand(), context.getPos());
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
    public final void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {
        // Cast to player
        if (!(entityLiving instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity)entityLiving;

        // Deduct mana and do the effect
        if (PlayerEntityHelper.GetActualExperienceTotal(player) >= xpCost) {
            if (timeLeft <= 71980) { // TODO do time calculation properly
                stack.attemptDamageItem(cooldownTime, player.getRNG(), null);
                PlayerEntityHelper.AddActualExperienceTotal(player, -xpCost);
                this.onMagicItemUse(player, player.getActiveHand(), player.getPosition());
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
