package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.models.properties.PlayerLocation;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UnloadedRide implements Ride {
    private String identifier;

    public UnloadedRide(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDisplayName() {
        return identifier;
    }

    @Override
    public List<String> getDisplayDescription() {
        return List.of();
    }

    @Override
    public ItemStack getDisplayItem() {
        return null;
    }

    @Override
    public PlayerLocation getWarpLocation() {
        return null;
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public boolean canExitDuringRide() {
        return false;
    }
}
