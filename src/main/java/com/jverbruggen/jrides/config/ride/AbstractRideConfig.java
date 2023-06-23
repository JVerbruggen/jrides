package com.jverbruggen.jrides.config.ride;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.config.gates.GatesConfig;
import com.jverbruggen.jrides.models.properties.PlayerLocation;

import java.util.List;

public abstract class AbstractRideConfig extends BaseConfig {
    private final String manifestVersion;
    private final String identifier;
    private final String displayName;
    private final List<String> displayDescription;
    private final ItemConfig displayItem;
    private final PlayerLocation warpLocation;
    private final GatesConfig gates;
    private final boolean canExitDuringRide;

    public AbstractRideConfig(String manifestVersion, String identifier, String displayName, List<String> displayDescription, ItemConfig displayItem, PlayerLocation warpLocation, GatesConfig gates, boolean canExitDuringRide) {
        this.manifestVersion = manifestVersion;
        this.identifier = identifier;
        this.displayName = displayName;
        this.displayDescription = displayDescription;
        this.displayItem = displayItem;
        this.warpLocation = warpLocation;
        this.gates = gates;
        this.canExitDuringRide = canExitDuringRide;
    }

    public String getManifestVersion() {
        return manifestVersion;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getDisplayDescription() {
        return displayDescription;
    }

    public ItemConfig getDisplayItem() {
        return displayItem;
    }

    public PlayerLocation getWarpLocation() {
        return warpLocation;
    }

    public GatesConfig getGates() {
        return gates;
    }

    public boolean canExitDuringRide() {
        return canExitDuringRide;
    }
}