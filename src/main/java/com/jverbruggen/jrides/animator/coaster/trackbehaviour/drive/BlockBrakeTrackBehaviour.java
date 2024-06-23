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
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.result.BlockSectionSafetyResult;

import javax.annotation.Nullable;

public class BlockBrakeTrackBehaviour extends BaseTrackBehaviour {
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;

    private BlockBrakePhase phase;
    private final Frame stopFrame;
    private final boolean canSpawn;

    private final int minWaitTicks;
    private int minWaitTicksState;

    public BlockBrakeTrackBehaviour(CartMovementFactory cartMovementFactory, Frame stopFrame, boolean canSpawn, double driveSpeed, double deceleration, double acceleration, int minWaitTicks) {
        super(cartMovementFactory);
        this.deceleration = deceleration;
        this.acceleration = acceleration;
        this.driveSpeed = driveSpeed;
        this.phase = BlockBrakePhase.IDLE;
        this.stopFrame = stopFrame;
        this.canSpawn = canSpawn;

        this.minWaitTicks = minWaitTicks;
        this.minWaitTicksState = 0;

        trainExitedAtEnd(null, null);
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        Speed newSpeed = currentSpeed.clone();
        Train train = trainHandle.getTrain();

        boolean goIntoSwitch = true;
        while(goIntoSwitch){
            goIntoSwitch = false;
            switch (phase){
                case IDLE:
                    train.setStatusMessage("Idle");
                    if(getNextSectionSafety(train).safe() && minWaitTicks <= 0){
                        phase = BlockBrakePhase.DRIVING;
                        train.getNextSection().setEntireBlockReservation(train);
                    } else
                        phase = BlockBrakePhase.DRIVING_UNTIL_STOP;
                    goIntoSwitch = true;
                    break;
                case DRIVING_UNTIL_STOP:
                    train.setStatusMessage("Driving until stop");
                    if(train.getHeadSection().hasPassed(stopFrame, train.getHeadOfTrainFrame())){
                        if(getNextSectionSafety(train).safe() && minWaitTicks <= 0){
                            phase = BlockBrakePhase.DRIVING;
                            train.getNextSection().setEntireBlockReservation(train);
                        }else{
                            phase = BlockBrakePhase.STOPPING;
                        }
                        goIntoSwitch = true;
                    }else{
                        newSpeed.approach(acceleration, deceleration, driveSpeed);
                    }
                    break;
                case STOPPING:
                    train.setStatusMessage("Stopping" + newSpeed.getSpeedPerTick());
                    if(newSpeed.isZero()){
                        phase = BlockBrakePhase.WAITING;
                        goIntoSwitch = true;
                    }
                    newSpeed.minus(deceleration, 0);
                    break;
                case WAITING:
                    BlockSectionSafetyResult safetyResult = getNextSectionSafety(train);
                    train.setStatusMessage("Waiting \n" + train.getHeadSection() + "\n"
                        + train.getHeadSection().next(train) + " safety: " + safetyResult);
                    if(safetyResult.safe() && isMinWaitTimerFinished()){
                        train.getNextSection().setEntireBlockReservation(train);
                        resetMinWaitTimer();
                        phase = BlockBrakePhase.DRIVING;
                        goIntoSwitch = true;
                    }
                    break;
                case DRIVING:
                    train.setStatusMessage("Driving");
                    newSpeed.approach(acceleration, deceleration, driveSpeed);
                    break;
            }
        }

        return calculateTrainMovement(train, section, newSpeed);
    }

    private boolean isMinWaitTimerFinished(){
        if(minWaitTicks <= 0) return true;
        return minWaitTicksState++ >= minWaitTicks;
    }

    private void resetMinWaitTimer(){
        minWaitTicksState = 0;
    }

    @Override
    public void trainExitedAtStart(@Nullable Train train, @Nullable Section section) {

    }

    @Override
    public void trainExitedAtEnd(@Nullable Train train, @Nullable Section section){
        this.phase = BlockBrakePhase.IDLE;
    }

    @Override
    public String getName() {
        return "BlockBrake";
    }

    @Override
    public boolean canBlock() {
        return true;
    }

    @Override
    public boolean canSpawnOn() {
        return canSpawn;
    }

    @Override
    public Frame getSpawnFrame() {
        return stopFrame;
    }

    @Override
    protected void setParentTrackOnFrames(Track parentTrack) {
        stopFrame.setTrack(parentTrack);
    }
}

enum BlockBrakePhase{
    IDLE,
    DRIVING_UNTIL_STOP,
    STOPPING,
    WAITING,
    DRIVING
}