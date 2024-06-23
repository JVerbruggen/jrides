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

package com.jverbruggen.jrides.config.trigger.music;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.effect.train.music.ExternalMusicEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

public class MusicTriggerConfig extends BaseTriggerConfig {
    private final MusicTriggerConfigHandler handler;
    private final String resource;
    private final String descriptor;

    public MusicTriggerConfig(MusicTriggerConfigHandler handler, String resource, String descriptor) {
        super(TriggerType.MUSIC);
        this.handler = handler;
        this.resource = resource;
        this.descriptor = descriptor;
    }

    @SuppressWarnings("unused")
    public MusicTriggerConfigHandler getHandler() {
        return handler;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getResource() {
        return resource;
    }

    public static MusicTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        MusicTriggerConfigHandler handler = MusicTriggerConfigHandler.fromString(getString(configurationSection, "handler"));
        String resource = getString(configurationSection, "resource");
        String descriptor = getString(configurationSection, "descriptor", "default");

        return new MusicTriggerConfig(handler, resource, descriptor);
    }

    @Override
    public TrainEffectTrigger createTrigger() {
        return new ExternalMusicEffectTrigger(getResource(), getDescriptor());
    }
}