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

import com.jverbruggen.jrides.animator.coaster.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.result.BlockSectionSafetyResult;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public interface Section extends Comparable<Section> {
    Frame getSpawnFrame();
    Frame getStartFrame();
    Frame getEndFrame();
    boolean isInSection(Frame frame);
    boolean isInRawFrameRange(Frame frame);
    Track getParentTrack();
    void setParentTrack(Track track);
    TrackBehaviour getTrackBehaviour();
    boolean isOccupied();
    boolean isOccupiedBy(Train train);
    Train getOccupiedBy();
    boolean canHandleOccupation(Train train);
    boolean canBlock();
    BlockSectionSafetyResult getBlockSectionSafety(@Nullable Train train);
    BlockSectionSafetyResult getBlockSectionSafety(@Nullable Train train, boolean checkConflicts);

    void setLocalReservation(@Nonnull Train train);
    void clearLocalReservation(@Nonnull Train authority);
    boolean canReserveLocally(@Nullable Train train);
    boolean canReserveEntireBlock(@Nonnull Train train);
    void setEntireBlockReservation(@Nonnull Train train);
    void clearEntireBlockReservation(@Nonnull Train authority);
    void clearEntireBlockReservation(@Nonnull Train authority, List<Section> done);
    @Nullable Train getReservedBy();
    boolean isReserved();
    boolean isReservedBy(Train train);

    boolean addOccupation(@NonNull  Train train);
    void removeOccupation(@NonNull  Train train);
    boolean canTrainSpawnOn();
    boolean hasPassed(Frame staticFrame, Frame movingFrame);
    boolean hasPassedInverse(Frame staticFrame, Frame movingFrame);
    boolean passesCycle();
    boolean shouldJumpAtStart();
    boolean shouldJumpAtEnd();

    void setConflictSections(List<Section> sections);

    Vector3 getLocationFor(Frame frame);
    Quaternion getOrientationFor(Frame frame);

    Section acceptAsNext(Train train, boolean processPassing);

    @Nullable
    Section next(Train train);
    @Nullable
    Section next(Train train, boolean processPassing);
    @Nullable
    Section previous(Train train);
    @Nullable
    Section previous(Train train, boolean processPassing);

    Collection<Section> allNextSections(Train train);
    Collection<Section> allPreviousSections(Train train);

    boolean isNextSectionFor(Train train, Section section);
    boolean isPreviousSectionFor(Train train, Section section);
    void setNext(Section section);
    void setPrevious(Section section);
    boolean spansOver(Train train);
    boolean positiveDirectionToGoTo(Section section, Train forTrain);

    /**
     * Is section forwards
     * i.e. do trains traverse backwards or forwards through it?
     * @return true if forwards
     */
    boolean isForwards();

    boolean nextConnectsToStart();
    boolean previousConnectsToStart();

    String getName();
}
