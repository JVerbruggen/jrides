package com.jverbruggen.jrides.models.properties.frame;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;
import org.bukkit.Bukkit;

public class AutoTrackUpdateFrame implements Frame {
    private int value;
    private Track track;
    private Section section;
    private boolean invertFrameAddition;

    public AutoTrackUpdateFrame(int frame, Track track, Section section) {
        this(frame, track, section, false);
    }

    public AutoTrackUpdateFrame(int frame, Track track, Section section, boolean invertFrameAddition) {
        this.value = frame;
        this.track = track;
        this.section = section;
        this.invertFrameAddition = invertFrameAddition;

        updateTrack(frame);
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getValueString() {
        return String.valueOf(getValue());
    }

    @Override
    public void setValue(int frame) {
        this.value = frame;
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

        JRidesPlugin.getLogger().info(LogType.SECTIONS, "Updated frame " + this + " to section " + section);

        if(section.getParentTrack() != this.getTrack()){
            setTrack(section.getParentTrack());
        }
    }

    @Override
    public Frame add(int frames){
        if(invertFrameAddition) frames = -frames;
        return addRaw(frames);
    }

    private Frame addRaw(int frames){
        int newFrame = this.value + frames;
        return updateTrack(newFrame);
    }

    private Frame updateTrack(int toFrame){
        if(toFrame > track.getUpperFrame()){ // Going forwards and out of bounds
            setValue(getTrack().getLowerFrame());
            return addRaw(toFrame - track.getUpperFrame());
        }else if(toFrame < track.getLowerFrame()){ // Going backwards and out of bounds
            setValue(getTrack().getUpperFrame());
            return addRaw(toFrame - track.getLowerFrame());
        }else{ // Within bounds
            setValue(toFrame);
            return this;
        }
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Frame clone(){
        return new AutoTrackUpdateFrame(value, track, section, invertFrameAddition);
    }

    @Override
    public Frame capture() {
        return clone();
    }

    @Override
    public void setInvertedFrameAddition(boolean inverted) {
        JRidesPlugin.getLogger().info(LogType.SECTIONS, "Set inverted frame addition " + inverted);
        invertFrameAddition = inverted;
    }

    @Override
    public boolean isInvertedFrameAddition() {
        return invertFrameAddition;
    }

    @Override
    public void updateTo(Frame other){
        setValue(other.getValue());

        Track otherTrack = other.getTrack();
        Section otherSection = other.getSection();
        if(otherTrack != null && otherTrack != this.getTrack()) setTrack(otherTrack);
        if(otherSection != null && otherSection != this.getSection()) setSection(otherSection);
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

    public static AutoTrackUpdateFrame of(Frame frame){
        return new AutoTrackUpdateFrame(frame.getValue(), frame.getTrack(), frame.getSection());
    }
}
