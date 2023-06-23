package com.jverbruggen.jrides.models.ride.flatride;

import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.AbstractRide;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FlatRide extends AbstractRide {
    public FlatRide(String identifier, String displayName, List<String> displayDescription, ItemStack displayItem, PlayerLocation warpLocation, boolean canExitDuringRide) {
        super(identifier, displayName, displayDescription, displayItem, warpLocation, canExitDuringRide);
    }
}
