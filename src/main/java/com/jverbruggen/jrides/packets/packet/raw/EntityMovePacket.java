package com.jverbruggen.jrides.packets.packet.raw;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.packet.MultiplePacket;
import com.jverbruggen.jrides.packets.Packet;

import java.util.LinkedList;
import java.util.List;

public class EntityMovePacket extends MultiplePacket implements Packet {
    private int entityId;
    private Vector3 location;
    private double yawRotation;

    public EntityMovePacket(ProtocolManager protocolManager, int entityId, Vector3 location, double yawRotation) {
        super(protocolManager);
        this.entityId = entityId;
        this.location = location;
        this.yawRotation = yawRotation;
    }

    @Override
    public List<PacketContainer> getPackets() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);
        packet.getIntegers()
                .write(0, entityId);
        packet.getShorts()
                .write(0, (short)((location.getX() * 32) * 128))
                .write(1, (short)((location.getY() * 32) * 128))
                .write(2, (short)((location.getZ() * 32) * 128));
        packet.getBytes()
                .write(0, (byte)(yawRotation));
        packet.getBooleans()
                .write(0, false);

        PacketContainer rotationPacket = new PacketContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        rotationPacket.getIntegers()
                .write(0, entityId);
        rotationPacket.getBytes()
                .write(0, (byte) yawRotation);

        List<PacketContainer> packets = new LinkedList<>();
        packets.add(packet);
        packets.add(rotationPacket);

        return packets;
    }
}
