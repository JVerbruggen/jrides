package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.models.ride.coaster.Section;
import com.jverbruggen.jrides.models.ride.coaster.SimpleSection;
import com.jverbruggen.jrides.models.ride.coaster.SimpleTrack;
import com.jverbruggen.jrides.models.ride.coaster.Track;

import java.util.List;

public class TrackFactory {
    public Track createSimpleTrack(List<NoLimitsExportPositionRecord> positions, int startOffset){
        List<Section> sections = List.of(new SimpleSection(startOffset));

        return new SimpleTrack(positions, sections);
    }
}
