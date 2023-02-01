package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;
import java.util.stream.Collectors;

public class SplineBasedTrack implements Track {
    private List<NoLimitsExportPositionRecord> splinePositions;
    private List<Section> sections;
    private int splinePositionsCount;

    public SplineBasedTrack(List<NoLimitsExportPositionRecord> splinePositions, List<Section> sections) {
        this.splinePositions = splinePositions;
        this.sections = sections;
        this.splinePositionsCount = splinePositions.size();

        this.sections.forEach(s-> s.setParentTrack(this));
    }

    @Override
    public int getRawPositionsCount() {
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
        return splinePositions.get(frame.getValue()).toVector3();
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
