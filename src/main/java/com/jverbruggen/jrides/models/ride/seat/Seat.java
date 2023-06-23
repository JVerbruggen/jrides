package com.jverbruggen.jrides.models.ride.seat;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public interface Seat {
    Player getPassenger();
    void setPassenger(Player player);
    boolean hasPassenger();
    boolean ejectPassengerSoft(boolean teleport);

    Vector3 getOffset();
    void setLocation(Vector3 location, Quaternion orientation);

    VirtualEntity getEntity();
    void setRestraint(boolean locked);
    boolean restraintsActive();

    void setParentSeatHost(SeatHost seatHost);
    SeatHost getParentSeatHost();

    RideHandle getParentRideHandle();
}