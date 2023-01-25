package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.TrainEnd;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public interface Train {
    String getName();
    List<Cart> getCarts();

    Frame getMassMiddleFrame();
    Frame getHeadOfTrainFrame();
    Frame getTailOfTrainFrame();
    Vector3 getCurrentLocation();
    void setCurrentLocation(Vector3 newLocation);

    List<Section> getCurrentSections();
    Section getHeadSection();
    Section getTailSection();
    void addCurrentSection(Section section);
    void addCurrentSection(Section section, TrainEnd sectionEnd);
    void removeCurrentSection(Section section);

    void setCrashed(boolean crashed);
    boolean isCrashed();
}
