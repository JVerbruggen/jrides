package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;


public class FullStopAndGoTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour{
    private final int stopTime;
    private Phase phase;
    private int stopTimeCounter;

    public FullStopAndGoTrackBehaviour(CartMovementFactory cartMovementFactory, int stopTime) {
        super(cartMovementFactory);

        this.stopTime = stopTime;
        trainExitedAtEnd();
    }

    @Override
    public TrainMovement move(Speed currentSpeed, Vector3 currentLocation, Train train, Track track) {
//        Bukkit.broadcastMessage("Brake " + phase.toString());
        Speed newSpeed = currentSpeed.clone();

        final double deceleration = 0.5;
        final double acceleration = 0.1;

        switch (phase){
            case STOPPING:
                if(currentSpeed.is(0)) phase = Phase.STOPPED;
                newSpeed.minus(deceleration, 0);
                break;
            case STOPPED:
                if(stopTimeCounter <= 0) phase = Phase.LEAVING;
                stopTimeCounter--;
                break;
            case LEAVING:
                newSpeed.add(acceleration, 1.0);
                break;
        }

        return calculateTrainMovement(train, track, newSpeed);
//
//        Frame newHeadOfTrainFrame = train.getHeadOfTrainFrame().clone().add(currentSpeed.getFrameIncrement());
//        Vector3 newTrainLocation = track.getRawPositions().get(train.getMassMiddleFrame().getValue()).toVector3();
//
//        HashMap<Cart, CartMovement> cartMovements = cartMovementFactory.createOnTrackCartMovement(train.getCarts(), track);
//
//        return new TrainMovement(newSpeed, newHeadOfTrainFrame, newTrainLocation, cartMovements);
    }

    @Override
    public void trainExitedAtStart() {

    }

    @Override
    public void trainExitedAtEnd(){
        this.phase = Phase.STOPPING;
        this.stopTimeCounter = this.stopTime;
    }

    @Override
    public String getName() {
        return "FullStopAndGo";
    }

    @Override
    public boolean canBlock() {
        return false;
    }
}

enum Phase{
    STOPPING,
    STOPPED,
    LEAVING
}