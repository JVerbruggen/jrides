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
    private final double enterDriveSpeed;
    private final double exitDriveSpeed;
    private final double acceleration;
    private final double deceleration;
    private final Vector3 origin;
    private final Vector3 modelOffsetPosition;
    private final Vector3 modelOffsetRotation;

    public TransferSectionSpecConfig(List<TransferSectionPositionSpecConfig> positions, double engage, double enterDriveSpeed, double exitDriveSpeed, double acceleration, double deceleration, Vector3 origin, Vector3 modelOffsetPosition, Vector3 modelOffsetRotation) {
        this.positions = positions;
        this.engage = engage;
        this.enterDriveSpeed = enterDriveSpeed;
        this.exitDriveSpeed = exitDriveSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
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

    public double getEnterDriveSpeed() {
        return enterDriveSpeed;
    }

    public double getExitDriveSpeed() {
        return exitDriveSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getDeceleration() {
        return deceleration;
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

        double engage = getDouble(configurationSection, "engage", 0.5);
        double enterDriveSpeed = getDouble(configurationSection, "enterDriveSpeed", 0.5);
        double exitDriveSpeed = getDouble(configurationSection, "exitDriveSpeed", enterDriveSpeed);
        double acceleration = getDouble(configurationSection, "acceleration", 0.1);
        double deceleration = getDouble(configurationSection, "deceleration", acceleration);
        Vector3 origin = Vector3.fromDoubleList(getDoubleList(configurationSection, "origin"));
        Vector3 modelOffsetPosition = Vector3.fromDoubleList(getDoubleList(configurationSection, "modelOffsetPosition"));
        Vector3 modelOffsetRotation = Vector3.fromDoubleList(getDoubleList(configurationSection, "modelOffsetRotation"));

        return new TransferSectionSpecConfig(positions, engage, enterDriveSpeed, exitDriveSpeed, acceleration, deceleration, origin, modelOffsetPosition, modelOffsetRotation);
    }
}
