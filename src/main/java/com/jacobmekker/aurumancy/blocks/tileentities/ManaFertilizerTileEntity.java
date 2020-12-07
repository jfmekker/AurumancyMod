package com.jacobmekker.aurumancy.blocks.tileentities;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.blocks.AurumancyBlocks;

import com.jacobmekker.aurumancy.data.BlockProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ManaFertilizerTileEntity extends TileEntity implements ITickableTileEntity {

    public static int GROW_PERIOD = 20 * 5; // every 5 seconds
    public static int MAX_RANGE = 4;
    public static int MAX_TRIES = 5;

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
        if (wait_time >= GROW_PERIOD) {
            Aurumancy.LOGGER.debug("ManaFertilizer tick");

            BlockState self = world.getBlockState(pos);
            int mana = self.get(BlockProperties.stored_mana);
            if (mana <= 0) {
                Aurumancy.LOGGER.debug("ManaFertilizer out of mana");
                wait_time = 0;
                return;
            }

            int tries = 0;
            while (tries < MAX_TRIES) {
                int x = world.rand.nextInt(MAX_RANGE * 2 + 1) - MAX_RANGE;
                int z = world.rand.nextInt(MAX_RANGE * 2 + 1) - MAX_RANGE;

                BlockPos crop_pos = pos.add(x,0,z);
                BlockState crop = world.getBlockState(crop_pos);
                if (crop.getBlock() instanceof CropsBlock) {
                    CropsBlock c = (CropsBlock) crop.getBlock();
                    if (!c.isMaxAge(crop)) {
                        c.grow(world, crop_pos, crop);
                        world.playEvent(2005, crop_pos, 0);
                        Aurumancy.LOGGER.debug("ManaFertilizer grew @ " + crop_pos.toString());
                        world.setBlockState(pos, self.with(BlockProperties.stored_mana, mana - 1));
                        break;
                    }
                }

                tries += 1;
            }

            wait_time = 0;
        }
    }
}
