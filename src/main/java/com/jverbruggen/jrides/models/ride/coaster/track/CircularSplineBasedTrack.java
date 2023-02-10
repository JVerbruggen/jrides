package com.jverbruggen.jrides.models.ride.coaster.track;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;
import java.util.stream.Collectors;

public class CircularSplineBasedTrack implements Track {
    private final String identifier;
    private List<NoLimitsExportPositionRecord> splinePositions;
    private List<Section> sections;
    private int splinePositionsCount;

    public CircularSplineBasedTrack(String identifier, List<NoLimitsExportPositionRecord> splinePositions, List<Section> sections) {
        this.identifier = identifier;
        this.splinePositions = splinePositions;
        this.sections = sections;
        this.splinePositionsCount = splinePositions.size();

        this.sections.forEach(s-> s.setParentTrack(this));
    }

    @Override
    public String getIdentifier() {
        return identifier;
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
//
//    @Override
//    public Frame getFrameFor(int value) {
//        return new CyclicFrame(value, this.getLength(), this);
//    }

    @Override
    public int getLowerFrame() {
        return 0;
    }

    @Override
    public int getUpperFrame() {
        return splinePositions.size()-1;
    }

    @Override
    public boolean inThisTrack(int frame) {
        return getLowerFrame() <= frame && frame <= getUpperFrame();
    }

    @Override
    public Track getNextTrack() {
        return this;
    }

    @Override
    public Track getPreviousTrack() {
        return this;
    }

    @Override
    public void setNextTrack(Track track) {

    }

    @Override
    public void setPreviousTrack(Track track) {

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
