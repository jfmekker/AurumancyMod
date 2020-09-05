package com.example.aurumancy.wands;

import com.example.aurumancy.rituals.Ritual;
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
public class RitualWand extends Item implements IForgeRegistryEntry<Item> {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public RitualWand(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Block block = world.getBlockState(pos).getBlock();
        PlayerEntity player = context.getPlayer();

        if (player == null) {
            LOGGER.log(Level.ERROR, "Player is null on RitualWand use event: " + context.toString());
            return super.onItemUse(context);
        }

        /*if (block.equals(Blocks.GOLD_BLOCK)) {
            Rituals.GOLD_ABSORB.doRitual(world, pos, player);
        }
        else if (block.equals(Blocks.LAPIS_BLOCK)) {
            Rituals.SPEEDY_LAPIS.doRitual(world, pos, player);
        }
        else if (block.equals(Blocks.IRON_BLOCK)) {
            player.setAir(0);
            world.destroyBlock(pos,false);

            world.addEntity(new TNTEntity(world, pos.getX(), pos.getY(), pos.getZ(), null));
            player.giveExperiencePoints(-25);
        }
        else if (block.equals(Blocks.NETHERRACK)) {
            Rituals.NETHER_TELEPORT.doRitual(world, pos, player);
        }
        else if (block.equals(Blocks.HONEY_BLOCK)) {
            Rituals.JUMP_WAND_CREATE.doRitual(world, pos, player);
        }*/

        boolean ritualMatched = false;
        for (Ritual r : Rituals.RITUAL_SORTED_LIST) {
            if (r.validateRitualComponents(world,pos)) {
                LOGGER.info("RitualWand successfully matched: " + r.toString());
                r.doRitual(world, pos, player);
                ritualMatched = true;
                break;
            }
        }
        if (!ritualMatched) LOGGER.info("RitualWand failed to match any rituals.");

        return super.onItemUse(context);
    }
}
