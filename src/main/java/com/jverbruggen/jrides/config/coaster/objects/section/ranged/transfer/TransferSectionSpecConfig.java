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

package com.jverbruggen.jrides.config.coaster.objects.section.ranged.transfer;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
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
    private final List<ModelConfig> transferModelConfigs;

    public TransferSectionSpecConfig(List<TransferSectionPositionSpecConfig> positions, double engage, double enterDriveSpeed, double exitDriveSpeed, double acceleration, double deceleration, Vector3 origin, List<ModelConfig> transferModelConfigs) {
        this.positions = positions;
        this.engage = engage;
        this.enterDriveSpeed = enterDriveSpeed;
        this.exitDriveSpeed = exitDriveSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.origin = origin;
        this.transferModelConfigs = transferModelConfigs;
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

    public List<ModelConfig> getTransferModelConfigs() {
        return transferModelConfigs;
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

        List<ModelConfig> transferModelConfigs = ModelConfig.multipleFromConfigurationSection(configurationSection.getConfigurationSection("model"));

        return new TransferSectionSpecConfig(positions, engage, enterDriveSpeed, exitDriveSpeed, acceleration, deceleration, origin, transferModelConfigs);
    }
}
