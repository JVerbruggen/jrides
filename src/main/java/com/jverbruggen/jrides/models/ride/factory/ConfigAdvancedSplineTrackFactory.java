/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.models.ride.factory;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.coaster.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.base.SectionConfig;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.factory.FrameFactory;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.track.compound.CompoundTrack;
import com.jverbruggen.jrides.models.ride.coaster.track.compound.LooseEndedSplineBasedTrack;
import com.jverbruggen.jrides.models.ride.factory.track.TrackDescription;
import com.jverbruggen.jrides.models.ride.factory.track.TrackType;
import com.jverbruggen.jrides.models.ride.section.builder.AdvancedSectionBuilder;
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
            }else throw new RuntimeException("TrackType not recognized");

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
        Frame startFrame = trackDescription.getStartFrame();
        Frame endFrame = trackDescription.getEndFrame();

        Track track = new LooseEndedSplineBasedTrack(trackDescription.getIdentifier(), positions, sectionBuilder.collectFor(parentTrackIdentifier),
                startFrame, endFrame);

        startFrame.setTrack(track);
        endFrame.setTrack(track);

        return track;
    }

    private AdvancedSectionBuilder createSectionBuilder(TrackConfig trackConfig){
        AdvancedSectionBuilder sectionBuilder = new AdvancedSectionBuilder();
        List<SectionConfig> sectionConfigs = trackConfig.getSections();

        for(int i = 0; i < sectionConfigs.size(); i++){
            sectionBuilder.add(sectionConfigs.get(i).build(trackBehaviourFactory, trackDescriptions, coasterHandle, coasterConfig));
        }

        sectionBuilder.calculate();
        sectionBuilder.populateTransfers(coasterHandle.getTransfers());
        sectionBuilder.populateBehaviours();

        return sectionBuilder;
    }
}
