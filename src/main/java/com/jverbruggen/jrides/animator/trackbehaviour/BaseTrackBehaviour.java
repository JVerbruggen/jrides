package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Cart;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;

import java.util.HashMap;

public abstract class BaseTrackBehaviour implements TrackBehaviour {
    protected final CartMovementFactory cartMovementFactory;

    protected BaseTrackBehaviour(CartMovementFactory cartMovementFactory) {
        this.cartMovementFactory = cartMovementFactory;
    }

    protected TrainMovement calculateTrainMovement(Train train, Track track, Speed speed){
        if(speed.is(0)){
            return new TrainMovement(speed, train.getHeadOfTrainFrame(), train.getTailOfTrainFrame(), train.getCurrentLocation(), null);
        }

        Frame newHeadOfTrainFrame = train.getHeadOfTrainFrame().clone().add(speed.getFrameIncrement());
        Frame newTailOfTrainFrame = train.getTailOfTrainFrame().clone().add(speed.getFrameIncrement());
        Vector3 newTrainLocation = track.getRawPositions().get(train.getMiddleOfTrainFrame().getValue()).toVector3();

        HashMap<Cart, CartMovement> cartMovements = cartMovementFactory.createOnTrackCartMovement(train.getCarts(), track);

        return new TrainMovement(speed, newHeadOfTrainFrame, newTailOfTrainFrame, newTrainLocation, cartMovements);
    }

}
