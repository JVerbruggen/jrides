package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.StartTrigger;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class StationTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour{
    private final double passThroughSpeed;
    private final double deceleration;
    private final double acceleration;
    private final double driverSpeed;

    private StationPhase phase;
    private final Frame stopFrame;
    private final boolean canSpawn;
    private StartTrigger startTrigger;

    public StationTrackBehaviour(CartMovementFactory cartMovementFactory, Frame stopFrame, boolean canSpawn, StartTrigger startTrigger) {
        super(cartMovementFactory);
        this.passThroughSpeed = 1.0;
        this.deceleration = 0.2;
        this.acceleration = 0.1;
        this.driverSpeed = 1.0;
        this.phase = StationPhase.IDLE;
        this.stopFrame = stopFrame;
        this.canSpawn = canSpawn;
        this.startTrigger = startTrigger;

        trainExitedAtEnd();
    }

    @Override
    public TrainMovement move(Speed currentSpeed, Train train, Track track) {
        Speed newSpeed = currentSpeed.clone();

        boolean goIntoSwitch = true;
        while(goIntoSwitch){
            goIntoSwitch = false;
            switch (phase){
                case IDLE:
                    phase = StationPhase.ARRIVING;
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
                    if(newSpeed.is(0)) phase = StationPhase.WAITING;
                    newSpeed.minus(deceleration, 0);
                    break;
                case STATIONARY:
                    if(startTrigger.isActive()){
                        startTrigger.reset();
                        phase = StationPhase.WAITING;
                        goIntoSwitch = true;
                    }
                    break;
                case WAITING:
                    if(train.getHeadSection().next().isBlockSectionSafe()){
                        phase = StationPhase.DEPARTING;
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

    @Override
    public void trainExitedAtStart() {

    }

    @Override
    public void trainExitedAtEnd(){
        this.phase = StationPhase.IDLE;
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

enum StationPhase{
    IDLE,
    ARRIVING,
    STOPPING,
    STATIONARY,
    WAITING,
    DEPARTING
}