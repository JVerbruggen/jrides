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

package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.result.BlockSectionSafetyResult;

import javax.annotation.Nullable;
import java.util.List;

public class SimpleSection extends BaseSection {
    private final Frame startFrame;
    private final Frame endFrame;
    private final boolean jumpAtStart;
    private final boolean jumpAtEnd;
    private final boolean forwards;
    private String name;
    private List<Section> conflictSections;

    public SimpleSection(Frame startFrame, Frame endFrame, TrackBehaviour trackBehaviour, boolean jumpAtStart, boolean jumpAtEnd, boolean forwards) {
        super(trackBehaviour);
        this.startFrame = startFrame.clone();
        this.endFrame = endFrame.clone();
        this.jumpAtStart = jumpAtStart;
        this.jumpAtEnd = jumpAtEnd;
        this.forwards = forwards;
        this.name = null;
        this.conflictSections = null;
    }

    @Override
    public Frame getSpawnFrame() {
        Frame behaviourDefinedSpawnFrame = trackBehaviour.getSpawnFrame();
        if(behaviourDefinedSpawnFrame == null)
            return getEndFrame();
        return behaviourDefinedSpawnFrame;
    }

    @Override
    public Frame getStartFrame() {
        return startFrame;
    }

    @Override
    public Frame getEndFrame() {
        return endFrame;
    }

    @Override
    public boolean isInRawFrameRange(Frame frame) {
        return Frame.isBetweenFrames(startFrame, endFrame, frame);
    }

    @Override
    public boolean isInSection(Frame frame) {
        return parentTrack.equals(frame.getTrack()) && isInRawFrameRange(frame);
    }

    @Override
    public TrackBehaviour getTrackBehaviour() {
        return trackBehaviour;
    }

    @Override
    public boolean canBlock() {
        return trackBehaviour.canBlock();
    }

    @Override
    public BlockSectionSafetyResult getBlockSectionSafety(@Nullable Train train, boolean checkConflicts) {
        Section next = next(train);

        if(trackBehaviour.canHandleBlockSectionSafety()){
            return ((SectionSafetyProvider) trackBehaviour).getEnteringSafety(train, next);
        }

        if(!this.canReserveLocally(train)){
            String reservedByString = getReservedBy() != null ? getReservedBy().getName() : "null";
            return new BlockSectionSafetyResult(false, train, "Cannot reserve locally, reserved by " + reservedByString);
        }
        if(train != null
                && this.getOccupiedBy() != train
                && this.getOccupiedBy() != null) return new BlockSectionSafetyResult(false, train, "Already occupied by " + this.getOccupiedBy());
        if(!this.trackBehaviour.accepts(train)) return new BlockSectionSafetyResult(false, train, "Track behaviour not ready for train");
        if(checkConflicts
                && conflictSections != null
                && conflictSections.stream().anyMatch(s -> !s.getBlockSectionSafety(train, false).safe()))
            return new BlockSectionSafetyResult(false, train, "One of conflicting sections unsafe");

        if(this.canBlock()) return new BlockSectionSafetyResult(true, train, "OK: Can block self");

        if(next == null){
            throw new RuntimeException("Section was null for train when checking for block section safety");
        }

        if(!next.isPreviousSectionFor(train, this))
            return new BlockSectionSafetyResult(false, train, "Train and section order mismatch");

        return next.getBlockSectionSafety(train);
    }

    @Override
    public boolean canTrainSpawnOn() {
        return trackBehaviour.canSpawnOn();
    }

    @Override
    public boolean hasPassed(Frame staticFrame, Frame movingFrame) {
        int startFrameValue = startFrame.getValue();
        int endFrameValue = endFrame.getValue();
        int staticFrameValue = staticFrame.getValue();
        int movingFrameValue = movingFrame.getValue();

        boolean normalPassed = startFrameValue < endFrameValue && staticFrameValue <= movingFrameValue;
        boolean cyclicPassed = (startFrameValue > endFrameValue && (
            (staticFrameValue <= movingFrameValue && staticFrameValue >= startFrameValue)
            || (staticFrameValue <= movingFrameValue && staticFrameValue <= endFrameValue && movingFrameValue <= endFrameValue)
            || (staticFrameValue >= movingFrameValue && staticFrameValue >= startFrameValue && movingFrameValue <= endFrameValue)
        ));

        return normalPassed || cyclicPassed;
    }

    @Override
    public boolean hasPassedInverse(Frame staticFrame, Frame movingFrame) {
        return hasPassed(movingFrame, staticFrame);
    }

    @Override
    public boolean passesCycle() {
        return startFrame.getValue() > endFrame.getValue();
    }

    @Override
    public boolean shouldJumpAtStart() {
        return jumpAtStart;
    }

    @Override
    public boolean shouldJumpAtEnd() {
        return jumpAtEnd;
    }

    @Override
    public void setConflictSections(List<Section> sections) {
        this.conflictSections = sections;
    }

    @Override
    public boolean spansOver(Train train) {
        Frame headOfTrainFrame = train.getHeadOfTrainFrame();
        Frame tailOfTrainFrame = train.getTailOfTrainFrame();
        JRidesPlugin.getLogger().info(LogType.SECTIONS,
                "From " + this + " Spans over " + headOfTrainFrame.getValue() + " - " + tailOfTrainFrame.getValue());


        Frame lowerFrame = train.isFacingForwards() ? tailOfTrainFrame : headOfTrainFrame;
        Frame upperFrame = train.isFacingForwards() ? headOfTrainFrame : tailOfTrainFrame;

        return isInSection(headOfTrainFrame)
                || isInSection(tailOfTrainFrame)
                || Frame.isBetweenFrames(lowerFrame, upperFrame, endFrame)
                || Frame.isBetweenFrames(lowerFrame, upperFrame, startFrame);
    }

    @Override
    public boolean isForwards() {
        return forwards;
    }

    @Override
    public void setParentTrack(Track track) {
        super.setParentTrack(track);

        startFrame.setTrack(track);
        endFrame.setTrack(track);
    }

    @Override
    public String toString() {
        return "<" + startFrame.getValueString() + "-" + endFrame.getValueString() + " " + trackBehaviour.getName() + " occ:" + isOccupied() + ">";
    }

    @Override
    public String getName() {
        if(name == null) return toString();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean previousConnectsToStart() {
        if(trackBehaviour.canHandleConnections()){
            return trackBehaviour.previousConnectsToStart();
        }
        return false;
    }

    @Override
    public boolean nextConnectsToStart() {
        if(trackBehaviour.canHandleConnections()){
            return trackBehaviour.nextConnectsToStart();
        }
        return true;
    }
}
