package com.example.aurumancy.wands;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractWandItem extends Item implements IForgeRegistryEntry<Item> {

    // Directly reference a log4j logger.
    protected static final Logger LOGGER = LogManager.getLogger();

    public AbstractWandItem(Properties properties, int cost) {
        super(properties);
        this.xpCost = cost;
    }

    protected int xpCost;

    protected abstract void rightClickUsage(World world, PlayerEntity player, Hand hand);

    protected abstract void blockUsage(ItemUseContext context);

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        // Deduct mana and do the effect
        if (player.experienceTotal >= xpCost) {
            player.giveExperiencePoints(-xpCost);
            this.rightClickUsage(world, player, hand);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        // Deduct mana and do the effect
        if (context.getPlayer() != null && context.getPlayer().experienceTotal >= xpCost) {
            context.getPlayer().giveExperiencePoints(-xpCost);
            this.blockUsage(context);
        }
        return super.onItemUse(context);
    }

}
