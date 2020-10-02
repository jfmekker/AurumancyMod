package com.example.aurumancy.blocks;

import com.example.aurumancy.utils.ColorChecker;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TeleportCircleTileEntity extends TileEntity {

    public DyeColor color;

    @SubscribeEvent
    public static void registerTE(RegistryEvent.Register<TileEntityType<?>> evt) {
        evt.getRegistry().register(AurumancyBlocks.TELEPORT_CIRCLE_TILE_ENTITY_TYPE.get());
    }

    public TeleportCircleTileEntity() {
        super(AurumancyBlocks.TELEPORT_CIRCLE_TILE_ENTITY_TYPE.get());
    }

    public boolean checkValidityAndColor() {
        if (!hasWorld() || getWorld() == null) return false;
        World w = getWorld();

        Block[] colored_blocks = {
                w.getBlockState(pos.add(1,0,1)).getBlock(),
                w.getBlockState(pos.add(1,0,-1)).getBlock(),
                w.getBlockState(pos.add(-1,0,1)).getBlock(),
                w.getBlockState(pos.add(-1,0,-1)).getBlock()
        };
        DyeColor new_color = null;
        for (int i = 0 ; i < 4 ; i += 1) {
            Block b = colored_blocks[i];
            DyeColor b_color = ColorChecker.GetDyeColor(b);

            if (!(b instanceof GlazedTerracottaBlock) || b_color == null) {
                color = null;
                return false;
            }

            if (b_color != color) {
                if (new_color == null) {
                    if (i != 0) {
                        color = null;
                        return false;
                    }
                    new_color = b_color;
                }
                else if (b_color != new_color) {
                    color = null;
                    return false;
                }
            }
        }
        if (new_color != null) {
            LogManager.getLogger().debug("Teleport circle setting new color to " + new_color.getName());
            color = new_color;
        }

        Block[] lantern_blocks = {
                w.getBlockState(pos.add(1,0,0)).getBlock(),
                w.getBlockState(pos.add(-1,0,0)).getBlock(),
                w.getBlockState(pos.add(0,0,1)).getBlock(),
                w.getBlockState(pos.add(0,0,-1)).getBlock()
        };
        for (Block b : lantern_blocks) {
            if (b != Blocks.SEA_LANTERN) {
                color = null;
                return false;
            }
        }

        return true;
    }
}


