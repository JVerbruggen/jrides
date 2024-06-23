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

package com.jverbruggen.jrides.animator.coaster.trackbehaviour;

import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;
import com.jverbruggen.jrides.models.ride.section.result.BlockSectionSafetyResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class BaseTrackBehaviour implements TrackBehaviour {
    protected final CartMovementFactory cartMovementFactory;
    protected Track parentTrack;

    protected BaseTrackBehaviour(CartMovementFactory cartMovementFactory) {
        this.cartMovementFactory = cartMovementFactory;
        this.parentTrack = null;
    }

    protected BlockSectionSafetyResult getNextSectionSafety(Train train){
        Section nextSection = train.getNextSection();
        if(nextSection == null) return new BlockSectionSafetyResult(false, train, "No next section present");
        return nextSection.getBlockSectionSafety(train);
    }

    protected boolean shouldReserveNextSection(Train train){
        Section nextSection = train.getNextSection();
        if(nextSection == null) return false;
        return nextSection.getReservedBy() != train;
    }

    protected TrainMovement calculateTrainMovement(Train train, Section section, Speed speed){
        if(speed.isZero()){
            return new TrainMovement(speed, train.getHeadOfTrainFrame(), train.getMiddleOfTrainFrame(), train.getTailOfTrainFrame(), train.getCurrentLocation(), null);
        }

        int speedFrameIncrement = speed.getFrameIncrement();

        Frame newHeadOfTrainFrame = train.getHeadOfTrainFrame().clone().add(speedFrameIncrement);
        Frame newMiddleOfTrainFrame = train.getMiddleOfTrainFrame().clone().add(speedFrameIncrement);
        Frame newTailOfTrainFrame = train.getTailOfTrainFrame().clone().add(speedFrameIncrement);

        Vector3 newTrainLocation = newMiddleOfTrainFrame.getSection().getLocationFor(newMiddleOfTrainFrame);


        return new TrainMovement(speed, newHeadOfTrainFrame, newMiddleOfTrainFrame, newTailOfTrainFrame, newTrainLocation, null);
    }

    protected abstract void setParentTrackOnFrames(Track parentTrack);

    @Override
    public void setParentTrack(Track parentTrack) {
        this.parentTrack = parentTrack;
        setParentTrackOnFrames(parentTrack);
    }

    @Override
    public Track getParentTrack() {
        return parentTrack;
    }

    @Override
    public Section getSectionAtEnd(Train train, boolean process) {
        return null;
    }

    @Override
    public Section getSectionAtStart(Train train, boolean process) {
        return null;
    }

    @Override
    public void trainPassed(Train train) {

    }

    @Override
    public boolean definesNextSection() {
        return false;
    }

    @Override
    public Vector3 getBehaviourDefinedPosition(Vector3 originalPosition) {
        return originalPosition;
    }

    @Override
    public Quaternion getBehaviourDefinedOrientation(Quaternion originalOrientation) {
        return originalOrientation;
    }

    @Override
    public boolean accepts(Train train) {
        return true;
    }

    @Override
    public Section getSectionNext(Train train, boolean process) {
        return getSectionAtEnd(train, process);
    }

    @Override
    public Section getSectionPrevious(Train train, boolean process) {
        return getSectionAtStart(train, process);
    }

    @Override
    public Collection<Section> getAllNextSections(Train train) {
        return List.of(getSectionNext(train, false));
    }

    @Override
    public Collection<Section> getAllPreviousSections(Train train) {
        return List.of(getSectionPrevious(train, false));
    }

    @Override
    public void populateSectionReferences(Map<SectionReference, Section> sectionMap) {
    }

    @Override
    public boolean definesNextAccepting() {
        return false;
    }

    @Override
    public Section acceptAsNext(Train train, boolean canProcessPassed) {
        return null;
    }

    @Override
    public boolean canHandleBlockSectionSafety() {
        return false;
    }

    @Override
    public boolean canHandleConnections() {
        return false;
    }

    @Override
    public boolean nextConnectsToStart() {
        return true;
    }

    @Override
    public boolean previousConnectsToStart() {
        return false;
    }
}
