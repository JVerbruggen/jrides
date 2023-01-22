package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.entity.Player;

public interface Seat {
    Player getPassenger();
    void setPassenger(Player player);
    boolean hasPassenger();
}
