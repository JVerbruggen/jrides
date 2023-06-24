package com.jverbruggen.jrides.config.flatride.structure.attachment;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LinearActuatorConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.RotorConfig;
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
        else throw new RuntimeException("No arm offset or arm distance specified for rotor");

        int armDuplicate = getInt(configurationSection, "armDuplicate", 1);

        return new RelativeMultipleAttachmentConfig(armTo, offsetPosition, armDuplicate);
    }

    @Override
    public void createSeatWithAttachment(SeatConfig seatConfig, List<FlatRideComponent> components, RideHandle rideHandle) {
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
    public void createLinearActuator(LinearActuatorConfig linearActuatorConfig, List<FlatRideComponent> components, RideHandle rideHandle) {
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
    public void createRotorWithAttachment(RotorConfig rotorConfig, List<FlatRideComponent> components, RideHandle rideHandle) {
        List<FlatRideComponent> attachedToComponents = FlatRideComponent.findAllMatching(components, getToComponentIdentifier());

        for(FlatRideComponent attachedTo : attachedToComponents){
            List<FlatRideComponent> createdComponents = FlatRideComponent.createDistributedAttachedRotors(
                    rotorConfig.getIdentifier(),
                    attachedTo,
                    new Quaternion(),
                    getOffsetPosition(),
                    rotorConfig.getFlatRideComponentSpeed(),
                    rotorConfig.getFlatRideModels(),
                    getAmount());

            components.addAll(createdComponents);
        }
    }
}
