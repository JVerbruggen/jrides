package com.jverbruggen.jrides.packets.packet;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.packets.Packet;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class MultiplePacket implements Packet {
    protected ProtocolManager protocolManager;

    public abstract List<PacketContainer> getPackets();

    protected boolean sendPacket(Player player, PacketContainer packet){
        try {
            this.protocolManager.sendServerPacket(player.getBukkitPlayer(), packet);
            return true;
        }catch(InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean send(Player player) {
        for(PacketContainer packet : getPackets()){
            sendPacket(player, packet);
        }
        return true;
    }

    @Override
    public void sendAll(List<Player> players) {
        for(PacketContainer packet : getPackets()){
            for(Player player : players){
                sendPacket(player, packet);
            }
        }
    }
}
