package com.jverbruggen.jrides.animator.trackbehaviour;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.List;

public class LaunchTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour{
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;
    private final int waitTicks;
    private final Frame engageFrame;
    private final double launchAcceleration;
    private final double launchMaxSpeed;
    private final List<EffectTriggerHandle> launchEffectTriggers;

    private int waitTicksState;
    private LaunchPhase phase;

    public LaunchTrackBehaviour(CartMovementFactory cartMovementFactory, double driveSpeed, double deceleration, double acceleration, int waitTicks, Frame engageFrame, double launchAcceleration, double launchMaxSpeed, List<EffectTriggerHandle> launchEffectTriggers) {
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
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Track track) {
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
                        if(waitTicks < 0 && train.getHeadSection().next().isBlockSectionSafe()){
                            this.phase = LaunchPhase.LAUNCHING;
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
                    if(newSpeed.is(0)){
                        phase = LaunchPhase.WAITING;
                        goIntoSwitch = true;
                    }
                    newSpeed.minus(deceleration, 0);
                    break;
                case WAITING:
                    if(doneWaiting() && train.getHeadSection().next().isBlockSectionSafe()){
                        phase = LaunchPhase.LAUNCHING;
                        launchEffectTriggers.forEach(e -> e.execute(train));
                        goIntoSwitch = true;
                    }
                    break;
                case LAUNCHING:
                    newSpeed.approach(launchAcceleration, launchAcceleration, launchMaxSpeed);
                    break;
            }
        }

        return calculateTrainMovement(train, track, newSpeed);
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