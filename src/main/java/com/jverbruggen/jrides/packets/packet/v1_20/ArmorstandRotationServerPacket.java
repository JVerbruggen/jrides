package com.jverbruggen.jrides.packets.packet.v1_20;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import com.jverbruggen.jrides.models.math.Vector3;
import org.joml.Vector3f;

import java.util.List;

public class ArmorstandRotationServerPacket extends com.jverbruggen.jrides.packets.packet.raw.ArmorstandRotationServerPacket {
    public ArmorstandRotationServerPacket(ProtocolManager protocolManager, int entityId, int rotationType, Vector3 rotation) {
        super(protocolManager, entityId, rotationType, rotation);
    }

    @Override
    public PacketContainer getPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, this.entityId);

        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.getVectorSerializer();


//        List<WrappedDataValue> values = Lists.newArrayList(
//                new WrappedDataValue(rotationType, serializer, rotation.toVector3F())
//        );
        List<WrappedDataValue> values = Lists.newArrayList(
                new WrappedDataValue(rotationType, WrappedDataWatcher.Registry.get(Vector3f.class), rotation.toVector3fJoml())
        );
        packet.getDataValueCollectionModifier().write(0, values);

        return packet;
    }
}
