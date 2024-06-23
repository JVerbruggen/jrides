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

package com.jverbruggen.jrides.config.trigger.external.animation;

import com.jverbruggen.jrides.animator.animatedjava.AnimatedJavaExecutor;
import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.external.animation.AnimatedJavaEffectTrigger;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class AnimatedJavaTriggerConfig extends BaseTriggerConfig {
    private final Vector3 location;
    private final String rigName;
    private final String animationName;
    private final String rootEntitySelectorTag;
    private final int despawnAfterTicks;

    public AnimatedJavaTriggerConfig(Vector3 location, String rigName, String animationName, String rootEntitySelectorTag, int despawnAfterTicks) {
        super(TriggerType.ANIMATED_JAVA);
        this.location = location;
        this.rigName = rigName;
        this.animationName = animationName;
        this.rootEntitySelectorTag = rootEntitySelectorTag;
        this.despawnAfterTicks = despawnAfterTicks;
    }

    public static AnimatedJavaTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        Vector3 location = Vector3.fromDoubleList(getDoubleList(configurationSection, "location"));
        String rigName = getString(configurationSection, "rigName");
        String animationName = getString(configurationSection, "animationName");
        String rootEntitySelectorTag = getString(configurationSection, "rootEntitySelectorTag");
        int despawnAfterTicks = getInt(configurationSection, "despawnOnFinish", -1);

        return new AnimatedJavaTriggerConfig(location, rigName, animationName, rootEntitySelectorTag, despawnAfterTicks);
    }

    @Override
    public EffectTrigger createTrigger() {
        AnimatedJavaExecutor executor = new AnimatedJavaExecutor(rootEntitySelectorTag, rigName, location);
        return new AnimatedJavaEffectTrigger(executor, animationName, despawnAfterTicks);
    }
}
