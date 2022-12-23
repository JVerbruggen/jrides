package com.jverbruggen.jrides.packets.options;

import java.util.UUID;

public class SpawnVirtualEntityPacketOptions {
    private int entityId;
    private int entityType;
    private double locationX;
    private double locationY;
    private double locationZ;
    private double yawRotation;
    private UUID uuid;

    public int getEntityId() {
        return entityId;
    }

    public int getEntityType() {
        return entityType;
    }

    public double getLocationX() {
        return locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public double getLocationZ() {
        return locationZ;
    }

    public double getYawRotation() {
        return yawRotation;
    }

    public UUID getUuid() {
        return uuid;
    }
}
