package com.jverbruggen.jrides.animator.trackbehaviour.transfer;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.BaseTrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class TrainDisplacerTransferTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour {
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;


    public TrainDisplacerTransferTrackBehaviour(CartMovementFactory cartMovementFactory, double driveSpeed, double deceleration, double acceleration) {
        super(cartMovementFactory);
        this.deceleration = deceleration;
        this.acceleration = acceleration;
        this.driveSpeed = driveSpeed;
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Track track) {
        Speed newSpeed = currentSpeed.clone();
        Train train = trainHandle.getTrain();

        newSpeed.approach(acceleration, deceleration, driveSpeed);

        return calculateTrainMovement(train, track, newSpeed);
    }

    @Override
    public void trainExitedAtStart() {

    }

    @Override
    public void trainExitedAtEnd(){

    }

    @Override
    public String getName() {
        return "BrakeAndDrive";
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
