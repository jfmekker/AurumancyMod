package com.jacobmekker.aurumancy.rituals;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Class for Aurumancy Rituals.
 */
public class Ritual {

    /**
     * List of components involved in the ritual.
     */
    private List<Item> components;

    /**
     * Mana cost to conduct the ritual.
     */
    private int xpCost;

    /**
     * The actual effect the ritual does.
     */
    private RitualAction effect;

    /**
     * Minimum circle power required for ritual.
     */
    private CirclePower requiredPower;

    /**
     * Private constructor for Rituals.
     * @param cost Experience point / mana cost to do the ritual.
     * @param action Effect to enact when ritual completes.
     * @param compList List of ritual components required.
     */
    public Ritual(int cost, @Nullable RitualAction action, List<Item> compList, CirclePower power) {
        xpCost = cost;
        effect = action;
        components = compList;
        requiredPower = power;
    }

    /**
     * Check if a list of ItemStacks exactly matches the component list.
     * @param stacks Item stacks to compare to the component list.
     * @return True if this ritual is a match. False if there are missing
     * or extra components/items.
     */
    public boolean validateRitualComponents(List<ItemStack> stacks) {
        List<Item> components_copy = new ArrayList<>(components);
        List<Item> stacks_copy = new ArrayList<>();
        for (ItemStack s : stacks) {
            for (int i = 1; i <= s.getCount(); i += 1) {
                stacks_copy.add(s.getItem());
            }
        }

        int i = 0;
        while (i < stacks_copy.size()) {
            int j = 0;
            while (j < components_copy.size()) {
                if (components_copy.get(j) == stacks_copy.get(i)) {
                    components_copy.remove(j);
                    stacks_copy.remove(i);
                    i -= 1;
                    break;
                }
                j += 1;
            }
            i += 1;
        }
        return stacks_copy.size() == 0 && components_copy.size() == 0;
    }

    /**
     * Apply this rituals effect to the world or player.
     * @param world World do conduct effect in.
     * @param center Center block position of ritual.
     * @param player Player conducting the ritual.
     * @return True if effect has been successfully applied.
     */
    public boolean doRitual(World world, BlockPos center, PlayerEntity player, CirclePower power) {
        List<ItemEntity> entities = world.getEntitiesWithinAABB(
                EntityType.ITEM,
                new AxisAlignedBB(
                        center.add(-2,-2,-2),
                        center.add(2,2,2)),
                Objects::nonNull);

        List<ItemStack> stacks = new ArrayList<>();
        for (ItemEntity e : entities) stacks.add(e.getItem());

        // Check that:
        //   we have mana
        //   we have circle power
        //   we have all (and only all) components
        if (player.experienceTotal > xpCost
                && power.meetsOrExceeds(requiredPower)
                && validateRitualComponents(stacks)) {
            player.giveExperiencePoints(-xpCost);

            for (ItemEntity e : entities) e.remove();

            if (effect != null) effect.doAction(world, center, player);

            return true;
        }
        return false;
    }
}
