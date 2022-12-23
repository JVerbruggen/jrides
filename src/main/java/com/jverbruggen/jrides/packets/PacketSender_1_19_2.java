package com.jverbruggen.jrides.packets;

import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.packets.options.SpawnVirtualEntityPacketOptions;
import com.jverbruggen.jrides.packets.packet.SpawnVirtualEntityPacket;

import javax.annotation.Nullable;

public class PacketSender_1_19_2 implements PacketSender {
    private ProtocolManager protocolManager;

    public PacketSender_1_19_2(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    @Override
    public Packet sendPacket(PacketType type, @Nullable Object options){
        switch(type){
            case SPAWN_VIRTUAL_ENTITY:
                return new SpawnVirtualEntityPacket(protocolManager, (SpawnVirtualEntityPacketOptions) options);
        }
        throw new IllegalArgumentException("Options is not correct");
    }

}
