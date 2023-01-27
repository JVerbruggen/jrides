package com.jverbruggen.jrides.config.gates;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Set;

public class GatesConfig {
    private final HashMap<String, GateOwnerConfigSpec> gateOwnerSpecs;

    public GatesConfig(HashMap<String, GateOwnerConfigSpec> gateOwnerSpecs) {
        this.gateOwnerSpecs = gateOwnerSpecs;
    }

    public GateOwnerConfigSpec getGateOwnerSpec(String ownerName){
        if(!gateOwnerSpecs.containsKey(ownerName)) throw new RuntimeException(ownerName + " has no gates!");

        return gateOwnerSpecs.get(ownerName);
    }

    public static GatesConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        HashMap<String, GateOwnerConfigSpec> gateOwnerSpecs = new HashMap<>();
        Set<String> gateOwners = configurationSection.getKeys(false);

        for(String gateOwner : gateOwners){
            GateOwnerConfigSpec spec = GateOwnerConfigSpec.fromConfigurationSection(configurationSection.getConfigurationSection(gateOwner));
            gateOwnerSpecs.put(gateOwner, spec);
        }

        return new GatesConfig(gateOwnerSpecs);
    }
}
