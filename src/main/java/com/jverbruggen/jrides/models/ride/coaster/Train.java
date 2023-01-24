package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public interface Train {
    String getName();
    List<Cart> getCarts();
//    int getCartDistanceFor(int index);
//    int getTotalLengthInFrames();
    Frame getMassMiddleFrame();
    Frame getHeadOfTrainFrame();
    Section getCurrentSection();
    void setCurrentSection(Section section);

    void setCrashed(boolean crashed);
    boolean isCrashed();
}
