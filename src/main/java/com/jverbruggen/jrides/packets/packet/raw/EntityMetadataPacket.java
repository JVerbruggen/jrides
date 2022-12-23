package com.jverbruggen.jrides.packets.packet.raw;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.Packet;

import java.lang.reflect.InvocationTargetException;

public class EntityMetadataPacket implements Packet {
    private ProtocolManager protocolManager;
    private int entityId;
    private boolean invisible;
    private Vector3 headRotation;

    public EntityMetadataPacket(ProtocolManager protocolManager, int entityId, boolean invisible) {
        this.protocolManager = protocolManager;
        this.entityId = entityId;
        this.invisible = invisible;
    }

    public EntityMetadataPacket(ProtocolManager protocolManager, int entityId, boolean invisible, Vector3 headRotation) {
        this.protocolManager = protocolManager;
        this.entityId = entityId;
        this.invisible = invisible;
        this.headRotation = headRotation;
    }

    @Override
    public boolean send(Player player) {
        PacketContainer invisiblePacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        invisiblePacket.getIntegers().write(0, this.entityId);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setObject(0, serializer, (byte)(0x20));
        invisiblePacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            this.protocolManager.sendServerPacket(player.getBukkitPlayer(), invisiblePacket);
        }catch(InvocationTargetException e) {
            e.printStackTrace();
        }

        return true;
    }
}
