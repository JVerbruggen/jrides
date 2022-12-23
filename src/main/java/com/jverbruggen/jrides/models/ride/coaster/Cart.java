package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Seat;

import java.util.List;

public interface Cart {
    List<Seat> getSeats();
    List<Player> getPassengers();
}
