package com.jverbruggen.jrides.packets.packet.raw;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.packets.Packet;
import com.jverbruggen.jrides.packets.packet.SingularPacket;

public class EntityMountPacket extends SingularPacket implements Packet {
    private final int entityId;
    private final Player mountedPlayer;

    public EntityMountPacket(ProtocolManager protocolManager, int entityId, Player mountedPlayer) {
        super(protocolManager);

        this.entityId = entityId;
        this.mountedPlayer = mountedPlayer;
    }

    @Override
    public PacketContainer getPacket() {
        int[] mountingEntity;
        if(mountedPlayer != null){
            mountingEntity = new int[]{mountedPlayer.getBukkitPlayer().getEntityId()};
        }else{
            mountingEntity = new int[]{};
        }

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.MOUNT);
        packet.getIntegers().write(0, entityId);
        packet.getIntegerArrays().write(0, mountingEntity);

        return packet;
    }
}
