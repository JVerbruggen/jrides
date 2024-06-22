package com.jverbruggen.jrides.packets.packet.v1_19;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.Packet;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;

public class EntityTeleportServerPacket extends SingularServerPacket implements Packet {
    private int entityId;
    private Vector3 location;

    public EntityTeleportServerPacket(ProtocolManager protocolManager, int entityId, Vector3 location) {
        super(protocolManager);
        this.entityId = entityId;
        this.location = location;
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
        packet.getIntegers()
                .write(0, this.entityId);
        packet.getDoubles()
                .write(0, (double) (location.getBlockX()))
                .write(1, (double) (location.getBlockY()))
                .write(2, (double) (location.getBlockZ()));
        packet.getBooleans()
                .write(0, false);

        return packet;
    }
}
