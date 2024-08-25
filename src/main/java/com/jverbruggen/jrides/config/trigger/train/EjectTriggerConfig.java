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

package com.jverbruggen.jrides.config.trigger.train;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.train.EjectEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

public class EjectTriggerConfig extends BaseTriggerConfig {
    private final boolean asFinished;

    public EjectTriggerConfig(boolean asFinished) {
        super(TriggerType.EJECT);
        this.asFinished = asFinished;
    }

    public static EjectTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        boolean asFinished = getBoolean(configurationSection, "asFinished", true);

        return new EjectTriggerConfig(asFinished);
    }

    @Override
    public EffectTrigger createTrigger(String rideIdentifier) {
        return new EjectEffectTrigger(asFinished);
    }
}
