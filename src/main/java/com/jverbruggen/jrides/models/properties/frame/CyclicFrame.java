package com.jverbruggen.jrides.models.properties.frame;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

public class CyclicFrame implements Frame {
    private int frame;
    private final int cycle;
    private Track track;
    private Section section;

    public CyclicFrame(int frame, int cycle, Track track, Section section) {
        this.frame = frame;
        this.cycle = cycle;
        this.track = track;
        this.section = section;
    }

    @Override
    public int getValue() {
        return frame;
    }

    @Override
    public String getValueString() {
        return String.valueOf(getValue());
    }

    @Override
    public void setValue(int frame) {
        this.frame = Frame.getCyclicFrameValue(frame, cycle);
    }

    @Override
    public Track getTrack() {
        return track;
    }

    @Override
    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    public Section getSection() {
        return section;
    }

    @Override
    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public Frame add(int frames) {
        setValue(this.frame + frames);
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Frame clone() {
        return new CyclicFrame(frame, cycle, track, section);
    }

    @Override
    public Frame capture() {
        return clone();
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
        return "<CyclicFrame: " + getValue() + ">";
    }
//
//    public static CyclicFrame fromFrame(Frame frame, int cycle){
//        int value = Frame.getCyclicFrameValue(frame.getValue(), cycle);
//        return new CyclicFrame(value, cycle);
//    }
}
