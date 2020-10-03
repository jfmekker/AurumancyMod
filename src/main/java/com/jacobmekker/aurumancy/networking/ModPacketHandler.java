package com.jacobmekker.aurumancy.networking;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.networking.messages.SummonLightningMessage;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Class to handle sending messages between client and server.
 */
public class ModPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Aurumancy.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextMessageId = 1;

    public static void registerMessages() {
        INSTANCE.registerMessage(nextMessageId++,
                SummonLightningMessage.class,
                SummonLightningMessage::BuildModMessagePacket,
                SummonLightningMessage::new,
                SummonLightningMessage::HandleNetworkContext);
    }

}
