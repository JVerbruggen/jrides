package com.jverbruggen.jrides.models.ride.coaster.track;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public interface Track {
    String getIdentifier();
    int getLength();

    List<Section> getSections();
    Section getNextSpawnSection();

    Vector3 getLocationFor(Frame frame);
    Quaternion getOrientationFor(Frame frame);
    List<Vector3> getAllPositions();
    int getLowerFrame();
    int getUpperFrame();
    Track getNextTrack();
    Track getPreviousTrack();
    void setNextTrack(Track track);
    void setPreviousTrack(Track track);
}
