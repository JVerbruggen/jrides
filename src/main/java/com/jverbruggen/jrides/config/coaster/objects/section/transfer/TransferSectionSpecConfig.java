package com.jverbruggen.jrides.config.coaster.objects.section.transfer;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TransferSectionSpecConfig extends BaseConfig {
    private final List<TransferSectionPositionSpecConfig> positions;
    private final double engage;
    private final Vector3 origin;

    public TransferSectionSpecConfig(List<TransferSectionPositionSpecConfig> positions, double engage, Vector3 origin) {
        this.positions = positions;
        this.engage = engage;
        this.origin = origin;
    }

    public List<TransferSectionPositionSpecConfig> getPositions() {
        return positions;
    }

    public double getEngage() {
        return engage;
    }

    public Vector3 getOrigin() {
        return origin;
    }

    public static TransferSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection){
        List<TransferSectionPositionSpecConfig> positions = new ArrayList<>();

        ConfigurationSection positionsConfigurationSection = configurationSection.getConfigurationSection("positions");
        Set<String> positionsStrings = positionsConfigurationSection.getKeys(false);
        for(String positionsString : positionsStrings){
            ConfigurationSection positionConfigurationSection = positionsConfigurationSection.getConfigurationSection(positionsString);
            TransferSectionPositionSpecConfig transferPosition = TransferSectionPositionSpecConfig.fromConfigurationSection(positionConfigurationSection);
            positions.add(transferPosition);
        }

        double engage = getDouble(configurationSection, "engage");
        Vector3 origin = Vector3.fromDoubleList(getDoubleList(configurationSection, "origin"));

        return new TransferSectionSpecConfig(positions, engage, origin);
    }
}
