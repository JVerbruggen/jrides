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
import com.jverbruggen.jrides.models.ride.section.PointSection;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

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

    @Override
    public List<String> getConflictSectionStrings() {
        return null;
    }
}
