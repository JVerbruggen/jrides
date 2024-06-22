package com.jverbruggen.jrides.packets.packet.v1_19;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.jverbruggen.jrides.packets.packet.SingularServerPacket;
import com.jverbruggen.jrides.packets.Packet;

import java.util.Optional;

/**
 * Pre < 1.19
 */
public class EntityMetadataServerPacket extends SingularServerPacket implements Packet {
    private final int entityId;
    private final boolean invisible;
    private final String customName;

    public EntityMetadataServerPacket(ProtocolManager protocolManager, int entityId, boolean invisible, String customName) {
        super(protocolManager);
        this.entityId = entityId;
        this.invisible = invisible;
        this.customName = customName;
    }

    @Override
    public PacketContainer getPacket() {
        byte modifier = 0x0;
        if(this.invisible) modifier |= 0x20;

        return createMetaDataPacket(this.entityId, modifier, this.customName);
    }

    protected PacketContainer createMetaDataPacket(int entityId, byte modifier, String customName){
        PacketContainer metaDataPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        metaDataPacket.getIntegers().write(0, entityId);
        boolean customNameVisible = customName != null;

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), modifier);
        if(customNameVisible){
            watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)),
                    Optional.of(WrappedChatComponent.fromText(customName).getHandle()));
        }
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)),
                customNameVisible);

        metaDataPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        return metaDataPacket;
    }
}
