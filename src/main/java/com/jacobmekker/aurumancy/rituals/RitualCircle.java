package com.jacobmekker.aurumancy.rituals;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RitualCircle {

    private static final int OVERWORLD_CIRCLE_SIZE = 3;
    private static final int NETHER_CIRCLE_SIZE = 6;
    private static final int END_CIRCLE_SIZE = 9;

    private static final int OVERWORLD_CIRCLE_MAX_PILLARS = 4;
    private static final int NETHER_CIRCLE_MAX_PILLARS = 8;
    private static final int END_CIRCLE_MAX_PILLARS = 16;

    private static final int OVERWORLD_CIRCLE_PILLAR_HEIGHT = 2;
    private static final int NETHER_CIRCLE_PILLAR_HEIGHT = 4;
    private static final int END_CIRCLE_PILLAR_HEIGHT = 6;

    public static @Nullable CirclePower GetCirclePower(World world, BlockPos center) {
        CirclePower power = new CirclePower();
        power.add(GetCircleOverworldPower(world, center.add(0,-1,0)));
        power.add(GetCircleNetherPower(world, center.add(0,-2,0)));
        power.add(GetCircleEndPower(world, center.add(0,-3,0)));
        return (power.total() == 0) ? null : power;
    }

    private static @Nullable CirclePower GetPillarOverworldPower(World world, BlockPos bottom) {
        CirclePower pillar = new CirclePower();

        // Check pillar material
        int i = 1;
        Block material = null;
        while (i <= OVERWORLD_CIRCLE_PILLAR_HEIGHT) {
            Block block = world.getBlockState(bottom.add(0,i,0)).getBlock();
            material = material == null ? block : material;
            if (material != block) return null;

            i += 1;
        }
        if (material instanceof FenceBlock)
            pillar.add(0,0,0);
        else if (material == Blocks.IRON_BLOCK)
            pillar.add(2,0,0);
        else if (material == Blocks.OBSIDIAN)
            pillar.add(5,0,0);
        else
            return null;

        // Check pillar top
        Block block = world.getBlockState(bottom.add(0,i,0)).getBlock();
        if (block == Blocks.AIR)
            pillar.add(0,0,0);
        else if (block == Blocks.CAMPFIRE)
            pillar.add(1,0,0);
        else if (block == Blocks.LAPIS_BLOCK)
            pillar.add(2,0,0);
        else if (block == Blocks.EMERALD_BLOCK)
            pillar.add(5,0,0);
        else if (block == Blocks.ENCHANTING_TABLE)
            pillar.add(10,0,0);
        else
            return null;

        return pillar;
    }

    private static @Nullable CirclePower GetCircleOverworldPower(World world, BlockPos center) {
        CirclePower circle = new CirclePower(1,0,0);

        int num_pillars = 0;
        for (int i = -OVERWORLD_CIRCLE_SIZE; i <= OVERWORLD_CIRCLE_SIZE ; i++) {
            for (int j = -OVERWORLD_CIRCLE_SIZE; j <= OVERWORLD_CIRCLE_SIZE; j++) {
                Block block = world.getBlockState(center.add(i,0,j)).getBlock();

                // Must be stone bricks except pillars on coal block
                if (block != Blocks.STONE_BRICKS) {
                    if (block == Blocks.COAL_BLOCK) {
                        // Check pillar
                        if (num_pillars < OVERWORLD_CIRCLE_MAX_PILLARS) {
                            CirclePower pillar = GetPillarOverworldPower(world, center.add(i,0,j));
                            if (pillar == null) return null;
                            circle.add(pillar);
                            num_pillars += 1;
                        }
                        else return null;
                    }
                    else {
                        return null;
                    }
                }
            }
        }

        return circle;
    }

    private static @Nullable CirclePower GetPillarNetherPower(World world, BlockPos bottom) {
        CirclePower pillar = new CirclePower();

        // Check pillar material
        int i = 1;
        Block material = null;
        while (i <= NETHER_CIRCLE_PILLAR_HEIGHT) {
            Block block = world.getBlockState(bottom.add(0,i,0)).getBlock();
            material = material == null ? block : material;
            if (material != block) return null;

            i += 1;
        }
        if (material == Blocks.NETHERRACK)
            pillar.add(0,0,0);
        else if (material == Blocks.QUARTZ_BLOCK) // TODO: final?
            pillar.add(0,2,0);
        else if (material == Blocks.PRISMARINE_BRICKS)
            pillar.add(0,5,0);
        else
            return null;

        // Check pillar top
        Block block = world.getBlockState(bottom.add(0,i,0)).getBlock();
        if (block == Blocks.AIR)
            pillar.add(0,0,0);
        else if (block == Blocks.FIRE)
            pillar.add(0,1,0);
        else if (block == Blocks.GLOWSTONE)
            pillar.add(0,2,0);
        else if (block == Blocks.BREWING_STAND)
            pillar.add(0,5,0);
        else if (block == Blocks.BEACON)
            pillar.add(0,10,0);
        else
            return null;

        return pillar;
    }

    private static @Nullable CirclePower GetCircleNetherPower(World world, BlockPos center) {
        CirclePower circle = new CirclePower(0,1,0);

        int num_pillars = 0;
        for (int i = -NETHER_CIRCLE_SIZE; i <= NETHER_CIRCLE_SIZE ; i++) {
            for (int j = -NETHER_CIRCLE_SIZE; j <= NETHER_CIRCLE_SIZE; j++) {
                Block block = world.getBlockState(center.add(i,0,j)).getBlock();

                // Must be nether bricks except pillars on soul sand
                if (block != Blocks.NETHER_BRICKS) {
                    if (block == Blocks.SOUL_SAND) {
                        // Check pillar
                        if (num_pillars < NETHER_CIRCLE_MAX_PILLARS) {
                            CirclePower pillar = GetPillarNetherPower(world, center.add(i,0,j));
                            if (pillar == null) return null;
                            circle.add(pillar);
                            num_pillars += 1;
                        }
                        else return null;
                    }
                    else {
                        return null;
                    }
                }
            }
        }

        return circle;
    }

    private static @Nullable CirclePower GetPillarEndPower(World world, BlockPos bottom) {
        CirclePower pillar = new CirclePower();

        // Check pillar material
        int i = 1;
        Block material = null;
        while (i <= END_CIRCLE_PILLAR_HEIGHT) {
            Block block = world.getBlockState(bottom.add(0,i,0)).getBlock();
            material = material == null ? block : material;
            if (material != block) return null;

            i += 1;
        }
        if (material == Blocks.END_STONE)
            pillar.add(0,0,0);
        else if (material == Blocks.PURPUR_PILLAR)
            pillar.add(0,0,2);
        else if (material instanceof ShulkerBoxBlock)
            pillar.add(0,0,5);
        else
            return null;

        // Check pillar top
        Block block = world.getBlockState(bottom.add(0,i,0)).getBlock();
        if (block == Blocks.AIR)
            pillar.add(0,0,0);
        else if (block == Blocks.DIAMOND_BLOCK) // TODO ender block?
            pillar.add(0,0,1);
        else if (block == Blocks.END_ROD)
            pillar.add(0,0,2);
        else if (block instanceof ShulkerBoxBlock)
            pillar.add(0,0,5);
        else if (block == Blocks.DRAGON_HEAD)
            pillar.add(0,0,10);
        else
            return null;

        return pillar;
    }

    private static @Nullable CirclePower GetCircleEndPower(World world, BlockPos center) {
        CirclePower circle = new CirclePower(0,0,1);

        int num_pillars = 0;
        for (int i = -END_CIRCLE_SIZE; i <= END_CIRCLE_SIZE ; i++) {
            for (int j = -END_CIRCLE_SIZE; j <= END_CIRCLE_SIZE; j++) {
                Block block = world.getBlockState(center.add(i,0,j)).getBlock();

                // Must be end stone bricks except pillars on obsidian
                if (block != Blocks.END_STONE_BRICKS) {
                    if (block == Blocks.OBSIDIAN) {
                        // Check pillar
                        if (num_pillars < END_CIRCLE_MAX_PILLARS) {
                            CirclePower pillar = GetPillarEndPower(world, center.add(i,0,j));
                            if (pillar == null) return null;
                            circle.add(pillar);
                            num_pillars += 1;
                        }
                        else return null;
                    }
                    else {
                        return null;
                    }
                }
            }
        }

        return circle;
    }
}
