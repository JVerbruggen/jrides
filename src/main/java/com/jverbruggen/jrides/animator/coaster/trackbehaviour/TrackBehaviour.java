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

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public interface TrackBehaviour {
    TrainMovement move(Speed currentSpeed, TrainHandle trainHandle, Section section);
    void trainExitedAtStart(@Nullable Train train, @Nullable Section section);
    void trainExitedAtEnd(@Nullable Train train, @Nullable Section section);
    void trainPassed(Train train);

    String getName();
    boolean canBlock();
    boolean canSpawnOn();
    Frame getSpawnFrame();

    void setParentTrack(Track parentTrack);
    Track getParentTrack();

    Section getSectionNext(Train train, boolean process);
    Section getSectionPrevious(Train train, boolean process);
    Section getSectionAtStart(Train train, boolean process);
    Section getSectionAtEnd(Train train, boolean process);
    boolean accepts(Train train);

    Collection<Section> getAllNextSections(Train train);
    Collection<Section> getAllPreviousSections(Train train);

    boolean definesNextSection();
    Vector3 getBehaviourDefinedPosition(Vector3 originalPosition);
    Quaternion getBehaviourDefinedOrientation(Quaternion originalOrientation);

    void populateSectionReferences(Map<SectionReference, Section> sectionMap);

    boolean definesNextAccepting();
    Section acceptAsNext(Train train, boolean canProcessPassed);

    boolean canHandleBlockSectionSafety();

    boolean canHandleConnections();
    boolean previousConnectsToStart();
    boolean nextConnectsToStart();
}
