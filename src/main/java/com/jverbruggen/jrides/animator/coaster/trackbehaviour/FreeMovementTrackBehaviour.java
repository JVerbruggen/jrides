/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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
