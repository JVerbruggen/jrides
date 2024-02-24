package com.jverbruggen.jrides.animator.coaster.trackbehaviour.drive;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.BaseTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SectionSafetyProvider;
import com.jverbruggen.jrides.models.ride.section.result.BlockSectionSafetyResult;

public class ProximityDriveTrackBehaviour extends BaseTrackBehaviour implements SectionSafetyProvider {
    private final double deceleration;
    private final double acceleration;
    private final double driveSpeed;
    private final double minTrainDistance;

    public ProximityDriveTrackBehaviour(CartMovementFactory cartMovementFactory, double driveSpeed, double deceleration, double acceleration, double minTrainDistance) {
        super(cartMovementFactory);
        this.deceleration = deceleration;
        this.acceleration = acceleration;
        this.driveSpeed = driveSpeed;
        this.minTrainDistance = minTrainDistance;
    }

    @Override
    public TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section) {
        Speed newSpeed = currentSpeed.clone();
        Train train = trainHandle.getTrain();

        newSpeed.approach(acceleration, deceleration, driveSpeed);

        return calculateTrainMovement(train, section, newSpeed);
    }

    @Override
    public void trainExitedAtStart() {

    }

    @Override
    public void trainExitedAtEnd(){

    }

    @Override
    public String getName() {
        return "ProximityDrive";
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

    @Override
    public boolean canHandleBlockSectionSafety() {
        return true;
    }

    @Override
    public BlockSectionSafetyResult getEnteringSafety(Train train, Section nextSection) {
//        BlockSectionSafetyResult nextSectionSafety = nextSection.getBlockSectionSafety(train);
//        if()
        return new BlockSectionSafetyResult(true, train, "");
    }
}
