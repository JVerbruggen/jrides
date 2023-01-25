package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class FreeMovementTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour {

    public FreeMovementTrackBehaviour(CartMovementFactory cartMovementFactory) {
        super(cartMovementFactory);
    }

    public TrainMovement move(Speed currentSpeed, Train train, Track track) {
        // --- Constants
        final double dragFactorPerTick = 0.9992;
        final double gravityAccelerationPerTick = 0.6;

        // --- New mass middle calculation
        Vector3 newHeadOfTrainLocation = track.getRawPositions().get(train.getHeadOfTrainFrame().getValue()).toVector3();
        Vector3 newMiddleLocation = track.getRawPositions().get(train.getMiddleOfTrainFrame().getValue()).toVector3();
        Vector3 newTailOfTrainLocation = track.getRawPositions().get(train.getTailOfTrainFrame().getValue()).toVector3();
        Vector3 newMassMiddle = Train.calculateMassMiddlePoint(newHeadOfTrainLocation, newMiddleLocation, newTailOfTrainLocation);

        // --- Gravity speed calculation
        Speed newSpeed = currentSpeed.clone();
        double dy = train.getMassMiddlePoint().getY() - newMassMiddle.getY(); // negative if going up
        newSpeed.add(dy*gravityAccelerationPerTick);
        newSpeed.multiply(dragFactorPerTick);
        if(newSpeed.getSpeedPerTick() < 1) newSpeed.setSpeedPerTick(1);

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

}
