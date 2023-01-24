package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public interface Train {
    List<Cart> getCarts();
//    int getCartDistanceFor(int index);
//    int getTotalLengthInFrames();
//    int getMassMiddleFrame();
    Section getCurrentSection();
    void setCurrentSection(Section section);
}
