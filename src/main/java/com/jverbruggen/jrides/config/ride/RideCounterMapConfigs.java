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
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.stream.Collectors;

public class RideCounterMapConfigs extends BaseConfig {
    private final Map<String, RideCounterMapConfig> rideCounterMapConfigs;

    public RideCounterMapConfigs(Map<String, RideCounterMapConfig> rideCounterMapConfigs) {
        this.rideCounterMapConfigs = rideCounterMapConfigs;
    }

    public RideCounterMapConfigs() {
        this(null);
    }

    public Map<String, RideCounterMapConfig> getRideCounterMapConfigs() {
        return rideCounterMapConfigs;
    }

    public static RideCounterMapConfigs fromConfigurationSection(String rideIdentifier, ConfigurationSection configurationSection) {
        if(configurationSection == null) return new RideCounterMapConfigs();

        Map<String, RideCounterMapConfig> rideCounterMapConfigs = configurationSection.getKeys(false).stream().collect(
                Collectors.toMap(
                        key -> key,
                        key -> RideCounterMapConfig.fromConfigurationSection(rideIdentifier, configurationSection.getConfigurationSection(key))
                )
        );

        return new RideCounterMapConfigs(rideCounterMapConfigs);
    }


}
