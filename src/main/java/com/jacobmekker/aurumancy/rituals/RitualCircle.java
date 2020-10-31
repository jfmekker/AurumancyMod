package com.jacobmekker.aurumancy.rituals;

import com.jacobmekker.aurumancy.Aurumancy;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RitualCircle {

    public static @Nullable CirclePower GetCirclePower(World world, BlockPos center) {
        CirclePower circle = new CirclePower(1,0,0);

        int size = 3;
        for (int i = -size; i < size ; i++) {
            for (int j = -size; j < size; j++) {
                Block block = world.getBlockState(center.add(i,0,j)).getBlock();

                // Must be stone bricks except:
                //   the center block
                //   pillars on coal block
                if (block == Blocks.STONE_BRICKS || (i == 0 && j == 0)) {
                    // Do nothing
                }
                else if (block == Blocks.COAL_BLOCK) {
                    // Check pillar
                    CirclePower pillar = GetPillarPower(world, center.add(i,0,j));
                    if (pillar == null) return null;
                    circle.add(pillar);
                }
                else {
                    return null;
                }
            }
        }

        return circle;
    }

    private static @Nullable CirclePower GetPillarPower(World world, BlockPos bottom) {
        int height = 2;
        CirclePower pillar = new CirclePower();

        // Check pillar material
        int i = 1;
        Block material = null;
        while (i <= height) {
            Block block = world.getBlockState(bottom.add(0,i,0)).getBlock();
            material = material == null ? block : material;
            if (material != block) return null;

            if (block instanceof FenceBlock) {
                pillar.add(0,0,0);
            }
            else if (block == Blocks.IRON_BLOCK) {
                pillar.add(2,0,0);
            }
            else if (block == Blocks.OBSIDIAN) {
                pillar.add(5,0,0);
            }

            i += 1;
        }

        // Check pillar top
        // TODO

        return pillar;
    }

}
