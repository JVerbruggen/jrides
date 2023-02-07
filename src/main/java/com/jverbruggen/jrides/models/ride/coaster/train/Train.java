package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.TrackEnd;
import com.jverbruggen.jrides.models.properties.TrainEnd;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public interface Train {
    String getName();
    List<Cart> getCarts();

    Frame getMiddleOfTrainFrame();
    Frame getHeadOfTrainFrame();
    Frame getTailOfTrainFrame();
    Vector3 getCurrentHeadLocation();
    Vector3 getCurrentLocation();
    Vector3 getCurrentTailLocation();
    void setCurrentLocation(Vector3 headLocation, Vector3 middleLocation, Vector3 tailLocation);
    Vector3 getMassMiddlePoint();

    List<Section> getCurrentSections();
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
    boolean isFacingForwards();
    TrackEnd getDirection();

    void onPlayerEnter(Player player);
    void onPlayerExit(Player player);
    List<Player> getPassengers();

    void setStationaryAt(StationHandle stationaryAt);
    boolean isStationary();
    StationHandle getStationaryAt();

    void setHandle(TrainHandle trainHandle);
    TrainHandle getHandle();

    static Vector3 calculateMassMiddlePoint(Vector3 headLocation, Vector3 middleLocation, Vector3 tailLocation){
        return Vector3.average(headLocation, middleLocation, middleLocation, tailLocation); // Middle is twice as heavy as sides
    }

    void ejectPassengers();

    void playRestraintOpenSound();
    void playRestraintCloseSound();
    void playDispatchSound();

    void setStatusMessage(String statusMessage);
    void addStatusMessageListener(Player player);
    void removeStatusMessageListener(Player player);
    boolean statusModeEnabled(Player player);
}
