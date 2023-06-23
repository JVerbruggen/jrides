package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.properties.PlayerLocation;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class AbstractRide implements Ride {
    private String identifier;
    private String displayName;
    private List<String> displayDescription;
    private ItemStack displayItem;
    private PlayerLocation warpLocation;
    private boolean canExitDuringRide;

    public AbstractRide(String identifier, String displayName, List<String> displayDescription, ItemStack displayItem, PlayerLocation warpLocation, boolean canExitDuringRide) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.displayDescription = displayDescription;
        this.displayItem = displayItem;
        this.warpLocation = warpLocation;
        this.canExitDuringRide = canExitDuringRide;
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
