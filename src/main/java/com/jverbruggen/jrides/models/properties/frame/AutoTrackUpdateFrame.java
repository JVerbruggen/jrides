package com.jverbruggen.jrides.models.properties.frame;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;
import org.bukkit.Bukkit;

public class AutoTrackUpdateFrame implements Frame {
    private int frame;
    private Track track;
    private Section section;
    private boolean invertFrameAddition;

    public AutoTrackUpdateFrame(int frame, Track track, Section section) {
        this(frame, track, section, false);
    }

    public AutoTrackUpdateFrame(int frame, Track track, Section section, boolean invertFrameAddition) {
        this.frame = frame;
        this.track = track;
        this.section = section;
        this.invertFrameAddition = invertFrameAddition;

        updateTrack(frame);
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
        if(invertFrameAddition) frames = -frames;

        int newFrame = this.frame + frames;
        return updateTrack(newFrame);
    }

    private Frame updateTrack(int toFrame){
        if(toFrame > track.getUpperFrame()){ // Going forwards and out of bounds
            Track newTrack = track.getNextTrack();
            setTrack(newTrack);
            setValue(newTrack.getLowerFrame());
            return add(toFrame - track.getUpperFrame());
        }else if(toFrame < track.getLowerFrame()){ // Going backwards and out of bounds
            Track newTrack = track.getPreviousTrack();
            setTrack(newTrack);
            setValue(newTrack.getUpperFrame());
            return add(toFrame - track.getLowerFrame());
        }else{ // Within bounds
            setValue(toFrame);
            return this;
        }
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Frame clone(){
        return new AutoTrackUpdateFrame(frame, track, section, invertFrameAddition);
    }

    @Override
    public Frame capture() {
        return clone();
    }

    @Override
    public void setInvertedFrameAddition(boolean inverted) {
        Bukkit.broadcastMessage("Set inverted frame addition " + inverted);
        invertFrameAddition = inverted;
    }

    @Override
    public void updateTo(Frame other){
        this.frame = other.getValue();
    }

    @Override
    public String toString() {
        String invertedTag = "";
        if(invertFrameAddition) invertedTag = " [invert] ";
        return "<AU-Frame: " + getValue() + invertedTag + " (" + section.getName() + ")>";
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
