package com.jverbruggen.jrides.config.flatride.structure.attachment;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxisFactory;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LinearActuatorConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.RotorConfig;
import com.jverbruggen.jrides.config.flatride.structure.basic.StaticStructureConfig;
import com.jverbruggen.jrides.config.flatride.structure.seat.SeatConfig;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class RelativeMultipleAttachmentConfig extends AbstractRelativeMultipleAttachmentConfig {

    protected RelativeMultipleAttachmentConfig(String toComponentIdentifier, Vector3 offsetPosition, int amount) {
        super(toComponentIdentifier, offsetPosition, amount);
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

        return new RelativeMultipleAttachmentConfig(armTo, offsetPosition, armDuplicate);
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
    public void createStaticStructureWithAttachment(StaticStructureConfig config, List<FlatRideComponent> components, FlatRideHandle rideHandle) {
        List<FlatRideComponent> attachedToComponents = FlatRideComponent.findAllMatching(components, getToComponentIdentifier());
        for(FlatRideComponent attachedTo : attachedToComponents) {
            FlatRideComponent component = FlatRideComponent.createStaticStructure(
                    config.getIdentifier(),
                    config.getIdentifier(),
                    attachedTo,
                    new Quaternion(),
                    getOffsetPosition(),
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
                    rotorConfig.getPlayerControlConfig(),
                    RotorAxisFactory.createAxisFromString(rotorConfig.getRotorAxis()),
                    rotorConfig.getFlatRideModels(),
                    getAmount());

            components.addAll(createdComponents);
        }
    }
}
