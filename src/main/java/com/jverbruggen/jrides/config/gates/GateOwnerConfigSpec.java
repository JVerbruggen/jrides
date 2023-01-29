package com.jverbruggen.jrides.config.gates;

import org.bukkit.configuration.ConfigurationSection;

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
        GateSpecConfig gateSpecConfigEntry = null;
        GateSpecConfig gateSpecConfigExit = null;

        if (configurationSection.contains("entry")) gateSpecConfigEntry = GateSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("entry"));
        if (configurationSection.contains("exit")) gateSpecConfigExit = GateSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("exit"));

        return new GateOwnerConfigSpec(gateSpecConfigEntry, gateSpecConfigExit);
    }
}