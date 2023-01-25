package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
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
    public TrainMovement move(Speed currentSpeed, Train train, Track track) {
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