package com.example.aurumancy.networking;

import com.example.aurumancy.networking.messages.SummonLightningMessage;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("aurumancy", "main"),
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
