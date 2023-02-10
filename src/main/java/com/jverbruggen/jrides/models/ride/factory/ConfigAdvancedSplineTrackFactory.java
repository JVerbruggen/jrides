package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.SectionConfig;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.SimpleFrame;
import com.jverbruggen.jrides.models.properties.frame.factory.FrameFactory;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.track.compound.CompoundTrack;
import com.jverbruggen.jrides.models.ride.coaster.track.compound.LooseEndedSplineBasedTrack;
import com.jverbruggen.jrides.models.ride.factory.track.TrackDescription;
import com.jverbruggen.jrides.models.ride.factory.track.TrackType;
import com.jverbruggen.jrides.models.ride.section.AdvancedSectionBuilder;
import com.jverbruggen.jrides.models.ride.section.SectionReference;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class ConfigAdvancedSplineTrackFactory implements TrackFactory {
    private final TrackBehaviourFactory trackBehaviourFactory;
    private final FrameFactory frameFactory;

    private final CoasterHandle coasterHandle;
    private final CoasterConfig coasterConfig;
    private final List<TrackDescription> trackDescriptions;

    public ConfigAdvancedSplineTrackFactory(CoasterHandle coasterHandle, CoasterConfig coasterConfig, List<TrackDescription> trackDescriptions) {
        this.coasterHandle = coasterHandle;
        this.coasterConfig = coasterConfig;
        this.trackDescriptions = trackDescriptions;
        this.trackBehaviourFactory = ServiceProvider.getSingleton(TrackBehaviourFactory.class);
        this.frameFactory = ServiceProvider.getSingleton(FrameFactory.class);
    }

    @Override
    public Track createTrack(){
        // --- Calculate sections
        TrackConfig trackConfig = coasterConfig.getTrack();
        AdvancedSectionBuilder sectionBuilder = createSectionBuilder(trackConfig);

        // --- Assemble tracks
        List<Track> childrenTracks = new ArrayList<>();
        for(TrackDescription trackDescription : trackDescriptions){
            Track track;
            if(trackDescription.getTrackType() == TrackType.TRACK){
                track = createSplineTrack(trackDescription, sectionBuilder);
            }else if(trackDescription.getTrackType() == TrackType.TRANSFER){
                track = null;
            }else throw new RuntimeException("TrackType not recognized");

            trackDescription.getStartFrame().setTrack(track);
            trackDescription.getEndFrame().setTrack(track);

            childrenTracks.add(track);
        }

        // --- Tie track and section ends together
        Track prevTrack = null;
        Track firstTrack = null;
        for(int i = 0; i < childrenTracks.size(); i++){
            Track track = childrenTracks.get(i);
            if(firstTrack == null) firstTrack = track;
            boolean isLast = i == childrenTracks.size()-1;

            if(prevTrack != null){
                prevTrack.setNextTrack(track);
                track.setPreviousTrack(prevTrack);
            }
            if(isLast){
                track.setNextTrack(firstTrack);
                firstTrack.setPreviousTrack(track);
            }

            prevTrack = track;
        }

        return new CompoundTrack(childrenTracks);
    }

    public Track createSplineTrack(TrackDescription trackDescription, AdvancedSectionBuilder sectionBuilder){
        List<NoLimitsExportPositionRecord> positions = trackDescription.getPositions();

        String parentTrackIdentifier = trackDescription.getIdentifier();

        return new LooseEndedSplineBasedTrack(trackDescription.getIdentifier(), positions, sectionBuilder.collectFor(parentTrackIdentifier),
                trackDescription.getStartFrame(), trackDescription.getEndFrame());
    }

    private AdvancedSectionBuilder createSectionBuilder(TrackConfig trackConfig){
        AdvancedSectionBuilder sectionBuilder = new AdvancedSectionBuilder();
        List<SectionConfig> sectionConfigs = trackConfig.getSections();

        Frame firstStartFrame = null;
        Frame previousEndFrame = null;

        for(int i = 0; i < sectionConfigs.size(); i++){
            SectionConfig sectionConfig = sectionConfigs.get(i);
            String sectionIdentifier = sectionConfig.getIdentifier();
            String nextSectionIdentifier = sectionConfig.getNextSection();
            String parentTrackIdentifier = sectionConfig.getParentTrackIdentifier();

            TrackDescription trackDescription = trackDescriptions.stream()
                    .filter(d -> d.getIdentifier().equalsIgnoreCase(sectionConfig.getParentTrackIdentifier()))
                    .findFirst().orElseThrow();

            Frame startFrame = new SimpleFrame(sectionConfig.getLowerRange());
            Frame endFrame = new SimpleFrame(sectionConfig.getUpperRange());

            TrackBehaviour trackBehaviour = trackBehaviourFactory.getTrackBehaviourFor(coasterHandle, coasterConfig, sectionConfig, trackDescription.getCycle());
            if(trackBehaviour == null) return null;

            sectionBuilder.add(new SectionReference(sectionIdentifier, startFrame, endFrame, trackBehaviour, nextSectionIdentifier, parentTrackIdentifier));

            if(firstStartFrame == null) firstStartFrame = startFrame;
            previousEndFrame = endFrame;
        }

        sectionBuilder.calculate();
        sectionBuilder.populateTransfers(coasterHandle.getTransfers());

        return sectionBuilder;
    }
}
