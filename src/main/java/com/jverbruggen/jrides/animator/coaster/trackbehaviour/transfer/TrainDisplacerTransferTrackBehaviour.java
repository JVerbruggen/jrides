package com.jverbruggen.jrides.animator.coaster.trackbehaviour.transfer;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.BaseTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.coaster.transfer.Transfer;
import com.jverbruggen.jrides.models.ride.coaster.transfer.TransferPosition;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TrainDisplacerTransferTrackBehaviour extends BaseTrackBehaviour {
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
                    if(newSpeed.isZero()){
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
                        phase = TransferPhase.WAITING;
                        goIntoSwitch = true;
                    }
                    break;
                case WAITING:
                    Section currentSection = train.getHeadSection();
                    Section nextSection = currentSection.next(train);
                    if(nextSection != null && nextSection.getBlockSectionSafety(train).safe()){
                        transfer.unlockTrain();
                        transfer.releaseRequest();
                        phase = TransferPhase.DRIVING;
                        goIntoSwitch = true;

                        boolean positiveTrainDirection = currentSection.positiveDirectionToGoTo(nextSection, train);
                        boolean currentlyFacingForwards = train.isFacingForwards();

                        train.setFacingForwards(currentlyFacingForwards == positiveTrainDirection);
                        train.setDrivingDirection(positiveTrainDirection);
                        nextSection.setEntireBlockReservation(train);
                    }
                    break;
                case DRIVING:
                    newSpeed.approach(acceleration, deceleration, driveSpeed);
                    break;
            }
        }

        return calculateTrainMovement(train, section, newSpeed);
    }

    @Override
    public void trainExitedAtStart() {
        throw new RuntimeException("Not supported exited train at start train displacer");
    }

    @Override
    public void trainExitedAtEnd(){
        phase = TransferPhase.IDLE;
        transfer.trainExitedTransfer();
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
    public boolean definesNextSection() {
        return true;
    }

    @Override
    public Vector3 getBehaviourDefinedPosition(Vector3 originalPosition) {
        Vector3 transferLocation = transfer.getCurrentLocation();
        Quaternion transferOrientation = transfer.getCurrentOrientation();

        Vector3 offsetFromOrigin = Vector3.subtract(originalPosition, transfer.getOrigin());

        Matrix4x4 rotationMatrix = new Matrix4x4();
        rotationMatrix.translate(transferLocation);
        rotationMatrix.rotate(transferOrientation);
        rotationMatrix.translate(offsetFromOrigin);

        return rotationMatrix.toVector3();
    }

    @Override
    public Quaternion getBehaviourDefinedOrientation(Quaternion originalOrientation) {
        Quaternion transferOrientation = transfer.getCurrentOrientation();
        return Quaternion.multiply(transferOrientation, originalOrientation);
    }

    @Override
    public Section getSectionAtStart(Train train, boolean process) {
        if(!transfer.canSafelyInteractWith(train.getHandle())){
            JRidesPlugin.getLogger().info(LogType.SECTIONS, "No safe interact at start");
            return null;
        }
        return transfer.getCurrentTransferPosition().getSectionAtStart();
    }

    @Override
    public Section getSectionAtEnd(Train train, boolean process) {
        if(!transfer.canSafelyInteractWith(train.getHandle())){
            JRidesPlugin.getLogger().info(LogType.SECTIONS, "No safe interact at end");
            return null;
        }
        return transfer.getCurrentTransferPosition().getSectionAtEnd();
    }

    @Override
    public Collection<Section> getAllNextSections(Train train) {
        return getAllNeighbors(train);
    }

    @Override
    public Collection<Section> getAllPreviousSections(Train train) {
        return getAllNeighbors(train);
    }

    private Collection<Section> getAllNeighbors(Train train){
        List<Section> sections = new ArrayList<>();
        for(TransferPosition transferPosition : transfer.getPossiblePositions()){
            Section start = transferPosition.getSectionAtStart();
            Section end = transferPosition.getSectionAtEnd();
            if(start != null) sections.add(start);
            if(end != null) sections.add(end);
        }
        return sections;
    }

    @Override
    public Section getSectionNext(Train train, boolean process) {
        Section logicalNext = getSectionAtEnd(train, process);
        if(logicalNext != null)
            return logicalNext;
        else{
            return getSectionAtStart(train, process);
        }
    }

    @Override
    public Section getSectionPrevious(Train train, boolean process) {
        Section logicalNext = getSectionAtStart(train, process);
        if(logicalNext != null)
            return logicalNext;
        else{
            return getSectionAtEnd(train, process);
        }
    }

    @Override
    public boolean accepts(Train train) {
        return transfer.canSafelyInteractWith(train.getHandle());
    }
}

enum TransferPhase{
    IDLE,
    DRIVING_UNTIL_STOP,
    STOPPING,
    TRANSFERRING,
    WAITING,
    DRIVING
}
