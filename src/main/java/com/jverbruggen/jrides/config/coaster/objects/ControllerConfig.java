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

package com.jverbruggen.jrides.config.coaster.objects;

import com.jverbruggen.jrides.config.coaster.objects.controller.DualControllerSpecConfig;
import org.bukkit.configuration.ConfigurationSection;

public class ControllerConfig extends BaseConfig {
    public static final String CONTROLLER_DEFAULT = "default";
    public static final String CONTROLLER_ALTERNATE = "alternate";
    public static final String CONTROLLER_SIMULTANEOUS = "simultaneous";

    private String type;
    private DualControllerSpecConfig dualControllerSpecConfig;

    public ControllerConfig(String type, DualControllerSpecConfig dualControllerSpecConfig) {
        this.type = type;
        this.dualControllerSpecConfig = dualControllerSpecConfig;
    }

    public String getType() {
        return type;
    }

    public DualControllerSpecConfig getAlternateControllerSpecConfig() {
        return dualControllerSpecConfig;
    }

    public static ControllerConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return null;

        String type = getString(configurationSection, "type", "default");
        DualControllerSpecConfig dualControllerSpecConfig = null;

        if(type.equalsIgnoreCase(CONTROLLER_ALTERNATE))
            dualControllerSpecConfig = DualControllerSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("spec"));
        else if(type.equalsIgnoreCase(CONTROLLER_SIMULTANEOUS))
            dualControllerSpecConfig = DualControllerSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("spec"));

        return new ControllerConfig(type, dualControllerSpecConfig);
    }
}

