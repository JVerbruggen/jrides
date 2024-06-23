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
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.ProximityUtils;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SectionSafetyProvider;
import com.jverbruggen.jrides.models.ride.section.result.BlockSectionSafetyResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ProximityDriveTrackBehaviour extends BaseTrackBehaviour implements SectionSafetyProvider {
    private final boolean canSpawn;
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;
    private final double minTrainDistance;
    private final Frame stopFrame;
    private final HashMap<Train, ProximityDrivePhase> trainPhases;
    private final List<Train> reservations;

    public ProximityDriveTrackBehaviour(CartMovementFactory cartMovementFactory, boolean canSpawn, double driveSpeed, double deceleration, double acceleration, double minTrainDistance, Frame stopFrame) {
        super(cartMovementFactory);
        this.canSpawn = canSpawn;
        this.deceleration = deceleration;
        this.acceleration = acceleration;
        this.driveSpeed = driveSpeed;
        this.minTrainDistance = minTrainDistance;
        this.stopFrame = stopFrame;
        this.trainPhases = new LinkedHashMap<>();
        this.reservations = new ArrayList<>();
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        Speed newSpeed = currentSpeed.clone();
        Train train = trainHandle.getTrain();
        if(!trainPhases.containsKey(train)){
            throw new RuntimeException("New occupation not handled correctly");
        }

        boolean goIntoSwitch = true;
        ProximityDrivePhase phase = trainPhases.get(train);
        while(goIntoSwitch){
            goIntoSwitch = false;

            switch (phase) {
                case DRIVING_UNTIL_STOP -> {
                    train.setStatusMessage("Driving until stop");
                    if (train.getHeadSection().hasPassed(stopFrame, train.getHeadOfTrainFrame())) {
                        if (getNextSectionSafety(train).safe()) {
                            phase = ProximityDrivePhase.DRIVING_TO_NEXT;
                            train.getNextSection().setEntireBlockReservation(train);
                        } else {
                            phase = ProximityDrivePhase.STOPPING;
                        }
                        goIntoSwitch = true;
                    } else {
                        boolean tooCloseToOther = this.isTooCloseToOtherTrains(train);

                        if(tooCloseToOther){
                            newSpeed.approach(acceleration, deceleration, 0);
                        }else{
                            newSpeed.approach(acceleration, deceleration, driveSpeed);
                        }
                    }
                }
                case STOPPING -> {
                    train.setStatusMessage("Stopping" + newSpeed.getSpeedPerTick());
                    if (newSpeed.isZero()) {
                        phase = ProximityDrivePhase.WAITING;
                        goIntoSwitch = true;
                    }
                    newSpeed.minus(deceleration, 0);
                }
                case WAITING -> {
                    train.setStatusMessage("Waiting \n" + train.getHeadSection() + "\n"
                            + train.getHeadSection().next(train));
                    if (getNextSectionSafety(train).safe()) {
                        train.getNextSection().setEntireBlockReservation(train);
                        phase = ProximityDrivePhase.DRIVING_TO_NEXT;
                        goIntoSwitch = true;
                    }
                }
                case DRIVING_TO_NEXT -> {
                    train.setStatusMessage("Driving");
                    newSpeed.approach(acceleration, deceleration, driveSpeed);
                }
            }
        }


        return calculateTrainMovement(train, section, newSpeed);
    }

    private boolean isTooCloseToOtherTrains(Train train){
        return ProximityUtils.isTooCloseToOtherTrains(train, trainPhases.keySet().stream().toList(), minTrainDistance);
    }

    @Override
    public void trainExitedAtStart(@Nullable Train train, @Nullable Section section) {
        trainExited(train);
    }

    @Override
    public void trainExitedAtEnd(@Nullable Train train, @Nullable Section section){
        trainExited(train);
    }

    private void trainExited(@Nullable Train train){
        if(train == null) return;
        trainPhases.remove(train);
        reservations.remove(train);
    }

    @Override
    public String getName() {
        return "ProximityDrive";
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

    @Override
    public boolean canHandleBlockSectionSafety() {
        return true;
    }

    @Override
    public BlockSectionSafetyResult getEnteringSafety(Train train, Section nextSection) {
//        BlockSectionSafetyResult nextSectionSafety = nextSection.getBlockSectionSafety(train);
//        if()
        return new BlockSectionSafetyResult(true, train, "");
    }

    @Override
    public boolean canHandleOccupation(@Nonnull Train train) {
        if(reservations.contains(train)) return true;
        if(trainPhases.containsKey(train)) return true;

        return !ProximityUtils.isTooCloseToOtherTrains(train, trainPhases.keySet().stream().toList(), minTrainDistance);
    }

    @Override
    public void handleNewReservation(@Nonnull Train train) {
        reservations.add(train);
    }

    @Override
    public void handleClearReservation(@Nonnull Train train) {
        reservations.remove(train);
    }

    @Override
    public Train getReservation() {
        return null;
    }

    @Override
    public void handleNewOccupation(@Nonnull Train train) {
        trainPhases.put(train, ProximityDrivePhase.DRIVING_UNTIL_STOP);
    }

    @Override
    public void handleClearOccupation(@Nonnull Train train) {

    }

    @Override
    public boolean isReservedBy(@Nonnull Train train) {
        return isOccupiedBy(train);
    }

    @Override
    public boolean isOccupied() {
        return !trainPhases.isEmpty();
    }

    @Override
    public boolean isOccupiedBy(@Nonnull Train train) {
        return trainPhases.containsKey(train);
    }
}

enum ProximityDrivePhase{
    DRIVING_UNTIL_STOP,
    STOPPING,
    WAITING,
    DRIVING_TO_NEXT
}
