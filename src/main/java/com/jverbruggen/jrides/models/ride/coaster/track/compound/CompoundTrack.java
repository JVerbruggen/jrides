package com.jverbruggen.jrides.models.ride.coaster.track.compound;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;
import java.util.stream.Collectors;

public class CompoundTrack implements Track {
    private final List<Track> children;

    public CompoundTrack(List<Track> children) {
        this.children = children;
    }

    public List<Track> getChildren() {
        return children;
    }

    @Override
    public int getLength() {
        return splinePositionsCount;
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
        if(!frame.getTrack().equals(this))
            throw new RuntimeException("Cannot get frame location if the frame is not on this track");

        return splinePositions.get(frame.getValue()).toVector3();
    }

    @Override
    public Quaternion getOrientationFor(Frame frame) {
        if(!frame.getTrack().equals(this))
            throw new RuntimeException("Cannot get frame location if the frame is not on this track");

        return splinePositions.get(frame.getValue()).getOrientation();
    }

    @Override
    public List<Vector3> getAllPositions() {
        return splinePositions.stream().map(NoLimitsExportPositionRecord::toVector3).collect(Collectors.toList());
    }

    public int countPositions(){
        return splinePositions.size();
    }

    public NoLimitsExportPositionRecord getPosition(int index){
        return splinePositions.get(index);
    }

    @Override
    public String toString() {
        return "<Track>";
    }
}
