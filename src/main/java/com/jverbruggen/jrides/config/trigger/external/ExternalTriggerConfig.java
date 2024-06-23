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

package com.jverbruggen.jrides.config.trigger.external;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.effect.train.external.ExternalEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExternalTriggerConfig extends BaseTriggerConfig {
    private final Map<String, String> data;

    public ExternalTriggerConfig(Map<String, String> data) {
        super(TriggerType.EXTERNAL_EVENT);

        this.data = data;
    }

    public static ExternalTriggerConfig fromConfigurationSection(@Nonnull ConfigurationSection configurationSection){
        Map<String, String> data = configurationSection.getKeys(false)
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        k -> Objects.requireNonNull(configurationSection.getString(k))));

        return new ExternalTriggerConfig(data);
    }

    @Override
    public TrainEffectTrigger createTrigger() {
        return new ExternalEffectTrigger(data);
    }
}