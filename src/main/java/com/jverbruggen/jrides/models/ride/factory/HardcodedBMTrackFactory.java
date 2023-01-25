package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
import com.jverbruggen.jrides.models.properties.factory.FrameFactory;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SectionBuilder;
import com.jverbruggen.jrides.models.ride.section.SimpleSection;
import com.jverbruggen.jrides.models.ride.coaster.SimpleTrack;
import com.jverbruggen.jrides.models.ride.coaster.Track;

import java.util.List;

public class HardcodedBMTrackFactory implements TrackFactory {
    private final TrackBehaviourFactory trackBehaviourFactory;
    private final FrameFactory frameFactory;

    public HardcodedBMTrackFactory(TrackBehaviourFactory trackBehaviourFactory, FrameFactory frameFactory) {
        this.trackBehaviourFactory = trackBehaviourFactory;
        this.frameFactory = frameFactory;
    }

    @Override
    public Track createSimpleTrack(CoasterConfig coasterConfig, List<NoLimitsExportPositionRecord> positions, int startOffset){
        Frame stationBegin = new SimpleFrame(startOffset - 1700);
        Frame stationEnd = new SimpleFrame(startOffset - 1100);
        Frame blockBrakeBegin = new SimpleFrame(startOffset - 2200);
        Frame liftEnd = new SimpleFrame(startOffset + 200);
        Frame blockBlackEngage = frameFactory.createFrameBetween(blockBrakeBegin, stationBegin, 0.9);
        Frame stationBlockEngage = frameFactory.createFrameBetween(stationBegin, stationEnd, 0.95);
        Frame liftBlockEngage = frameFactory.createFrameBetween(stationEnd, liftEnd, 0.98);


        SectionBuilder sectionBuilder = new SectionBuilder();
        List<Section> sections = sectionBuilder
                .add(new SimpleSection(stationBegin, stationEnd, trackBehaviourFactory.getBlockBrakeBehaviour(stationBlockEngage, true)))
                .add(new SimpleSection(stationEnd, liftEnd, trackBehaviourFactory.getBlockBrakeBehaviour(liftBlockEngage, true)))
                .add(new SimpleSection(liftEnd, blockBrakeBegin, trackBehaviourFactory.getTrackBehaviour()))
                .add(new SimpleSection(blockBrakeBegin, stationBegin, trackBehaviourFactory.getBlockBrakeBehaviour(blockBlackEngage, true)))
                .collect();

        return new SimpleTrack(positions, sections);
    }
}
