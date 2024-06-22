package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.event.action.RideAction;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.seat.Seat;

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
    Quaternion getRotation();

    double getYaw();
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
