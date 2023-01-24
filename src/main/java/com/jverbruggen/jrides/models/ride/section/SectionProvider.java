package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import com.jverbruggen.jrides.models.ride.section.exception.SectionNotFoundException;

import java.util.List;

public class SectionProvider {
    private final Track track;

    public SectionProvider(Track track) {
        this.track = track;
    }

    public Section getSectionFor(Train train, Frame frame){
        Section currentSection = train.getCurrentSection();
        if(currentSection.isInSection(frame)) return null;

        List<Section> sections = track.getSections();
        // TODO: currentSection.next() (or something similar) would be more efficient to find next section

        Section found = null;
        int i = 0;
        while(found == null && i < sections.size()){
            Section compare = sections.get(i);

            if(compare.isInSection(frame)) found = compare;

            i++;
        }

        if(found == null) throw new SectionNotFoundException(train);

        return found;
    }
}
