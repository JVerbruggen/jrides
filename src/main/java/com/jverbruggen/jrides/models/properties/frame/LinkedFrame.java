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

package com.jverbruggen.jrides.models.properties.frame;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

public class LinkedFrame implements Frame {
    private final Frame linkedTo;
    private final int offsetFromLink;
    private Section section;
    private boolean canSetValue;

    public LinkedFrame(Frame linkedTo, int offsetFromLink, Section section) {
        this.linkedTo = linkedTo;
        this.offsetFromLink = offsetFromLink;
        this.section = section;
        this.canSetValue = true;
    }

    @Override
    public int getValue() {
        return linkedTo.clone().add(offsetFromLink).getValue();
    }

    @Override
    public String getValueString() {
        return String.valueOf(getValue());
    }

    @Override
    public void setValue(int frame) {
        if(!canSetValue)
            throw new RuntimeException("Cannot set value of this linked frame " + this);
        linkedTo.setValue(frame - offsetFromLink);
    }

    @Override
    public Track getTrack() {
        return linkedTo.getTrack();
    }

    @Override
    public void setTrack(Track track) {
        linkedTo.setTrack(track);
    }

    @Override
    public Section getSection() {
        throw new RuntimeException("LinkedFrames dont have accurate sections");
    }

    @Override
    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public Frame add(int frames) {
        linkedTo.add(frames);
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Frame clone() {
        return new LinkedFrame(linkedTo, offsetFromLink, section);
    }

    @Override
    public Frame capture() {
        return new LinkedFrame(linkedTo.capture(), offsetFromLink, section);
    }

    @Override
    public void setInvertedFrameAddition(boolean inverted) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isInvertedFrameAddition() {
        return false;
    }

    @Override
    public void updateTo(Frame other) {
        setValue(other.getValue());
    }

    @Override
    public String toString() {
        return "<LinkedFrame: " + getValue() + " to: " + linkedTo + ">";
    }
}
