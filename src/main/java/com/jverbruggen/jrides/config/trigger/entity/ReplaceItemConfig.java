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
import com.jverbruggen.jrides.config.coaster.objects.item.ItemStackConfig;
import com.jverbruggen.jrides.effect.entity.EntityMovementTrigger;
import com.jverbruggen.jrides.effect.entity.ReplaceItemEffectTrigger;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class ReplaceItemConfig extends BaseConfig implements EntityMovementConfig {
    private final ItemStackConfig itemStackConfig;
    private final int delayTicks;

    public ReplaceItemConfig(ItemStackConfig itemStackConfig, int delayTicks) {
        this.itemStackConfig = itemStackConfig;
        this.delayTicks = delayTicks;
    }

    @Override
    public EntityMovementTrigger createTrigger(VirtualEntity virtualEntity) {
        TrainModelItem trainModelItem = new TrainModelItem(itemStackConfig.createItemStack());
        return new ReplaceItemEffectTrigger(trainModelItem, virtualEntity, delayTicks);
    }

    @Override
    public Vector3 getInitialPosition() {
        return null;
    }

    @Override
    public Vector3 getInitialRotation() {
        return null;
    }

    public static ReplaceItemConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        ItemStackConfig itemStackConfig = ItemStackConfig.fromConfigurationSection(configurationSection.getConfigurationSection("item"));
        int delayTicks = getInt(configurationSection, "delayTicks", 0);

        return new ReplaceItemConfig(itemStackConfig, delayTicks);
    }
}
