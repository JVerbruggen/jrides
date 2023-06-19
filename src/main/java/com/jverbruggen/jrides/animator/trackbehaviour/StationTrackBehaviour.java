package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
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
    private final double passThroughSpeed;
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;

    private final CoasterHandle coasterHandle;
    private final JRidesLogger logger;
    private Train handlingTrain;
    private StationPhase phase;
    private final Frame stopFrame;
    private final boolean canSpawn;
    private TriggerContext triggerContext;
    private final CoasterStationHandle stationHandle;
    private final DispatchLock trainInStationDispatchLock;
    private final DispatchLock blockSectionOccupiedDispatchLock;
    private final DispatchLock restraintsLock;
    private boolean stopping;
    private boolean dispatching;

    public StationTrackBehaviour(CoasterHandle coasterHandle, CartMovementFactory cartMovementFactory, Frame stopFrame, boolean canSpawn, TriggerContext triggerContext,
                                 CoasterStationHandle stationHandle, DispatchLock trainInStationDispatchLock, DispatchLock blockSectionOccupiedDispatchLock,
                                 DispatchLock restraintsLock, double driveSpeed) {
        super(cartMovementFactory);
        this.coasterHandle = coasterHandle;
        this.logger = JRidesPlugin.getLogger();
        this.passThroughSpeed = 1.0;
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
            switch (phase){
                case IDLE:
                    phase = StationPhase.ARRIVING;
                    handlingTrain = train;
                    goIntoSwitch = true;
                    break;
                case ARRIVING:
                    if(train.getHeadSection().hasPassed(stopFrame, train.getHeadOfTrainFrame())){
                        phase = StationPhase.STOPPING;
                        goIntoSwitch = true;
                    }else{
                        newSpeed.approach(acceleration, deceleration, 1.0);
                    }
                    break;
                case STOPPING:
                    if(newSpeed.isZero()) {
                        if(!stopping) stationHandle.runEntryEffectTriggers(train);
                        stopping = true;

                        if(!stationHandle.entryEffectTriggersDone()) break;

                        phase = StationPhase.STATIONARY;
                        stationHandle.setStationaryTrain(train);

                        if(stationHandle.isExit())
                            PlayerFinishedRideEvent.sendFinishedRideEvent(train.getPassengers()
                                    .stream()
                                    .map(p -> (JRidesPlayer)p)
                                    .collect(Collectors.toList()), coasterHandle.getRide());

                        coasterHandle.getRideController().onTrainArrive(train, stationHandle);
                        trainInStationDispatchLock.unlock();
                        restraintsLock.setLocked(true);
                        train.setRestraintForAll(false);

                        if(stationHandle.shouldEject())
                            train.ejectPassengers();

                        stopping = false;
                        goIntoSwitch = true;
                    }else
                        newSpeed.minus(deceleration, 0);
                    break;
                case STATIONARY:
                    BlockSectionSafetyResult safety = getNextSectionSafety(train);
                    if(safety.safe()){
                        blockSectionOccupiedDispatchLock.unlock();
                    }else{
                        blockSectionOccupiedDispatchLock.lock();
                        blockSectionOccupiedDispatchLock.setDebugStatus(safety.reason());
                    }

                    DispatchTrigger dispatchTrigger = triggerContext.getDispatchTrigger();
                    if(dispatching || dispatchTrigger.isActive()){
                        trainInStationDispatchLock.lock();
                        dispatchTrigger.reset();

                        if(!dispatching) stationHandle.runExitEffectTriggers(train);
                        dispatching = true;

                        if(!stationHandle.exitEffectTriggersDone()) break;

                        trainHandle.resetEffects();

                        phase = StationPhase.WAITING;
                        dispatching = false;
                        goIntoSwitch = true;
                    }
                    break;
                case WAITING:
                    Section nextSection = train.getNextSection();
                    if(nextSection != null && nextSection.getBlockSectionSafety(train).safe()){
                        nextSection.setEntireBlockReservation(train);
                        phase = StationPhase.DEPARTING;
                        blockSectionOccupiedDispatchLock.lock();
                        coasterHandle.getRideController().onTrainDepart(train, stationHandle);
                        stationHandle.setStationaryTrain(null);
                        train.playDispatchSound();
                        goIntoSwitch = true;
                    }
                    break;
                case DEPARTING:
                    newSpeed.add(acceleration, 1.0);
                    break;
            }
        }

        return calculateTrainMovement(train, section, newSpeed);
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

