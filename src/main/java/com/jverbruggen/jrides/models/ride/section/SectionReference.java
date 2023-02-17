package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.config.coaster.objects.connection.ConnectionsConfig;
import com.jverbruggen.jrides.models.properties.frame.Frame;

import java.util.Map;

public class SectionReference {
    private final String sectionIdentifier;
    private final String nextSectionIdentifier;
    private String previousSectionIdentifier;
    private final String parentTrackIdentifier;
    private final Frame startFrame;
    private final Frame endFrame;
    private final TrackBehaviour trackBehaviour;
    private final ConnectionsConfig connectionsConfig;
    private final boolean jumpAtStart;
    private final boolean jumpAtEnd;

    public SectionReference(String sectionIdentifier, Frame startFrame, Frame endFrame, TrackBehaviour trackBehaviour, String nextSectionIdentifier,
                            String parentTrackIdentifier, ConnectionsConfig connectionsConfig, boolean jumpAtStart, boolean jumpAtEnd) {
        this.sectionIdentifier = sectionIdentifier;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.trackBehaviour = trackBehaviour;
        this.nextSectionIdentifier = nextSectionIdentifier;
        this.connectionsConfig = connectionsConfig;
        this.jumpAtStart = jumpAtStart;
        this.jumpAtEnd = jumpAtEnd;
        this.previousSectionIdentifier = null;
        this.parentTrackIdentifier = parentTrackIdentifier;
    }

    public void setPreviousSectionIdentifier(String previousSectionIdentifier) {
        this.previousSectionIdentifier = previousSectionIdentifier;
    }

    public String getPreviousSectionIdentifier() {
        return previousSectionIdentifier;
    }

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

    public String getNextSectionIdentifier() {
        return nextSectionIdentifier;
    }

    public String getParentTrackIdentifier() {
        return parentTrackIdentifier;
    }

    public boolean isJumpAtEnd() {
        return jumpAtEnd;
    }

    public boolean isJumpAtStart() {
        return jumpAtStart;
    }

    @Deprecated
    public ConnectionsConfig getConnectionsConfig() {
        return connectionsConfig;
    }

    public static Section findByIdentifier(String sectionIdentifier, Map<SectionReference, Section> sectionMap){
        return sectionMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().getSectionIdentifier().equalsIgnoreCase(sectionIdentifier))
                .findFirst().orElseThrow().getValue();
    }
}
