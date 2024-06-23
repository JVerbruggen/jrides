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
import com.jverbruggen.jrides.effect.entity.EntityMovementTrigger;
import com.jverbruggen.jrides.effect.entity.EntityProjectileEffect;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class EntityProjectileConfig extends BaseConfig implements EntityMovementConfig {
    private final Vector3 initialPosition;
    private final Vector3 initialRotation;

    private final Vector3 initialPositionalVelocity;
    private final Vector3 initialRotationalVelocity;
    private final Vector3 positionalAcceleration;
    private final Vector3 rotationalAcceleration;
    private final boolean resetOnStart;
    private final int animationTimeTicks;
    private final int delayTicks;

    public EntityProjectileConfig(Vector3 initialPosition, Vector3 initialRotation, Vector3 initialPositionalVelocity, Vector3 initialRotationalVelocity,
                                  Vector3 positionalAcceleration, Vector3 rotationalAcceleration, boolean resetOnStart, int animationTimeTicks, int delayTicks) {
        this.initialPosition = initialPosition;
        this.initialRotation = initialRotation;
        this.initialPositionalVelocity = initialPositionalVelocity;
        this.initialRotationalVelocity = initialRotationalVelocity;
        this.positionalAcceleration = positionalAcceleration;
        this.rotationalAcceleration = rotationalAcceleration;
        this.resetOnStart = resetOnStart;
        this.animationTimeTicks = animationTimeTicks;
        this.delayTicks = delayTicks;
    }

    public static EntityProjectileConfig fromConfigurationSection(ConfigurationSection configurationSection){
        Vector3 initialPosition = Vector3.fromDoubleList(getDoubleList(configurationSection, "initialPosition"));
        Vector3 initialRotation = Vector3.fromDoubleList(getDoubleList(configurationSection, "initialRotation", createDoubleList(0, 0, 0)));
        Vector3 initialPositionalVelocity = Vector3.fromDoubleList(getDoubleList(configurationSection, "initialPositionalVelocity"));
        Vector3 initialRotationalVelocity = Vector3.fromDoubleList(getDoubleList(configurationSection, "initialRotationalVelocity", createDoubleList(0, 0, 0)));
        Vector3 positionalAcceleration = Vector3.fromDoubleList(getDoubleList(configurationSection, "positionalAcceleration", createDoubleList(0,  -0.08,0)));
        Vector3 rotationalAcceleration = Vector3.fromDoubleList(getDoubleList(configurationSection, "rotationalAcceleration", createDoubleList(0, 0, 0)));
        int animationTimeTicks = getInt(configurationSection, "animationTimeTicks", 60);
        int delayTicks = getInt(configurationSection, "delayTicks", 0);
        boolean resetOnStart = getBoolean(configurationSection, "resetOnStart", true);

        return new EntityProjectileConfig(initialPosition, initialRotation, initialPositionalVelocity, initialRotationalVelocity,
                positionalAcceleration, rotationalAcceleration, resetOnStart, animationTimeTicks, delayTicks);
    }

    @Override
    public EntityMovementTrigger createTrigger(VirtualEntity virtualEntity) {
        Quaternion initialRotation = Quaternion.fromAnglesVector(this.initialRotation);
        Quaternion initialRotationalVelocity = Quaternion.fromAnglesVector(this.initialRotationalVelocity);
        Quaternion rotationalAcceleration = Quaternion.fromAnglesVector(this.rotationalAcceleration);

        virtualEntity.setRendered(false);
        virtualEntity.setLocation(initialPosition);
        virtualEntity.setRotation(initialRotation);

        return new EntityProjectileEffect(virtualEntity, resetOnStart, initialPosition, initialRotation, initialPositionalVelocity, initialRotationalVelocity,
                positionalAcceleration, rotationalAcceleration, animationTimeTicks, delayTicks);
    }

    @Override
    public Vector3 getInitialPosition() {
        return initialPosition;
    }

    @Override
    public Vector3 getInitialRotation() {
        return initialRotation;
    }

}
