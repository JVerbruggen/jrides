package com.jverbruggen.jrides.animator.trackbehaviour.brake;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.BaseTrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;


public class FullStopAndGoTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour {
    private final int stopTime;
    private Phase phase;
    private int stopTimeCounter;

    public FullStopAndGoTrackBehaviour(CartMovementFactory cartMovementFactory, int stopTime) {
        super(cartMovementFactory);

        this.stopTime = stopTime;
        trainExitedAtEnd();
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
//        Bukkit.broadcastMessage("Brake " + phase.toString());
        Speed newSpeed = currentSpeed.clone();
        Train train = trainHandle.getTrain();

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

        return calculateTrainMovement(train, section, newSpeed);
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

    @Override
    public boolean canSpawnOn() {
        return false;
    }

    @Override
    public Frame getSpawnFrame() {
        return null;
    }

    @Override
    protected void setParentTrackOnFrames(Track parentTrack) {

    }
}

enum Phase{
    STOPPING,
    STOPPED,
    LEAVING
}