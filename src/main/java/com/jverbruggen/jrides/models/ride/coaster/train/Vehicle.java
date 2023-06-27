package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.List;

public interface Vehicle {
    List<Player> getPassengers();

    boolean isStationary();

    boolean getRestraintState();
    void setRestraintForAll(boolean locked);

    void ejectPassengers();

    void playRestraintOpenSound();

    void playRestraintCloseSound();

    void playDispatchSound();

    String getName();

    void setCrashed(boolean crashed);
    boolean isCrashed();
    Vector3 getCurrentLocation();
}
