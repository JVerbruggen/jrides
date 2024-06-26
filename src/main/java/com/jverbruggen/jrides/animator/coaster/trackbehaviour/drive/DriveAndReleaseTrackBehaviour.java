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

enum DriveAndReleasePhase{
    IDLE,
    DRIVING,
    STOPPING,
    WAITING,
    RELEASE,
}

public class DriveAndReleaseTrackBehaviour extends BaseTrackBehaviour {
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;
    private DriveAndReleasePhase phase;
    private final Frame stopFrame;
    private final int minWaitTicks;
    private int minWaitTicksState;
    private final double gravityConstant;
    private final double dragConstant;

    public DriveAndReleaseTrackBehaviour(CartMovementFactory cartMovementFactory, double driveSpeed, double deceleration, double acceleration, Frame stopFrame, int minWaitTicks, double gravityConstant, double dragConstant) {
        super(cartMovementFactory);
        this.deceleration = deceleration;
        this.acceleration = acceleration;
        this.driveSpeed = driveSpeed;
        this.stopFrame = stopFrame;
        this.minWaitTicks = minWaitTicks;
        this.gravityConstant = gravityConstant;
        this.dragConstant = dragConstant;
        this.minWaitTicksState = 0;
        this.phase = DriveAndReleasePhase.IDLE;
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        Speed newSpeed = currentSpeed.clone();
        Train train = trainHandle.getTrain();

        boolean goIntoSwitch = true;
        while(goIntoSwitch){
            goIntoSwitch = false;
            switch (phase){
                case IDLE -> {
                    minWaitTicksState = 0;
                    phase = DriveAndReleasePhase.DRIVING;
                    goIntoSwitch = true;
                }
                case DRIVING -> {
                    if(shouldStop(train)){
                        phase = DriveAndReleasePhase.STOPPING;
                        goIntoSwitch = true;
                    }else{
                        newSpeed.approach(acceleration, deceleration, driveSpeed);
                    }
                }
                case STOPPING -> {
                    newSpeed.approach(acceleration, deceleration, 0);
                    if(newSpeed.isZero()){
                        phase = DriveAndReleasePhase.WAITING;
                        goIntoSwitch = true;
                    }
                }
                case WAITING -> {
                    if(++minWaitTicksState >= minWaitTicks){
                        phase = DriveAndReleasePhase.RELEASE;
                        goIntoSwitch = true;
                    }
                }
                case RELEASE -> newSpeed = FreeMovementTrackBehaviour.calculateGravityActedSpeed(
                        trainHandle, section, currentSpeed, gravityConstant, dragConstant
                );
            }
        }

        return calculateTrainMovement(train, section, newSpeed);
    }

    private boolean shouldStop(Train train){
        if (driveSpeed >= 0)
            return train.getHeadSection().hasPassed(stopFrame, train.getHeadOfTrainFrame());
        return train.getHeadSection().hasPassedInverse(stopFrame, train.getHeadOfTrainFrame());
    }

    private void trainExited(){
        this.phase = DriveAndReleasePhase.IDLE;
        minWaitTicksState = 0;
    }

    @Override
    public void trainExitedAtStart(@Nullable Train train, @Nullable Section section) {
        trainExited();
    }

    @Override
    public void trainExitedAtEnd(@Nullable Train train, @Nullable Section section){
        trainExited();
    }

    @Override
    public String getName() {
        return "DriveAndRelease";
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
