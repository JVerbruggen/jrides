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

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RidesConfig {
    private final List<RidesConfigObject> rides;

    public RidesConfig(List<RidesConfigObject> rides) {
        this.rides = rides;
    }

    public List<RidesConfigObject> getRides() {
        return rides;
    }

    public static RidesConfig fromConfigurationSection(ConfigurationSection configurationSection){
        List<RidesConfigObject> rides = new ArrayList<>();
        if(configurationSection != null){
            rides = configurationSection.getKeys(false)
                    .stream()
                    .map(i -> RidesConfigObject.fromConfigurationSection(configurationSection.getConfigurationSection(i)))
                    .collect(Collectors.toList());
        }

        return new RidesConfig(rides);
    }
}
