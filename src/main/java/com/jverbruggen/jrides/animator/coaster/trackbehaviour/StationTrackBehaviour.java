package com.jverbruggen.jrides.animator.coaster.trackbehaviour;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.event.player.PlayerFinishedRideEvent;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.result.BlockSectionSafetyResult;

import java.util.stream.Collectors;

public class StationTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour{
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
                                 DispatchLock restraintsLock, double driveSpeed, boolean forwardsDispatch, int passThroughCount) {
        super(cartMovementFactory);
        this.coasterHandle = coasterHandle;
        this.logger = JRidesPlugin.getLogger();
        this.deceleration = 0.2;
        this.acceleration = 0.1;
        this.driveSpeed = driveSpeed;
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

        trainExitedAtEnd();
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        Speed newSpeed = currentSpeed.clone();
        Train train = trainHandle.getTrain();

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
                        newSpeed.approach(acceleration, deceleration, driveSpeed);
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

                        if (stationHandle.isExit())
                            PlayerFinishedRideEvent.sendFinishedRideEvent(train.getPassengers()
                                    .stream()
                                    .map(p -> (JRidesPlayer) p)
                                    .collect(Collectors.toList()), coasterHandle.getRide());

                        coasterHandle.getRideController().onTrainArrive(train, stationHandle);
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
                    DispatchTrigger dispatchTrigger = triggerContext.getDispatchTrigger();
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
                        coasterHandle.getRideController().onTrainDepart(train, stationHandle);
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
    public void trainExitedAtStart() {
        trainExited();
    }

    @Override
    public void trainExitedAtEnd(){
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

