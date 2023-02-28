package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.Bukkit;

public class SimpleSection extends BaseSection {
    private Frame startFrame;
    private Frame endFrame;
    private final boolean jumpAtStart;
    private final boolean jumpAtEnd;
    private String name;

    public SimpleSection(Frame startFrame, Frame endFrame, TrackBehaviour trackBehaviour, boolean jumpAtStart, boolean jumpAtEnd) {
        super(trackBehaviour);
        this.startFrame = startFrame.clone();
        this.endFrame = endFrame.clone();
        this.jumpAtStart = jumpAtStart;
        this.jumpAtEnd = jumpAtEnd;
        this.name = null;
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
        return startFrame;
    }

    @Override
    public Frame getEndFrame() {
        return endFrame;
    }

    @Override
    public boolean isInSection(Frame frame) {
        return Frame.isBetweenFrames(startFrame, endFrame, frame);
    }

    @Override
    public TrackBehaviour getTrackBehaviour() {
        return trackBehaviour;
    }

    @Override
    public boolean canBlock() {
        return trackBehaviour.canBlock();
    }

    @Override
    public boolean isBlockSectionSafe(Train train) {
        if(this.isOccupied()) return false;
        if(!this.trackBehaviour.accepts(train)) return false;
        if(this.canBlock()) return true;

        Section next = next(train);
        return next.isPreviousSectionFor(train, this)
                && next.isBlockSectionSafe(train);
    }

    @Override
    public boolean canTrainSpawnOn() {
        return trackBehaviour.canSpawnOn();
    }

    @Override
    public boolean hasPassed(Frame staticFrame, Frame movingFrame) {
        int startFrameValue = startFrame.getValue();
        int endFrameValue = endFrame.getValue();
        int staticFrameValue = staticFrame.getValue();
        int movingFrameValue = movingFrame.getValue();

        boolean normalPassed = startFrameValue < endFrameValue && staticFrameValue <= movingFrameValue;
        boolean cyclicPassed = (startFrameValue > endFrameValue && (
            (staticFrameValue <= movingFrameValue && staticFrameValue >= startFrameValue)
            || (staticFrameValue <= movingFrameValue && staticFrameValue <= endFrameValue && movingFrameValue <= endFrameValue)
            || (staticFrameValue >= movingFrameValue && staticFrameValue >= startFrameValue && movingFrameValue <= endFrameValue)
        ));

        return normalPassed || cyclicPassed;
    }

    @Override
    public boolean passesCycle() {
        return startFrame.getValue() > endFrame.getValue();
    }

    @Override
    public boolean shouldJumpAtStart() {
        return jumpAtStart;
    }

    @Override
    public boolean shouldJumpAtEnd() {
        return jumpAtEnd;
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
                || Frame.isBetweenFrames(lowerFrame, upperFrame, endFrame)
                || Frame.isBetweenFrames(lowerFrame, upperFrame, startFrame);
    }

    @Override
    public void setParentTrack(Track track) {
        super.setParentTrack(track);

        startFrame.setTrack(track);
        endFrame.setTrack(track);
    }

    @Override
    public String toString() {
        return "<" + startFrame + "-" + endFrame + " " + trackBehaviour.getName() + " occ:" + isOccupied() + ">";
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
