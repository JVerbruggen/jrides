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
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.SimpleDispatchLock;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.gate.FenceGate;
import com.jverbruggen.jrides.models.ride.gate.Gate;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class GateOwnerConfigSpec {
    private final GateSpecConfig gateSpecConfigEntry;
    private final GateSpecConfig gateSpecConfigExit;

    public GateOwnerConfigSpec(GateSpecConfig gateSpecConfigEntry, GateSpecConfig gateSpecConfigExit) {
        this.gateSpecConfigEntry = gateSpecConfigEntry;
        this.gateSpecConfigExit = gateSpecConfigExit;
    }

    public GateSpecConfig getGateSpecConfigEntry() {
        return gateSpecConfigEntry;
    }

    public GateSpecConfig getGateSpecConfigExit() {
        return gateSpecConfigExit;
    }

    public static GateOwnerConfigSpec fromConfigurationSection(ConfigurationSection configurationSection) {
        if(configurationSection == null)
            throw new RuntimeException("Configuration section not defined for gate");

        GateSpecConfig gateSpecConfigEntry = null;
        GateSpecConfig gateSpecConfigExit = null;

        if (configurationSection.contains("entry")) gateSpecConfigEntry = GateSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("entry"));
        if (configurationSection.contains("exit")) gateSpecConfigExit = GateSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("exit"));

        return new GateOwnerConfigSpec(gateSpecConfigEntry, gateSpecConfigExit);
    }

    public List<Gate> createGates(String stationName, World world, DispatchLockCollection gatesGenericLock){
        LanguageFile languageFile = JRidesPlugin.getLanguageFile();
        List<Gate> gates = new ArrayList<>();

        List<GateConfig> gateConfigs = getGateSpecConfigEntry().getGates();
        for(int i = 0; i < gateConfigs.size(); i++){
            GateConfig gateConfig = gateConfigs.get(i);
            String gateName = stationName + "_gate_" + i;
            Vector3 location = gateConfig.getLocation();
            final String gateDisplayName = "" + i;
            gates.add(new FenceGate(gateName,
                    new SimpleDispatchLock(gatesGenericLock,
                            languageFile.get(LanguageFileField.NOTIFICATION_RIDE_GATE_NOT_CLOSED,
                                    b -> b.add(LanguageFileTag.name, gateDisplayName)),
                            false),
                    location.toBukkitLocation(world).getBlock()));
        }

        return gates;
    }
}
