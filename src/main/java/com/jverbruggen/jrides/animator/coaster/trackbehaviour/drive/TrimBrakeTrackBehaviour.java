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

package com.jverbruggen.jrides.animator.coaster.trackbehaviour.drive;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.BaseTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.FreeMovementTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;

import javax.annotation.Nullable;

public class TrimBrakeTrackBehaviour extends BaseTrackBehaviour {
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
    public void trainExitedAtStart(@Nullable Train train, @Nullable Section section) {

    }

    @Override
    public void trainExitedAtEnd(@Nullable Train train, @Nullable Section section){

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
