package com.jverbruggen.jrides.models.ride.coaster.track.compound;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.ArrayList;
import java.util.List;

public class CompoundTrack implements Track {
    private final List<Track> children;
    private final List<Section> sections;
    private final List<Vector3> allPositions;

    public CompoundTrack(List<Track> children) {
        this.children = children;
        this.sections = new ArrayList<>();
        this.allPositions = new ArrayList<>();

        this.children.forEach(s -> {
            this.sections.addAll(s.getSections());
            this.allPositions.addAll(s.getAllPositions());
        });
    }

    public List<Track> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "<CompoundTrack>";
    }

    @Override
    public String getIdentifier() {
        return "Main Compound Track";
    }

    @Override
    public int getLength() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public List<Section> getSections() {
        return sections;
    }

    @Override
    public Section getNextSpawnSection() {
        for(Section section : sections){
            if(!section.isOccupied() && section.canTrainSpawnOn()) return section;
        }
        return null;
    }

    @Override
    public Vector3 getLocationFor(Frame frame) {
        Track track = frame.getTrack();
        if(track.equals(this))
            throw new RuntimeException("Frame cannot be bound to compound track");
        return track.getLocationFor(frame);
    }

    @Override
    public Quaternion getOrientationFor(Frame frame) {
        Track track = frame.getTrack();
        if(track.equals(this))
            throw new RuntimeException("Frame cannot be bound to compound track");
        return track.getOrientationFor(frame);
    }

    @Override
    public List<Vector3> getAllPositions() {
        return allPositions;
    }

    @Override
    public int getLowerFrame() {
        throw new RuntimeException("Cannot get frame bounds for compound track");
    }

    @Override
    public int getUpperFrame() {
        throw new RuntimeException("Cannot get frame bounds for compound track");
    }

    @Override
    public Track getNextTrack() {
        throw new RuntimeException("Cannot get adjacent track for compound track");
    }

    @Override
    public Track getPreviousTrack() {
        throw new RuntimeException("Cannot get adjacent track for compound track");
    }

    @Override
    public void setNextTrack(Track track) {
        throw new RuntimeException("Cannot set adjacent track for compound track");
    }

    @Override
    public void setPreviousTrack(Track track) {
        throw new RuntimeException("Cannot set adjacent track for compound track");
    }
}
