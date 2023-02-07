package com.jverbruggen.jrides.models.properties;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;
import org.bukkit.Bukkit;

public class AutoTrackUpdateFrame implements Frame {
    private int frame;
    private Track track;

    public AutoTrackUpdateFrame(int frame, Track track) {
        this.frame = frame;
        this.track = track;
    }

    public AutoTrackUpdateFrame(int frame) {
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
        if(track == this.track) return;

        this.track = track;
    }
//
//    @Override
//    public Section getSection() {
//        throw new RuntimeException("Cannot get section of AU frame");
//    }
//
//    @Override
//    public void setSection(Section section) {
//        throw new RuntimeException("Cannot set section of AU frame");
//    }

    @Override
    public Frame add(int frames){
        int newFrame = this.frame + frames;
        if(newFrame > track.getUpperFrame()){ // Going forwards and out of bounds
            Track newTrack = track.getNextTrack();
            setTrack(newTrack);
            setValue(newTrack.getLowerFrame());
            return add(newFrame - track.getUpperFrame());
        }else if(newFrame < track.getLowerFrame()){ // Going backwards and out of bounds
            Track newTrack = track.getPreviousTrack();
            setTrack(newTrack);
            setValue(newTrack.getUpperFrame());
            return add(newFrame - track.getLowerFrame());
        }else{ // Within bounds
            setValue(newFrame);
            return this;
        }
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Frame clone(){
        return new AutoTrackUpdateFrame(frame, track);
    }

    @Override
    public Frame capture() {
        return clone();
    }

    @Override
    public void updateTo(Frame other){
        this.frame = other.getValue();
    }

    @Override
    public String toString() {
        return "<AU-Frame: " + getValue() + ">";
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
