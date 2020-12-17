package com.jacobmekker.aurumancy.blocks.tileentities;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.blocks.AurumancyBlocks;

import com.jacobmekker.aurumancy.blocks.ManaFertilizerBlock;
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

import java.util.ArrayList;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ManaFertilizerTileEntity extends TileEntity implements ITickableTileEntity {

    public static int GROW_PERIOD = 20 * 5; // every 5 seconds
    public static int MAX_RANGE = 4;
    public static int MAX_TRIES = 5;
    public static double MANA_USE_CHANCE = 0.25;

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

        wait_time += 1;
        if (wait_time >= GROW_PERIOD) {
            BlockState self = world.getBlockState(pos);
            int mana = self.get(BlockProperties.stored_mana);
            Aurumancy.LOGGER.trace("ManaFertilizer tick, stored_mana=" + mana);

            // Skip if out of mana
            if (mana <= 0) {
                wait_time = 0;
                return;
            }

            // Get list of valid targets
            ArrayList<BlockPos> crops = new ArrayList<>();
            for (int x = -MAX_RANGE; x <= MAX_RANGE; x++) {
                for (int z = -MAX_RANGE; z <= MAX_RANGE; z++) {
                    BlockState state = world.getBlockState(pos.add(x,0,z));
                    if (state.getBlock() instanceof CropsBlock) {
                        CropsBlock block = (CropsBlock) state.getBlock();
                        if (!block.isMaxAge(state))
                            crops.add(pos.add(x, 0, z));
                    }
                    else if (state.getBlock() instanceof ManaFertilizerBlock) {
                        // MF fails if another MF is in range
                        wait_time = 0;
                        return;
                    }
                }
            }

            // Grow random target
            if (crops.size() > 0) {
                int i = world.rand.nextInt(crops.size());
                BlockPos crop_pos = crops.get(i);
                BlockState crop = world.getBlockState(crop_pos);
                CropsBlock block = (CropsBlock) crop.getBlock();

                block.grow(world, crop_pos, crop);
                world.playEvent(2005, crop_pos, 0);
                Aurumancy.LOGGER.trace("ManaFertilizer grew @ " + crop_pos.toString());
                if (world.rand.nextDouble() < MANA_USE_CHANCE)
                    world.setBlockState(pos, self.with(BlockProperties.stored_mana, mana - 1));
            }

            wait_time = 0;
        }
    }
}
