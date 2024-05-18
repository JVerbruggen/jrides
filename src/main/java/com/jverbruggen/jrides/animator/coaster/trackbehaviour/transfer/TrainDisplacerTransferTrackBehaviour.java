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
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class TrainDisplacerTransferTrackBehaviour extends BaseTrackBehaviour {
    private final double deceleration;
    private final double acceleration;
    private final double enterDriveSpeed;
    private final double exitDriveSpeed;
    private final Frame stopFrame;
    private final Transfer transfer;

    private TransferPhase phase;

    public TrainDisplacerTransferTrackBehaviour(CartMovementFactory cartMovementFactory, double enterDriveSpeed, double deceleration, double acceleration, double exitDriveSpeed, Frame stopFrame, Transfer transfer) {
        super(cartMovementFactory);
        this.deceleration = deceleration;
        this.acceleration = acceleration;
        this.enterDriveSpeed = enterDriveSpeed;
        this.exitDriveSpeed = exitDriveSpeed;
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
                        newSpeed.approach(acceleration, deceleration, enterDriveSpeed);
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
                    Section goToSection = getGoToSection(train);
//                    Section goToSection = currentSection.next(train);
                    if(goToSection != null && goToSection.getBlockSectionSafety(train).safe()){
                        transfer.unlockTrain();
                        transfer.releaseRequest();
                        phase = TransferPhase.DRIVING;
                        goIntoSwitch = true;

                        boolean positiveTowardsSectionDirection = currentSection.positiveDirectionToGoTo(goToSection, train);
                        boolean currentlyFacingForwards = train.isFacingForwards();
                        TransferPosition currentTransferPosition = transfer.getCurrentTransferPosition();

                        boolean isSectionForwardsType;
                        if(positiveTowardsSectionDirection)
                            isSectionForwardsType = currentTransferPosition.isSectionAtEndForwards();
                        else
                            isSectionForwardsType = currentTransferPosition.isSectionAtStartForwards();

                        boolean trainWillBeForwards = currentlyFacingForwards == isSectionForwardsType;

                        boolean driveDirectionForwards = positiveTowardsSectionDirection == currentlyFacingForwards;

//                        train.setFacingForwards(currentlyFacingForwards == positiveTowardsSectionDirection);
                        train.setFacingForwards(trainWillBeForwards);
//                        train.setDrivingDirection(positiveTowardsSectionDirection);
//                        train.setDrivingDirection(trainWillBeForwards);
                        train.setDrivingDirection(true);
                        goToSection.setEntireBlockReservation(train);
                    }
                    break;
                case DRIVING:
                    newSpeed.approach(acceleration, deceleration, exitDriveSpeed);
                    break;
            }
        }

        return calculateTrainMovement(train, section, newSpeed);
    }

    private Section getGoToSection(Train train){
        Section sectionAtStart = getSectionAtStart(train, false);
        Section sectionAtEnd = getSectionAtEnd(train, false);
        if(sectionAtEnd != null)
            return sectionAtEnd;
        if(sectionAtStart != null)
            return sectionAtStart;
        throw new RuntimeException("No Exit section available on transfer");
    }

    @Override
    public void trainExitedAtStart(@Nullable Train train, @Nullable Section section) {
        throw new RuntimeException("Not supported exited train at start train displacer");
    }

    @Override
    public void trainExitedAtEnd(@Nullable Train train, @Nullable Section section){
        phase = TransferPhase.IDLE;

        if(section == null || section.getBlockSectionSafety(null).safe()){
            Bukkit.broadcastMessage("Safe enough");
            transfer.trainExitedTransfer();
        }else{
            Bukkit.broadcastMessage("Not safe for next one yet");
        }
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

    private boolean canTransferSafelyInteractWith(Train train){
        TrainHandle trainHandle = null;
        if(train != null)
            trainHandle = train.getHandle();

        return transfer.canSafelyInteractWith(trainHandle);
    }

    @Override
    public Section getSectionAtStart(Train train, boolean process) {
        if(!canTransferSafelyInteractWith(train)){
            JRidesPlugin.getLogger().info(LogType.SECTIONS, "No safe interact at start");
            return null;
        }
        return transfer.getCurrentTransferPosition().getSectionAtStart();
    }

    @Override
    public Section getSectionAtEnd(Train train, boolean process) {
        if(!canTransferSafelyInteractWith(train)){
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
            return null;
//            return getSectionAtStart(train, process);
        }
    }

    @Override
    public Section getSectionPrevious(Train train, boolean process) {
        Section logicalNext = getSectionAtStart(train, process);
        if(logicalNext != null) {
            Bukkit.broadcastMessage("Logical next: " + logicalNext.getName());
            return logicalNext;
        }else{
            Bukkit.broadcastMessage("Logical next: null");
//            return getSectionAtEnd(train, process);
            return null;
        }
    }

    @Override
    public boolean accepts(Train train) {
        return canTransferSafelyInteractWith(train);
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
