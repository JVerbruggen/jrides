package com.jverbruggen.jrides.models.ride.coaster.track.compound;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public class CompoundTrack implements Track {
    private final List<Track> children;

    public CompoundTrack(List<Track> children) {
        this.children = children;
    }

    public List<Track> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "<Track>";
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public List<Section> getSections() {
        return null;
    }

    @Override
    public Section getNextSpawnSection() {
        return null;
    }

    @Override
    public Vector3 getLocationFor(Frame frame) {
        return null;
    }

    @Override
    public Quaternion getOrientationFor(Frame frame) {
        return null;
    }

    @Override
    public List<Vector3> getAllPositions() {
        return null;
    }

    @Override
    public Frame getFrameFor(int value) {
        return null;
    }
}
