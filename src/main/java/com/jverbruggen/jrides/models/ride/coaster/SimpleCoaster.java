package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.ArrayList;
import java.util.List;

public class SimpleCoaster implements Coaster {
    private List<Train> trains;
    private String identifier;
    private String displayName;
    private PlayerLocation warpLocation;


    public SimpleCoaster(String identifier, String displayName, PlayerLocation warpLocation) {
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
    public PlayerLocation getWarpLocation() {
        return warpLocation;
    }
}
