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

package com.jverbruggen.jrides.models.ride.coaster.track.compound;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.ArrayList;
import java.util.List;

public class CompoundTrack implements Track {
    private final List<Track> children;
    private final List<Section> sections;
    private final List<Vector3> allPositions;

    public CompoundTrack(List<Track> children) {
        this.children = children;
        this.sections = new ArrayList<>();
        this.allPositions = new ArrayList<>();

        this.children.forEach(s -> {
            this.sections.addAll(s.getSections());
            this.allPositions.addAll(s.getAllPositions());
        });
    }

    public List<Track> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "<CompoundTrack>";
    }

    @Override
    public String getIdentifier() {
        return "Main Compound Track";
    }

    @Override
    public int getLength() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public List<Section> getSections() {
        return sections;
    }

    @Override
    public Section getNextSpawnSection() {
        for(Section section : sections){
            if(!section.isOccupied() && section.canTrainSpawnOn()) return section;
        }
        return null;
    }

    @Override
    public Vector3 getLocationFor(Frame frame) {
        Track track = frame.getTrack();
        if(track.equals(this))
            throw new RuntimeException("Frame cannot be bound to compound track");
        return track.getLocationFor(frame);
    }

    @Override
    public Quaternion getOrientationFor(Frame frame) {
        Track track = frame.getTrack();
        if(track.equals(this))
            throw new RuntimeException("Frame cannot be bound to compound track");
        return track.getOrientationFor(frame);
    }

    @Override
    public List<Vector3> getAllPositions() {
        return allPositions;
    }

    @Override
    public int getLowerFrame() {
        throw new RuntimeException("Cannot get frame bounds for compound track");
    }

    @Override
    public int getUpperFrame() {
        throw new RuntimeException("Cannot get frame bounds for compound track");
    }

    @Override
    public boolean inThisTrack(int frame) {
        return true;
    }

    @Override
    public Track getNextTrack() {
        throw new RuntimeException("Cannot get adjacent track for compound track");
    }

    @Override
    public Track getPreviousTrack() {
        throw new RuntimeException("Cannot get adjacent track for compound track");
    }

    @Override
    public void setNextTrack(Track track) {
        throw new RuntimeException("Cannot set adjacent track for compound track");
    }

    @Override
    public void setPreviousTrack(Track track) {
        throw new RuntimeException("Cannot set adjacent track for compound track");
    }
}
