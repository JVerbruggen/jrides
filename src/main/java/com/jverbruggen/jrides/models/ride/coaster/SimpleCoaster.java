package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class SimpleCoaster implements Coaster {
    private List<Train> trains;
    private String identifier;
    private String displayName;
    private Vector3 warpLocation;

    public SimpleCoaster(String identifier, String displayName, Vector3 warpLocation) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.warpLocation = warpLocation;
        this.trains = new ArrayList<>();
    }

    @Override
    public List<Train> getTrains() {
        return trains;
    }

    @Override
    public void addTrain(Train train) {
        trains.add(train);
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Vector3 playerWarpLocation() {
        return warpLocation;
    }
}
