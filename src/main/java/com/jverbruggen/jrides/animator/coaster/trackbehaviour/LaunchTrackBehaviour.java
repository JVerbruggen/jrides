package com.jverbruggen.jrides.animator.coaster.trackbehaviour;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;

import javax.annotation.Nullable;
import java.util.List;

public class LaunchTrackBehaviour extends BaseTrackBehaviour {
    private final boolean canSpawn;
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;
    private final boolean forwardsLaunch;

    private final int waitTicks;
    private final Frame engageFrame;
    private final double launchAcceleration;
    private final double launchSpeed;
    private final double launchSpeedBackward;
    private final List<TrainEffectTriggerHandle> launchEffectTriggers;

    private int waitTicksState;
    private LaunchPhase phase;

    public LaunchTrackBehaviour(CartMovementFactory cartMovementFactory, boolean canSpawn, double driveSpeed, double deceleration, double acceleration, boolean forwardsLaunch, int waitTicks, Frame engageFrame, double launchAcceleration, double launchSpeed, double launchSpeedBackward, List<TrainEffectTriggerHandle> launchEffectTriggers) {
        super(cartMovementFactory);
        this.canSpawn = canSpawn;
        this.deceleration = deceleration;
        this.acceleration = acceleration;
        this.driveSpeed = driveSpeed;
        this.forwardsLaunch = forwardsLaunch;
        this.waitTicks = waitTicks;
        this.engageFrame = engageFrame;
        this.launchSpeed = launchSpeed;
        this.launchAcceleration = launchAcceleration;
        this.launchSpeedBackward = launchSpeedBackward;
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
                    if(waitTicks < 0 && getNextSectionSafety(train).safe()){
                        train.getNextSection().setEntireBlockReservation(train);
                        this.phase = LaunchPhase.LAUNCHING;
                    }else{
                        this.phase = LaunchPhase.DRIVING;
                    }

                    goIntoSwitch = true;
                    break;
                case DRIVING:
                    if (train.getHeadSection().hasPassed(engageFrame, train.getHeadOfTrainFrame())) {
                        this.phase = LaunchPhase.STOPPING;
                        goIntoSwitch = true;
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
                    if(getNextSectionSafety(train).safe() && doneWaiting()){
                        phase = LaunchPhase.LAUNCHING_FROM_STANDSTILL;
                        train.getNextSection().setEntireBlockReservation(train);
                        goIntoSwitch = true;
                        playLaunchEffects(train);
                    }
                    break;
                case LAUNCHING_FROM_STANDSTILL:
                    if(forwardsLaunch){
                        newSpeed.approach(launchAcceleration, launchAcceleration, launchSpeed);
                    }else{
                        newSpeed.approach(launchAcceleration, launchAcceleration, -launchSpeedBackward);
                    }
                    break;
                case LAUNCHING:
                    newSpeed.approach(launchAcceleration, launchAcceleration, launchSpeed);
                    break;
            }
        }

        return calculateTrainMovement(train, section, newSpeed);
    }

    private void playLaunchEffects(Train train){
        if(launchEffectTriggers == null) return;
        launchEffectTriggers.forEach(e -> e.executeForTrain(train));
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
    public void trainExitedAtStart(@Nullable Train train, @Nullable Section section) {
        reset();
    }

    @Override
    public void trainExitedAtEnd(@Nullable Train train, @Nullable Section section){
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
        return canSpawn;
    }

    @Override
    public Frame getSpawnFrame() {
        return engageFrame;
    }

    @Override
    protected void setParentTrackOnFrames(Track parentTrack) {
        engageFrame.setTrack(parentTrack);
    }
}

enum LaunchPhase{
    IDLE,
    DRIVING,
    STOPPING,
    WAITING,
    LAUNCHING_FROM_STANDSTILL,
    LAUNCHING
}