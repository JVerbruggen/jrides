package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Vector3;

import java.util.List;
import java.util.UUID;

public interface VirtualEntity {
    String getUniqueIdentifier();
    UUID getUUID();
    Player getPassenger();
    void setPassenger(Player player);
    Vector3 getLocation();
    void setLocation(Vector3 location);
    List<Player> getViewers();
    void addViewer(Player player);
    void removeViewer(Player player);
    boolean isViewer(Player player);
    void spawnFor(Player player);
    void spawnForAll(List<Player> players);
    void despawnFor(Player player);
    void despawn();
}
