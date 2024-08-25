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
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.external.CommandAsPlayerEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

public class CommandAsPlayerTriggerConfig extends BaseTriggerConfig {
    private final String command;

    public CommandAsPlayerTriggerConfig(String command) {
        super(TriggerType.COMMAND_AS_PLAYER);
        this.command = command;
    }

    public static CommandAsPlayerTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        String command = getString(configurationSection, "command");

        return new CommandAsPlayerTriggerConfig(command);
    }

    @Override
    public EffectTrigger createTrigger(String rideIdentifier) {
        return new CommandAsPlayerEffectTrigger(command);
    }
}
