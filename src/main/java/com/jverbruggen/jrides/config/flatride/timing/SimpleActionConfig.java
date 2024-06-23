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

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasPosition;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.*;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.towards.TowardsPositionInstruction;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleActionConfig extends BaseConfig implements ActionConfig {
    private final String targetIdentifier;
    private Float speed;
    private Float accelerate;
    private Boolean allowControl;
    private Float targetPosition;

    public SimpleActionConfig(String targetIdentifier) {
        this.targetIdentifier = targetIdentifier;
        speed = null;
        accelerate = null;
        allowControl = null;
        targetPosition = null;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getAccelerate() {
        return accelerate == null ? 0 : Math.abs(accelerate);
    }

    public void setAccelerate(Float accelerate) {
        this.accelerate = accelerate;
    }

    public Boolean allowsControl() {
        return allowControl;
    }

    public void setAllowControl(Boolean allowControl) {
        this.allowControl = allowControl;
    }

    public Float getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Float targetPosition) {
        this.targetPosition = targetPosition;
    }

    @Override
    public List<TimingAction> getTimingAction(FlatRideHandle flatRideHandle, List<FlatRideComponent> flatRideComponents){
        List<FlatRideComponent> targetedFlatRideComponents = flatRideComponents.stream()
                .filter(c -> c.equalsToIdentifier(targetIdentifier))
                .toList();

        if(targetedFlatRideComponents.size() == 0) return Collections.emptyList();

        List<TimingAction> timingActions = new ArrayList<>();
        if(targetPosition != null){
            HasPosition sampleComponent = (HasPosition) targetedFlatRideComponents.get(0);
            double minSpeed = getSpeed() != null ? getSpeed() : sampleComponent.getFlatRideComponentSpeed().getMinSpeed();
            double maxSpeed = getSpeed() != null ? -getSpeed() : sampleComponent.getFlatRideComponentSpeed().getMaxSpeed();

            timingActions.add(new InstructionBinding(
                    new TowardsPositionInstruction(getAccelerate(), minSpeed, maxSpeed, getTargetPosition()), targetedFlatRideComponents));
        }else if(speed != null){ // SpeedInstruction and TowardsPositionInstruction are mutually exclusive.
            timingActions.add(new InstructionBinding(
                    new SpeedInstruction(getAccelerate(), getSpeed()), targetedFlatRideComponents));
        }
        if(allowControl != null){
            if(targetPosition != null && allowControl) throw new RuntimeException("No support for target position and control");
            timingActions.add(new InstructionBinding(
                    new ControlInstruction(allowsControl()), targetedFlatRideComponents));
        }

        return timingActions;
    }
}
