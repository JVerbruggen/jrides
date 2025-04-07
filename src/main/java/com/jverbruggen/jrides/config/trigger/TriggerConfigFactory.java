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

package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.config.trigger.cart.CartRestraintTriggerConfig;
import com.jverbruggen.jrides.config.trigger.entity.MultiEntityMovementConfig;
import com.jverbruggen.jrides.config.trigger.external.CommandAsPlayerTriggerConfig;
import com.jverbruggen.jrides.config.trigger.external.CommandForPlayerTriggerConfig;
import com.jverbruggen.jrides.config.trigger.external.CommandTriggerConfig;
import com.jverbruggen.jrides.config.trigger.external.ExternalTriggerConfig;
import com.jverbruggen.jrides.config.trigger.external.animation.AnimatedJavaTriggerConfig;
import com.jverbruggen.jrides.config.trigger.external.animation.BlenderTriggerConfig;
import com.jverbruggen.jrides.config.trigger.music.MusicTriggerConfig;
import com.jverbruggen.jrides.config.trigger.structure.StructureConfig;
import com.jverbruggen.jrides.config.trigger.train.EjectTriggerConfig;
import com.jverbruggen.jrides.config.trigger.train.ResetTriggerConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class TriggerConfigFactory {
    private final Map<String, TriggerConfig> triggerConfigList;

    public TriggerConfigFactory() {
        this.triggerConfigList = new HashMap<>();
    }

    public TriggerConfig fromConfigurationSection(String rideIdentifier, String effectName, ConfigurationSection configurationSection){
        if(configurationSection == null) throw new RuntimeException("Trigger configuration was null for effect " + effectName);

        TriggerType type = TriggerType.fromString(configurationSection.getString("type"));
        TriggerConfig triggerConfig;
        String mapKey = rideIdentifier + ":" + effectName;
        if(triggerConfigList.containsKey(mapKey)){
            JRidesPlugin.getLogger().warning("Duplicate trigger identifier in triggers of ride " + rideIdentifier + ": '" + effectName + "'. Ignoring new one.");
            return triggerConfigList.get(mapKey);
        }

        triggerConfig = switch (type) {
            case RESET ->
                    ResetTriggerConfig.fromConfigurationSection(configurationSection);
            case MUSIC ->
                    MusicTriggerConfig.fromConfigurationSection(configurationSection);
            case COMMAND ->
                    CommandTriggerConfig.fromConfigurationSection(configurationSection);
            case EJECT ->
                    EjectTriggerConfig.fromConfigurationSection(configurationSection);
            case COMMAND_FOR_PLAYER ->
                    CommandForPlayerTriggerConfig.fromConfigurationSection(configurationSection);
            case COMMAND_AS_PLAYER ->
                    CommandAsPlayerTriggerConfig.fromConfigurationSection(configurationSection);
            case ANIMATION_SEQUENCE ->
                    MultiEntityMovementConfig.fromConfigurationSection(configurationSection);
            case BLENDER_ANIMATION ->
                    BlenderTriggerConfig.fromConfigurationSection(configurationSection);
            case ANIMATED_JAVA ->
                    AnimatedJavaTriggerConfig.fromConfigurationSection(configurationSection);
            case STRUCTURE ->
                    StructureConfig.fromConfigurationSection(configurationSection);
            case EXTERNAL_EVENT ->
                    ExternalTriggerConfig.fromConfigurationSection(configurationSection);
            case CART_ROTATE ->
                    throw new RuntimeException("Not supported yet cart rotate");
            case CART_RESTRAINT ->
                    CartRestraintTriggerConfig.fromConfigurationSection(configurationSection);
        };

        triggerConfigList.put(mapKey, triggerConfig);

        return triggerConfig;
    }
}
