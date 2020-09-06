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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("aurumancy")
public class RitualWandItem extends AbstractWandItem {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public RitualWandItem(Properties properties) {
        super(properties, 0);
    }

    @Override
    protected void rightClickUsage(World world, PlayerEntity player, Hand hand) { }

    @Override
    protected void blockUsage(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Block block = world.getBlockState(pos).getBlock();
        PlayerEntity player = context.getPlayer();

        if (player == null) {
            LOGGER.log(Level.ERROR, "Player is null on RitualWandItem use event: " + context.toString());
            return;
        }

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
