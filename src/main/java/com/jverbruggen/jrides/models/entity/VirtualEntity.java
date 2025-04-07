/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.event.action.RideAction;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.CanSpawn;
import com.jverbruggen.jrides.models.ride.Has6DOFPosition;
import com.jverbruggen.jrides.models.ride.seat.Seat;

import java.util.List;
import java.util.UUID;

public interface VirtualEntity extends Has6DOFPosition, CanSpawn {
    String getUniqueIdentifier();
    UUID getUUID();
    int getEntityId();

    Player getPassenger();
    boolean allowsPassenger();
    boolean hasPassenger();
    void setPassenger(Player player);

    Vector3 getLocation();
    Quaternion getRotation();

    double getYaw();

    /**
     * Set location for virtual entity.
     * Location should be without any corrections.
     * @param location Position without any corrections regarding implementation.
     */
    void setLocation(Vector3 location);


    void setRotation(Quaternion orientation);
    List<Player> getViewers();
    void addViewer(Player player);
    void removeViewer(Player player);
    boolean isViewer(Player player);
    void spawnFor(Player player);
    void spawnForAll(List<Player> players, boolean hard);
    void despawnFor(Player player, boolean unview);
    void despawn();
    boolean shouldRenderFor(Player player);
    boolean isAlive();
    void setHostSeat(Seat seat);
    Seat getHostSeat();
    void setModel(TrainModelItem model);
    void setCustomName(String name);
    void setCustomAction(RideAction rideAction);
    boolean hasCustomAction();
    void runCustomAction(Player player);
    void setBelongsToRide(RideHandle rideHandle);

    void setRendered(boolean render);
    boolean isRendered();
}
