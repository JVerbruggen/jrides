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

package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.animator.flatride.rotor.ModelWithOffset;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModelConfig extends BaseConfig {
    private final ItemConfig itemConfig;
    private final Vector3 position;
    private final Quaternion rotation;

    public ModelConfig(ItemConfig itemConfig, Vector3 position, Quaternion rotation) {
        this.itemConfig = itemConfig;
        this.position = position;
        this.rotation = rotation;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public ItemConfig getItemConfig() {
        return itemConfig;
    }

    public ModelWithOffset toModelWithOffset(Vector3 rootPosition, ViewportManager viewportManager) {
        return toModelWithOffset(rootPosition, new Quaternion(), viewportManager);
    }

    public ModelWithOffset toModelWithOffset(Vector3 rootPosition, Quaternion rootOrientation, ViewportManager viewportManager){
        Vector3 spawnPosition = Vector3.add(rootPosition, position);
        VirtualEntity virtualEntity = itemConfig.spawnEntity(viewportManager, spawnPosition, rootOrientation, null);

        return new ModelWithOffset(virtualEntity, position.clone(), rotation.clone());
    }

    public static ModelConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        if(configurationSection == null) return new ModelConfig(null, Vector3.zero(), new Quaternion());

        ItemConfig itemConfig = ItemConfig.fromConfigurationSection(configurationSection);

        Vector3 vector = Vector3.fromDoubleList(getDoubleList(configurationSection, "position", List.of(0d,0d,0d)));
        Quaternion rotation = Quaternion.fromDoubleList(getDoubleList(configurationSection, "rotation", List.of(0d,0d,0d)));

        return new ModelConfig(itemConfig, vector, rotation);
    }

    public static List<ModelConfig> multipleFromConfigurationSection(ConfigurationSection configurationSection){
        List<ModelConfig> modelConfigs = new ArrayList<>();
        if(configurationSection == null)
            return modelConfigs;

        Set<String> keys = configurationSection.getKeys(false);
        for(String key : keys){
            ModelConfig modelConfig = fromConfigurationSection(getConfigurationSection(configurationSection, key));
            modelConfigs.add(modelConfig);
        }
        return modelConfigs;
    }
}
