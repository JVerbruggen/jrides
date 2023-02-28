package com.jverbruggen.jrides.config.coaster.objects.section.ranged.transfer;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TransferSectionSpecConfig extends BaseConfig {
    private final List<TransferSectionPositionSpecConfig> positions;
    private final double engage;
    private final Vector3 origin;
    private final Vector3 modelOffsetPosition;
    private final Vector3 modelOffsetRotation;

    public TransferSectionSpecConfig(List<TransferSectionPositionSpecConfig> positions, double engage, Vector3 origin, Vector3 modelOffsetPosition, Vector3 modelOffsetRotation) {
        this.positions = positions;
        this.engage = engage;
        this.origin = origin;
        this.modelOffsetPosition = modelOffsetPosition;
        this.modelOffsetRotation = modelOffsetRotation;
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

    public Vector3 getModelOffsetPosition() {
        return modelOffsetPosition;
    }

    public Vector3 getModelOffsetRotation() {
        return modelOffsetRotation;
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
        Vector3 modelOffsetPosition = Vector3.fromDoubleList(getDoubleList(configurationSection, "modelOffsetPosition"));
        Vector3 modelOffsetRotation = Vector3.fromDoubleList(getDoubleList(configurationSection, "modelOffsetRotation"));

        return new TransferSectionSpecConfig(positions, engage, origin, modelOffsetPosition, modelOffsetRotation);
    }
}
