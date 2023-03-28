package com.jverbruggen.jrides.models.properties.frame;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

public interface Frame {
    int getValue();
    String getValueString();

    void setValue(int frame);

    Track getTrack();
    void setTrack(Track track);

    Section getSection();
    void setSection(Section section);

    Frame add(int frames);
    Frame clone();
    Frame capture();

    void setInvertedFrameAddition(boolean inverted);
    boolean isInvertedFrameAddition();
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
