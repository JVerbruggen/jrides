/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.config.ride;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.InteractionEntitiesConfig;
import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemStackConfig;
import com.jverbruggen.jrides.config.gates.GatesConfig;
import com.jverbruggen.jrides.models.properties.PlayerLocation;

import java.util.List;

public abstract class AbstractRideConfig extends BaseConfig {
    private final String manifestVersion;
    private final String identifier;
    private final String displayName;
    private final List<String> displayDescription;
    private final ItemStackConfig displayItem;
    private final PlayerLocation warpLocation;
    private final boolean warpEnabled;
    private final PlayerLocation customEjectLocation;
    private final GatesConfig gates;
    private final SoundsConfig soundsConfig;
    private final boolean canExitDuringRide;
    private final InteractionEntitiesConfig interactionEntities;
    private final RideCounterMapConfigs rideCounterMapConfigs;
    private final boolean debugMode;

    public AbstractRideConfig(String manifestVersion, String identifier, String displayName, List<String> displayDescription, ItemStackConfig displayItem, PlayerLocation warpLocation, boolean warpEnabled, PlayerLocation customEjectLocation, GatesConfig gates, SoundsConfig soundsConfig, boolean canExitDuringRide, InteractionEntitiesConfig interactionEntities, RideCounterMapConfigs rideCounterMapConfigs, boolean debugMode) {
        this.manifestVersion = manifestVersion;
        this.identifier = identifier;
        this.displayName = displayName;
        this.displayDescription = displayDescription;
        this.displayItem = displayItem;
        this.warpLocation = warpLocation;
        this.warpEnabled = warpEnabled;
        this.customEjectLocation = customEjectLocation;
        this.gates = gates;
        this.soundsConfig = soundsConfig;
        this.canExitDuringRide = canExitDuringRide;
        this.interactionEntities = interactionEntities;
        this.rideCounterMapConfigs = rideCounterMapConfigs;
        this.debugMode = debugMode;
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

    public ItemStackConfig getDisplayItem() {
        return displayItem;
    }

    public PlayerLocation getWarpLocation() {
        return warpLocation;
    }

    public boolean isWarpEnabled() {
        return warpEnabled;
    }

    public PlayerLocation getCustomEjectLocation() {
        return customEjectLocation;
    }

    public SoundsConfig getSoundsConfig() {
        return soundsConfig;
    }

    public GatesConfig getGates() {
        return gates;
    }

    public boolean canExitDuringRide() {
        return canExitDuringRide;
    }

    public InteractionEntitiesConfig getInteractionEntities() {
        return interactionEntities;
    }

    public RideCounterMapConfigs getRideCounterMapConfigs() {
        return rideCounterMapConfigs;
    }

    public boolean isDebugMode() {
        return debugMode;
    }
}
