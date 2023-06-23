package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.AbstractRide;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SimpleCoaster extends AbstractRide implements Coaster {
    private List<Train> trains;

    public SimpleCoaster(String identifier, String displayName, List<String> displayDescription, ItemStack displayItem, PlayerLocation warpLocation, boolean canExitDuringRide) {
        super(identifier, displayName, displayDescription, displayItem, warpLocation, canExitDuringRide);
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
}
