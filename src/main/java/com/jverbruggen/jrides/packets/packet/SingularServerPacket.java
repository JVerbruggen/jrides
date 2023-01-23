package com.jverbruggen.jrides.packets.packet;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.packets.Packet;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class SingularServerPacket implements Packet {
    protected ProtocolManager protocolManager;

    public SingularServerPacket(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    public abstract PacketContainer getPacket();

    protected boolean sendPacket(Player player, PacketContainer packet){
        this.protocolManager.sendServerPacket(player.getBukkitPlayer(), packet);
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
