package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class PointSection extends BaseSection {
    private final Frame point;
    private String name;

    public PointSection(TrackBehaviour trackBehaviour, Frame point, String name) {
        super(trackBehaviour);
        this.point = point;
        this.name = name;
    }

    @Override
    public Frame getSpawnFrame() {
        Frame behaviourDefinedSpawnFrame = trackBehaviour.getSpawnFrame();
        if(behaviourDefinedSpawnFrame == null)
            return getEndFrame();
        return behaviourDefinedSpawnFrame;
    }

    @Override
    public Frame getStartFrame() {
        return point;
    }

    @Override
    public Frame getEndFrame() {
        return point;
    }

    @Override
    public boolean isInSection(Frame frame) {
        return isInRawFrameRange(frame);
    }

    @Override
    public boolean isInRawFrameRange(Frame frame) {
        return frame.getValue() == this.point.getValue();
    }

    @Override
    public TrackBehaviour getTrackBehaviour() {
        return trackBehaviour;
    }

    @Override
    public boolean canBlock() {
        return false;
    }

    @Override
    public boolean isBlockSectionSafe(Train train) {
        Section next = next(train);
        if(next == null) return false;
        return next.isBlockSectionSafe(train);
    }

    @Override
    public boolean canTrainSpawnOn() {
        return trackBehaviour.canSpawnOn();
    }

    @Override
    public boolean hasPassed(Frame staticFrame, Frame movingFrame) {
        int staticFrameValue = staticFrame.getValue();
        int movingFrameValue = movingFrame.getValue();

        return staticFrameValue <= movingFrameValue;
    }

    @Override
    public boolean passesCycle() {
        return false;
    }

    @Override
    public boolean shouldJumpAtStart() {
        return false;
    }

    @Override
    public boolean shouldJumpAtEnd() {
        return false;
    }

    @Override
    public boolean spansOver(Train train) {
        Frame headOfTrainFrame = train.getHeadOfTrainFrame();
        Frame tailOfTrainFrame = train.getTailOfTrainFrame();
        JRidesPlugin.getLogger().info(LogType.SECTIONS,
                "From " + this + " Spans over " + headOfTrainFrame.getValue() + " - " + tailOfTrainFrame.getValue());

        Frame lowerFrame = train.isPositiveDrivingDirection() ? tailOfTrainFrame : headOfTrainFrame;
        Frame upperFrame = train.isPositiveDrivingDirection() ? headOfTrainFrame : tailOfTrainFrame;

        return isInSection(headOfTrainFrame)
                || isInSection(tailOfTrainFrame)
                || Frame.isBetweenFrames(lowerFrame, upperFrame, point);
    }

    @Override
    public void setParentTrack(Track track) {
        super.setParentTrack(track);
        point.setTrack(track);
    }

    @Override
    public String toString() {
        return "<PointSection at " + point + ">";
    }

    @Override
    public String getName() {
        if(name == null) return toString();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}