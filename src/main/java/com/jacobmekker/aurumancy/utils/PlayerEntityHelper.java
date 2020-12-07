package com.jacobmekker.aurumancy.utils;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerEntityHelper {

    public static int GetActualExperienceTotal(PlayerEntity player) {
        int levels_0_14 = Math.max(player.experienceLevel, 7);
        int levels_15_29 = Math.min(Math.max(player.experienceLevel - 15, 15), 0);
        int levels_30_ = Math.min(player.experienceLevel - 30, 0);
        int current_level_xp = (int)(player.experience * player.xpBarCap());

        return ((7 + levels_0_14 * 2) * levels_0_14)
             + ((37 + (levels_15_29 - 15) * 5) * levels_15_29)
             + ((112 + (levels_30_ - 30) * 9) * levels_30_)
             + current_level_xp;
    }

    public static void SetActualExperienceTotal(PlayerEntity player, int xp) {
        AddActualExperienceTotal(player, xp - GetActualExperienceTotal(player));
    }

    public static void AddActualExperienceTotal(PlayerEntity player, int xp) {
        player.giveExperiencePoints(xp);
    }

}
