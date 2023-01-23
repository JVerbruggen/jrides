package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;

import java.util.List;

public class SimpleTrack implements Track {
    private List<NoLimitsExportPositionRecord> positions;
    private List<Section> sections;

    public SimpleTrack(List<NoLimitsExportPositionRecord> positions, List<Section> sections) {
        this.positions = positions;
        this.sections = sections;
    }

    public List<NoLimitsExportPositionRecord> getRawPositions() {
        return positions;
    }

    @Override
    public List<Section> getSections() {
        return sections;
    }

    @Override
    public Section getNextSpawnSection() {
        return sections.get(0);
    }

    public int countPositions(){
        return positions.size();
    }

    public NoLimitsExportPositionRecord getPosition(int index){
        return positions.get(index);
    }
}
