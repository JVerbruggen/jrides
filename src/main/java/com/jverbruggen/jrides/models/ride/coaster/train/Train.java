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

package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.TrackEnd;
import com.jverbruggen.jrides.models.properties.TrainEnd;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public interface Train extends Vehicle {
    String getName();
    List<CoasterCart> getCarts();
    int size();

    Frame getFrontFacingTrainFrame();
    Frame getBackFacingTrainFrame();

    Frame getMiddleOfTrainFrame();
    Frame getHeadOfTrainFrame();
    Frame getTailOfTrainFrame();
    Vector3 getCurrentHeadLocation();
    Vector3 getCurrentLocation();
    Vector3 getCurrentTailLocation();
    void setCurrentLocation(Vector3 headLocation, Vector3 middleLocation, Vector3 tailLocation);
    Vector3 getMassMiddlePoint();

    List<Section> getReservedSections();
    void addReservedSection(Section section);
    void removeReservedSection(Section section);

    List<Section> getCurrentSections();
    Section getNextSection();
    Section getHeadSection();
    Section getTailSection();
    void addCurrentSection(Section section);
    void addCurrentSection(Section section, TrainEnd sectionEnd);
    void removeCurrentSection(Section section);

    void setRestraintForAll(boolean locked);
    boolean getRestraintState();

    void setCrashed(boolean crashed);
    boolean isCrashed();
    boolean equals(Train other);
    TrackEnd getDirection();
    boolean isPositiveDrivingDirection();
    boolean drivingTowardsEnd();
    boolean isFacingForwards();
    void setFacingForwards(boolean forwards);
    void setDrivingDirection(boolean positive);
    void setInvertedFrameAddition(boolean inverted);

    void setStationaryAt(CoasterStationHandle stationaryAt);

    CoasterStationHandle getStationaryAt();

    void setHandle(TrainHandle trainHandle);
    TrainHandle getHandle();

    static Vector3 calculateMassMiddlePoint(Vector3 headLocation, Vector3 middleLocation, Vector3 tailLocation){
        return Vector3.average(headLocation, middleLocation, middleLocation, tailLocation); // Middle is twice as heavy as sides
    }

    void sendPositionMessage(String positionMessage);
    void addPositionMessageListener(MessageReceiver messageReceiver);
    void removePositionMessageListener(MessageReceiver messageReceiver);
    boolean positionMessageEnabled(MessageReceiver messageReceiver);

    void setStatusMessage(String statusMessage);
    void addStatusMessageListener(Player player);
    void removeStatusMessageListener(Player player);
    boolean statusModeEnabled(Player player);

    void despawn();
    void reset();
}
