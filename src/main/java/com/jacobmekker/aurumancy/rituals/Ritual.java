package com.jacobmekker.aurumancy.rituals;

import com.jacobmekker.aurumancy.Aurumancy;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Class for Aurumancy Rituals.
 */
public class Ritual implements Comparable<Ritual> {

    /**
     * 2D array of Blocks that determines the ritual setup. Must be square and size must be odd.
     */
    private Block[][] blocks;

    /**
     * Size of the 'blocks' array.
     */
    public int size;

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
     * @param blockSet Blocks that must be in place for the ritual. Must be square and size must be odd.
     * @param cost Experience point / mana cost to do the ritual.
     * @param action Effect to enact when ritual completes.
     * @return A newly constructed ritual, or null if any arguments are invalid.
     */
    public static @Nullable Ritual BuildValidRitual(Block[][] blockSet, int cost, @Nullable RitualAction action) {
        if (blockSet == null
                || blockSet.length != blockSet[0].length
                || blockSet.length % 2 != 1
                || blockSet[blockSet.length / 2][blockSet.length / 2] == null) {
            Aurumancy.LOGGER.debug("BuildValidRitual failed.");
            return null;
        }

        return new Ritual(blockSet, cost, action);
    }

    /**
     * Private constructor for Rituals.
     * @param blockSet Blocks that must be in place for the ritual. Must be square and size must be odd.
     * @param cost Experience point / mana cost to do the ritual.
     * @param action Effect to enact when ritual completes.
     */
    private Ritual(Block[][] blockSet, int cost, @Nullable RitualAction action) {
        blocks = blockSet;
        size = blockSet.length;
        xpCost = cost;
        effect = action;
    }

    /**
     * Given a world and block position, find if all ritual blocks are in place.
     * @param world World to do the check in.
     * @param center Position of center block.
     * @return True if ths ritual is a match.
     */
    public boolean validateRitualComponents(World world, BlockPos center) {
        for (int i = 0; i < size; i += 1) {
            int xCoordOffset = i - (size / 2);
            for (int j = 0; j < size; j += 1) {
                int zCoordOffset = j - (size / 2);
                Block b = world.getBlockState(center.add(xCoordOffset, 0, zCoordOffset)).getBlock();
                if (b != blocks[i][j] && blocks[i][j] != null)
                    return false;
            }
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

            for (int i = 0; i < size; i += 1) {
                int xCoordOffset = i - (size / 2);
                for (int j = 0; j < size; j += 1) {
                    int zCoordOffset = j - (size / 2);
                    world.destroyBlock(center.add(xCoordOffset,0,zCoordOffset), false);
                }
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
        return blocks[size/2 + 1][size/2 + 1];
    }

    /**
     * Compare to another ritual for sorting.
     * @param other Other ritual to compare against.
     * @return Difference in ritual size.
     */
    @Override
    public int compareTo(Ritual other) {
        return size - other.size;
    }
}
