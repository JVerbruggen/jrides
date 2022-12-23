package com.jverbruggen.jrides.packets.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.packets.Packet;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class SpawnVirtualEntityPacket implements Packet {
    private ProtocolManager protocolManager;

    private int entityId;
    private int entityType;
    private double locationX;
    private double locationY;
    private double locationZ;
    private double yawRotation;
    private UUID uuid;

    public SpawnVirtualEntityPacket(ProtocolManager protocolManager, int entityId, int entityType, double locationX, double locationY, double locationZ, double yawRotation, UUID uuid) {
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
    public boolean send(Player player) {
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
        try {
            protocolManager.sendServerPacket(player.getBukkitPlayer(), packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return true;
    }
}
