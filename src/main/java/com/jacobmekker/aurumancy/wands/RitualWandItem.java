package com.jacobmekker.aurumancy.wands;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.rituals.Ritual;
import com.jacobmekker.aurumancy.rituals.Rituals;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

@Mod(Aurumancy.MODID)
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
            Aurumancy.LOGGER.error("Player is null on RitualWandItem use event: " + context.toString());
            return;
        }

        if (world.isRemote) return;
        super.blockUsage(context);

        boolean ritualMatched = false;
        for (Ritual r : Rituals.RITUAL_SORTED_LIST) {
            if (r.validateRitualComponents(world,pos)) {
                Aurumancy.LOGGER.info("RitualWandItem successfully matched: " + r.toString());
                r.doRitual(world, pos, player);
                ritualMatched = true;
                break;
            }
        }
        if (!ritualMatched) Aurumancy.LOGGER.info("RitualWandItem failed to match any rituals.");
    }

}
