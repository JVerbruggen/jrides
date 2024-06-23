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

package com.jverbruggen.jrides.models.ride.factory.track;

import com.jverbruggen.jrides.animator.coaster.NoLimitsExportPositionRecord;
import com.jverbruggen.jrides.models.properties.frame.Frame;

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

    public int getCycle(){
        return getPositions().size()-1;
    }
}

