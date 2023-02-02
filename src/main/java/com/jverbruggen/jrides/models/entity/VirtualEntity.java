package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.List;
import java.util.UUID;

public interface VirtualEntity {
    String getUniqueIdentifier();
    UUID getUUID();
    int getEntityId();

    Player getPassenger();
    boolean allowsPassenger();
    boolean hasPassenger();
    void setPassenger(Player player);

    Vector3 getLocation();
    double getYaw();
    void setLocation(Vector3 location, Quaternion orientation);
    List<Player> getViewers();
    void addViewer(Player player);
    void removeViewer(Player player);
    boolean isViewer(Player player);
    void spawnFor(Player player);
    void spawnForAll(List<Player> players);
    void despawnFor(Player player);
    void despawn();
    boolean shouldRenderFor(Player player);
}
