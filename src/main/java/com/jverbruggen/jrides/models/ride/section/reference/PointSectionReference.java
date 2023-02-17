package com.jverbruggen.jrides.models.ride.section.reference;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.section.PointSection;
import com.jverbruggen.jrides.models.ride.section.Section;

public class PointSectionReference extends SectionReference {
    private final String sectionIdentifier;
    private final String nextSectionIdentifier;
    private String previousSectionIdentifier;
    private final String parentTrackIdentifier;
    private final Frame point;
    private final TrackBehaviour trackBehaviour;

    public PointSectionReference(String sectionIdentifier, Frame point, TrackBehaviour trackBehaviour, String nextSectionIdentifier,
                                 String parentTrackIdentifier) {
        this.sectionIdentifier = sectionIdentifier;
        this.point = point;
        this.trackBehaviour = trackBehaviour;
        this.nextSectionIdentifier = nextSectionIdentifier;
        this.previousSectionIdentifier = null;
        this.parentTrackIdentifier = parentTrackIdentifier;
    }

    @Override
    public void setPreviousSectionIdentifier(String previousSectionIdentifier) {
        this.previousSectionIdentifier = previousSectionIdentifier;
    }

    @Override
    public String getPreviousSectionIdentifier() {
        return previousSectionIdentifier;
    }

    @Override
    public String getSectionIdentifier() {
        return sectionIdentifier;
    }

    public TrackBehaviour getTrackBehaviour() {
        return trackBehaviour;
    }

    @Override
    public String getNextSectionIdentifier() {
        return nextSectionIdentifier;
    }

    @Override
    public String getParentTrackIdentifier() {
        return parentTrackIdentifier;
    }

    @Override
    public Section makeSection() {
        return new PointSection(trackBehaviour, point, sectionIdentifier);
    }
}
