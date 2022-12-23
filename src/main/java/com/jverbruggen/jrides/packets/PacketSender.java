package com.jverbruggen.jrides.packets;

import javax.annotation.Nullable;

public interface PacketSender {
    Packet sendPacket(PacketType type, @Nullable Object options);
}
