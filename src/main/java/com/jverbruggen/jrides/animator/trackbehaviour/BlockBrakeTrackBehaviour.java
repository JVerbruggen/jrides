package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import org.bukkit.Bukkit;

public class BlockBrakeTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour{
    private final double passThroughSpeed;
    private final double deceleration;
    private final double acceleration;
    private final double driverSpeed;

    private BlockBrakePhase phase;
    private final Frame stopFrame;
    private final boolean canSpawn;

    public BlockBrakeTrackBehaviour(CartMovementFactory cartMovementFactory, Frame stopFrame, boolean canSpawn) {
        super(cartMovementFactory);
        this.passThroughSpeed = 1.0;
        this.deceleration = 0.2;
        this.acceleration = 0.1;
        this.driverSpeed = 1.0;
        this.phase = BlockBrakePhase.IDLE;
        this.stopFrame = stopFrame;
        this.canSpawn = canSpawn;

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
                    if(train.getHeadSection().next().isBlockSectionSafe())
                        phase = BlockBrakePhase.PASSING_THROUGH;
                    else
                        phase = BlockBrakePhase.DRIVING_UNTIL_STOP;
                    goIntoSwitch = true;
                    break;
                case PASSING_THROUGH:
                    if(newSpeed.getSpeedPerTick() > driverSpeed){
                        newSpeed.minus(deceleration, driverSpeed);
                    }
                    else{
                        phase = BlockBrakePhase.DRIVING;
                        goIntoSwitch = true;
                    }
                    break;
                case DRIVING_UNTIL_STOP:
                    if(train.getHeadSection().hasPassed(stopFrame, train.getHeadOfTrainFrame())){
                        if(train.getHeadSection().next().isBlockSectionSafe()){
                            phase = BlockBrakePhase.DRIVING;
                        }else{
                            phase = BlockBrakePhase.STOPPING;
                        }
                        goIntoSwitch = true;
                    }else{
                        newSpeed.approach(acceleration, deceleration, 1.0);
                    }
                    break;
                case STOPPING:
                    if(newSpeed.is(0)) phase = BlockBrakePhase.WAITING;
                    newSpeed.minus(deceleration, 0);
                    break;
                case WAITING:
                    if(train.getHeadSection().next().isBlockSectionSafe()){
                        phase = BlockBrakePhase.DRIVING;
                        goIntoSwitch = true;
                    }
                    break;
                case DRIVING:
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
}

enum BlockBrakePhase{
    IDLE,
    PASSING_THROUGH,
    DRIVING_UNTIL_STOP,
    STOPPING,
    WAITING,
    DRIVING
}