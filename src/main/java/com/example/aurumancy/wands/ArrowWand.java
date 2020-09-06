package com.example.aurumancy.wands;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("aurumancy")
public class ArrowWand extends Item implements IForgeRegistryEntry<Item> {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public ArrowWand(Properties properties) {
        super(properties);
    }

    private int xpCost = 1;

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (player.experienceTotal >= xpCost) {
            Vec3d eyePos = player.getEyePosition(0);
            float yaw = player.getYaw(0);
            float pitch = player.getPitch(0);

            LOGGER.info("ArrowWand used at look yaw=" + yaw + " pitch=" + pitch);
            LOGGER.info("ArrowWand used at eye position: " + eyePos.toString());

            float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F)) * 2;
            float f1 = -MathHelper.sin(pitch * ((float)Math.PI / 180F)) * 2;
            float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F)) * 2;

            ArrowEntity arrow = new ArrowEntity(world,eyePos.x + f, eyePos.y + f1, eyePos.z + f2);
            world.addEntity(arrow);
            arrow.shoot(player, pitch, yaw, 0, 1, 0.5f);

            player.giveExperiencePoints(-xpCost);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return super.onItemUse(context);
    }
}
