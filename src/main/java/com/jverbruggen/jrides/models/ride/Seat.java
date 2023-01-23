package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;

public interface Seat {
    Player getPassenger();
    void setPassenger(Player player);
    boolean hasPassenger();

    Vector3 getOffset();
    void setLocation(Vector3 location);


}
