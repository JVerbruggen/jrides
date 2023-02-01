package com.jverbruggen.jrides.models.properties;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;

public class SimpleFrame implements Frame {
    private int frame;
    private Track track;

    public SimpleFrame(int frame, Track track) {
        this.frame = frame;
        this.track = track;
    }

    public SimpleFrame(int frame) {
        this.frame = frame;
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
    public Frame add(int frames){
        this.frame += frames;
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Frame clone(){
        return new SimpleFrame(frame, track);
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
