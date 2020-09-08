package com.example.aurumancy.networking;

import net.minecraft.network.PacketBuffer;

public abstract class ModMessage {

    public abstract void BuildModMessagePacket(PacketBuffer packet);

}
