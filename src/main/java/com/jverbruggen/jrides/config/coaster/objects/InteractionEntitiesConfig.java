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

package com.jverbruggen.jrides.config.coaster.objects;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.event.action.OperateRideAction;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InteractionEntitiesConfig extends BaseConfig {
    private final Map<String, ModelConfig> interactionEntities;

    public InteractionEntitiesConfig(Map<String, ModelConfig> interactionEntities) {
        this.interactionEntities = interactionEntities;
    }

    public InteractionEntitiesConfig(){
        this.interactionEntities = new HashMap<>();
    }

    public void spawnEntities(RideHandle rideHandle){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        for(Map.Entry<String, ModelConfig> entry : interactionEntities.entrySet()){
            ModelConfig modelConfig = entry.getValue();
            Vector3 spawnPosition = modelConfig.getPosition();
            Quaternion spawnRotation = modelConfig.getRotation();
            String customName = "Controller";
            VirtualEntity virtualEntity = modelConfig.getItemConfig().spawnEntity(viewportManager, spawnPosition, spawnRotation, customName);

            virtualEntity.setCustomAction(new OperateRideAction());
            virtualEntity.setBelongsToRide(rideHandle);
        }
    }

    public static InteractionEntitiesConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection){
        if(configurationSection == null)
            return new InteractionEntitiesConfig();

        Set<String> keys = configurationSection.getKeys(false);

        Map<String, ModelConfig> entityLocations = new HashMap<>();

        for(String key : keys){
            ConfigurationSection keyConfigurationSection = configurationSection.getConfigurationSection(key);
            ModelConfig modelConfig = ModelConfig.fromConfigurationSection(keyConfigurationSection);

            entityLocations.put(key, modelConfig);
        }

        return new InteractionEntitiesConfig(entityLocations);
    }
}
