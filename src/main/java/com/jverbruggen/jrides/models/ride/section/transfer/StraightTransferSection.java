package com.jverbruggen.jrides.models.ride.section.transfer;

import com.jverbruggen.jrides.animator.coaster.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.BaseSection;
import com.jverbruggen.jrides.models.ride.section.result.BlockSectionSafetyResult;

public class StraightTransferSection extends BaseSection {
    public TransferPosition currentTransferPosition;
    public Track transferTrack;

    private Frame transferStartFrame;
    private Frame transferEndFrame;

    public StraightTransferSection() {
        super(null);
    }

    @Override
    public Frame getSpawnFrame() {
        return getEndFrame();
    }

    @Override
    public Frame getStartFrame() {
        return transferStartFrame;
    }

    @Override
    public Frame getEndFrame() {
        return transferEndFrame;
    }

    @Override
    public boolean isInSection(Frame frame) {
        return false;
    }

    @Override
    public boolean isInRawFrameRange(Frame frame) {
        return false;
    }

    @Override
    public TrackBehaviour getTrackBehaviour() {
        return null;
    }

    @Override
    public boolean canBlock() {
        return false;
    }

    @Override
    public BlockSectionSafetyResult getBlockSectionSafety(Train train, boolean checkConflicts) {
        return BlockSectionSafetyResult.emptyFalse();
    }

    @Override
    public boolean canTrainSpawnOn() {
        return false;
    }

    @Override
    public boolean hasPassed(Frame staticFrame, Frame movingFrame) {
        return false;
    }

    @Override
    public boolean hasPassedInverse(Frame staticFrame, Frame movingFrame) {
        return false;
    }

    @Override
    public boolean passesCycle() {
        return false;
    }

    @Override
    public boolean spansOver(Train train) {
        return false;
    }
}
