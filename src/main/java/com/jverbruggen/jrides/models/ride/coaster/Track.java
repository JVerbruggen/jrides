package com.jverbruggen.jrides.models.ride.coaster;

        import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
        import com.jverbruggen.jrides.models.ride.section.Section;

        import java.util.List;

public interface Track {
    List<NoLimitsExportPositionRecord> getRawPositions();
    int getRawPositionsCount();
    List<Section> getSections();
    Section getNextSpawnSection();
}
