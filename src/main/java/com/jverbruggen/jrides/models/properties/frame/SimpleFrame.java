package com.jverbruggen.jrides.models.properties.frame;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

public class SimpleFrame implements Frame {
    private int frame;
    private Track track;
    private Section section;

    public SimpleFrame(int frame, Track track, Section section) {
        this.frame = frame;
        this.track = track;
        this.section = section;
    }

    public SimpleFrame(int frame) {
        this.frame = frame;
    }

    public SimpleFrame(int frame, Track track) {
        this.frame = frame;
        this.track = track;
    }

    @Override
    public int getValue() {
        return frame;
    }

    @Override
    public void setValue(int frame) {
        this.frame = frame;
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
    public Frame add(int frames){
        this.frame += frames;
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Frame clone(){
        return new SimpleFrame(frame, track, section);
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
    public void updateTo(Frame other){
        this.frame = other.getValue();
    }

    @Override
    public String toString() {
        return "<Frame: " + getValue() + ">";
    }

    public boolean equals(Frame other) {
        return this.getValue() == other.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Frame) return equals((Frame) obj);
        return super.equals(obj);
    }
}
