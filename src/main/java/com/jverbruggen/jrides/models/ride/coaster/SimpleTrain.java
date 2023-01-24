package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.LinkedFrame;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;

public class SimpleTrain implements Train {
    private final String name;
    private List<Cart> carts;
    private final int cartDistance;
    private final int totalLengthInFrames;
    private Frame massMiddleFrame;
    private Frame headOfTrainFrame;
    private Section currentSection;
    private boolean crashed;

    public SimpleTrain(String name, List<Cart> carts, int cartDistance, Frame headOfTrainFrame, LinkedFrame massMiddleFrame, Section section) {
        this.name = name;
        this.carts = carts;
        this.cartDistance = cartDistance;
        this.totalLengthInFrames = carts.size()*cartDistance;
        this.headOfTrainFrame = headOfTrainFrame;
        this.massMiddleFrame = massMiddleFrame;
        this.currentSection = section;
        this.crashed = false;
    }

    @Override
    public String getName() {
        return name;
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
    @Override
    public Frame getMassMiddleFrame() {
        return massMiddleFrame;
    }

    @Override
    public Frame getHeadOfTrainFrame() {
        return headOfTrainFrame;
    }

    @Override
    public Section getCurrentSection() {
        return currentSection;
    }

    @Override
    public void setCurrentSection(Section section) {
        this.currentSection = section;
    }

    @Override
    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }

    @Override
    public boolean isCrashed() {
        return crashed;
    }

    @Override
    public String toString() {
        return "<Train at position " + massMiddleFrame + ">";
    }
}
