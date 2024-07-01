package com.jverbruggen.jrides.packets.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.packets.Packet;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;
import org.joml.Quaternionf;

import java.util.List;

public class ItemDisplayRotationPacket extends SingularServerPacket implements Packet {

    private final int entityId;
    private final Quaternion orientation;
    private final int positionRotationInterpolationDuration;

    public ItemDisplayRotationPacket(ProtocolManager protocolManager, int entityId, Quaternion orientation, int positionRotationInterpolationDuration) {
        super(protocolManager);
        this.entityId = entityId;
        this.orientation = orientation;
        this.positionRotationInterpolationDuration = positionRotationInterpolationDuration;
    }

    @Override
    public PacketContainer getPacket() {
        return createMetaDataPacket(entityId, orientation);
    }

    protected PacketContainer createMetaDataPacket(int entityId, Quaternion orientation) {
        PacketContainer metaDataPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        metaDataPacket.getIntegers().write(0, entityId);

        List<WrappedDataValue> values = Lists.newArrayList(
                new WrappedDataValue(8, WrappedDataWatcher.Registry.get(Integer.class), -1),
                new WrappedDataValue(9, WrappedDataWatcher.Registry.get(Integer.class), positionRotationInterpolationDuration),
                new WrappedDataValue(13, WrappedDataWatcher.Registry.get(Quaternionf.class), new Quaternionf(orientation.getX(), orientation.getY(), orientation.getZ(), orientation.getW()))
        );

        metaDataPacket.getDataValueCollectionModifier().write(0, values);

        return metaDataPacket;
    }
}
