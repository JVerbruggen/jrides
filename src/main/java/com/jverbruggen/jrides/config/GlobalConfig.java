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

package com.jverbruggen.jrides.config;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.global.RidesMenuConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class GlobalConfig extends BaseConfig {
    private final String worldName;
    private final RidesMenuConfig ridesMenuConfig;

    public GlobalConfig(String worldName, RidesMenuConfig ridesMenuConfig) {
        this.worldName = worldName;
        this.ridesMenuConfig = ridesMenuConfig;
    }

    public String getWorldName() {
        return worldName;
    }

    public RidesMenuConfig getRidesMenuConfig() {
        return ridesMenuConfig;
    }

    public static void fillDefaults(YamlConfiguration yamlConfiguration){
        String worldName = Bukkit.getWorlds().get(0).getName();
        yamlConfiguration.set("config.worldName", worldName);

        RidesMenuConfig.fillDefaults(yamlConfiguration);
    }

    public static GlobalConfig fromConfigurationSection(ConfigurationSection configurationSection){
        String worldName = getString(configurationSection, "worldName");
        RidesMenuConfig ridesMenuConfig = RidesMenuConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "ridesMenu"));

        return new GlobalConfig(worldName, ridesMenuConfig);
    }
}
