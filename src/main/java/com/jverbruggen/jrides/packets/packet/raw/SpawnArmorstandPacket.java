package com.jverbruggen.jrides.packets.packet.raw;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.packets.Packet;
import com.jverbruggen.jrides.packets.packet.SingularPacket;

import java.util.UUID;

public class SpawnArmorstandPacket extends SingularPacket implements Packet {
    private ProtocolManager protocolManager;

    private int entityId;
    private int entityType;
    private double locationX;
    private double locationY;
    private double locationZ;
    private double yawRotation;
    private UUID uuid;

    public SpawnArmorstandPacket(ProtocolManager protocolManager, int entityId, int entityType, double locationX, double locationY, double locationZ, double yawRotation, UUID uuid) {
        this.protocolManager = protocolManager;
        this.entityId = entityId;
        this.entityType = entityType;
        this.locationX = locationX;
        this.locationY = locationY;
        this.locationZ = locationZ;
        this.yawRotation = yawRotation;
        this.uuid = uuid;
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        packet.getIntegers()
                .write(0, entityId)
                .write(1, entityType);
        packet.getDoubles()
                .write(0, locationX)
                .write(1, locationY)
                .write(2, locationZ);
        packet.getBytes()
                .write(0, (byte)yawRotation);
        packet.getUUIDs()
                .write(0, uuid);

        return packet;
    }
}
