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

package com.jverbruggen.jrides.animator.flatride.linearactuator;

import com.jverbruggen.jrides.animator.flatride.AbstractInterconnectedFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasPosition;
import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.LinearActuatorMode;
import com.jverbruggen.jrides.animator.flatride.rotor.ModelWithOffset;
import com.jverbruggen.jrides.config.flatride.structure.attachment.joint.RelativeAttachmentJointConfig;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.List;

public class LinearActuator extends AbstractInterconnectedFlatRideComponent implements HasPosition {
    private final FlatRideComponentSpeed flatRideComponentSpeed;
    private final Vector3 actuatorState;
    private final LinearActuatorMode actuatorMode;

    public LinearActuator(String identifier, String groupIdentifier, boolean root, RelativeAttachmentJointConfig joint, List<ModelWithOffset> modelWithOffsets, FlatRideComponentSpeed flatRideComponentSpeed, LinearActuatorMode actuatorMode) {
        super(identifier, groupIdentifier, root, joint, modelWithOffsets);
        this.flatRideComponentSpeed = flatRideComponentSpeed;

        this.actuatorState = new Vector3(0,0,0);
        this.actuatorMode = actuatorMode;
    }

    @Override
    public Vector3 getPosition() {
        return Vector3.add(super.getPosition(), actuatorState);
    }

    @Override
    public void tick() {
        actuatorMode.tick(flatRideComponentSpeed, actuatorState);

        for(Attachment attachment : getChildren()){
            attachment.update();
            attachment.getChild().tick();
        }

        updateFlatRideModels();
    }

    @Override
    public FlatRideComponentSpeed getFlatRideComponentSpeed() {
        return flatRideComponentSpeed;
    }

    @Override
    public void setInstructionPosition(double position) {
        actuatorMode.setPosition(actuatorState, position);
    }

    @Override
    public double getInstructionPosition() {
        return actuatorMode.getPosition(actuatorState);
    }

    @Override
    public double getLowerOperatingRange() {
        return actuatorMode.getLowerBound();
    }

    @Override
    public double getUpperOperatingRange() {
        return actuatorMode.getUpperBound();
    }

    @Override
    public void setLowerOperatingRange(double lower) {
        throw new RuntimeException("Cannot set operating range for linear actuator");
    }

    @Override
    public void setUpperOperatingRange(double upper) {
        throw new RuntimeException("Cannot set operating range for linear actuator");
    }

    @Override
    public void goTowards(double targetPosition, double fromPosition, double acceleration, FlatRideComponentSpeed componentSpeed) {
        boolean shouldAccelerate;
        double currentPosition = getInstructionPosition();

        // If from position is lower than target
        if(fromPosition <= targetPosition){
            // Then it should accelerate while the current position is lower than the target
            shouldAccelerate = currentPosition < targetPosition;
        }else{
            // Otherwise do the reverse
            shouldAccelerate = targetPosition < currentPosition;
        }

        if(shouldAccelerate)
            componentSpeed.accelerate(acceleration);
    }
}
