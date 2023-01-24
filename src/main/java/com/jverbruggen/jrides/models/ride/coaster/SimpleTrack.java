package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public class SimpleTrack implements Track {
    private List<NoLimitsExportPositionRecord> positions;
    private List<Section> sections;
    private int positionsCount;

    public SimpleTrack(List<NoLimitsExportPositionRecord> positions, List<Section> sections) {
        this.positions = positions;
        this.sections = sections;
        this.positionsCount = positions.size();
    }

    public List<NoLimitsExportPositionRecord> getRawPositions() {
        return positions;
    }

    @Override
    public int getRawPositionsCount() {
        return positionsCount;
    }

    @Override
    public List<Section> getSections() {
        return sections;
    }

    @Override
    public Section getNextSpawnSection() {
        for(Section section : sections){
            if(!section.isOccupied()) return section;
        }
        return null;
    }

    public int countPositions(){
        return positions.size();
    }

    public NoLimitsExportPositionRecord getPosition(int index){
        return positions.get(index);
    }

    @Override
    public String toString() {
        return "<Track>";
    }
}
