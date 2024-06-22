package com.jverbruggen.jrides.packets.packet.v1_19;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;
import com.jverbruggen.jrides.packets.Packet;

public class AttachEntityServerPacket extends SingularServerPacket implements Packet {
    private int entityId;
    private int leashedToEntityId;

    public AttachEntityServerPacket(ProtocolManager protocolManager, int entityId, int leashedToEntityId) {
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
