package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Vector3;

import java.util.UUID;

public interface VirtualEntity {
    String getUniqueIdentifier();
    UUID getUUID();
    Player getPassenger();
    void setPassenger(Player player);
    Vector3 getLocation();
    void spawnFor(Player player);
}
