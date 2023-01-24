package com.jverbruggen.jrides.models.properties;

public interface Frame {
    int getValue();

    void setValue(int frame);

    Frame add(int frames);

    Frame clone();

    void updateTo(Frame other);

    static int getCyclicFrameValue(int nonCyclicFrame, int totalFrames){
        return Math.floorMod(nonCyclicFrame, totalFrames);
    }
}
