package com.jverbruggen.jrides.animator.trackbehaviour.transfer;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.BaseTrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.coaster.transfer.Transfer;
import com.jverbruggen.jrides.models.ride.section.Section;
import org.bukkit.Bukkit;

public class TrainDisplacerTransferTrackBehaviour extends BaseTrackBehaviour implements TrackBehaviour {
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;
    private final Frame stopFrame;
    private final Transfer transfer;

    private TransferPhase phase;

    public TrainDisplacerTransferTrackBehaviour(CartMovementFactory cartMovementFactory, double driveSpeed, double deceleration, double acceleration, Frame stopFrame, Transfer transfer) {
        super(cartMovementFactory);
        this.deceleration = deceleration;
        this.acceleration = acceleration;
        this.driveSpeed = driveSpeed;
        this.stopFrame = stopFrame;
        this.transfer = transfer;
        this.phase = TransferPhase.IDLE;
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        Speed newSpeed = currentSpeed.clone();
        Train train = trainHandle.getTrain();

        boolean goIntoSwitch = true;
        while(goIntoSwitch){
            goIntoSwitch = false;
            switch(phase){
                case IDLE:
                    phase = TransferPhase.DRIVING_UNTIL_STOP;
                    goIntoSwitch = true;
                    break;
                case DRIVING_UNTIL_STOP:
                    if(train.getHeadSection().hasPassed(stopFrame, train.getHeadOfTrainFrame())){
                        phase = TransferPhase.STOPPING;
                        goIntoSwitch = true;
                    }else{
                        newSpeed.approach(acceleration, deceleration, driveSpeed);
                    }
                    break;
                case STOPPING:
                    if(newSpeed.is(0)){
                        phase = TransferPhase.TRANSFERRING;

                        transfer.setTrain(trainHandle);
                        transfer.lockTrain();
                        transfer.setTargetPosition(1, true);

                        goIntoSwitch = true;
                    }
                    newSpeed.minus(deceleration, 0);
                    break;
                case TRANSFERRING:
                    if(transfer.hasReachedRequest()){
                        transfer.unlockTrain();
                        transfer.releaseRequest();

                        phase = TransferPhase.DRIVING;
                        goIntoSwitch = true;
                        Bukkit.broadcastMessage("DONE");
                    }
                    break;
                case DRIVING:
                    newSpeed.approach(acceleration, deceleration, driveSpeed);
                    Bukkit.broadcastMessage("next: " + section.next());
                    break;
            }
        }

        return calculateTrainMovement(train, section, newSpeed);
    }

    @Override
    public void trainExitedAtStart() {

    }

    @Override
    public void trainExitedAtEnd(){
        phase = TransferPhase.IDLE;
    }

    @Override
    public String getName() {
        return "Transfer";
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

    @Override
    public boolean canMoveFromParentTrack() {
        return true;
    }

    @Override
    public Vector3 getBehaviourDefinedPosition() {
        return transfer.getOffset();
    }

    @Override
    public Section getSectionAtStart() {
        return transfer.getCurrentTransferPosition().getSectionAtStart();
    }

    @Override
    public Section getSectionAtEnd() {
        return transfer.getCurrentTransferPosition().getSectionAtEnd();
    }
}

enum TransferPhase{
    IDLE,
    DRIVING_UNTIL_STOP,
    STOPPING,
    TRANSFERRING,
    DRIVING
}
