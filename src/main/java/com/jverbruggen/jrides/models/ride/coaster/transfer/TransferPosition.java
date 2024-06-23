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

package com.jverbruggen.jrides.models.ride.coaster.transfer;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.section.Section;

public class TransferPosition {
    private final Vector3 location;
    private final Quaternion orientation;
    private final int moveTicks;

    private final String sectionAtStartReference;
    private final String sectionAtEndReference;
    private final boolean sectionAtStartForwards;
    private final boolean sectionAtEndForwards;
    private final boolean sectionAtStartConnectsToStart;
    private final boolean sectionAtEndConnectsToStart;

    private Section sectionAtStart;
    private Section sectionAtEnd;

    public TransferPosition(Vector3 location, Quaternion orientation, int moveTicks, String sectionAtStartReference, String sectionAtEndReference, boolean sectionAtStartForwards, boolean sectionAtEndForwards, boolean sectionAtStartConnectsToStart, boolean sectionAtEndConnectsToStart) {
        this.location = location;
        this.orientation = orientation;
        this.moveTicks = moveTicks;
        this.sectionAtStartReference = sectionAtStartReference;
        this.sectionAtEndReference = sectionAtEndReference;
        this.sectionAtStartForwards = sectionAtStartForwards;
        this.sectionAtEndForwards = sectionAtEndForwards;
        this.sectionAtStartConnectsToStart = sectionAtStartConnectsToStart;
        this.sectionAtEndConnectsToStart = sectionAtEndConnectsToStart;

        this.sectionAtStart = null;
        this.sectionAtEnd = null;
    }

    public Vector3 getLocation() {
        return location;
    }

    public Quaternion getOrientation() {
        return orientation;
    }

    public int getMoveTicks() {
        return moveTicks;
    }

    public String getSectionAtEndReference() {
        return sectionAtEndReference;
    }

    public String getSectionAtStartReference() {
        return sectionAtStartReference;
    }

    public Section getSectionAtStart() {
        return sectionAtStart;
    }

    public void setSectionAtStart(Section sectionAtStart) {
        this.sectionAtStart = sectionAtStart;
    }

    public Section getSectionAtEnd() {
        return sectionAtEnd;
    }

    public void setSectionAtEnd(Section sectionAtEnd) {
        this.sectionAtEnd = sectionAtEnd;
    }

    public boolean isSectionAtEndForwards() {
        return sectionAtEndForwards;
    }

    public boolean isSectionAtStartForwards() {
        return sectionAtStartForwards;
    }

    public boolean isSectionAtStartConnectsToStart() {
        return sectionAtStartConnectsToStart;
    }

    public boolean isSectionAtEndConnectsToStart() {
        return sectionAtEndConnectsToStart;
    }

    @Override
    public String toString() {
        return "<TPos start:" + sectionAtStart + ", end:" + sectionAtEnd + ">";
    }
}
