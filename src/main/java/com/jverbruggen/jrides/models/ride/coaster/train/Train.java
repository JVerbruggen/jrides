package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.animator.TrainHandle;
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
    List<Cart> getCarts();
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

    void onPlayerEnter(Player player);
    void onPlayerExit(Player player);

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
}
