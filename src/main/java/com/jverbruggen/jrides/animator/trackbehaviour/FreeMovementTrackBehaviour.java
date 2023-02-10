package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;

public class FreeMovementTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour {
    private final double gravityConstant;
    private final double dragConstant;

    public FreeMovementTrackBehaviour(CartMovementFactory cartMovementFactory, double gravityConstant, double dragConstant) {
        super(cartMovementFactory);

        this.gravityConstant = gravityConstant;
        this.dragConstant = dragConstant;
    }

    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        // --- Constants

        // --- New mass middle calculation
        Train train = trainHandle.getTrain();
        Vector3 newHeadOfTrainLocation = section.getLocationFor(train.getHeadOfTrainFrame());
        Vector3 newTailOfTrainLocation = section.getLocationFor(train.getTailOfTrainFrame());

        // --- Gravity speed calculation
        Speed newSpeed = currentSpeed.clone();
        double pitch = getGravityPitch(train, newHeadOfTrainLocation, newTailOfTrainLocation);

        double dy = Math.sin(pitch/180*3.141592);

        newSpeed.add(dy * this.gravityConstant);
        newSpeed.multiply(this.dragConstant);

        return calculateTrainMovement(train, section, newSpeed);
    }

    private double getGravityPitch(Train train, Vector3 newHeadOfTrainLocation, Vector3 newTailOfTrainLocation){
        if(train.getCarts().size() == 1){
            return -train.getCarts().get(0).getOrientation().getRoll();
        }else{
            Vector3 headTailDifference = Vector3.subtract(newHeadOfTrainLocation, newTailOfTrainLocation);
            return Quaternion.fromLookDirection(headTailDifference.toBukkitVector()).getPitch();
        }
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

    @Override
    protected void setParentTrackOnFrames(Track parentTrack) {

    }
}
