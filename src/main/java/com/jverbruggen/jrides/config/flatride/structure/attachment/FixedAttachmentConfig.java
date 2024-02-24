package com.jverbruggen.jrides.config.flatride.structure.attachment;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.Limb;
import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.attachment.FixedAttachment;
import com.jverbruggen.jrides.animator.flatride.linearactuator.LinearActuator;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
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

        List<FlatRideModel> flatRideModels = linearActuatorConfig.getFlatRideModels().stream()
                .map(config -> config.toFlatRideModel(position, viewportManager))
                .collect(Collectors.toList());

        LinearActuator linearActuator = new LinearActuator(
                linearActuatorConfig.getIdentifier(), linearActuatorConfig.getIdentifier(),
                linearActuatorConfig.isRoot(), null,
                flatRideModels,
                linearActuatorConfig.getFlatRideComponentSpeed(),
                linearActuatorConfig.getLinearActuatorTypeConfig().createActuatorMode());

        Attachment attachment = new FixedAttachment(linearActuator, position, rotation);
        linearActuator.setAttachedTo(attachment);

        components.add(linearActuator);
    }

    @Override
    public void createLimb(LimbConfig limbConfig, List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        List<FlatRideModel> flatRideModels = limbConfig.getFlatRideModels().stream()
                .map(config -> config.toFlatRideModel(position, viewportManager))
                .collect(Collectors.toList());

        Limb limb = new Limb(
                limbConfig.getIdentifier(),
                limbConfig.getIdentifier(),
                limbConfig.isRoot(),
                null,
                flatRideModels
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

        List<FlatRideModel> flatRideModels = rotorConfig.getFlatRideModels().stream()
                .map(config -> config.toFlatRideModel(position, viewportManager))
                .collect(Collectors.toList());

        Rotor rotor = new Rotor(rotorConfig.getIdentifier(), rotorConfig.getIdentifier(), rotorConfig.isRoot(),
                null, flatRideModels, rotorConfig.getFlatRideComponentSpeed(),
                RotorAxisFactory.createAxisFromString(rotorConfig.getRotorAxis()),
                rotorConfig.getRotorTypeConfig().createActuatorMode());
        rotor.createPlayerControl(rotorConfig.getPlayerControlConfig());

        Attachment attachment = new FixedAttachment(rotor, position, rotation);
        rotor.setAttachedTo(attachment);

        components.add(rotor);
    }
}
