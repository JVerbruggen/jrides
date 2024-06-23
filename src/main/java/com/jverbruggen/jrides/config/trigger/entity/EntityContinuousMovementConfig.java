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
import com.jverbruggen.jrides.effect.entity.EntityContinuousMovementEffectTrigger;
import com.jverbruggen.jrides.effect.entity.EntityMovementTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class EntityContinuousMovementConfig extends BaseConfig implements EntityMovementConfig {
    private final Vector3 initialLocation;
    private final Vector3 initialRotation;
    private final boolean resetOnStart;

    private final Vector3 locationDelta;
    private final Vector3 rotationDelta;
    private final int animationTimeTicks;
    private final int delayTicks;

    public EntityContinuousMovementConfig(Vector3 initialLocation, Vector3 initialRotation, boolean resetOnStart, Vector3 locationDelta, Vector3 rotationDelta, int animationTimeTicks, int delayTicks) {
        this.initialLocation = initialLocation;
        this.initialRotation = initialRotation;
        this.resetOnStart = resetOnStart;
        this.locationDelta = locationDelta;
        this.rotationDelta = rotationDelta;
        this.animationTimeTicks = animationTimeTicks;
        this.delayTicks = delayTicks;
    }

    public static EntityContinuousMovementConfig fromConfigurationSection(ConfigurationSection configurationSection){
        Vector3 initialLocation = Vector3.fromDoubleList(getDoubleList(configurationSection, "initialLocation", null));
        Vector3 initialRotation = Vector3.fromDoubleList(getDoubleList(configurationSection, "initialRotation", null));
        Vector3 locationDelta = Vector3.fromDoubleList(getDoubleList(configurationSection, "locationDelta", null));
        Vector3 rotationDelta = Vector3.fromDoubleList(getDoubleList(configurationSection, "rotationDelta", null));
        int animationTimeTicks = getInt(configurationSection, "animationTimeTicks", 20);
        int delayTicks = getInt(configurationSection, "delayTicks", 0);
        boolean resetOnStart = getBoolean(configurationSection, "resetOnStart", true);

        return new EntityContinuousMovementConfig(initialLocation, initialRotation, resetOnStart, locationDelta, rotationDelta, animationTimeTicks, delayTicks);
    }

    @Override
    public EntityMovementTrigger createTrigger(VirtualEntity virtualEntity) {
        Quaternion initialRotation = null;
        if(this.initialRotation != null){
            initialRotation = Quaternion.fromAnglesVector(this.initialRotation);
            virtualEntity.setRotation(initialRotation);
        }

        Quaternion rotationDelta = Quaternion.fromAnglesVector(this.rotationDelta);

        return new EntityContinuousMovementEffectTrigger(virtualEntity, initialLocation, initialRotation, resetOnStart, locationDelta, rotationDelta, animationTimeTicks, delayTicks);
    }

    @Override
    public Vector3 getInitialPosition() {
        return initialLocation;
    }

    @Override
    public Vector3 getInitialRotation() {
        return initialRotation;
    }
}
