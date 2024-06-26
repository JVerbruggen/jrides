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

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.effect.entity.EntityMovementTrigger;
import com.jverbruggen.jrides.effect.train.SequenceTrainEffectTrigger;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.LocRot;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityMovementCollectionConfig extends BaseConfig {
    private final String identifier;
    private final ItemConfig itemConfig;
    private final Vector3 spawnLocation;
    private final Vector3 spawnRotation;
    private final List<EntityMovementConfig> entityMovementConfigs;

    public EntityMovementCollectionConfig(String identifier, ItemConfig itemConfig, Vector3 spawnLocation, Vector3 spawnRotation, List<EntityMovementConfig> entityMovementConfigs) {
        this.identifier = identifier;
        this.itemConfig = itemConfig;
        this.spawnLocation = spawnLocation;
        this.spawnRotation = spawnRotation;
        this.entityMovementConfigs = entityMovementConfigs;
    }

    static EntityMovementCollectionConfig fromConfigurationSection(String identifier, @Nullable ConfigurationSection configurationSection){
        if(configurationSection == null) return null;

        ConfigurationSection animationConfigurationSection = configurationSection.getConfigurationSection("animation");

        List<EntityMovementConfig> entityMovements = new ArrayList<>();
        if(animationConfigurationSection != null){
            Set<String> keys = animationConfigurationSection.getKeys(false);
            for(String key : keys){
                EntityMovementConfig movementConfig = getMovementConfig(identifier, animationConfigurationSection.getConfigurationSection(key));
                if(movementConfig == null) continue;

                entityMovements.add(movementConfig);
            }
        }

        Vector3 spawnLocation = Vector3.fromDoubleList(getDoubleList(configurationSection, "spawnLocation", null));
        Vector3 spawnRotation = Vector3.fromDoubleList(getDoubleList(configurationSection, "spawnRotation", null));
        ItemConfig itemConfig = ItemConfig.fromConfigurationSection(configurationSection);

        return new EntityMovementCollectionConfig(identifier, itemConfig, spawnLocation, spawnRotation, entityMovements);
    }

    static EntityMovementConfig getMovementConfig(String identifier, @Nullable ConfigurationSection configurationSection){
        if(configurationSection == null) return null;

        String movementType = getString(configurationSection, "type", null);
        if(movementType != null && movementType.equalsIgnoreCase("projectile")){
            return EntityProjectileConfig.fromConfigurationSection(configurationSection);
        }

        if(configurationSection.contains("locationFrom") || configurationSection.contains("rotationFrom")){
            return EntityFromToMovementConfig.fromConfigurationSection(configurationSection);
        }else if(configurationSection.contains("locationDelta") || configurationSection.contains("rotationDelta")){
            return EntityContinuousMovementConfig.fromConfigurationSection(configurationSection);
        }else if(configurationSection.contains("item")){
            return ReplaceItemConfig.fromConfigurationSection(configurationSection);
        }

        throw new RuntimeException("Unknown entity movement in " + identifier);
    }

    private LocRot getSpawnLocation(){
        if(spawnLocation != null) {
            return LocRot.fromLocationRotation(spawnLocation, spawnRotation);
        }

        if(entityMovementConfigs.size() == 0) throw new RuntimeException("Cannot get spawn location");

        LocRot initialLocation = null;
        for(EntityMovementConfig config : entityMovementConfigs){
            Vector3 possibleLocation = config.getInitialPosition();
            if(possibleLocation != null){
                initialLocation = new LocRot(possibleLocation, config.getInitialRotation());
                break;
            }
        }

        if(initialLocation == null) throw new RuntimeException("Animation does not support initial location, cannot spawn entity");

        return initialLocation;
    }

    public TrainEffectTrigger createTrigger(ViewportManager viewportManager) {
        LocRot spawnLocation = getSpawnLocation();
        Quaternion rotation = Quaternion.fromAnglesVector(spawnLocation.rotation());
        VirtualEntity virtualEntity = itemConfig.spawnEntity(viewportManager, spawnLocation.location(), rotation, null);
        virtualEntity.setRotation(Quaternion.fromAnglesVector(spawnLocation.rotation()));

        List<EntityMovementTrigger> triggerSequence = entityMovementConfigs.stream()
                .map(c -> c.createTrigger(virtualEntity))
                .collect(Collectors.toList());

        return new SequenceTrainEffectTrigger(identifier, triggerSequence);
    }
}
