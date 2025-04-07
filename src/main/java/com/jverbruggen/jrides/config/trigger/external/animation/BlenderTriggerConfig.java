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

import com.jverbruggen.jrides.animator.blender.BlenderAnimationExecutor;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemStackConfig;
import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.external.animation.BlenderEffectTrigger;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.flatride.AnimationHandle;
import com.jverbruggen.jrides.state.ride.flatride.AnimationLoader;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

public class BlenderTriggerConfig extends BaseTriggerConfig {
    private final Vector3 location;
    private final String animationName;
    private final String reuseEntity;
    private final int despawnAfterTicks;
    private final boolean preloadAnim;
    private final ItemStackConfig headModelConfig;

    public BlenderTriggerConfig(Vector3 location, String animationName, String reuseEntity, int despawnAfterTicks, boolean preloadAnim, ItemStackConfig headModelConfig) {
        super(TriggerType.ANIMATED_JAVA);
        this.location = location;
        this.animationName = animationName;
        this.reuseEntity = reuseEntity;
        this.despawnAfterTicks = despawnAfterTicks;
        this.preloadAnim = preloadAnim;
        this.headModelConfig = headModelConfig;
    }

    public static BlenderTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        Vector3 location = Vector3.fromDoubleList(getDoubleList(configurationSection, "location"));
        String animationName = getString(configurationSection, "animationName");
        String reuseEntity = getString(configurationSection, "reuseEntity", null);
        boolean preloadAnim = getBoolean(configurationSection, "preloadAnim", false);
        int despawnAfterTicks = getInt(configurationSection, "despawnOnFinish", -1);
        ItemStackConfig headModelConfig = ItemStackConfig.fromConfigurationSection(configurationSection.getConfigurationSection("item"));

        return new BlenderTriggerConfig(location, animationName, reuseEntity, despawnAfterTicks, preloadAnim, headModelConfig);
    }

    @Override
    public EffectTrigger createTrigger(String rideIdentifier) {
        TrainModelItem headModel = new TrainModelItem(headModelConfig.createItemStack());

        VirtualEntity targetEntity = ServiceProvider.getSingleton(ViewportManager.class).findOrSpawnModelEntity(reuseEntity, location, headModel);
        AnimationHandle animationHandle = ServiceProvider.getSingleton(AnimationLoader.class).loadCoasterEffectAnimation(animationName, rideIdentifier);
        BlenderAnimationExecutor blenderAnimationExecutor = new BlenderAnimationExecutor(location, animationHandle, targetEntity, animationName);

        if(preloadAnim)
            blenderAnimationExecutor.playAnimationFrame();

        return new BlenderEffectTrigger(blenderAnimationExecutor, targetEntity, despawnAfterTicks);
    }
}
