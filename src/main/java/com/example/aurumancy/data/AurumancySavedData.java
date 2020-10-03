package com.example.aurumancy.data;

import com.example.aurumancy.Aurumancy;
import com.example.aurumancy.blocks.TeleportCircleBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.logging.log4j.LogManager;

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
        LogManager.getLogger().debug("Reading AurumancySavedData.");

        // Read teleport circle data
        CompoundNBT tc_data = nbt.getCompound("teleport_circles");
        int[] D = tc_data.getIntArray("dimension_types");
        int[] X = tc_data.getIntArray("positions_x");
        int[] Y = tc_data.getIntArray("positions_y");
        int[] Z = tc_data.getIntArray("positions_z");
        for (int i = 0 ; i < D.length ; i += 1) {
            DimensionType d = DimensionType.getById(D[i]);
            if (d == null) continue;
            TeleportCircleBlock.circles.add(
                    new Tuple<>( d, new BlockPos(X[i], Y[i], Z[i]) )
            );
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        LogManager.getLogger().debug("Writing AurumancySavedData.");

        // Write teleport circle data
        int size = TeleportCircleBlock.circles.size();
        int[] D = new int[size];
        int[] X = new int[size];
        int[] Y = new int[size];
        int[] Z = new int[size];
        for (int i = 0 ; i < size ; i += 1) {
            D[i] = TeleportCircleBlock.circles.get(i).getA().getId();
            X[i] = TeleportCircleBlock.circles.get(i).getB().getX();
            Y[i] = TeleportCircleBlock.circles.get(i).getB().getY();
            Z[i] = TeleportCircleBlock.circles.get(i).getB().getZ();
        }
        CompoundNBT tc_data = new CompoundNBT();
        tc_data.putIntArray("dimension_types", D);
        tc_data.putIntArray("positions_x", X);
        tc_data.putIntArray("positions_y", Y);
        tc_data.putIntArray("positions_z", Z);
        compound.put("teleport_circles", tc_data);

        return compound;
    }
}
