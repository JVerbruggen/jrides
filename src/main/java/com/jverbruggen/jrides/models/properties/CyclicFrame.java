package com.jverbruggen.jrides.models.properties;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;

public class CyclicFrame implements Frame {
    private int frame;
    private final int cycle;

    public CyclicFrame(int frame, int cycle) {
        this.frame = frame;
        this.cycle = cycle;
    }

    @Override
    public int getValue() {
        return frame;
    }

    @Override
    public void setValue(int frame) {
        this.frame = Frame.getCyclicFrameValue(frame, cycle);
    }

    @Override
    public Track getTrack() {
        throw new RuntimeException("Unimplemented");
    }

    @Override
    public void setTrack(Track track) {
        throw new RuntimeException("Unimplemented");
    }

    @Override
    public Frame add(int frames) {
        setValue(this.frame + frames);
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Frame clone() {
        return new CyclicFrame(frame, cycle);
    }

    @Override
    public void updateTo(Frame other) {
        setValue(other.getValue());
    }

    @Override
    public String toString() {
        return "<CyclicFrame: " + getValue() + ">";
    }

    public static CyclicFrame fromFrame(Frame frame, int cycle){
        int value = Frame.getCyclicFrameValue(frame.getValue(), cycle);
        return new CyclicFrame(value, cycle);
    }
}
