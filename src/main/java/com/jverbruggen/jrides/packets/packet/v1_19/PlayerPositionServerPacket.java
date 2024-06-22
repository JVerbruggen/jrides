package com.jverbruggen.jrides.packets.packet.v1_19;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.Packet;
import com.jverbruggen.jrides.packets.packet.SingularClientPacket;

public class PlayerPositionServerPacket extends SingularClientPacket implements Packet {
    private final Vector3 position;

    public PlayerPositionServerPacket(ProtocolManager protocolManager, Vector3 position) {
        super(protocolManager);

        this.position = position;
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Client.POSITION);
        packet.getDoubles()
                .write(0, position.getX())
                .write(1, position.getY())
                .write(2, position.getZ());

        return packet;
    }
}
