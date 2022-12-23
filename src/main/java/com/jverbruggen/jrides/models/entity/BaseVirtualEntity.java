package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;

import java.util.UUID;

public abstract class BaseVirtualEntity implements VirtualEntity {
    protected PacketSender packetSender;

    protected UUID uuid;
    protected int entityId;
    protected Vector3 location;

    public BaseVirtualEntity(PacketSender packetSender, int entityId) {
        this.packetSender = packetSender;
        this.entityId = entityId;
        this.uuid = UUID.randomUUID();
        this.location = new Vector3(0,0,0);
    }

    @Override
    public Vector3 getLocation() {
        return location;
    }

    @Override
    public String getUniqueIdentifier() {
        return uuid.toString();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
