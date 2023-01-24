package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class SimpleSection implements Section {
    private Frame startFrame;
    private Frame endFrame;
    private final TrackBehaviour trackBehaviour;
    private Train occupiedBy;

    public SimpleSection(Frame startFrame, Frame endFrame, TrackBehaviour trackBehaviour) {
        this.startFrame = startFrame.clone();
        this.endFrame = endFrame.clone().add(-1);
        this.trackBehaviour = trackBehaviour;
        this.occupiedBy = null;
    }

    @Override
    public Frame getStartFrame() {
        return startFrame;
    }

    @Override
    public Frame getEndFrame() {
        return endFrame;
    }

    @Override
    public boolean isInSection(Frame frame) {
        int startFrameValue = startFrame.getValue();
        int endFrameValue = endFrame.getValue();
        int compareFrameValue = frame.getValue();

        boolean inNormalSection = (startFrameValue < endFrameValue)
                && (startFrameValue <= compareFrameValue && compareFrameValue <= endFrameValue);
        boolean inCycledSection = (endFrameValue < startFrameValue)
                && (startFrameValue <= compareFrameValue || compareFrameValue <= endFrameValue);

        return inNormalSection || inCycledSection;
    }

    @Override
    public TrackBehaviour getTrackBehaviour() {
        return trackBehaviour;
    }

    @Override
    public boolean isOccupied() {
        return occupiedBy != null;
    }

    @Override
    public void setOccupation(Train train) {
        if(occupiedBy != null) throw new RuntimeException("Two trains cannot be in same section!");
        occupiedBy = train;
    }

    @Override
    public String toString() {
        return "<Section from " + startFrame + " to " + endFrame + ">";
    }
}
