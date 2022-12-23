package com.jverbruggen.jrides.packets.packet.raw;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.Packet;

import java.lang.reflect.InvocationTargetException;

public class ArmorstandRotationPacket implements Packet {
    public static class Type{
        public static final int HEAD = 16;
        public static final int BODY = 17;
        public static final int OFF_HAND = 18;
        public static final int MAIN_HAND = 19;
        public static final int LEFT_LEG = 20;
        public static final int RIGHT_LEG = 21;
    }

    private ProtocolManager protocolManager;
    private int entityId;
    private int rotationType;
    private Vector3 rotation;

    public ArmorstandRotationPacket(ProtocolManager protocolManager, int entityId, int rotationType, Vector3 rotation) {
        this.protocolManager = protocolManager;
        this.entityId = entityId;
        this.rotationType = rotationType;
        this.rotation = rotation;
    }

    @Override
    public boolean send(Player player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, this.entityId);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Vector3.class);
        watcher.setObject(rotationType, serializer, rotation);
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            this.protocolManager.sendServerPacket(player.getBukkitPlayer(), packet);
        }catch(InvocationTargetException e) {
            e.printStackTrace();
        }

        return true;
    }
}
