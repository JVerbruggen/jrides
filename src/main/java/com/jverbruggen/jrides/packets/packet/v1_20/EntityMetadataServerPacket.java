package com.jverbruggen.jrides.packets.packet.v1_20;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

/**
 * 1.20 - current
 */
public class EntityMetadataServerPacket extends com.jverbruggen.jrides.packets.packet.v1_19.EntityMetadataServerPacket {
    public EntityMetadataServerPacket(ProtocolManager protocolManager, int entityId, boolean invisible, String customName) {
        super(protocolManager, entityId, invisible, customName);
    }

    @Override
    protected PacketContainer createMetaDataPacket(int entityId, byte modifier, String customName){
        PacketContainer metaDataPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        metaDataPacket.getIntegers().write(0, entityId);

        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);

        boolean customNameVisible = customName != null;

        List<WrappedDataValue> values = Lists.newArrayList(
                new WrappedDataValue(0, serializer, modifier),
                new WrappedDataValue(3, WrappedDataWatcher.Registry.get(Boolean.class), customNameVisible)
        );

        if (customNameVisible)
            values.add(new WrappedDataValue(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true), Optional.of(WrappedChatComponent.fromText(customName).getHandle())));

        metaDataPacket.getDataValueCollectionModifier().write(0, values);

        return metaDataPacket;
    }
}
