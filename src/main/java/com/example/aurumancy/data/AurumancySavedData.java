package com.example.aurumancy.data;

import com.example.aurumancy.Aurumancy;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.WorldSavedData;

public class AurumancySavedData extends WorldSavedData {

    public static final String DATA_NAME = Aurumancy.MODID + "_data";

    public AurumancySavedData() {
        super(DATA_NAME);
    }

    public AurumancySavedData(String name) {
        super(name);
    }

    @Override
    public void read(CompoundNBT nbt) {

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return null;
    }
}
