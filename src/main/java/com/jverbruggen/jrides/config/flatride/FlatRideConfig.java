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

package com.jverbruggen.jrides.config.flatride;

import com.jverbruggen.jrides.config.coaster.objects.InteractionEntitiesConfig;
import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemStackConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfig;
import com.jverbruggen.jrides.config.flatride.timing.TimingConfig;
import com.jverbruggen.jrides.config.gates.GatesConfig;
import com.jverbruggen.jrides.config.gates.StationConfig;
import com.jverbruggen.jrides.config.ride.AbstractRideConfig;
import com.jverbruggen.jrides.config.ride.RideCounterMapConfigs;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlatRideConfig extends AbstractRideConfig {
    private final StationConfig stationConfig;
    private final StructureConfig structureConfig;
    private final TimingConfig timingConfig;

    public FlatRideConfig(String manifestVersion, String identifier, String displayName, List<String> displayDescription, ItemStackConfig displayItem,
                          PlayerLocation warpLocation, boolean warpEnabled, PlayerLocation customEjectLocation, GatesConfig gates, InteractionEntitiesConfig interactionEntities,
                          boolean canExitDuringRide, StationConfig stationConfig, SoundsConfig soundsConfig, StructureConfig structureConfig,
                          TimingConfig timingConfig, RideCounterMapConfigs rideCounterMapConfigs, boolean debugMode) {
        super(manifestVersion, identifier, displayName, displayDescription, displayItem, warpLocation, warpEnabled, customEjectLocation, gates, soundsConfig, canExitDuringRide, interactionEntities, rideCounterMapConfigs, debugMode);
        this.stationConfig = stationConfig;
        this.structureConfig = structureConfig;
        this.timingConfig = timingConfig;
    }

    public StructureConfig getStructureConfig() {
        return structureConfig;
    }

    public TimingConfig getTimingConfig() {
        return timingConfig;
    }

    public StationConfig getStationConfig() {
        return stationConfig;
    }

    public static FlatRideConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String manifestVersion = configurationSection.getString("manifestVersion");
        String identifier = configurationSection.getString("identifier");
        String displayName = configurationSection.getString("displayName");

        List<String> displayDescription = Arrays.stream(getString(configurationSection, "displayDescription", "").split("\\\\n"))
                .map(d -> ChatColor.GRAY + d)
                .collect(Collectors.toList());
        if(displayDescription.size() == 1 && ChatColor.stripColor(displayDescription.get(0)).equals(""))
            displayDescription.clear();

        ItemStackConfig displayItem = ItemStackConfig.fromConfigurationSection(configurationSection.getConfigurationSection("displayItem"));
        PlayerLocation warpLocation = PlayerLocation.fromDoubleList(configurationSection.getDoubleList("warpLocation"));
        boolean warpEnabled = configurationSection.getBoolean("warpEnabled", true);
        PlayerLocation customEjectLocation = PlayerLocation.fromDoubleList(getDoubleList(configurationSection, "customEjectLocation", null));
        GatesConfig gates = GatesConfig.fromConfigurationSection(configurationSection.getConfigurationSection("gates"));
        SoundsConfig sounds = SoundsConfig.fromConfigurationSection(configurationSection.getConfigurationSection("sounds"));
        boolean canExitDuringRide = getBoolean(configurationSection, "canExitDuringRide", false);
        InteractionEntitiesConfig interactionEntities = InteractionEntitiesConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "interactionEntities"));
        StationConfig stationConfig = StationConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "station"));
        StructureConfig structureConfig = StructureConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "structure"));
        TimingConfig timingConfig = TimingConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "timing"));
        RideCounterMapConfigs rideCounterMapConfigs = RideCounterMapConfigs.fromConfigurationSection(identifier, getConfigurationSection(configurationSection, "rideCounterMaps"));
        boolean debugMode = getBoolean(configurationSection, "debugMode", false);

        return new FlatRideConfig(manifestVersion, identifier, displayName, displayDescription, displayItem, warpLocation, warpEnabled,
                customEjectLocation, gates, interactionEntities, canExitDuringRide, stationConfig, sounds, structureConfig,
                timingConfig, rideCounterMapConfigs, debugMode);
    }
}
