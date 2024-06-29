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

package com.jverbruggen.jrides.animator.coaster.trackbehaviour.station;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.BaseTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.FreeMovementTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.trigger.SimpleDispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.event.player.PlayerFinishedRideEvent;
import com.jverbruggen.jrides.event.ride.RideFinishedEvent;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.models.entity.Passenger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.result.BlockSectionSafetyResult;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

public class StationTrackBehaviour extends BaseTrackBehaviour {
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;

    private final CoasterHandle coasterHandle;
    private final JRidesLogger logger;
    private Train handlingTrain;
    private StationPhase phase;
    private final Frame stopFrame;
    private final boolean canSpawn;
    private final TriggerContext triggerContext;
    private final CoasterStationHandle stationHandle;
    private final DispatchLock trainInStationDispatchLock;
    private final DispatchLock blockSectionOccupiedDispatchLock;
    private final DispatchLock restraintsLock;

    private boolean stopping;
    private boolean dispatching;

    private final int passThroughCount;
    private int passThroughCountState;
    private final boolean forwardsDispatch;
    private final byte dispatchSpeedMultiplier;

    public StationTrackBehaviour(CoasterHandle coasterHandle, CartMovementFactory cartMovementFactory, Frame stopFrame, boolean canSpawn, TriggerContext triggerContext,
                                 CoasterStationHandle stationHandle, DispatchLock trainInStationDispatchLock, DispatchLock blockSectionOccupiedDispatchLock,
                                 DispatchLock restraintsLock, double driveSpeed, double acceleration, double deceleration, boolean forwardsDispatch, int passThroughCount) {
        super(cartMovementFactory);
        this.coasterHandle = coasterHandle;
        this.logger = JRidesPlugin.getLogger();
        this.driveSpeed = driveSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.handlingTrain = null;
        this.phase = StationPhase.IDLE;
        this.stopFrame = stopFrame;
        this.canSpawn = canSpawn;
        this.triggerContext = triggerContext;
        this.stationHandle = stationHandle;
        this.dispatching = false;
        this.stopping = false;
        this.passThroughCount = passThroughCount;
        this.passThroughCountState = passThroughCount;

        this.forwardsDispatch = forwardsDispatch;
        this.dispatchSpeedMultiplier = (byte) (forwardsDispatch ? 1 : -1);

        this.trainInStationDispatchLock = trainInStationDispatchLock;
        this.blockSectionOccupiedDispatchLock = blockSectionOccupiedDispatchLock;
        this.restraintsLock = restraintsLock;

        trainExitedAtEnd(null, null);
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        Speed newSpeed = currentSpeed.clone();
        Train train = trainHandle.getTrain();

        if(train == null) throw new RuntimeException("Train was null in StationTrackBehaviour");

        if(handlingTrain != null && !train.equals(handlingTrain)){
            logger.warning("Train " + train.getName() + " has entered station, train is blocked");
            return null;
        }

        boolean goIntoSwitch = true;
        while(goIntoSwitch){
            goIntoSwitch = false;
            switch (phase) {
                case IDLE -> {
                    handlingTrain = train;
                    if (shouldPassThrough()) {
                        phase = StationPhase.PASSING_THROUGH;
                        passThroughCountState++;
                    } else if (isTrainSpeedPositive(trainHandle)) {
                        phase = StationPhase.ARRIVING;
                    } else {
                        phase = StationPhase.REVERSING;
                    }
                    goIntoSwitch = true;
                }
                case PASSING_THROUGH -> newSpeed = FreeMovementTrackBehaviour.calculateGravityActedSpeed(
                        trainHandle, section, currentSpeed, coasterHandle.getGravityConstant(), coasterHandle.getDragConstant()
                );
                case REVERSING -> {
                    if (isTrainSpeedPositive(trainHandle)) {
                        phase = StationPhase.ARRIVING;
                        goIntoSwitch = true;
                    } else {
                        newSpeed.approach(deceleration, acceleration, driveSpeed);
                    }
                }
                case ARRIVING -> {
                    if (train.getHeadSection().hasPassed(stopFrame, train.getHeadOfTrainFrame())) {
                        phase = StationPhase.STOPPING;
                        goIntoSwitch = true;
                    } else {
                        newSpeed.approach(acceleration, deceleration, driveSpeed);
                    }
                }
                case STOPPING -> {
                    if (newSpeed.isZero()) {
                        if (!stopping) stationHandle.runEntryEffectTriggers(train);
                        stopping = true;

                        if (!stationHandle.entryEffectTriggersDone()) break;

                        phase = StationPhase.STATIONARY;
                        stationHandle.setStationaryTrain(train);

                        if (stationHandle.isExit()) {
                            PlayerFinishedRideEvent.sendFinishedRideEvent(train.getPassengers()
                                    .stream()
                                    .map(p -> (JRidesPlayer) p.getPlayer())
                                    .collect(Collectors.toList()), coasterHandle.getRide());
                            RideFinishedEvent.send(coasterHandle.getRide(), train.getPassengers().stream().map(Passenger::getPlayer).toList());
                        }

                        coasterHandle.getRideController().onVehicleArrive(train, stationHandle);
                        trainInStationDispatchLock.unlock();
                        restraintsLock.setLocked(true);
                        train.setRestraintForAll(false);

                        if (stationHandle.shouldEject())
                            train.ejectPassengers();

                        stopping = false;
                        goIntoSwitch = true;
                    } else
                        newSpeed.minus(deceleration, 0);
                }
                case STATIONARY -> {
                    BlockSectionSafetyResult safety = getNextSectionSafety(train);
                    if (safety.safe()) {
                        blockSectionOccupiedDispatchLock.unlock();
                    } else {
                        blockSectionOccupiedDispatchLock.lock();
                        blockSectionOccupiedDispatchLock.setDebugStatus(safety.reason());
                    }
                    SimpleDispatchTrigger dispatchTrigger = triggerContext.getDispatchTrigger();
                    if (dispatching || dispatchTrigger.isActive()) {
                        trainInStationDispatchLock.lock();
                        dispatchTrigger.reset();

                        if (!dispatching) stationHandle.runExitEffectTriggers(train);
                        dispatching = true;

                        if (!stationHandle.exitEffectTriggersDone()) break;

                        trainHandle.resetEffects();
                        passThroughCountState = 0;

                        phase = StationPhase.WAITING;
                        dispatching = false;
                        goIntoSwitch = true;
                    }
                }
                case WAITING -> {
                    Section nextSection = getNextSection(train);
                    if (nextSection != null && nextSection.getBlockSectionSafety(train).safe()) {
                        nextSection.setEntireBlockReservation(train);
                        phase = StationPhase.DEPARTING;
                        blockSectionOccupiedDispatchLock.lock();
                        coasterHandle.getRideController().onVehicleDepart(train, stationHandle);
                        stationHandle.setStationaryTrain(null);
                        train.playDispatchSound();
                        goIntoSwitch = true;
                    }
                }
                case DEPARTING -> newSpeed.approach(
                        acceleration,
                        acceleration,
                        driveSpeed*dispatchSpeedMultiplier);
            }
        }

        return calculateTrainMovement(train, section, newSpeed);
    }

    private Section getNextSection(Train train){
        if(forwardsDispatch) return train.getNextSection();
        return train.getHeadSection().previous(train);
    }

    private boolean isTrainSpeedPositive(TrainHandle trainHandle){
        return trainHandle.getSpeed().isPositive();
    }

    private boolean shouldPassThrough(){
        return this.passThroughCountState < this.passThroughCount;
    }

    private void trainExited(){
        this.phase = StationPhase.IDLE;
        this.handlingTrain = null;
        this.stationHandle.setStationaryTrain(null);
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
        return "Station";
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

