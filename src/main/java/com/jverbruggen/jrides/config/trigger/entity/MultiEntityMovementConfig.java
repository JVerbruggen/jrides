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

package com.jverbruggen.jrides.config.trigger.entity;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.entity.MultipleEffectExecutorTrigger;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MultiEntityMovementConfig extends BaseTriggerConfig {
    private final List<EntityMovementCollectionConfig> entityMovements;

    public MultiEntityMovementConfig(List<EntityMovementCollectionConfig> entityMovements) {
        super(TriggerType.ANIMATION_SEQUENCE);
        this.entityMovements = entityMovements;
    }

    public List<EntityMovementCollectionConfig> getEntityMovements() {
        return entityMovements;
    }

    public List<TrainEffectTrigger> createTriggers(ViewportManager viewportManager){
        List<TrainEffectTrigger> armorstandMovements = new ArrayList<>();
        for(EntityMovementCollectionConfig entityMovementConfig : getEntityMovements()) {
            armorstandMovements.add(entityMovementConfig.createTrigger(viewportManager));
        }

        return armorstandMovements;
    }

    public static MultiEntityMovementConfig fromConfigurationSection(ConfigurationSection configurationSection){
        ConfigurationSection entitiesSection = configurationSection.getConfigurationSection("entities");
        if(entitiesSection == null) throw new RuntimeException("Trigger section needs 'entities' key");

        Set<String> entityIdentifiers = entitiesSection.getKeys(false);

        List<EntityMovementCollectionConfig> entityMovements = new ArrayList<>();
        for(String identifier : entityIdentifiers){
            EntityMovementCollectionConfig entityMovementCollection = EntityMovementCollectionConfig.fromConfigurationSection(identifier, entitiesSection.getConfigurationSection(identifier));
            if(entityMovementCollection == null){
                JRidesPlugin.getLogger().warning("Entity movement " + identifier + " was null");
                continue;
            }
            entityMovements.add(entityMovementCollection);
        }

        return new MultiEntityMovementConfig(entityMovements);
    }

    @Override
    public TrainEffectTrigger createTrigger(String rideIdentifier) {
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
        List<TrainEffectTrigger> entityMovementTriggers = createTriggers(viewportManager);

        return new MultipleEffectExecutorTrigger(entityMovementTriggers);
    }
}
