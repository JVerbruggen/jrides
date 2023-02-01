package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class SimpleSection extends BaseSection {
    private Frame startFrame;
    private Frame endFrame;
    private final TrackBehaviour trackBehaviour;


    public SimpleSection(Frame startFrame, Frame endFrame, TrackBehaviour trackBehaviour) {
        super();
        this.startFrame = startFrame.clone();
        this.endFrame = endFrame.clone().add(-1);
        this.trackBehaviour = trackBehaviour;
    }

    @Override
    public Frame getStartFrame() {
        return startFrame;
    }

    @Override
    public Frame getSpawnFrame() {
        Frame behaviourDefinedSpawnFrame = trackBehaviour.getSpawnFrame();
        if(behaviourDefinedSpawnFrame == null)
            return getEndFrame();
        return behaviourDefinedSpawnFrame;
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
    public boolean isBlockSectionSafe() {
        if(this.isOccupied()) return false;
        if(this.canBlock()) return true;

        return next().isBlockSectionSafe();
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
    public boolean spansOver(Train train) {
        Frame headOfTrainFrame = train.getHeadOfTrainFrame();
        Frame tailOfTrainFrame = train.getTailOfTrainFrame();

        return isInSection(headOfTrainFrame)
                || isInSection(tailOfTrainFrame)
                || Frame.isBetweenFrames(tailOfTrainFrame, headOfTrainFrame, endFrame)
                || Frame.isBetweenFrames(tailOfTrainFrame, headOfTrainFrame, startFrame);
    }

    @Override
    public String toString() {
        return "<Section from " + startFrame + " to " + endFrame + " of type " + trackBehaviour.getName() + ">";
    }
}
