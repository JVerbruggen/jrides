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
