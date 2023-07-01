package com.jverbruggen.jrides.animator.coaster.trackbehaviour.drive;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.BaseTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.FreeMovementTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;

public class TrimBrakeTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour {
    private final double gravityConstant;
    private final double dragConstantWithTrim;

    public TrimBrakeTrackBehaviour(CartMovementFactory cartMovementFactory, double gravityConstant, double dragConstantWithTrim) {
        super(cartMovementFactory);
        this.gravityConstant = gravityConstant;
        this.dragConstantWithTrim = dragConstantWithTrim;
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        Train train = trainHandle.getTrain();

        Speed newSpeed = FreeMovementTrackBehaviour.calculateGravityActedSpeed(
                trainHandle, section, currentSpeed, gravityConstant, dragConstantWithTrim
        );

        return calculateTrainMovement(train, section, newSpeed);
    }

    @Override
    public void trainExitedAtStart() {

    }

    @Override
    public void trainExitedAtEnd(){

    }

    @Override
    public String getName() {
        return "TrimBrake";
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
