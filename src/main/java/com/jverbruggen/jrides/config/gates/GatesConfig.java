package com.jverbruggen.jrides.config.gates;

import com.jverbruggen.jrides.JRidesPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Set;

public class GatesConfig {
    private final HashMap<String, GateOwnerConfigSpec> gateOwnerSpecs;

    public GatesConfig(HashMap<String, GateOwnerConfigSpec> gateOwnerSpecs) {
        this.gateOwnerSpecs = gateOwnerSpecs;
    }

    public GatesConfig() {
        gateOwnerSpecs = null;
    }

    public GateOwnerConfigSpec getGateOwnerSpec(String ownerName){
        if(gateOwnerSpecs == null || !gateOwnerSpecs.containsKey(ownerName)){
            JRidesPlugin.getLogger().warning(ownerName + " has no gates!");
            return null;
        }

        return gateOwnerSpecs.get(ownerName);
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
