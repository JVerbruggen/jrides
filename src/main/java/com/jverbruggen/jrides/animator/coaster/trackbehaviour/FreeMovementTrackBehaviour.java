package com.jverbruggen.jrides.animator.coaster.trackbehaviour;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;

import javax.annotation.Nullable;

public class FreeMovementTrackBehaviour extends BaseTrackBehaviour {
    private final double gravityConstant;
    private final double dragConstant;

    public FreeMovementTrackBehaviour(CartMovementFactory cartMovementFactory, double gravityConstant, double dragConstant) {
        super(cartMovementFactory);

        this.gravityConstant = gravityConstant;
        this.dragConstant = dragConstant;
    }

    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        Train train = trainHandle.getTrain();
        Speed newSpeed = calculateGravityActedSpeed(trainHandle, section, currentSpeed, gravityConstant, dragConstant);

        return calculateTrainMovement(train, section, newSpeed);
    }

    @Override
    public void trainExitedAtStart(@Nullable Train train, @Nullable Section section) {

    }

    @Override
    public void trainExitedAtEnd(@Nullable Train train, @Nullable Section section) {

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

    private static double getGravityPitch(Train train, Vector3 newHeadOfTrainLocation, Vector3 newTailOfTrainLocation){
        if(train.getCarts().size() == 1){
            int forwardsMulti = -1;
            if(!train.isFacingForwards())
                forwardsMulti = 1;
            return forwardsMulti * train.getCarts().get(0).getOrientation().getRoll();
        }else{
            Vector3 headTailDifference = Vector3.subtract(newHeadOfTrainLocation, newTailOfTrainLocation);
            return Quaternion.fromLookDirection(headTailDifference.toBukkitVector()).getPitch();
        }
    }

    public static Speed calculateGravityActedSpeed(TrainHandle trainHandle, Section section, Speed currentSpeed, double gravityConstant, double dragConstant){
        // --- New mass middle calculation
        Train train = trainHandle.getTrain();
        Section backFacingSection = train.getBackFacingTrainFrame().getSection();
        Vector3 newForwardsFacingFrameLocation = section.getLocationFor(train.getFrontFacingTrainFrame());
        Vector3 newBackwardsFacingFrameLocation = backFacingSection.getLocationFor(train.getBackFacingTrainFrame());

        // --- Gravity speed calculation
        Speed newSpeed = currentSpeed.clone();
        double pitch = getGravityPitch(train, newForwardsFacingFrameLocation, newBackwardsFacingFrameLocation);

        double dy = Math.sin(pitch/180*3.141592);

        newSpeed.add(dy * gravityConstant);
        newSpeed.multiply(dragConstant);

        return newSpeed;
    }
}
