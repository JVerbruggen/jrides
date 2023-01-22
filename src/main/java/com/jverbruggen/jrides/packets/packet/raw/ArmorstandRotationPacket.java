package com.jverbruggen.jrides.packets.packet.raw;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.Vector3F;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.packet.SingularPacket;
import com.jverbruggen.jrides.packets.Packet;

public class ArmorstandRotationPacket extends SingularPacket implements Packet {
    public static class Type{
        public static final int HEAD = 16;
        public static final int BODY = 17;
        public static final int OFF_HAND = 18;
        public static final int MAIN_HAND = 19;
        public static final int LEFT_LEG = 20;
        public static final int RIGHT_LEG = 21;
    }

    private int entityId;
    private int rotationType;
    private Vector3 rotation;

    public ArmorstandRotationPacket(ProtocolManager protocolManager, int entityId, int rotationType, Vector3 rotation) {
        super(protocolManager);
        this.entityId = entityId;
        this.rotationType = rotationType;
        this.rotation = rotation;
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, this.entityId);

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.getVectorSerializer();
        watcher.setObject(rotationType, serializer, rotation.toVector3F());
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        return packet;
    }
}
