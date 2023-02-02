package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.exception.SectionNotFoundException;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class SectionProvider {
    public @NonNull Section getSectionFor(Train train, Section currentSection, Frame fromFrame, Frame toFrame){
        if(fromFrame.getTrack() != toFrame.getTrack()){
            return getSectionOnDifferentTrack(currentSection, toFrame);
        }

        List<Section> sections = toFrame.getTrack().getSections();
        // TODO: currentSection.next() (or something similar) would be more efficient to find next section

        Section found = null;
        int i = 0;
        while(found == null && i < sections.size()){
            Section compare = sections.get(i);

            if(compare.isInSection(toFrame)) found = compare;

            i++;
        }

        if(found == null) throw new SectionNotFoundException(train);

        return found;
    }

    public Section getSectionOnDifferentTrack(Section currentSection, Frame toFrame){
        Track newTrack = toFrame.getTrack();
        List<Section> newTrackSections = newTrack.getSections();

        // If rolling forwards
        Section logicalNextSection = currentSection.next();
        Section firstSectionNewTrack = newTrackSections.get(0);
        if(logicalNextSection.equals(firstSectionNewTrack)){
            return logicalNextSection;
        }

        // If rolling backwards
        Section logicalPreviousSection = currentSection.previous();
        Section lastSectionNewTrack = newTrackSections.get(newTrackSections.size()-1);
        if(logicalPreviousSection.equals(lastSectionNewTrack)){
            return logicalPreviousSection;
        }

        throw new RuntimeException("Unknown situation to handle section on different track");
    }
}
