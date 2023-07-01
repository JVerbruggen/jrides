package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class TrimBrakeSectionSpecConfig extends BaseConfig {
    private final double trimResistanceConstant;

    public TrimBrakeSectionSpecConfig(double trimResistanceConstant) {
        this.trimResistanceConstant = trimResistanceConstant;
    }

    public double getTrimResistanceConstant() {
        return trimResistanceConstant;
    }

    public static TrimBrakeSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double trimResistanceConstant = getDouble(configurationSection, "trimResistanceConstant", 0.9);

        return new TrimBrakeSectionSpecConfig(trimResistanceConstant);
    }
}
