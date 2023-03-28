package com.jverbruggen.jrides.models.properties.frame;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

public class LinkedFrame implements Frame {
    private final Frame linkedTo;
    private final int offsetFromLink;
    private Section section;
    private boolean canSetValue;

    public LinkedFrame(Frame linkedTo, int offsetFromLink, Section section) {
        this.linkedTo = linkedTo;
        this.offsetFromLink = offsetFromLink;
        this.section = section;
        this.canSetValue = true;
    }

    @Override
    public int getValue() {
        return linkedTo.clone().add(offsetFromLink).getValue();
    }

    @Override
    public String getValueString() {
        return String.valueOf(getValue());
    }

    @Override
    public void setValue(int frame) {
        if(!canSetValue)
            throw new RuntimeException("Cannot set value of this linked frame " + this);
        linkedTo.setValue(frame - offsetFromLink);
    }

    @Override
    public Track getTrack() {
        return linkedTo.getTrack();
    }

    @Override
    public void setTrack(Track track) {
        linkedTo.setTrack(track);
    }

    @Override
    public Section getSection() {
        throw new RuntimeException("LinkedFrames dont have accurate sections");
    }

    @Override
    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public Frame add(int frames) {
        linkedTo.add(frames);
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Frame clone() {
        return new LinkedFrame(linkedTo, offsetFromLink, section);
    }

    @Override
    public Frame capture() {
        return new LinkedFrame(linkedTo.capture(), offsetFromLink, section);
    }

    @Override
    public void setInvertedFrameAddition(boolean inverted) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isInvertedFrameAddition() {
        return false;
    }

    @Override
    public void updateTo(Frame other) {
        setValue(other.getValue());
    }

    @Override
    public String toString() {
        return "<LinkedFrame: " + getValue() + " to: " + linkedTo + ">";
    }
}
