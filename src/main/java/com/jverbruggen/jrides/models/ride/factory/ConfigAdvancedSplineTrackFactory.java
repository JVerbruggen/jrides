package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.SectionConfig;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
import com.jverbruggen.jrides.models.properties.factory.FrameFactory;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.track.compound.CompoundTrack;
import com.jverbruggen.jrides.models.ride.coaster.track.compound.LooseEndedSplineBasedTrack;
import com.jverbruggen.jrides.models.ride.factory.track.TrackDescription;
import com.jverbruggen.jrides.models.ride.factory.track.TrackType;
import com.jverbruggen.jrides.models.ride.section.SectionBuilder;
import com.jverbruggen.jrides.models.ride.section.SimpleSection;
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
        List<Track> childrenTracks = new ArrayList<>();

        for(TrackDescription trackDescription : trackDescriptions){
            Track track;
            if(trackDescription.getTrackType() == TrackType.TRACK){
                track = createSplineTrack(trackDescription);
            }else if(trackDescription.getTrackType() == TrackType.TRANSFER){
                track = null;
            }
        }

        return new CompoundTrack(childrenTracks);
    }

    public Track createSplineTrack(TrackDescription trackDescription){
        List<NoLimitsExportPositionRecord> positions = trackDescription.getPositions();
        SectionBuilder sectionBuilder = new SectionBuilder(false);
        int totalFrames = positions.size();
        int globalOffset = coasterConfig.getTrack().getOffset();

        TrackConfig trackConfig = coasterConfig.getTrack();

        Frame firstStartFrame = null;
        Frame previousEndFrame = null;
        List<SectionConfig> sectionConfigs = trackConfig.getSections();

        for(int i = 0; i < sectionConfigs.size(); i++){
            SectionConfig sectionConfig = sectionConfigs.get(i);

            // set startFrame of this section to the previous endFrame is it exists
            Frame startFrame = (previousEndFrame != null)
                    ? previousEndFrame
                    : new SimpleFrame(sectionConfig.getLowerRange() + globalOffset);

            Frame endFrame;
            // Set endFrame to startFrame if theres only 1 section
            if(sectionConfigs.size() == 1){
                endFrame = startFrame;
                // .. or to firstStartFrame if it is the last, to make it cyclic
            }else if (i == sectionConfigs.size()-1){
                endFrame = firstStartFrame;
                // .. or else make a new endFrame
            }else{
                endFrame = new SimpleFrame(sectionConfig.getUpperRange() + globalOffset);
            }

            TrackBehaviour trackBehaviour = trackBehaviourFactory.getTrackBehaviourFor(coasterHandle, coasterConfig, sectionConfig, totalFrames);
            if(trackBehaviour == null) return null;

            sectionBuilder.add(new SimpleSection(startFrame, endFrame, trackBehaviour));

            if(firstStartFrame == null) firstStartFrame = startFrame;
            previousEndFrame = endFrame;
        }

        return new LooseEndedSplineBasedTrack(positions, sectionBuilder.collect(), trackDescription.getStartFrame(), trackDescription.getEndFrame());
    }
}
