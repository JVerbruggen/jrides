package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SimpleCoaster implements Coaster {
    private List<Train> trains;
    private String identifier;
    private String displayName;
    private List<String> displayDescription;
    private ItemStack displayItem;
    private PlayerLocation warpLocation;
    private boolean canExitDuringRide;

    public SimpleCoaster(String identifier, String displayName, List<String> displayDescription, ItemStack displayItem, PlayerLocation warpLocation, boolean canExitDuringRide) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.displayDescription = displayDescription;
        this.displayItem = displayItem;
        this.warpLocation = warpLocation;
        this.trains = new ArrayList<>();
        this.canExitDuringRide = canExitDuringRide;
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
    public ItemStack getDisplayItem() {
        return displayItem;
    }

    @Override
    public List<String> getDisplayDescription() {
        return displayDescription;
    }

    @Override
    public PlayerLocation getWarpLocation() {
        return warpLocation;
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public boolean canExitDuringRide() {
        return canExitDuringRide;
    }
}
