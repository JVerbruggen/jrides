package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Cart;
import com.jverbruggen.jrides.models.ride.coaster.Track;

import java.util.List;

public interface TrackBehaviour {
    TrainMovement move(Frame currentMassMiddleFrame, Speed currentSpeed, Vector3 currentLocation, List<Cart> carts, Track track);
    void trainExitedAtStart();
    void trainExitedAtEnd();
}
