package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public class LaunchTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour{
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;
    private final int waitTicks;
    private final Frame engageFrame;
    private final double launchAcceleration;
    private final double launchMaxSpeed;
    private final List<TrainEffectTriggerHandle> launchEffectTriggers;

    private int waitTicksState;
    private LaunchPhase phase;

    public LaunchTrackBehaviour(CartMovementFactory cartMovementFactory, double driveSpeed, double deceleration, double acceleration, int waitTicks, Frame engageFrame, double launchAcceleration, double launchMaxSpeed, List<TrainEffectTriggerHandle> launchEffectTriggers) {
        super(cartMovementFactory);
        this.deceleration = deceleration;
        this.acceleration = acceleration;
        this.driveSpeed = driveSpeed;
        this.waitTicks = waitTicks;
        this.engageFrame = engageFrame;
        this.launchMaxSpeed = launchMaxSpeed;
        this.launchAcceleration = launchAcceleration;
        this.launchEffectTriggers = launchEffectTriggers;

        this.phase = LaunchPhase.IDLE;
        this.waitTicksState = 0;
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        Speed newSpeed = currentSpeed.clone();
        Train train = trainHandle.getTrain();

        boolean goIntoSwitch = true;
        while(goIntoSwitch) {
            goIntoSwitch = false;
            switch (this.phase) {
                case IDLE:
                    goIntoSwitch = true;
                    this.phase = LaunchPhase.DRIVING;
                    break;
                case DRIVING:
                    if (train.getHeadSection().hasPassed(engageFrame, train.getHeadOfTrainFrame())) {
                        if(waitTicks < 0 && isNextSectionSafe(train)){
                            this.phase = LaunchPhase.LAUNCHING;
                            train.getNextSection().setEntireBlockReservation(train);
                            goIntoSwitch = true;
                        }else{
                            this.phase = LaunchPhase.STOPPING;
                            goIntoSwitch = true;
                        }
                    }else{
                        newSpeed.approach(acceleration, deceleration, driveSpeed);
                    }
                    break;
                case STOPPING:
                    if(newSpeed.isZero()){
                        phase = LaunchPhase.WAITING;
                        goIntoSwitch = true;
                    }
                    newSpeed.minus(deceleration, 0);
                    break;
                case WAITING:
                    if(doneWaiting() && isNextSectionSafe(train)){
                        phase = LaunchPhase.LAUNCHING;
                        train.getNextSection().setEntireBlockReservation(train);
                        goIntoSwitch = true;
                        playLaunchEffects(train);
                    }
                    break;
                case LAUNCHING:
                    newSpeed.approach(launchAcceleration, launchAcceleration, launchMaxSpeed);
                    break;
            }
        }

        return calculateTrainMovement(train, section, newSpeed);
    }

    private void playLaunchEffects(Train train){
        if(launchEffectTriggers == null) return;
        launchEffectTriggers.forEach(e -> e.execute(train));
    }

    private void reset(){
        waitTicksState = 0;
        phase = LaunchPhase.IDLE;
    }

    private boolean doneWaiting(){
        if(waitTicksState >= waitTicks)
            return true;
        waitTicksState++;
        return false;
    }

    @Override
    public void trainExitedAtStart() {
        reset();
    }

    @Override
    public void trainExitedAtEnd(){
        reset();
    }

    @Override
    public String getName() {
        return "Launch";
    }

    @Override
    public boolean canBlock() {
        return true;
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

enum LaunchPhase{
    IDLE,
    DRIVING,
    STOPPING,
    WAITING,
    LAUNCHING
}