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

package com.jverbruggen.jrides.models.properties.frame.factory;

import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.SimpleFrame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.provider.SectionProvider;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

public class FrameFactory {
    private final SectionProvider sectionProvider;

    public FrameFactory() {
        sectionProvider = ServiceProvider.getSingleton(SectionProvider.class);
    }

    public SimpleFrame getStaticFrame(int frameIndex, Track track){
        if(track == null)
            throw new RuntimeException("Track cannot be null");

        SimpleFrame frame = new SimpleFrame(frameIndex, track);
        Section section = sectionProvider.findSectionInBulk(frame, track.getSections());
        if(section == null)
            throw new RuntimeException("Could not find section for static frame " + frameIndex);

        frame.setTrack(track);
        frame.setSection(section);

        return frame;
    }

    public SimpleFrame getBlankStaticFrame(int frameIndex){
        return new SimpleFrame(frameIndex);
    }

    public Frame createFrameBetween(Frame a, Frame b, double percentage){
        assert percentage <= 1 && percentage >= 0;

        int aValue = a.getValue();
        int bValue = b.getValue();
        int delta = bValue - aValue;
        int offset = (int) (delta * percentage);

        return new SimpleFrame(aValue + offset);
    }
}
