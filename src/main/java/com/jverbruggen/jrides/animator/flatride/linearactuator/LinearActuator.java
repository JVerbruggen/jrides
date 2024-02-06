package com.jverbruggen.jrides.animator.flatride.linearactuator;

import com.jverbruggen.jrides.animator.flatride.AbstractInterconnectedFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasPosition;
import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.ActuatorMode;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.config.flatride.structure.attachment.joint.RelativeAttachmentJointConfig;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.List;

public class LinearActuator extends AbstractInterconnectedFlatRideComponent implements HasPosition {
    private final FlatRideComponentSpeed flatRideComponentSpeed;
    private final Vector3 actuatorState;
    private final ActuatorMode actuatorMode;

    public LinearActuator(String identifier, String groupIdentifier, boolean root, RelativeAttachmentJointConfig joint, List<FlatRideModel> flatRideModels, FlatRideComponentSpeed flatRideComponentSpeed, ActuatorMode actuatorMode) {
        super(identifier, groupIdentifier, root, joint, flatRideModels);
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
        componentSpeed.accelerate(acceleration);
    }
}
