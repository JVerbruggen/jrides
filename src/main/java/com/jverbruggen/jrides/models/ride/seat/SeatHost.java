package com.jverbruggen.jrides.models.ride.seat;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.seat.Seat;

import java.util.List;

public interface SeatHost {
    List<Seat> getSeats();
    List<Player> getPassengers();
    void ejectPassengers();
    void despawn();
    void setRestraint(boolean locked);
    boolean getRestraintState();
    PlayerLocation getEjectLocation();
    RideHandle getRideHandle();
    void onPlayerEnter(Player player);
    void onPlayerExit(Player player);
}
