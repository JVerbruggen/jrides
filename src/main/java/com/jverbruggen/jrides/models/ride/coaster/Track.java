package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public interface Track {
    int getRawPositionsCount();
    List<Section> getSections();
    Section getNextSpawnSection();
    Vector3 getLocationFor(Frame frame);
    Quaternion getOrientationFor(Frame frame);
    List<Vector3> getAllPositions();
}
