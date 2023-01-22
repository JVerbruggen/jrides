package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;

import java.util.List;

public class SimpleTrack implements Track {
    private List<NoLimitsExportPositionRecord> positions;

    public SimpleTrack(List<NoLimitsExportPositionRecord> positions) {
        this.positions = positions;
    }

    public List<NoLimitsExportPositionRecord> getRawPositions() {
        return positions;
    }

    public int countPositions(){
        return positions.size();
    }

    public NoLimitsExportPositionRecord getPosition(int index){
        return positions.get(index);
    }
}
