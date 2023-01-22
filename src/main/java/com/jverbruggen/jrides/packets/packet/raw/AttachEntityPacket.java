package com.jverbruggen.jrides.packets.packet.raw;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.packets.packet.SingularPacket;
import com.jverbruggen.jrides.packets.Packet;

public class AttachEntityPacket extends SingularPacket implements Packet {
    private int entityId;
    private int leashedToEntityId;

    public AttachEntityPacket(ProtocolManager protocolManager, int entityId, int leashedToEntityId) {
        super(protocolManager);
        this.entityId = entityId;
        this.leashedToEntityId = leashedToEntityId;
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer leashPacket = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
        leashPacket.getIntegers()
                .write(0, entityId)
                .write(1, leashedToEntityId);

        return leashPacket;
    }
}
