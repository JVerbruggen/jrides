package com.jverbruggen.jrides.models.properties;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;

public class LinkedFrame implements Frame {
    private final Frame linkedTo;
    private final int offsetFromLink;

    public LinkedFrame(Frame linkedTo, int offsetFromLink) {
        this.linkedTo = linkedTo;
        this.offsetFromLink = offsetFromLink;
    }

    @Override
    public int getValue() {
        return linkedTo.clone().add(offsetFromLink).getValue();
    }

    @Override
    public void setValue(int frame) {
        linkedTo.setValue(frame - offsetFromLink);
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
        linkedTo.add(frames);
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
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
