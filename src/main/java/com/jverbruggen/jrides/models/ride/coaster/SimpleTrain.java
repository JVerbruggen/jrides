package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public class SimpleTrain implements Train {
    private List<Cart> carts;
    private final int cartDistance;
    private final int headOfTrainOffset;
    private final int totalLengthInFrames;
    private int massMiddleFrame;
    private Section currentSection;

    public SimpleTrain(List<Cart> carts, int cartDistance, int headOfTrainOffset, int massMiddleFrame, Section section) {
        this.carts = carts;
        this.cartDistance = cartDistance;
        this.headOfTrainOffset = headOfTrainOffset;
        this.totalLengthInFrames = carts.size()*cartDistance;
        this.massMiddleFrame = massMiddleFrame;
        this.currentSection = section;
    }

    @Override
    public List<Cart> getCarts() {
        return carts;
    }

//    @Override
//    public int getCartDistanceFor(int index) {
//        return cartDistance*index;
//    }
//
//    @Override
//    public int getTotalLengthInFrames() {
//        return totalLengthInFrames;
//    }
//
//    @Override
//    public int getMassMiddleFrame() {
//        return massMiddleFrame;
//    }

    @Override
    public Section getCurrentSection() {
        return currentSection;
    }

    @Override
    public void setCurrentSection(Section section) {
        this.currentSection = section;
    }

    @Override
    public String toString() {
        return "<Train at position " + massMiddleFrame + ">";
    }
}
