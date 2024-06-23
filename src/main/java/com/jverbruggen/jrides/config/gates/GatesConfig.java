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

package com.jverbruggen.jrides.config.gates;

import com.jverbruggen.jrides.JRidesPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public class GatesConfig {
    private final HashMap<String, GateOwnerConfigSpec> gateOwnerSpecs;

    public GatesConfig(HashMap<String, GateOwnerConfigSpec> gateOwnerSpecs) {
        this.gateOwnerSpecs = gateOwnerSpecs;
    }

    public GatesConfig() {
        gateOwnerSpecs = null;
    }

    public Optional<GateOwnerConfigSpec> getGateOwnerSpec(String ownerName){
        if(gateOwnerSpecs == null || !gateOwnerSpecs.containsKey(ownerName)){
            JRidesPlugin.getLogger().warning(ownerName + " has no gates!");
            return Optional.empty();
        }

        return Optional.of(gateOwnerSpecs.get(ownerName));
    }

    public static GatesConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null) return new GatesConfig();

        HashMap<String, GateOwnerConfigSpec> gateOwnerSpecs = new HashMap<>();
        Set<String> gateOwners = configurationSection.getKeys(false);

        for(String gateOwner : gateOwners){
            GateOwnerConfigSpec spec = GateOwnerConfigSpec.fromConfigurationSection(configurationSection.getConfigurationSection(gateOwner));
            gateOwnerSpecs.put(gateOwner, spec);
        }

        return new GatesConfig(gateOwnerSpecs);
    }
}
