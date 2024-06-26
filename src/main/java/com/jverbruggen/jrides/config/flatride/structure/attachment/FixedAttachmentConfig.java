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
import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.attachment.FixedAttachment;
import com.jverbruggen.jrides.animator.flatride.linearactuator.LinearActuator;
import com.jverbruggen.jrides.animator.flatride.rotor.ModelWithOffset;
import com.jverbruggen.jrides.animator.flatride.rotor.Rotor;
import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxisFactory;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LimbConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LinearActuatorConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.RotorConfig;
import com.jverbruggen.jrides.config.flatride.structure.basic.StaticStructureConfig;
import com.jverbruggen.jrides.config.flatride.structure.seat.SeatConfig;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.VectorQuaternionState;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class FixedAttachmentConfig extends BaseConfig implements AttachmentConfig {
    private final Vector3 position;
    private final Quaternion rotation;

    public FixedAttachmentConfig(Vector3 position, Quaternion rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public static AttachmentConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        Vector3 position = Vector3.fromDoubleList(getDoubleList(configurationSection, "position"));
        Quaternion rotation = Quaternion.fromDoubleList(getDoubleList(configurationSection, "rotation", List.of(0d,0d,0d)));

        return new FixedAttachmentConfig(position, rotation);
    }

    @Override
    public void createSeatWithAttachment(SeatConfig config, List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        throw new RuntimeException("Attaching seat to fixed point not implemented. Also kinda weird.");
    }

    @Override
    public void createLinearActuator(LinearActuatorConfig linearActuatorConfig, List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        List<ModelWithOffset> modelWithOffsets = linearActuatorConfig.getFlatRideModels().stream()
                .map(config -> config.toModelWithOffset(position, viewportManager))
                .collect(Collectors.toList());

        LinearActuator linearActuator = new LinearActuator(
                linearActuatorConfig.getIdentifier(), linearActuatorConfig.getIdentifier(),
                linearActuatorConfig.isRoot(), null,
                modelWithOffsets,
                linearActuatorConfig.getFlatRideComponentSpeed(),
                linearActuatorConfig.getLinearActuatorTypeConfig().createActuatorMode());

        Attachment attachment = new FixedAttachment(linearActuator, position, rotation);
        linearActuator.setAttachedTo(attachment);

        components.add(linearActuator);
    }

    @Override
    public void createLimb(LimbConfig limbConfig, List<FlatRideComponent> components, FlatRideHandle rideHandle, String preloadAnim) {
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        List<ModelWithOffset> modelWithOffsets = limbConfig.getFlatRideModels().stream()
                .map(config -> config.toModelWithOffset(position, viewportManager))
                .collect(Collectors.toList());

        String limbIdentifier = limbConfig.getIdentifier();
        VectorQuaternionState vectorQuaternionState = Limb.getInitialPoseFromAnimation(preloadAnim, limbIdentifier, rideHandle);

        Limb limb = new Limb(
                limbIdentifier,
                limbIdentifier,
                limbConfig.isRoot(),
                null,
                modelWithOffsets,
                vectorQuaternionState
        );

        Attachment attachment = new FixedAttachment(limb, position, rotation);
        limb.setAttachedTo(attachment);

        components.add(limb);
    }

    @Override
    public void createStaticStructureWithAttachment(StaticStructureConfig config, List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        throw new RuntimeException("Not implementing this functionality. Weird.");
    }

    @Override
    public void createRotorWithAttachment(RotorConfig rotorConfig, List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        List<ModelWithOffset> modelWithOffsets = rotorConfig.getFlatRideModels().stream()
                .map(config -> config.toModelWithOffset(position, viewportManager))
                .collect(Collectors.toList());

        Rotor rotor = new Rotor(rotorConfig.getIdentifier(), rotorConfig.getIdentifier(), rotorConfig.isRoot(),
                null, modelWithOffsets, rotorConfig.getFlatRideComponentSpeed(),
                RotorAxisFactory.createAxisFromString(rotorConfig.getRotorAxis()),
                rotorConfig.getRotorTypeConfig().createActuatorMode());
        rotor.createPlayerControl(rotorConfig.getPlayerControlConfig());

        Attachment attachment = new FixedAttachment(rotor, position, rotation);
        rotor.setAttachedTo(attachment);

        components.add(rotor);
    }
}
