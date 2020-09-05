package com.example.aurumancy.wands;

import com.example.aurumancy.rituals.Rituals;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("aurumancy")
public class JumpWand extends Item implements IForgeRegistryEntry<Item> {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public JumpWand(Properties properties) {
        super(properties.maxDamage(10));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (player.experienceTotal >= 1) {
            if (player.isAirBorne) {
                if (player.getMotion().y < 0) {
                    player.setVelocity(player.getMotion().x, 0.75, player.getMotion().z);
                }
                else {
                    player.addVelocity(player.getMotion().x, 0.5, player.getMotion().z);
                }

            }
            else {
                player.addVelocity(0,1.5,0);
            }

            player.fallDistance = 0;
            player.giveExperiencePoints(-1);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return super.onItemUse(context);
    }
}
