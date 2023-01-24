package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.trackbehaviour.FreeMovementTrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SimpleSection;
import com.jverbruggen.jrides.models.ride.coaster.SimpleTrack;
import com.jverbruggen.jrides.models.ride.coaster.Track;

import java.util.List;

public class TrackFactory {
    private final TrackBehaviourFactory trackBehaviourFactory;

    public TrackFactory(TrackBehaviourFactory trackBehaviourFactory) {
        this.trackBehaviourFactory = trackBehaviourFactory;
    }

    public Track createSimpleTrack(List<NoLimitsExportPositionRecord> positions, int startOffset){
        int stationBegin = startOffset - 2000;
        int stationEnd = startOffset - 1000;

        TrackBehaviour stationSectionBehaviour = trackBehaviourFactory.getBrakeBehaviour();
        TrackBehaviour trackSectionBehaviour = trackBehaviourFactory.getTrackBehaviour();

        Section trackSection = new SimpleSection(stationEnd, stationBegin, trackSectionBehaviour);
        Section stationSection = new SimpleSection(stationBegin, stationEnd, stationSectionBehaviour);
        List<Section> sections = List.of(stationSection, trackSection);

        return new SimpleTrack(positions, sections);
    }
}
