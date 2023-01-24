package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
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
        Frame stationBegin = new SimpleFrame(startOffset - 2000);
        Frame stationEnd = new SimpleFrame(startOffset - 1000);

        Section stationSection = new SimpleSection(stationBegin, stationEnd, trackBehaviourFactory.getBrakeBehaviour(20_000));
        Section trackSection = new SimpleSection(stationEnd, stationBegin, trackBehaviourFactory.getTrackBehaviour());
        List<Section> sections = List.of(stationSection, trackSection);

        return new SimpleTrack(positions, sections);
    }
}
