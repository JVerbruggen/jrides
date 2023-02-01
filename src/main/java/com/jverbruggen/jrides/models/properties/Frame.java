package com.jverbruggen.jrides.models.properties;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;

public interface Frame {
    int getValue();

    void setValue(int frame);

    Track getTrack();

    void setTrack(Track track);

    Frame add(int frames);

    Frame clone();

    void updateTo(Frame other);

    static int getCyclicFrameValue(int nonCyclicFrame, int totalFrames){
        return Math.floorMod(nonCyclicFrame, totalFrames);
    }

    static boolean isBetweenFrames(Frame lowerFrame, Frame upperFrame, Frame compareFrame){
        int lowerFrameValue = lowerFrame.getValue();
        int upperFrameValue = upperFrame.getValue();
        int compareFrameValue = compareFrame.getValue();

        boolean inNormalSection = (lowerFrameValue < upperFrameValue)
                && (lowerFrameValue <= compareFrameValue && compareFrameValue <= upperFrameValue);
        boolean inCycledSection = (upperFrameValue < lowerFrameValue)
                && (lowerFrameValue <= compareFrameValue || compareFrameValue <= upperFrameValue);

        return inNormalSection || inCycledSection;
    }
}
