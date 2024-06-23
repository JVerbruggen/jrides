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

public class SimpleFrame implements Frame {
    private int frame;
    private Track track;
    private Section section;

    public SimpleFrame(int frame, Track track, Section section) {
        this.frame = frame;
        this.track = track;
        this.section = section;
    }

    public SimpleFrame(int frame) {
        this.frame = frame;
    }

    public SimpleFrame(int frame, Track track) {
        this.frame = frame;
        this.track = track;
    }

    @Override
    public int getValue() {
        return frame;
    }

    @Override
    public String getValueString() {
        return String.valueOf(getValue());
    }

    @Override
    public void setValue(int frame) {
        this.frame = frame;
    }

    @Override
    public Track getTrack() {
        return track;
    }

    @Override
    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    public Section getSection() {
        return section;
    }

    @Override
    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public Frame add(int frames){
        this.frame += frames;
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Frame clone(){
        return new SimpleFrame(frame, track, section);
    }

    @Override
    public Frame capture() {
        return clone();
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
    public void updateTo(Frame other){
        this.frame = other.getValue();
    }

    @Override
    public String toString() {
        return "<Frame: " + getValue() + ">";
    }

    public boolean equals(Frame other) {
        return this.getValue() == other.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Frame) return equals((Frame) obj);
        return super.equals(obj);
    }
}
