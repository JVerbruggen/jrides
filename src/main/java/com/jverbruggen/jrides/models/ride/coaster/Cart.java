package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;

import java.util.List;

public interface Cart {
    List<Seat> getSeats();
    List<Player> getPassengers();
    int getMassMiddleOffset();
    Vector3 getTrackOffset();
    void setRotation(Vector3 rotation);
    void setPosition(Vector3 position);
}
