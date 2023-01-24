package com.jverbruggen.jrides.models.properties;

public class LinkedFrame implements Frame {
    private final Frame linkedTo;
    private final int offsetFromLink;
    private final int totalFrames;

    public LinkedFrame(Frame linkedTo, int offsetFromLink, int totalFrames) {
        this.linkedTo = linkedTo;
        this.offsetFromLink = offsetFromLink;
        this.totalFrames = totalFrames;
    }

    @Override
    public int getValue() {
        return Frame.getCyclicFrameValue(linkedTo.getValue() + offsetFromLink, totalFrames);
    }

    @Override
    public void setValue(int frame) {
        linkedTo.setValue(Frame.getCyclicFrameValue(frame - offsetFromLink, totalFrames));
    }

    @Override
    public Frame add(int frames) {
        linkedTo.add(frames);
        return this;
    }

    @Override
    public Frame clone() {
        return new SimpleFrame(getValue());
    }

    @Override
    public void updateTo(Frame other) {
        setValue(other.getValue());
    }

    @Override
    public String toString() {
        return "<Frame: " + getValue() + ">";
    }
}
