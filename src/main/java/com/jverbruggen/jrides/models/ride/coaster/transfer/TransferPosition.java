package com.jverbruggen.jrides.models.ride.coaster.transfer;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.section.Section;

public class TransferPosition {
    private Vector3 location;
    private Quaternion orientation;
    private int moveTicks;

    private String sectionAtStartReference;
    private String sectionAtEndReference;

    private Section sectionAtStart;
    private Section sectionAtEnd;

    public TransferPosition(Vector3 location, Quaternion orientation, int moveTicks, String sectionAtStartReference, String sectionAtEndReference) {
        this.location = location;
        this.orientation = orientation;
        this.moveTicks = moveTicks;
        this.sectionAtStartReference = sectionAtStartReference;
        this.sectionAtEndReference = sectionAtEndReference;
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
}
