package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Vector3;

public interface VirtualEntity {
    String getUniqueIdentifier();
    Player getPassenger();
    void setPassenger(Player player);
    Vector3 getLocation();
}
