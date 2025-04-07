package com.jverbruggen.jrides.packets.packet.v1_20;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.google.common.collect.Lists;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.Packet;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3f;

import java.util.List;

public class ItemDisplayModelServerPacket extends SingularServerPacket implements Packet {
    private final int entityId;
    private final ItemDisplay.ItemDisplayTransform itemDisplayTransform;
    private final TrainModelItem model;
    private final Vector3 scale;

    public ItemDisplayModelServerPacket(ProtocolManager protocolManager, int entityId, ItemDisplay.ItemDisplayTransform itemDisplayTransform, TrainModelItem model, Vector3 scale) {
        super(protocolManager);
        this.entityId = entityId;
        this.itemDisplayTransform = itemDisplayTransform;
        this.model = model;
        this.scale = scale;
    }

    @Override
    public PacketContainer getPacket() {
        return createMetaDataPacket(entityId, itemDisplayTransform, model.getItemStack());
    }

    protected PacketContainer createMetaDataPacket(int entityId, ItemDisplay.ItemDisplayTransform itemDisplayTransform, ItemStack itemStack) {
        PacketContainer metaDataPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        metaDataPacket.getIntegers().write(0, entityId);

        List<WrappedDataValue> values = Lists.newArrayList(
                new WrappedDataValue(12, WrappedDataWatcher.Registry.get(Vector3f.class), new Vector3f((float)scale.x, (float)scale.y, (float)scale.z)),
                new WrappedDataValue(23, WrappedDataWatcher.Registry.getItemStackSerializer(false), MinecraftReflection.getMinecraftItemStack(itemStack)),
                new WrappedDataValue(24, WrappedDataWatcher.Registry.get(Byte.class), (byte) itemDisplayTransform.ordinal())
        );

        metaDataPacket.getDataValueCollectionModifier().write(0, values);

        return metaDataPacket;
    }
}
