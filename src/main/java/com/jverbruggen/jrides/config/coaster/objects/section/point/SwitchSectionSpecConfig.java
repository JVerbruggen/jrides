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

package com.jverbruggen.jrides.config.coaster.objects.section.point;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class SwitchSectionSpecConfig extends BaseConfig {
    private final List<String> destinations;
    private final String handler;
    private final String origin;

    public SwitchSectionSpecConfig(List<String> destinations, String handler, String origin) {
        this.destinations = destinations;
        this.handler = handler;
        this.origin = origin;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public String getHandler() {
        return handler;
    }

    public String getOrigin() {
        return origin;
    }

    public static SwitchSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection){
        List<String> destinations = getStringList(configurationSection, "destinations");
        String handler = getString(configurationSection, "handler", "roundrobin");
        String origin = getString(configurationSection, "origin");

        return new SwitchSectionSpecConfig(destinations, handler, origin);
    }
}
