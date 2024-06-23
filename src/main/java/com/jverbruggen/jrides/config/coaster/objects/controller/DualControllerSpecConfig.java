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

package com.jverbruggen.jrides.config.coaster.objects.controller;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class DualControllerSpecConfig extends BaseConfig {
    private String stationLeft;
    private String stationRight;

    public DualControllerSpecConfig(String stationLeft, String stationRight) {
        this.stationLeft = stationLeft;
        this.stationRight = stationRight;
    }

    public String getStationLeft() {
        return stationLeft;
    }

    public String getStationRight() {
        return stationRight;
    }

    public static DualControllerSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String stationLeft = getString(configurationSection, "stationLeft");
        String stationRight = getString(configurationSection, "stationRight");

        return new DualControllerSpecConfig(stationLeft, stationRight);
    }
}
