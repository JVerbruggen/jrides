package com.jverbruggen.jrides.packets;

import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.packet.SpawnVirtualEntityPacket;

import java.util.UUID;

public class PacketSender_1_19_2 implements PacketSender {
    private ProtocolManager protocolManager;

    public PacketSender_1_19_2(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    public void sendSpawnVirtualEntityPacket(Player player, Vector3 location, double yawRotation){
        int entityId = 1;
        int entityType = 1;
        double locationX = location.getX();
        double locationY = location.getY();
        double locationZ = location.getZ();
        UUID uuid = UUID.randomUUID();

        SpawnVirtualEntityPacket packet = new SpawnVirtualEntityPacket(
                protocolManager, entityId, entityType, locationX, locationY, locationZ, yawRotation, uuid);
        packet.send(player);
    }

}
