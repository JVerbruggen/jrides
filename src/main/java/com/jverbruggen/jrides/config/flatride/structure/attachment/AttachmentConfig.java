package com.jverbruggen.jrides.config.flatride.structure.attachment;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LinearActuatorConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.RotorConfig;
import com.jverbruggen.jrides.config.flatride.structure.seat.SeatConfig;

import java.util.List;

public interface AttachmentConfig {
    void createRotorWithAttachment(RotorConfig config, List<FlatRideComponent> components, RideHandle rideHandle);
    void createSeatWithAttachment(SeatConfig config, List<FlatRideComponent> components, RideHandle rideHandle);
    void createLinearActuator(LinearActuatorConfig linearActuatorConfig, List<FlatRideComponent> components, RideHandle rideHandle);
}
