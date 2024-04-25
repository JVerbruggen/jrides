package com.jverbruggen.jrides.packets.packet.v1_19;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;
import com.jverbruggen.jrides.packets.Packet;

import java.util.ArrayList;
import java.util.List;

public class EntityDestroyServerPacket extends SingularServerPacket implements Packet {
    private int entityId;

    public EntityDestroyServerPacket(ProtocolManager protocolManager, int entityId) {
        super(protocolManager);
        this.entityId = entityId;
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        List<Integer> toDestroy = new ArrayList<>();
        toDestroy.add(this.entityId);
        packet.getIntLists()
                .write(0, toDestroy);

        return packet;
    }
}
