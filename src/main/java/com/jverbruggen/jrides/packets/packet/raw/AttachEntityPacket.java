package com.jverbruggen.jrides.packets.packet.raw;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.packets.Packet;

import java.lang.reflect.InvocationTargetException;

public class AttachEntityPacket implements Packet {
    private ProtocolManager protocolManager;
    private int entityId;
    private int leashedToEntityId;

    public AttachEntityPacket(ProtocolManager protocolManager, int entityId, int leashedToEntityId) {
        this.protocolManager = protocolManager;
        this.entityId = entityId;
        this.leashedToEntityId = leashedToEntityId;
    }

    @Override
    public boolean send(Player player) {
        PacketContainer leashPacket = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
        leashPacket.getIntegers()
                .write(0, entityId)
                .write(1, leashedToEntityId);
        try {
            this.protocolManager.sendServerPacket(player.getBukkitPlayer(), leashPacket);
        }catch(InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;
    }
}
