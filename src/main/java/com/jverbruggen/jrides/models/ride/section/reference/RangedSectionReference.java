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

package com.jverbruggen.jrides.models.ride.section.reference;

import com.jverbruggen.jrides.animator.coaster.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SimpleSection;

import java.util.List;

public class RangedSectionReference extends SectionReference {
    private final String sectionIdentifier;
    private final String nextSectionIdentifier;
    private String previousSectionIdentifier;
    private final String parentTrackIdentifier;
    private final String arrivalUnlocks;
    private final Frame startFrame;
    private final Frame endFrame;
    private final TrackBehaviour trackBehaviour;
    private final boolean jumpAtStart;
    private final boolean jumpAtEnd;
    private final boolean forwards;

    private final List<String> conflictSectionsStrings;

    public RangedSectionReference(String sectionIdentifier, Frame startFrame, Frame endFrame, TrackBehaviour trackBehaviour, String nextSectionIdentifier,
                                  List<String> conflictSections, String parentTrackIdentifier, String arrivalUnlocks, boolean jumpAtStart, boolean jumpAtEnd, boolean forwards) {
        this.sectionIdentifier = sectionIdentifier;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.trackBehaviour = trackBehaviour;
        this.nextSectionIdentifier = nextSectionIdentifier;
        this.conflictSectionsStrings = conflictSections;
        this.arrivalUnlocks = arrivalUnlocks;
        this.jumpAtStart = jumpAtStart;
        this.jumpAtEnd = jumpAtEnd;
        this.forwards = forwards;
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

    public String getArrivalUnlocks() {
        return arrivalUnlocks;
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
        boolean forwards = isForwards();

        SimpleSection section = new SimpleSection(startFrame, endFrame, trackBehaviour, jumpAtStart, jumpAtEnd, forwards);
        section.setName(getSectionIdentifier());
        section.setArrivalUnlocks(arrivalUnlocks);
        return section;
    }

    @Override
    public List<String> getConflictSectionStrings() {
        return conflictSectionsStrings;
    }

    public boolean isJumpAtEnd() {
        return jumpAtEnd;
    }

    public boolean isJumpAtStart() {
        return jumpAtStart;
    }

    public boolean isForwards() {
        return forwards;
    }

}
