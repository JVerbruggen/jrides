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
import com.jverbruggen.jrides.effect.entity.EntityFromToMovementEffectTrigger;
import com.jverbruggen.jrides.effect.entity.EntityMovementTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class EntityFromToMovementConfig extends BaseConfig implements EntityMovementConfig {
    private final Vector3 locationFrom;
    private final Vector3 locationTo;
    private final Vector3 rotationFrom;
    private final Vector3 rotationTo;
    private final int animationTimeTicks;
    private final int delayTicks;

    private final boolean locationHasDelta;
    private final boolean rotationHasDelta;

    public EntityFromToMovementConfig(Vector3 locationFrom, Vector3 locationTo, Vector3 rotationFrom, Vector3 rotationTo, int animationTimeTicks, int delayTicks) {
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.rotationFrom = rotationFrom;
        this.rotationTo = rotationTo;
        this.animationTimeTicks = animationTimeTicks;
        this.delayTicks = delayTicks;
        this.locationHasDelta = locationFrom != null && locationTo != null && locationTo.distanceSquared(locationFrom) > 0.01;
        this.rotationHasDelta = rotationFrom != null && rotationTo != null && rotationTo.distanceSquared(rotationFrom) > 0.01;
    }

    public Vector3 getLocationFrom() {
        return locationFrom;
    }

    public Vector3 getLocationTo() {
        return locationTo;
    }

    public Vector3 getRotationFrom() {
        return rotationFrom;
    }

    public Vector3 getRotationTo() {
        return rotationTo;
    }

    public int getAnimationTimeTicks() {
        return animationTimeTicks;
    }

    public boolean isLocationHasDelta() {
        return locationHasDelta;
    }

    public boolean isRotationHasDelta() {
        return rotationHasDelta;
    }

    public static EntityFromToMovementConfig fromConfigurationSection(ConfigurationSection configurationSection){
        Vector3 locationFrom = Vector3.fromDoubleList(getDoubleList(configurationSection, "locationFrom", null));
        Vector3 locationTo = Vector3.fromDoubleList(getDoubleList(configurationSection, "locationTo", null));
        Vector3 rotationFrom = Vector3.fromDoubleList(getDoubleList(configurationSection, "rotationFrom", null));
        Vector3 rotationTo = Vector3.fromDoubleList(getDoubleList(configurationSection, "rotationTo", null));
        int animationTimeTicks = getInt(configurationSection, "animationTimeTicks", 20);
        int delayTicks = getInt(configurationSection, "delayTicks", 0);

        return new EntityFromToMovementConfig(locationFrom, locationTo, rotationFrom, rotationTo, animationTimeTicks, delayTicks);
    }

    @Override
    public EntityMovementTrigger createTrigger(VirtualEntity virtualEntity) {
        boolean hasLocationDelta = isLocationHasDelta();
        boolean hasRotationDelta = isRotationHasDelta();

        Vector3 locationFrom = hasLocationDelta ? getLocationFrom() : null;
        Vector3 locationTo = hasLocationDelta ? getLocationTo() : null;

        Quaternion rotationFrom = hasRotationDelta ? Quaternion.fromAnglesVector(getRotationFrom()) : null;
        Quaternion rotationTo = hasRotationDelta ? Quaternion.fromAnglesVector(getRotationTo()) : null;

        int animationTimeTicks = getAnimationTimeTicks();

        return new EntityFromToMovementEffectTrigger(virtualEntity, locationFrom, locationTo, rotationFrom, rotationTo, animationTimeTicks, delayTicks);
    }

    @Override
    public Vector3 getInitialPosition() {
        return locationFrom;
    }

    @Override
    public Vector3 getInitialRotation() {
        return rotationFrom;
    }
}
