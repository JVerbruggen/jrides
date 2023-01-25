package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.TrainEnd;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.ArrayList;
import java.util.List;

public class SimpleTrain implements Train {
    private final String name;
    private List<Cart> carts;
    private Frame massMiddleFrame;
    private Frame headOfTrainFrame;
    private Frame tailOfTrainFrame;
    private List<Section> currentSections;
    private boolean crashed;
    private Vector3 headLocation;
    private Vector3 middleLocation;
    private Vector3 tailLocation;

    public SimpleTrain(String name, List<Cart> carts, Frame headOfTrainFrame, Frame massMiddleFrame, Frame tailOfTrainFrame, Vector3 headLocation, Vector3 middleLocation, Vector3 tailLocation, Section section) {
        this.name = name;
        this.carts = carts;
        this.headOfTrainFrame = headOfTrainFrame;
        this.massMiddleFrame = massMiddleFrame;
        this.tailOfTrainFrame = tailOfTrainFrame;
        this.crashed = false;

        this.currentSections = new ArrayList<>();
        this.currentSections.add(section);
        this.headLocation = headLocation;
        this.middleLocation = middleLocation;
        this.tailLocation = tailLocation;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Cart> getCarts() {
        return carts;
    }

    @Override
    public Frame getMiddleOfTrainFrame() {
        return massMiddleFrame;
    }

    @Override
    public Frame getHeadOfTrainFrame() {
        return headOfTrainFrame;
    }

    @Override
    public Frame getTailOfTrainFrame() {
        return tailOfTrainFrame;
    }

    @Override
    public Vector3 getCurrentLocation() {
        return middleLocation;
    }

    @Override
    public void setCurrentLocation(Vector3 headLocation, Vector3 middleLocation, Vector3 tailLocation) {
        this.headLocation = headLocation;
        this.middleLocation = middleLocation;
        this.tailLocation = tailLocation;
    }

    @Override
    public Vector3 getMassMiddlePoint() {
        return Vector3.average(headLocation, middleLocation, tailLocation);
    }

    @Override
    public List<Section> getCurrentSections() {
        return currentSections;
    }

    @Override
    public Section getHeadSection() {
        if(currentSections.size() == 0) throw new RuntimeException("Train occupies 0 sections");
        return currentSections.get(0);
    }

    @Override
    public Section getTailSection() {
        if(currentSections.size() == 0) throw new RuntimeException("Train occupies 0 sections");
        return currentSections.get(currentSections.size()-1);
    }

    @Override
    public void addCurrentSection(Section section) {
        addCurrentSection(section, TrainEnd.HEAD);
    }

    @Override
    public void addCurrentSection(Section section, TrainEnd trainEnd) {
        if(trainEnd.equals(TrainEnd.HEAD))
            currentSections.add(0, section);
        else
            currentSections.add(section);
    }

    @Override
    public void removeCurrentSection(Section section) {
        if(!this.currentSections.contains(section)) throw new RuntimeException("Section removal mismatch");
        this.currentSections.remove(section);
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
        return "<Train " + getName() + " at position " + getHeadOfTrainFrame() + ">";
    }
}
