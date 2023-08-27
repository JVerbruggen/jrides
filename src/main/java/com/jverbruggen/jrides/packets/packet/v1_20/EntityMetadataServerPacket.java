package com.jverbruggen.jrides.packets.packet.v1_20;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.Packet;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;
import org.joml.Vector3f;

import java.util.List;

public class EntityMetadataServerPacket extends SingularServerPacket implements Packet {
    private final int entityId;
    private final boolean invisible;
    private Vector3 headRotation;

    public EntityMetadataServerPacket(ProtocolManager protocolManager, int entityId, boolean invisible) {
        super(protocolManager);
        this.entityId = entityId;
        this.invisible = invisible;
    }

    public EntityMetadataServerPacket(ProtocolManager protocolManager, int entityId, boolean invisible, Vector3 headRotation) {
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

        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);

        List<WrappedDataValue> values = Lists.newArrayList(
                new WrappedDataValue(0, serializer, modifier)
        );
        invisiblePacket.getDataValueCollectionModifier().write(0, values);

        return invisiblePacket;
    }
}
