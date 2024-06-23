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

package com.jverbruggen.jrides.config.flatride.timing;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ActionConfigFactory extends BaseConfig {
    public static List<ActionConfig> fromConfigurationSection(@Nullable ConfigurationSection configurationSection){
        List<ActionConfig> actionConfigs = new ArrayList<>();
        if(configurationSection == null) return actionConfigs;

        for(String key : configurationSection.getKeys(false)){
            ConfigurationSection actionConfigurationSection = configurationSection.getConfigurationSection(key);
            assert actionConfigurationSection != null;

            if(actionConfigurationSection.contains("animation")){
                actionConfigs.add(createAnimationActionConfig(key, actionConfigurationSection));
                continue;
            }

            SimpleActionConfig actionConfig = new SimpleActionConfig(key);
            if(actionConfigurationSection.contains("speed"))
                actionConfig.setSpeed((float) getDouble(actionConfigurationSection, "speed"));
            if(actionConfigurationSection.contains("accelerate"))
                actionConfig.setAccelerate((float) getDouble(actionConfigurationSection, "accelerate"));
            if(actionConfigurationSection.contains("allowControl"))
                actionConfig.setAllowControl(getBoolean(actionConfigurationSection, "allowControl"));
            if(actionConfigurationSection.contains("targetPosition"))
                actionConfig.setTargetPosition((float) getDouble(actionConfigurationSection, "targetPosition"));

            actionConfigs.add(actionConfig);
        }

        return actionConfigs;
    }

    public static ActionConfig createAnimationActionConfig(String targetIdentifier, @Nullable ConfigurationSection configurationSection){
        String animation = getString(configurationSection, "animation");

        return new AnimationActionConfig(animation, targetIdentifier);
    }
}
