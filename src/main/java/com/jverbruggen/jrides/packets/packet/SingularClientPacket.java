package com.jverbruggen.jrides.packets.packet;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.packets.Packet;

import java.util.List;

public abstract class SingularClientPacket implements Packet {
    protected ProtocolManager protocolManager;

    public SingularClientPacket(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    public abstract PacketContainer getPacket();

    protected boolean sendPacket(Player player, PacketContainer packet){
        this.protocolManager.receiveClientPacket(player.getBukkitPlayer(), packet);
        return true;
    }

    @Override
    public boolean send(Player player) {
        return sendPacket(player, getPacket());
    }

    @Override
    public void sendAll(List<Player> players) {
        PacketContainer packet = getPacket();
        for(Player player : players){
            sendPacket(player, packet);
        }
    }
}
