package com.jverbruggen.jrides.packets.packet.v1_20;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.List;

public class ArmorstandRotationServerPacket extends com.jverbruggen.jrides.packets.packet.v1_19.ArmorstandRotationServerPacket {
    public ArmorstandRotationServerPacket(ProtocolManager protocolManager, int entityId, int rotationType, Vector3 rotation) {
        super(protocolManager, entityId, rotationType, rotation);
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, this.entityId);

        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.getVectorSerializer();

        WrappedDataValue value = new WrappedDataValue(rotationType, serializer, null);
        value.setValue(rotation.toVector3F());
        List<WrappedDataValue> values = Lists.newArrayList(
                value
        );

        packet.getDataValueCollectionModifier().write(0, values);

        return packet;
    }
}
