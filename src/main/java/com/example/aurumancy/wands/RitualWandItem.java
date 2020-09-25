package com.example.aurumancy.wands;

import com.example.aurumancy.rituals.Ritual;
import com.example.aurumancy.rituals.Rituals;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("aurumancy")
public class RitualWandItem extends AbstractWandItem {

    /**
     * Construct a new Ritual Wand.
     * @param properties Item properties object
     */
    public RitualWandItem(Properties properties) {
        super(properties, 0, WandUsageType.BLOCK);
    }

    /**
     * Resolve usage on a block. Check for matching rituals with the target block as the center.
     * @param context Context object of event. Contains block position, world, player, etc.
     */
    @Override
    protected void blockUsage(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Block block = world.getBlockState(pos).getBlock();
        PlayerEntity player = context.getPlayer();

        if (player == null) {
            LOGGER.error("Player is null on RitualWandItem use event: " + context.toString());
            return;
        }

        if (world.isRemote) return;
        super.blockUsage(context);

        boolean ritualMatched = false;
        for (Ritual r : Rituals.RITUAL_SORTED_LIST) {
            if (r.validateRitualComponents(world,pos)) {
                LOGGER.info("RitualWandItem successfully matched: " + r.toString());
                r.doRitual(world, pos, player);
                ritualMatched = true;
                break;
            }
        }
        if (!ritualMatched) LOGGER.info("RitualWandItem failed to match any rituals.");
    }

}
