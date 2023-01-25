package com.jverbruggen.jrides.models.properties;

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
    public Frame add(int frames) {
        setValue(this.frame + frames);
        return this;
    }

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
        return new CyclicFrame(frame.getValue(), cycle);
    }
}
