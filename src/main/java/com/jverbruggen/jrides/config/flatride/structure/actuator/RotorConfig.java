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

package com.jverbruggen.jrides.config.flatride.structure.actuator;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.rotor.mode.RotorModeEnum;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfigItem;
import com.jverbruggen.jrides.config.flatride.structure.actuator.rotor.ContinuousRotorConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.rotor.RotorTypeConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.rotor.SineRotorConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.AttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.FixedAttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.RelativeMultipleAttachmentConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RotorConfig extends AbstractActuatorConfig {
    private final String rotorAxis;
    private final RotorPlayerControlConfig playerControlConfig;
    private final RotorTypeConfig rotorTypeConfig;


    public RotorConfig(String identifier, boolean root, String rotorAxis, RotorPlayerControlConfig playerControlConfig, FlatRideComponentSpeed flatRideComponentSpeed, AttachmentConfig attachmentConfig, List<ModelConfig> flatRideModels, RotorTypeConfig rotorTypeConfig) {
        super(identifier, root, attachmentConfig, flatRideComponentSpeed, flatRideModels);
        this.rotorAxis = rotorAxis;
        this.playerControlConfig = playerControlConfig;
        this.rotorTypeConfig = rotorTypeConfig;
    }

    @Override
    public void createAndAddTo(List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        getAttachmentConfig().createRotorWithAttachment(this, components, rideHandle);
    }

    public String getRotorAxis() {
        return rotorAxis;
    }

    public RotorTypeConfig getRotorTypeConfig() {
        return rotorTypeConfig;
    }

    public RotorPlayerControlConfig getPlayerControlConfig() {
        return playerControlConfig;
    }

    public static StructureConfigItem fromConfigurationSection(ConfigurationSection configurationSection, String identifier) {
        boolean root = getBoolean(configurationSection, "root", false);
        String axis = getString(configurationSection, "axis", "y");
        RotorPlayerControlConfig playerControlConfig = RotorPlayerControlConfig.fromConfigurationSection(identifier, configurationSection.getConfigurationSection("control"));
        String mode = getString(configurationSection, "mode", "continuous");

        RotorTypeConfig rotorTypeConfig = switch (RotorModeEnum.from(mode)){
            case SINE -> SineRotorConfig.fromConfigurationSection(configurationSection);
            case CONTINUOUS -> ContinuousRotorConfig.fromConfigurationSection(configurationSection);
        };

        AttachmentConfig attachmentConfig = null;
        if(configurationSection.contains("position")){
            attachmentConfig = FixedAttachmentConfig.fromConfigurationSection(configurationSection);
        }
        if(configurationSection.contains("arm")){
            if(attachmentConfig != null) throw new RuntimeException("Can only create one attachment type for a rotor");
            attachmentConfig = RelativeMultipleAttachmentConfig.fromConfigurationSection(configurationSection);
        }

        if(attachmentConfig == null) throw new RuntimeException("No attachment specified for rotor");

        FlatRideComponentSpeed flatRideComponentSpeed;
        if(configurationSection.contains("maxSpeed") || configurationSection.contains("minSpeed")){
            flatRideComponentSpeed = new FlatRideComponentSpeed(
                    (float)getDouble(configurationSection, "speed"),
                    (float)getDouble(configurationSection, "minSpeed"),
                    (float)getDouble(configurationSection, "maxSpeed"));
        }else{
            flatRideComponentSpeed = new FlatRideComponentSpeed((float)getDouble(configurationSection, "speed", 1));
        }

        List<ModelConfig> modelConfigs;
        if(configurationSection.contains("models"))
            modelConfigs = ModelConfig.multipleFromConfigurationSection(getConfigurationSection(configurationSection, "models"));
        else modelConfigs = new ArrayList<>();

        return new RotorConfig(identifier, root, axis, playerControlConfig, flatRideComponentSpeed, attachmentConfig, modelConfigs, rotorTypeConfig);
    }
}
