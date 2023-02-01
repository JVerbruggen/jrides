package com.jverbruggen.jrides.models.ride.factory.track;

import com.jverbruggen.jrides.animator.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.models.properties.Frame;

import java.util.List;

public class TrackDescription {
    private final String identifier;
    private final List<NoLimitsExportPositionRecord> positions;
    private final TrackType trackType;
    private final Frame startFrame;
    private final Frame endFrame;

    public TrackDescription(String identifier, List<NoLimitsExportPositionRecord> positions, TrackType trackType, Frame startFrame, Frame endFrame) {
        this.identifier = identifier;
        this.positions = positions;
        this.trackType = trackType;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<NoLimitsExportPositionRecord> getPositions() {
        return positions;
    }

    public TrackType getTrackType() {
        return trackType;
    }

    public Frame getStartFrame() {
        return startFrame;
    }

    public Frame getEndFrame() {
        return endFrame;
    }
}

