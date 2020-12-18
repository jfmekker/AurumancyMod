package com.jacobmekker.aurumancy.utils;

import com.jacobmekker.aurumancy.Aurumancy;

import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;

import static net.minecraft.block.Blocks.*;

public class ColorChecker {

    public static DyeColor GetDyeColor(Block block) {
        if (block == WHITE_GLAZED_TERRACOTTA) return DyeColor.WHITE;
        if (block == ORANGE_GLAZED_TERRACOTTA) return DyeColor.ORANGE;
        if (block == MAGENTA_GLAZED_TERRACOTTA) return DyeColor.MAGENTA;
        if (block == LIGHT_BLUE_GLAZED_TERRACOTTA) return DyeColor.LIGHT_BLUE;
        if (block == YELLOW_GLAZED_TERRACOTTA) return DyeColor.YELLOW;
        if (block == LIME_GLAZED_TERRACOTTA) return DyeColor.LIME;
        if (block == PINK_GLAZED_TERRACOTTA) return DyeColor.PINK;
        if (block == GRAY_GLAZED_TERRACOTTA) return DyeColor.GRAY;
        if (block == LIGHT_GRAY_GLAZED_TERRACOTTA) return DyeColor.LIGHT_GRAY;
        if (block == CYAN_GLAZED_TERRACOTTA) return DyeColor.CYAN;
        if (block == PURPLE_GLAZED_TERRACOTTA) return DyeColor.PURPLE;
        if (block == BLUE_GLAZED_TERRACOTTA) return DyeColor.BLUE;
        if (block == BROWN_GLAZED_TERRACOTTA) return DyeColor.BROWN;
        if (block == GREEN_GLAZED_TERRACOTTA) return DyeColor.GREEN;
        if (block == RED_GLAZED_TERRACOTTA) return DyeColor.RED;
        if (block == BLACK_GLAZED_TERRACOTTA) return DyeColor.BLACK;
        else {
            Aurumancy.LOGGER.trace("Unsupported block " + block.toString() + " given to ColorChecker. Returning null by default.");
            return null;
        }
    }

}
