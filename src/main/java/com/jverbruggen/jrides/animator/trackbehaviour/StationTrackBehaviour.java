package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class StationTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour{
    private final double passThroughSpeed;
    private final double deceleration;
    private final double acceleration;
    private final double driverSpeed;

    private final CoasterHandle coasterHandle;
    private final JRidesLogger logger;
    private Train handlingTrain;
    private StationPhase phase;
    private final Frame stopFrame;
    private final boolean canSpawn;
    private DispatchTrigger dispatchTrigger;
    private final StationHandle stationHandle;
    private final DispatchLock trainInStationDispatchLock;
    private final DispatchLock blockSectionOccupiedDispatchLock;

    public StationTrackBehaviour(CoasterHandle coasterHandle, CartMovementFactory cartMovementFactory, Frame stopFrame, boolean canSpawn, DispatchTrigger dispatchTrigger,
                                 StationHandle stationHandle, DispatchLock trainInStationDispatchLock, DispatchLock blockSectionOccupiedDispatchLock) {
        super(cartMovementFactory);
        this.coasterHandle = coasterHandle;
        this.logger = JRidesPlugin.getLogger();
        this.passThroughSpeed = 1.0;
        this.deceleration = 0.2;
        this.acceleration = 0.1;
        this.driverSpeed = 1.0;
        this.handlingTrain = null;
        this.phase = StationPhase.IDLE;
        this.stopFrame = stopFrame;
        this.canSpawn = canSpawn;
        this.dispatchTrigger = dispatchTrigger;
        this.stationHandle = stationHandle;

        this.trainInStationDispatchLock = trainInStationDispatchLock;
        this.blockSectionOccupiedDispatchLock = blockSectionOccupiedDispatchLock;

        trainExitedAtEnd();
    }

    @Override
    public TrainMovement move(Speed currentSpeed, Train train, Track track) {
        Speed newSpeed = currentSpeed.clone();

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
                    if(newSpeed.is(0)) {
                        phase = StationPhase.STATIONARY;
                        stationHandle.setStationaryTrain(train);
                        // TODO: on train arrive
                        coasterHandle.getRideController().onTrainArrive(train);
                        trainInStationDispatchLock.unlock();
                        goIntoSwitch = true;
                    }else
                        newSpeed.minus(deceleration, 0);
                    break;
                case STATIONARY:
                    if(train.getHeadSection().next().isBlockSectionSafe()){
                        blockSectionOccupiedDispatchLock.unlock();
                    }else{
                        blockSectionOccupiedDispatchLock.lock();
                    }

                    if(dispatchTrigger.isActive()){
                        phase = StationPhase.WAITING;
                        dispatchTrigger.reset();
                        trainInStationDispatchLock.lock();
                        goIntoSwitch = true;
                    }
                    break;
                case WAITING:
                    if(train.getHeadSection().next().isBlockSectionSafe()){
                        phase = StationPhase.DEPARTING;
                        blockSectionOccupiedDispatchLock.lock();
                        coasterHandle.getRideController().onTrainDepart(train);
                        goIntoSwitch = true;
                    }
                    break;
                case DEPARTING:
                    newSpeed.add(acceleration, 1.0);
                    break;
            }
        }

        return calculateTrainMovement(train, track, newSpeed);
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
}

