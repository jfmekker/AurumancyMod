package com.jacobmekker.aurumancy.rituals;

import com.jacobmekker.aurumancy.Aurumancy;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RitualCircle {

    public static @Nullable CirclePower GetCirclePower(World world, BlockPos center) {
        CirclePower power = new CirclePower();
        power.add(GetCircleOverworldPower(world, center));
        power.add(GetCircleNetherPower(world, center.add(0,-1,0)));
        power.add(GetCircleEndPower(world, center.add(0,-2,0)));
        return (power.op == 0 && power.np == 0 && power.ep == 0) ? null : power;
    }

    private static @Nullable CirclePower GetPillarOverworldPower(World world, BlockPos bottom) {
        int height = 2;
        CirclePower pillar = new CirclePower();

        // Check pillar material
        int i = 1;
        Block material = null;
        while (i <= height) {
            Block block = world.getBlockState(bottom.add(0,i,0)).getBlock();
            material = material == null ? block : material;
            if (material != block) return null;

            if (block instanceof FenceBlock)
                pillar.add(0,0,0);
            else if (block == Blocks.IRON_BLOCK)
                pillar.add(2,0,0);
            else if (block == Blocks.OBSIDIAN)
                pillar.add(5,0,0);
            else
                return null;

            i += 1;
        }

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

        int size = 3, num_pillars = 0;
        for (int i = -size; i <= size ; i++) {
            for (int j = -size; j <= size; j++) {
                Block block = world.getBlockState(center.add(i,0,j)).getBlock();

                // Must be stone bricks except:
                //   the center block
                //   pillars on coal block
                if (block == Blocks.STONE_BRICKS || (i == 0 && j == 0)) {
                    continue;
                }
                if (block == Blocks.COAL_BLOCK) {
                    // Check pillar
                    if (num_pillars < 4) {
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

        return circle;
    }

    private static @Nullable CirclePower GetPillarNetherPower(World world, BlockPos bottom) {
        int height = 4;
        CirclePower pillar = new CirclePower();

        // Check pillar material
        int i = 1;
        Block material = null;
        while (i <= height) {
            Block block = world.getBlockState(bottom.add(0,i,0)).getBlock();
            material = material == null ? block : material;
            if (material != block) return null;

            if (block == Blocks.NETHERRACK)
                pillar.add(0,1,0);
            else if (block == Blocks.QUARTZ_BLOCK) // TODO: final?
                pillar.add(0,2,0);
            else if (block == Blocks.PRISMARINE_BRICKS)
                pillar.add(0,5,0);
            else
                return null;

            i += 1;
        }

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

        int size = 6, num_pillars = 0;
        for (int i = -size; i <= size ; i++) {
            for (int j = -size; j <= size; j++) {
                Block block = world.getBlockState(center.add(i,0,j)).getBlock();

                // Must be nether bricks except:
                //   the center block
                //   pillars on soul sand
                if (block == Blocks.NETHER_BRICKS || (i == 0 && j == 0)) {
                    continue;
                }
                if (block == Blocks.SOUL_SAND) {
                    // Check pillar
                    if (num_pillars < 8) {
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

        return circle;
    }

    private static @Nullable CirclePower GetPillarEndPower(World world, BlockPos bottom) {
        int height = 6;
        CirclePower pillar = new CirclePower();

        // Check pillar material
        int i = 1;
        Block material = null;
        while (i <= height) {
            Block block = world.getBlockState(bottom.add(0,i,0)).getBlock();
            material = material == null ? block : material;
            if (material != block) return null;

            if (block == Blocks.END_STONE)
                pillar.add(0,0,1);
            else if (block == Blocks.PURPUR_PILLAR)
                pillar.add(0,0,2);
            else if (block instanceof ShulkerBoxBlock)
                pillar.add(0,0,5);
            else
                return null;

            i += 1;
        }

        // Check pillar top
        Block block = world.getBlockState(bottom.add(0,i,0)).getBlock();
        if (block == Blocks.AIR)
            pillar.add(0,0,0);
        else if (block == Blocks.FIRE)
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

        int size = 9, num_pillars = 0;
        for (int i = -size; i <= size ; i++) {
            for (int j = -size; j <= size; j++) {
                Block block = world.getBlockState(center.add(i,0,j)).getBlock();

                // Must be end stone bricks except:
                //   the center block
                //   pillars on obsidian
                if (block == Blocks.END_STONE_BRICKS || (i == 0 && j == 0)) {
                    continue;
                }
                if (block == Blocks.OBSIDIAN) {
                    // Check pillar
                    if (num_pillars < 16) {
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

        return circle;
    }
}
