package com.example.aurumancy.rituals;


import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Ritual implements Comparable {

    private Block[][] blocks;
    private  int size;

    private int xpCost;

    private RitualAction effect;

    public static @Nullable Ritual BuildValidRitual(Block[][] blockSet, int cost, @Nullable RitualAction action) {
        if (blockSet == null
                || blockSet.length != blockSet[0].length
                || blockSet.length % 2 != 1)
            return null;
        return new Ritual(blockSet, cost, action);
    }

    private Ritual(Block[][] blockSet, int cost, @Nullable RitualAction action) {
        blocks = blockSet;
        size = blockSet.length;
        xpCost = cost;
        effect = action;
    }
    
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

    public Block getCenter() {
        return blocks[size/2 + 1][size/2 + 1];
    }

    @Override
    public int compareTo(Object o) {
        Ritual other = (Ritual)o;

        if (this == other) {
            return 0;
        }

        if (size == other.size) {
            return (getCenter().getRegistryName().compareTo(other.getCenter().getRegistryName()));
        }
        else {
            return size - other.size;
        }
    }
}
