package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.api.JRidesRide;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Ride extends JRidesRide {
    String getIdentifier();
    String getDisplayName();
    List<String> getDisplayDescription();
    ItemStack getDisplayItem();

    PlayerLocation getWarpLocation();
    boolean isLoaded();
    boolean canExitDuringRide();
}
