package com.jacobmekker.aurumancy.utils;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerEntityHelper {

    public static int GetActualExperienceTotal(PlayerEntity player) {
        int xp = (int)(player.experience * player.xpBarCap());

        for (int level = 0; level < player.experienceLevel; level += 1) {
            if (level >= 30) {
                xp += 112 + (level - 30) * 9;
            } else if (level >= 15) {
                xp += 37 + (level - 15) * 5;
            }
            else {
                xp += 7 + level * 2;
            }
        }

        return xp;
    }

    public static void SetActualExperienceTotal(PlayerEntity player, int xp) {
        AddActualExperienceTotal(player, xp - GetActualExperienceTotal(player));
    }

    public static void AddActualExperienceTotal(PlayerEntity player, int xp) {
        player.giveExperiencePoints(xp);
    }

}
