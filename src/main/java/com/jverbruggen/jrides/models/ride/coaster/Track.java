package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;

import java.util.List;

public interface Track {
    List<NoLimitsExportPositionRecord> getRawPositions();
    List<Section> getSections();
    Section getNextSpawnSection();
}
