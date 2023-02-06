package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.Bukkit;

public class BlockBrakeTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour{
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;

    private BlockBrakePhase phase;
    private final Frame stopFrame;
    private final boolean canSpawn;

    public BlockBrakeTrackBehaviour(CartMovementFactory cartMovementFactory, Frame stopFrame, boolean canSpawn, double driveSpeed) {
        super(cartMovementFactory);
        this.deceleration = 0.1;
        this.acceleration = 0.1;
        this.driveSpeed = driveSpeed;
        this.phase = BlockBrakePhase.IDLE;
        this.stopFrame = stopFrame;
        this.canSpawn = canSpawn;

        trainExitedAtEnd();
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Track track) {
        Speed newSpeed = currentSpeed.clone();
        Train train = trainHandle.getTrain();

        boolean goIntoSwitch = true;
        while(goIntoSwitch){
            goIntoSwitch = false;
            switch (phase){
                case IDLE:
                    train.setStatusMessage("Idle");
                    if(train.getHeadSection().next().isBlockSectionSafe())
                        phase = BlockBrakePhase.PASSING_THROUGH;
                    else
                        phase = BlockBrakePhase.DRIVING_UNTIL_STOP;
                    goIntoSwitch = true;
                    break;
                case PASSING_THROUGH:
                    train.setStatusMessage("Passing through");
                    if(newSpeed.getSpeedPerTick() > driveSpeed){
                        newSpeed.minus(deceleration, driveSpeed);
                    }
                    else{
                        phase = BlockBrakePhase.DRIVING;
                        goIntoSwitch = true;
                    }
                    break;
                case DRIVING_UNTIL_STOP:
                    train.setStatusMessage("Driving until stop");
                    if(train.getHeadSection().hasPassed(stopFrame, train.getHeadOfTrainFrame())){
                        if(train.getHeadSection().next().isBlockSectionSafe()){
                            phase = BlockBrakePhase.DRIVING;
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
                    if(newSpeed.is(0)){
                        phase = BlockBrakePhase.WAITING;
                        goIntoSwitch = true;
                    }
                    newSpeed.minus(deceleration, 0);
                    break;
                case WAITING:
                    train.setStatusMessage("Waiting");
                    if(train.getHeadSection().next().isBlockSectionSafe()){
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

    @Override
    protected void setParentTrackOnFrames(Track parentTrack) {
        stopFrame.setTrack(parentTrack);
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