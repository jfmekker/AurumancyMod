package com.jacobmekker.aurumancy.rituals;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Class for Aurumancy Rituals.
 */
public class Ritual implements Comparable<Ritual> {

    /**
     * List of components involved in the ritual.
     */
    private List<Component> components;

    /**
     * Cost to conduct the ritual.
     */
    private int xpCost;

    /**
     * The actual effect the ritual does.
     */
    private RitualAction effect;

    /**
     * Factory method for constructing new rituals with validation.
     * @param cost Experience point / mana cost to do the ritual.
     * @param action Effect to enact when ritual completes.
     * @param compList List of ritual components required.
     * @return A newly constructed ritual, or null if any arguments are invalid.
     */
    public static @Nullable Ritual BuildValidRitual(int cost,
                                                    @Nullable RitualAction action,
                                                    List<Component> compList) {
        // TODO do checks

        return new Ritual(cost, action, compList);
    }

    /**
     * Private constructor for Rituals.
     * @param cost Experience point / mana cost to do the ritual.
     * @param action Effect to enact when ritual completes.
     * @param compList List of ritual components required.
     */
    private Ritual(int cost, @Nullable RitualAction action, List<Component> compList) {
        xpCost = cost;
        effect = action;
        components = compList;
    }

    /**
     * Given a world and block position, find if all ritual blocks are in place.
     * @param world World to do the check in.
     * @param center Position of center block.
     * @return True if ths ritual is a match.
     */
    public boolean validateRitualComponents(World world, BlockPos center) {
        for (Component component : components) {
            if (!component.checkComponent(world, center)) return false;
        }
        return true;
    }

    /**
     * Apply this rituals effect to the world or player.
     * @param world World do conduct effect in.
     * @param center Center block position of ritual.
     * @param player Player conducting the ritual.
     * @return True if effect has been successfully applied.
     */
    public boolean doRitual(World world, BlockPos center, PlayerEntity player) {
        if (player.experienceTotal > xpCost && validateRitualComponents(world, center)) {
            player.giveExperiencePoints(-xpCost);

            for (Component component : components) {
                component.tryConsume(world, center);
            }

            if (effect != null) effect.doAction(world, center, player);

            return true;
        }
        return false;
    }

    /**
     * Get the center block of the ritual.
     * @return The center block.
     */
    public Block getCenter() {
        for (Component component : components) {
            if (component.getOffset().equals(new Vec3i(0,0,0))) return component.getBlock();
        }
        return null;
    }

    public int getSize() {
        return components.size();
    }

    /**
     * Compare to another ritual for sorting.
     * @param other Other ritual to compare against.
     * @return Difference in ritual size.
     */
    @Override
    public int compareTo(Ritual other) {
        return components.size() - other.components.size();
    }
}
