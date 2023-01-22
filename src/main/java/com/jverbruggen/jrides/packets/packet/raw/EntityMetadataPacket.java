package com.jverbruggen.jrides.packets.packet.raw;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.packet.SingularPacket;
import com.jverbruggen.jrides.packets.Packet;

public class EntityMetadataPacket extends SingularPacket implements Packet {
    private int entityId;
    private boolean invisible;
    private Vector3 headRotation;

    public EntityMetadataPacket(ProtocolManager protocolManager, int entityId, boolean invisible) {
        super(protocolManager);
        this.entityId = entityId;
        this.invisible = invisible;
    }

    public EntityMetadataPacket(ProtocolManager protocolManager, int entityId, boolean invisible, Vector3 headRotation) {
        super(protocolManager);
        this.entityId = entityId;
        this.invisible = invisible;
        this.headRotation = headRotation;
    }

    @Override
    public PacketContainer getPacket() {
        byte modifier = 0x0;
        if(invisible) modifier |= 0x20;

        PacketContainer invisiblePacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        invisiblePacket.getIntegers().write(0, this.entityId);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setObject(0, serializer, modifier);
        invisiblePacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        return invisiblePacket;
    }
}
