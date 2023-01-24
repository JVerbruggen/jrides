package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class SimpleSection implements Section {
    private int startFrame;
    private int endFrame;
    private final TrackBehaviour trackBehaviour;
    private Train occupiedBy;

    public SimpleSection(int startFrame, int endFrame, TrackBehaviour trackBehaviour) {
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.trackBehaviour = trackBehaviour;
        this.occupiedBy = null;
    }

    @Override
    public int getStartFrame() {
        return startFrame;
    }

    @Override
    public int getEndFrame() {
        return this.endFrame;
    }

    @Override
    public boolean isInSection(int frame) {
        boolean inNormalSection = (startFrame < endFrame) && (startFrame <= frame && frame <= endFrame);
        boolean inCycledSection = (endFrame < startFrame) && (startFrame <= frame || frame <= endFrame);
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
}
