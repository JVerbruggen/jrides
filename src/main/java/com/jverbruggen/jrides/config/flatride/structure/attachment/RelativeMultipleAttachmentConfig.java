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

package com.jverbruggen.jrides.config.flatride.structure.attachment;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.Limb;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LimbConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LinearActuatorConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.RotorConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.joint.RelativeAttachmentJointConfig;
import com.jverbruggen.jrides.config.flatride.structure.basic.StaticStructureConfig;
import com.jverbruggen.jrides.config.flatride.structure.seat.SeatConfig;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.VectorQuaternionState;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class RelativeMultipleAttachmentConfig extends AbstractRelativeMultipleAttachmentConfig {
    private final RelativeAttachmentJointConfig joint;

    protected RelativeMultipleAttachmentConfig(String toComponentIdentifier, Vector3 offsetPosition, int amount, RelativeAttachmentJointConfig joint) {
        super(toComponentIdentifier, offsetPosition, amount);
        this.joint = joint;
    }

    public RelativeAttachmentJointConfig getJoint() {
        return joint;
    }

    public static AttachmentConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String armTo = getString(configurationSection, "arm");
        Vector3 offsetPosition;
        if(configurationSection.contains("armDistance"))
            offsetPosition = new Vector3(getDouble(configurationSection, "armDistance"), 0, 0);
        else if(configurationSection.contains("armOffset"))
            offsetPosition = Vector3.fromDoubleList(getDoubleList(configurationSection, "armOffset"));
        else offsetPosition = Vector3.zero();

        int armDuplicate = getInt(configurationSection, "armDuplicate", 1);

        RelativeAttachmentJointConfig joint = RelativeAttachmentJointConfig.fromConfigurationSection(configurationSection.getConfigurationSection("armJoint"));

        return new RelativeMultipleAttachmentConfig(armTo, offsetPosition, armDuplicate, joint);
    }

    @Override
    public void createSeatWithAttachment(SeatConfig seatConfig, List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        List<FlatRideComponent> attachedToComponents = FlatRideComponent.findAllMatching(components, getToComponentIdentifier());

        for(FlatRideComponent attachedTo : attachedToComponents){
            List<FlatRideComponent> createdComponents = FlatRideComponent.createDistributedSeats(
                rideHandle,
                seatConfig.getIdentifier(),
                attachedTo,
                new Quaternion(),
                getOffsetPosition(),
                seatConfig.getSeatYawOffset(),
                seatConfig.getFlatRideModels(),
                getAmount());

            components.addAll(createdComponents);
        }
    }

    @Override
    public void createLinearActuator(LinearActuatorConfig linearActuatorConfig, List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        List<FlatRideComponent> attachedToComponents = FlatRideComponent.findAllMatching(components, getToComponentIdentifier());

        for(FlatRideComponent attachedTo : attachedToComponents){
            List<FlatRideComponent> createdComponents = FlatRideComponent.createDistributedLinearActuators(
                    attachedTo,
                    new Quaternion(),
                    getOffsetPosition(),
                    linearActuatorConfig,
                    getAmount());

            components.addAll(createdComponents);
        }
    }

    @Override
    public void createLimb(LimbConfig limbConfig, List<FlatRideComponent> components, FlatRideHandle rideHandle, String preloadAnim) {
        List<FlatRideComponent> attachedToComponents = FlatRideComponent.findAllMatching(components, getToComponentIdentifier());

        String limbIdentifier = limbConfig.getIdentifier();
        VectorQuaternionState initialPose = Limb.getInitialPoseFromAnimation(preloadAnim, limbIdentifier, rideHandle);

        for(FlatRideComponent attachedTo : attachedToComponents){
            List<FlatRideComponent> createdComponents = FlatRideComponent.createLimb(
                    attachedTo,
                    new Quaternion(),
                    getOffsetPosition(),
                    limbConfig,
                    initialPose);

            components.addAll(createdComponents);
        }
    }

    @Override
    public void createStaticStructureWithAttachment(StaticStructureConfig config, List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        List<FlatRideComponent> attachedToComponents = FlatRideComponent.findAllMatching(components, getToComponentIdentifier());
        for(FlatRideComponent attachedTo : attachedToComponents) {
            FlatRideComponent component = FlatRideComponent.createStaticStructure(
                    config.getIdentifier(),
                    config.getIdentifier(),
                    attachedTo,
                    new Quaternion(),
                    getOffsetPosition(),
                    getJoint(),
                    config.getFlatRideModels()
            );

            components.add(component);
        }
    }

    @Override
    public void createRotorWithAttachment(RotorConfig rotorConfig, List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        List<FlatRideComponent> attachedToComponents = FlatRideComponent.findAllMatching(components, getToComponentIdentifier());

        for(FlatRideComponent attachedTo : attachedToComponents){
            List<FlatRideComponent> createdComponents = FlatRideComponent.createDistributedAttachedRotors(
                    rotorConfig.getIdentifier(),
                    attachedTo,
                    new Quaternion(),
                    getOffsetPosition(),
                    rotorConfig.getFlatRideComponentSpeed(),
                    rotorConfig.getRotorTypeConfig().createActuatorMode(),
                    rotorConfig.getPlayerControlConfig(),
                    getJoint(),
                    rotorConfig.getRotorAxis(),
                    rotorConfig.getFlatRideModels(),
                    getAmount());

            components.addAll(createdComponents);
        }
    }
}
