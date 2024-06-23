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

package com.jverbruggen.jrides.config.coaster.objects.item;

import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public interface ItemConfig {
    VirtualEntity spawnEntity(ViewportManager viewportManager, Vector3 spawnPosition, Quaternion spawnRotation, String customName);

    static ItemConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection){
        if(configurationSection == null) return null;

        if(configurationSection.contains("item"))
            return ItemStackConfig.fromConfigurationSection(configurationSection.getConfigurationSection("item"));
        else if(configurationSection.contains("entity"))
            return EntityConfig.fromConfigurationSection(configurationSection.getConfigurationSection("entity"));
        else throw new RuntimeException("Model config needs an item or an entity");
    }
}
