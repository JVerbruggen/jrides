package com.jverbruggen.jrides.packets;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;

public interface PacketSender {
    void sendSpawnVirtualEntityPacket(Player player, Vector3 location, double yawRotation);
}
