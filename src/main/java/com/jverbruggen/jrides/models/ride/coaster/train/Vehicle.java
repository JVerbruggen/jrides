package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.models.entity.Player;

import java.util.List;

public interface Vehicle {
    List<Player> getPassengers();

    boolean isStationary();

    void ejectPassengers();

    void playRestraintOpenSound();

    void playRestraintCloseSound();

    void playDispatchSound();
}
