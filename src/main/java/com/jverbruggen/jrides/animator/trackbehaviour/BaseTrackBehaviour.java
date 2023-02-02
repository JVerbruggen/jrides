package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.CyclicFrame;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.HashMap;

public abstract class BaseTrackBehaviour implements TrackBehaviour {
    protected final CartMovementFactory cartMovementFactory;
    protected Track parentTrack;

    protected BaseTrackBehaviour(CartMovementFactory cartMovementFactory) {
        this.cartMovementFactory = cartMovementFactory;
        this.parentTrack = null;
    }

    protected TrainMovement calculateTrainMovement(Train train, Track track, Speed speed){
        if(speed.is(0)){
            return new TrainMovement(speed, train.getHeadOfTrainFrame(), train.getTailOfTrainFrame(), train.getCurrentLocation(), null);
        }

        Frame newHeadOfTrainFrame = train.getHeadOfTrainFrame().clone().add(speed.getFrameIncrement());
        Frame newTailOfTrainFrame = train.getTailOfTrainFrame().clone().add(speed.getFrameIncrement());
        Vector3 newTrainLocation = track.getLocationFor(train.getMiddleOfTrainFrame());

        HashMap<Cart, CartMovement> cartMovements = cartMovementFactory.createOnTrackCartMovement(train.getCarts(), track);

        return new TrainMovement(speed, newHeadOfTrainFrame, newTailOfTrainFrame, newTrainLocation, cartMovements);
    }

    protected abstract void setParentTrackOnFrames(Track parentTrack);

    @Override
    public void setParentTrack(Track parentTrack) {
        this.parentTrack = parentTrack;
        setParentTrackOnFrames(parentTrack);
    }

    @Override
    public Track getParentTrack() {
        return parentTrack;
    }

    @Override
    public boolean definesAdjacentSections() {
        return false;
    }

    @Override
    public Section getSectionAtEnd() {
        return null;
    }

    @Override
    public Section getSectionAtStart() {
        return null;
    }
}
