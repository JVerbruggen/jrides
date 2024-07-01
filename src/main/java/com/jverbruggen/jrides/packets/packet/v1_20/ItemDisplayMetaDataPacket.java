package com.jverbruggen.jrides.packets.packet.v1_20;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import com.jverbruggen.jrides.packets.Packet;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;

import java.util.List;

public class ItemDisplayMetaDataPacket extends SingularServerPacket implements Packet {

    private final int entityId;
    private final int positionRotationInterpolationDuration;

    public ItemDisplayMetaDataPacket(ProtocolManager protocolManager, int entityId, int positionRotationInterpolationDuration) {
        super(protocolManager);
        this.entityId = entityId;
        this.positionRotationInterpolationDuration = positionRotationInterpolationDuration;
    }

    @Override
    public PacketContainer getPacket() {
        return createMetaDataPacket(entityId, positionRotationInterpolationDuration);
    }

    protected PacketContainer createMetaDataPacket(int entityId, int positionRotationInterpolationDuration) {
        PacketContainer metaDataPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        metaDataPacket.getIntegers().write(0, entityId);

        List<WrappedDataValue> values = Lists.newArrayList(
                new WrappedDataValue(8, WrappedDataWatcher.Registry.get(Integer.class), 0),
                new WrappedDataValue(10, WrappedDataWatcher.Registry.get(Integer.class), positionRotationInterpolationDuration)
        );

        metaDataPacket.getDataValueCollectionModifier().write(0, values);

        return metaDataPacket;
    }
}
