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

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class StationEffectsConfig extends BaseConfig {
    private final List<String> entryBlockingEffects;
    private final List<String> exitBlockingEffects;
    private final List<String> exitEffects;

    public StationEffectsConfig(List<String> entryBlockingEffects, List<String> exitBlockingEffects, List<String> exitEffects) {
        this.entryBlockingEffects = entryBlockingEffects;
        this.exitBlockingEffects = exitBlockingEffects;
        this.exitEffects = exitEffects;
    }

    public StationEffectsConfig() {
        this.entryBlockingEffects = null;
        this.exitBlockingEffects = null;
        this.exitEffects = null;
    }

    public List<String> getEntryBlockingEffects() {
        return entryBlockingEffects;
    }

    public List<String> getExitBlockingEffects() {
        return exitBlockingEffects;
    }

    public List<String> getExitEffects() {
        return exitEffects;
    }

    public static StationEffectsConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return new StationEffectsConfig();
        List<String> entryBlockingEffects = getStringList(configurationSection, "entryBlocking", List.of());
        List<String> exitBlockingEffects = getStringList(configurationSection, "exitBlocking", List.of());
        List<String> exitEffects = getStringList(configurationSection, "exit", List.of());

        return new StationEffectsConfig(entryBlockingEffects, exitBlockingEffects, exitEffects);
    }
}

