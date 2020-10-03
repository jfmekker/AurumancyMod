package com.jacobmekker.aurumancy.blocks;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class StarlightCollectorTileEntity extends TileEntity implements ITickableTileEntity {

    public static int spawn_ticks = (int)(20 * 7.5); // every 7.5 seconds
    public int stored_starlight = 0;

    @SubscribeEvent
    public static void registerTE(RegistryEvent.Register<TileEntityType<?>> evt) {
        evt.getRegistry().register(AurumancyBlocks.STARLIGHT_COLLECTOR_TILE_ENTITY_TYPE.get());
    }

    public StarlightCollectorTileEntity() {
        super(AurumancyBlocks.STARLIGHT_COLLECTOR_TILE_ENTITY_TYPE.get());
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        stored_starlight = compound.getInt("stored_starlight");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("stored_starlight", stored_starlight);
        return compound;
    }

    @Override
    public void tick() {
        if (!hasWorld() || getWorld() == null || getWorld().isRemote) return;

        stored_starlight += 1;
        if (stored_starlight >= spawn_ticks) {
            getWorld().addEntity(
                new ItemEntity(getWorld(), pos.getX(), pos.getY(), pos.getZ(),
                    new ItemStack(Items.GLOWSTONE_DUST)));
            stored_starlight -= spawn_ticks;
        }

        markDirty();
    }
}
