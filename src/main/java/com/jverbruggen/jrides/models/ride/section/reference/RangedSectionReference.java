package com.jverbruggen.jrides.models.ride.section.reference;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SimpleSection;

public class RangedSectionReference extends SectionReference {
    private final String sectionIdentifier;
    private final String nextSectionIdentifier;
    private String previousSectionIdentifier;
    private final String parentTrackIdentifier;
    private final Frame startFrame;
    private final Frame endFrame;
    private final TrackBehaviour trackBehaviour;
    private final boolean jumpAtStart;
    private final boolean jumpAtEnd;

    public RangedSectionReference(String sectionIdentifier, Frame startFrame, Frame endFrame, TrackBehaviour trackBehaviour, String nextSectionIdentifier,
                                  String parentTrackIdentifier, boolean jumpAtStart, boolean jumpAtEnd) {
        this.sectionIdentifier = sectionIdentifier;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.trackBehaviour = trackBehaviour;
        this.nextSectionIdentifier = nextSectionIdentifier;
        this.jumpAtStart = jumpAtStart;
        this.jumpAtEnd = jumpAtEnd;
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

    public Frame getStartFrame() {
        return startFrame;
    }

    public Frame getEndFrame() {
        return endFrame;
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
        Frame startFrame = getStartFrame();
        Frame endFrame = getEndFrame();
        TrackBehaviour trackBehaviour = getTrackBehaviour();
        boolean jumpAtStart = isJumpAtStart();
        boolean jumpAtEnd = isJumpAtEnd();

        SimpleSection section = new SimpleSection(startFrame, endFrame, trackBehaviour, jumpAtStart, jumpAtEnd);
        section.setName(getSectionIdentifier());
        return section;
    }

    public boolean isJumpAtEnd() {
        return jumpAtEnd;
    }

    public boolean isJumpAtStart() {
        return jumpAtStart;
    }

}
