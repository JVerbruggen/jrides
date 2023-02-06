package com.jverbruggen.jrides.config.coaster.objects.section.transfer;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TransferSectionSpecConfig {

    private final List<TransferSectionPositionSpecConfig> positions;

    public TransferSectionSpecConfig(List<TransferSectionPositionSpecConfig> positions) {
        this.positions = positions;
    }

    public List<TransferSectionPositionSpecConfig> getPositions() {
        return positions;
    }

    public static TransferSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection){
        List<TransferSectionPositionSpecConfig> positions = new ArrayList<>();

        Set<String> positionsStrings = configurationSection.getKeys(false);
        for(String positionsString : positionsStrings){
            ConfigurationSection positionsConfigurationSection = configurationSection.getConfigurationSection(positionsString);
            positions.add(TransferSectionPositionSpecConfig.fromConfigurationSection(positionsConfigurationSection));
        }

        return new TransferSectionSpecConfig(positions);
    }
}
