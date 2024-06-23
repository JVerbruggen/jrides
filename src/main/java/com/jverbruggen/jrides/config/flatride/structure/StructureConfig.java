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

package com.jverbruggen.jrides.config.flatride.structure;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LimbConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LinearActuatorConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.RotorConfig;
import com.jverbruggen.jrides.config.flatride.structure.basic.StaticStructureConfig;
import com.jverbruggen.jrides.config.flatride.structure.seat.SeatConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StructureConfig extends BaseConfig {
    private final List<StructureConfigItem> items;

    public StructureConfig(List<StructureConfigItem> items) {
        this.items = items;
    }

    public List<StructureConfigItem> getItems() {
        return items;
    }

    public static StructureConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        List<StructureConfigItem> items = new ArrayList<>();

        Set<String> keys = configurationSection.getKeys(false);
        for(String key : keys){
            ConfigurationSection itemConfigurationSection = getConfigurationSection(configurationSection, key);
            if(itemConfigurationSection == null) throw new RuntimeException("No contents in structure item " + key);

            String type = getString(itemConfigurationSection, "type");

            StructureConfigItem structureConfigItem = switch (type) {
                case "static" -> StaticStructureConfig.fromConfigurationSection(itemConfigurationSection, key);
                case "limb" -> LimbConfig.fromConfigurationSection(itemConfigurationSection, key);
                case "rotor" -> RotorConfig.fromConfigurationSection(itemConfigurationSection, key);
                case "seat" -> SeatConfig.fromConfigurationSection(itemConfigurationSection, key);
                case "linear_actuator" -> LinearActuatorConfig.fromConfigurationSection(itemConfigurationSection, key);
                default -> throw new RuntimeException("Unknown structure type '" + type + "'");
            };

            items.add(structureConfigItem);
        }

        return new StructureConfig(items);
    }
}
