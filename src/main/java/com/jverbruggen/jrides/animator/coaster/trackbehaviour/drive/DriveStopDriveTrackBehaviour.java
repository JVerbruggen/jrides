package com.jverbruggen.jrides.animator.coaster.trackbehaviour.drive;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.BaseTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;

import javax.annotation.Nullable;


enum DriveStopDrivePhase {
    IDLE,
    DRIVING_IN,
    STOPPING,
    WAITING,
    DRIVING_OUT
}

public class DriveStopDriveTrackBehaviour extends BaseTrackBehaviour {
    private DriveStopDrivePhase phase;
    private final int minWaitTicks;
    private int minWaitTicksState;

    private final Frame stopFrame;
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeedIn;
    private final double driveSpeedOut;

    public DriveStopDriveTrackBehaviour(CartMovementFactory cartMovementFactory, int minWaitTicks, Frame stopFrame, double deceleration, double acceleration, double driveSpeedIn, double driveSpeedOut) {
        super(cartMovementFactory);

        this.minWaitTicks = minWaitTicks;
        this.stopFrame = stopFrame;
        this.deceleration = deceleration;
        this.acceleration = acceleration;
        this.driveSpeedIn = driveSpeedIn;
        this.driveSpeedOut = driveSpeedOut;
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
                case IDLE -> {
                    minWaitTicksState = 0;
                    phase = DriveStopDrivePhase.DRIVING_IN;
                    goIntoSwitch = true;
                }
                case DRIVING_IN -> {
                    if(shouldStop(train)){
                        phase = DriveStopDrivePhase.STOPPING;
                        goIntoSwitch = true;
                    }else{
                        newSpeed.approach(acceleration, deceleration, driveSpeedIn);
                    }
                }
                case STOPPING -> {
                    newSpeed.approach(acceleration, deceleration, 0);
                    if(newSpeed.isZero()){
                        phase = DriveStopDrivePhase.WAITING;
                        goIntoSwitch = true;
                    }
                }
                case WAITING -> {
                    if(++minWaitTicksState >= minWaitTicks){
                        phase = DriveStopDrivePhase.DRIVING_OUT;
                        goIntoSwitch = true;
                    }
                }
                case DRIVING_OUT -> newSpeed.approach(acceleration, deceleration, driveSpeedOut);
            }
        }

        return calculateTrainMovement(train, section, newSpeed);
    }

    private boolean shouldStop(Train train){
        if (driveSpeedIn >= 0)
            return train.getHeadSection().hasPassed(stopFrame, train.getHeadOfTrainFrame());
        return train.getHeadSection().hasPassedInverse(stopFrame, train.getHeadOfTrainFrame());
    }

    @Override
    public void trainExitedAtStart(@Nullable Train train, @Nullable Section section) {
        trainExited();
    }

    @Override
    public void trainExitedAtEnd(@Nullable Train train, @Nullable Section section) {
        trainExited();
    }

    private void trainExited(){
        this.phase = DriveStopDrivePhase.IDLE;
        this.minWaitTicksState = this.minWaitTicks;
    }

    @Override
    public String getName() {
        return "DriveStopDrive";
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