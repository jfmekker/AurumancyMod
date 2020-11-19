package com.jacobmekker.aurumancy.blocks.tileentities;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.blocks.AurumancyBlocks;
import com.jacobmekker.aurumancy.data.BlockProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.antlr.v4.runtime.atn.BlockStartState;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ManaFertilizerTileEntity extends TileEntity implements ITickableTileEntity {

    public static int spawn_ticks = (int)(20 * 1.5); // every 1.5 seconds
    public int wait_time = 0;

    @SubscribeEvent
    public static void registerTE(RegistryEvent.Register<TileEntityType<?>> evt) {
        evt.getRegistry().register(AurumancyBlocks.MANA_FERTILIZER_TILE_ENTITY_TYPE.get());
    }

    public ManaFertilizerTileEntity() {
        super(AurumancyBlocks.MANA_FERTILIZER_TILE_ENTITY_TYPE.get());
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        // every 1.5 seconds (while loaded)
        wait_time += 1;
        if (wait_time >= spawn_ticks) {
            Aurumancy.LOGGER.debug("ManaFertilizer tick");

            // grow something
            // decrease mana in blockstate

            BlockState a = world.getBlockState(pos);
            for (int x=-5; x <= 5; x++) {
                for (int z=-5; z<= 5; z++) {
                    BlockPos crop_pos = pos.add(x,0,z);
                    BlockState crop = world.getBlockState(crop_pos);
                    if (crop.getBlock() instanceof CropsBlock) {
                        CropsBlock c = (CropsBlock) crop.getBlock();
                        //int mana = a.get(BlockProperties.stored_mana);
                        if (!c.isMaxAge(crop)) {
                            c.grow(world, crop_pos, crop);

                            //world.setBlockState(pos,a.with(BlockProperties.stored_mana, mana)); // TODO decrease
                            Aurumancy.LOGGER.debug("ManaFertilizer grew @ " + crop_pos.toString());
                        }
                    }
                }
            }

            wait_time -= spawn_ticks;
        }
    }
}
