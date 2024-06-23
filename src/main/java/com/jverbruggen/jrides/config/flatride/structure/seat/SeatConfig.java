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

package com.jverbruggen.jrides.config.flatride.structure.seat;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfigItem;
import com.jverbruggen.jrides.config.flatride.structure.attachment.AttachmentConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.RelativeMultipleAttachmentConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class SeatConfig extends BaseConfig implements StructureConfigItem {
    private final String identifier;
    private final AttachmentConfig attachmentConfig;
    private final List<ModelConfig> flatRideModels;
    private final int seatYawOffset;

    public SeatConfig(String identifier, AttachmentConfig attachmentConfig, List<ModelConfig> flatRideModels, int seatYawOffset) {
        this.identifier = identifier;
        this.attachmentConfig = attachmentConfig;
        this.flatRideModels = flatRideModels;
        this.seatYawOffset = seatYawOffset;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void createAndAddTo(List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        attachmentConfig.createSeatWithAttachment(this, components, rideHandle);
    }

    public AttachmentConfig getAttachmentConfig() {
        return attachmentConfig;
    }

    public List<ModelConfig> getFlatRideModels() {
        return flatRideModels;
    }


    public int getSeatYawOffset() {
        return seatYawOffset;
    }

    public static StructureConfigItem fromConfigurationSection(ConfigurationSection configurationSection, String identifier) {
        AttachmentConfig attachmentConfig = null;
        if(configurationSection.contains("arm")){
            attachmentConfig = RelativeMultipleAttachmentConfig.fromConfigurationSection(configurationSection);
        }

        if(attachmentConfig == null) throw new RuntimeException("No attachment specified for rotor");

        List<ModelConfig> modelConfigs;
        if(configurationSection.contains("models"))
            modelConfigs = ModelConfig.multipleFromConfigurationSection(getConfigurationSection(configurationSection, "models"));
        else modelConfigs = new ArrayList<>();

        int seatYawOffset = getInt(configurationSection, "seatRotationDegrees", 0);

        return new SeatConfig(identifier, attachmentConfig, modelConfigs, seatYawOffset);
    }

}
