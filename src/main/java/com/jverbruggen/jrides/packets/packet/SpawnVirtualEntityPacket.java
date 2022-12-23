package com.jverbruggen.jrides.packets.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.jverbruggen.jrides.packets.Packet;
import com.jverbruggen.jrides.packets.options.SpawnVirtualEntityPacketOptions;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class SpawnVirtualEntityPacket implements Packet {
    private SpawnVirtualEntityPacketOptions options;
    private ProtocolManager protocolManager;

    public SpawnVirtualEntityPacket(
            ProtocolManager protocolManager,
            SpawnVirtualEntityPacketOptions options) {
        this.protocolManager = protocolManager;
        this.options = options;
    }

    @Override
    public boolean send(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        packet.getIntegers()
                .write(0, options.getEntityId())
                .write(1, options.getEntityType());
        packet.getDoubles()
                .write(0, options.getLocationX())
                .write(1, options.getLocationY())
                .write(2, options.getLocationZ());
        packet.getBytes()
                .write(0, (byte)options.getYawRotation());
        packet.getUUIDs()
                .write(0, options.getUuid());
        try {
            this.protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;
    }
}
