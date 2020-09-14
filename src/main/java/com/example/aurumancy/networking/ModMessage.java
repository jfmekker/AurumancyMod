package com.example.aurumancy.networking;

import net.minecraft.network.PacketBuffer;

/**
 * Base class for mod messages between server and client.
 */
public abstract class ModMessage {

    /**
     * Fill a message packet buffer with message info.
     * @param packet Buffer to fill.
     */
    public abstract void BuildModMessagePacket(PacketBuffer packet);

}
