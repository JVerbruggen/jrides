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

package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class LaunchEffectsConfig {
    private final List<String> launchEffects;

    public LaunchEffectsConfig(List<String> launchEffects) {
        this.launchEffects = launchEffects;
    }

    public List<String> getLaunchEffects() {
        return launchEffects;
    }

    public static LaunchEffectsConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return new LaunchEffectsConfig(List.of());

        List<String> launchEffects = configurationSection.getStringList("launch");

        return new LaunchEffectsConfig(launchEffects);
    }
}

