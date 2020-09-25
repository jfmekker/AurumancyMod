package com.example.aurumancy.networking.messages;

import com.example.aurumancy.networking.ModMessage;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;

/**
 * SummonLightningMessage
 *
 * Message to the server to summon a lightning bolt.
 *
 * Field:         Type:
 * BlockPos.x     VarInt
 * BlockPos.y     VarInt
 * BlockPos.z     VarInt
 *
 */
public class SummonLightningMessage extends ModMessage {

    private BlockPos pos;

    public SummonLightningMessage(BlockPos pos) {
        this.pos = pos;
    }

    public SummonLightningMessage(PacketBuffer packet) {
        int x = packet.readVarInt();
        int y = packet.readVarInt();
        int z = packet.readVarInt();
        this.pos = new BlockPos(x, y, z);
    }

    @Override
    public void BuildModMessagePacket(PacketBuffer packet) {
        packet.writeVarInt(pos.getX());
        packet.writeVarInt(pos.getY());
        packet.writeVarInt(pos.getZ());
        LogManager.getLogger().debug("Encoding SummonLightningMessage.");
    }

    public void HandleNetworkContext(Supplier<NetworkEvent.Context> context) {
        LogManager.getLogger().debug("Queueing handle SummonLightningMessage.");
        context.get().enqueueWork(() -> {
            LogManager.getLogger().debug("Handling SummonLightningMessage.");
            ServerPlayerEntity spe = context.get().getSender();
            if (spe != null && spe.world.isAreaLoaded(pos, 1)) {
                LogManager.getLogger().debug("SummonLightningMessage summoning lightning.");
                LightningBoltEntity bolt = new LightningBoltEntity(spe.world,pos.getX(), pos.getY(), pos.getZ(), false);
                bolt.setCaster(spe);
                bolt.setPosition(pos.getX(),pos.getY(),pos.getZ());
                spe.getEntityWorld().addEntity(bolt);
                spe.getServerWorld().addLightningBolt(bolt);
            }
        });
        context.get().setPacketHandled(true);
    }

}
