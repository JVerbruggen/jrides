package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class FreeMovementTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour {
    private final double gravityConstant;
    private final double dragConstant;

    public FreeMovementTrackBehaviour(CartMovementFactory cartMovementFactory, double gravityConstant, double dragConstant) {
        super(cartMovementFactory);

        this.gravityConstant = gravityConstant;
        this.dragConstant = dragConstant;
    }

    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Track track) {
        // --- Constants

        // --- New mass middle calculation
        Train train = trainHandle.getTrain();
        Vector3 newHeadOfTrainLocation = track.getRawPositions().get(train.getHeadOfTrainFrame().getValue()).toVector3();
        Vector3 newMiddleLocation = track.getRawPositions().get(train.getMiddleOfTrainFrame().getValue()).toVector3();
        Vector3 newTailOfTrainLocation = track.getRawPositions().get(train.getTailOfTrainFrame().getValue()).toVector3();
        Vector3 newMassMiddle = Train.calculateMassMiddlePoint(newHeadOfTrainLocation, newMiddleLocation, newTailOfTrainLocation);

        // --- Gravity speed calculation
        Speed newSpeed = currentSpeed.clone();
        double dy = train.getMassMiddlePoint().getY() - newMassMiddle.getY(); // negative if going up
        newSpeed.add(dy * this.gravityConstant);
        newSpeed.multiply(this.dragConstant);
        if(newSpeed.getSpeedPerTick() < 0) newSpeed.setSpeedPerTick(0);

        return calculateTrainMovement(train, track, newSpeed);
    }

    @Override
    public void trainExitedAtStart() {

    }

    @Override
    public void trainExitedAtEnd() {

    }

    @Override
    public String getName() {
        return "FreeMovement";
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

}
