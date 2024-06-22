package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.animator.flatride.AbstractInterconnectedFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasPosition;
import com.jverbruggen.jrides.animator.flatride.interfaces.PlayerControllable;
import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxis;
import com.jverbruggen.jrides.animator.flatride.rotor.mode.RotorActuatorMode;
import com.jverbruggen.jrides.config.flatride.structure.actuator.RotorPlayerControlConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.joint.RelativeAttachmentJointConfig;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.SpeedUtil;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;

import javax.annotation.Nullable;
import java.util.List;

public class Rotor extends AbstractInterconnectedFlatRideComponent implements PlayerControllable, HasPosition {
    private final FlatRideComponentSpeed flatRideComponentSpeed;
    private final RotorAxis rotorAxis;
    private RotorPlayerControl playerControl;
    private boolean allowsControlState;
    private double lowerOperatingRange;
    private double upperOperatingRange;
    private final RotorActuatorMode actuatorMode;

    public Rotor(String identifier, String groupIdentifier, boolean root, RelativeAttachmentJointConfig joint, List<FlatRideModel> flatRideModels, FlatRideComponentSpeed flatRideComponentSpeed, RotorAxis rotorAxis, RotorActuatorMode actuatorMode) {
        super(identifier, groupIdentifier, root, joint, flatRideModels);
        this.rotorAxis = rotorAxis;
        this.actuatorMode = actuatorMode;
        this.playerControl = null;
        this.flatRideComponentSpeed = flatRideComponentSpeed;
        this.allowsControlState = false;
        this.lowerOperatingRange = 0;
        this.upperOperatingRange = 360;
    }

    @Override
    public Quaternion getRotation() {
        Attachment attachedTo = getAttachedTo();
        if(attachedTo == null) throw new RuntimeException("Rotor " + getIdentifier() + " not attached to anything");

        return Quaternion.multiply(attachedTo.getRotation(), rotorAxis.getQuaternion());
    }

    public FlatRideComponentSpeed getFlatRideComponentSpeed() {
        return flatRideComponentSpeed;
    }

    @Override
    public void tick() {
        if(allowsControl()){
            playerControl.apply();
        }

        actuatorMode.tick(flatRideComponentSpeed, rotorAxis);

        for(Attachment attachment : getChildren()){
            attachment.update();
            attachment.getChild().tick();
        }

        updateFlatRideModels();
    }

    @Override
    public boolean allowsControl() {
        return this.allowsControlState;
    }

    @Override
    public void setAllowControl(boolean allow) {
        if(this.allowsControlState == allow) return;

        if(allow){
            playerControl.reset();
            playerControl.sendStartNotification();
        }
        this.allowsControlState = allow;
    }

    @Nullable
    @Override
    public PlayerControl getPlayerControl() {
        if(playerControl != null)
            return playerControl;

        return super.getPlayerControl();
    }

    public void createPlayerControl(RotorPlayerControlConfig controlConfig){
        if(controlConfig == null) return;

        playerControl = controlConfig.createPlayerControl();
        playerControl.setRotor(this);
    }

    public double getRotorRotation(){
        return rotorAxis.getRotation();
    }

    public void setRotorRotation(double toValue){
        rotorAxis.setRotation(toValue);
    }

    @Override
    public void setInstructionPosition(double position) {
        setRotorRotation(position);
    }

    @Override
    public double getInstructionPosition() {
        return getRotorRotation();
    }

    @Override
    public double getLowerOperatingRange() {
        return lowerOperatingRange;
    }

    @Override
    public double getUpperOperatingRange() {
        return upperOperatingRange;
    }

    @Override
    public void setLowerOperatingRange(double lower) {
        lowerOperatingRange = lower;
    }

    @Override
    public void setUpperOperatingRange(double upper) {
        upperOperatingRange = upper;
    }

    @Override
    public void goTowards(double targetPosition, double fromPosition, double acceleration, FlatRideComponentSpeed componentSpeed) {
        double currentSpeed = componentSpeed.getSpeed();
        double currentPosition = this.getInstructionPosition();
        double absAcceleration = Math.abs(acceleration);
        double maxSpeed = componentSpeed.getMaxSpeed();
        double minSpeed = componentSpeed.getMinSpeed();

        boolean positiveAcceleration = acceleration >= 0;
        boolean goingForwards = currentSpeed > 0 || (currentSpeed == 0 && positiveAcceleration);

        double breakPosition = SpeedUtil.positionStartBraking(
                currentSpeed,
                goingForwards ? -absAcceleration : absAcceleration,
                targetPosition,
                0);

//        JRidesPlugin.getLogger().debug("s: " + currentSpeed + "(" + goingForwards + ") a: " + acceleration);
//        JRidesPlugin.getLogger().debug("f: " + fromPositionAcc + " c: " + hasPosition.getInstructionPosition() + " t: " + targetPositionAcc + " b: " + breakPosition);

        double margin = 0.1;

        boolean shouldBreak = SpeedUtil.hasPassed(fromPosition, this.getInstructionPosition(), breakPosition,
                positiveAcceleration, margin);
        boolean shouldHardBreak = SpeedUtil.hasPassed(fromPosition, this.getInstructionPosition(), targetPosition,
                goingForwards, absAcceleration);

//        JRidesPlugin.getLogger().debug("break: " + shouldBreak + " hard: " + shouldHardBreak + "\n----");

        if(shouldHardBreak){
            componentSpeed.setHard(0);
            if(Math.abs(targetPosition - this.getInstructionPosition()) < margin)
                this.setInstructionPosition(targetPosition);
        }else if(shouldBreak){
            componentSpeed.accelerateTowards(absAcceleration, 0);
            this.checkBump(currentPosition, componentSpeed, fromPosition, targetPosition, positiveAcceleration);
        }else{
            double targetSpeed = positiveAcceleration ? maxSpeed : minSpeed;

            componentSpeed.accelerateTowards(absAcceleration, targetSpeed);
            this.checkBump(currentPosition, componentSpeed, fromPosition, targetPosition, positiveAcceleration);
        }
    }

    private void checkBump(double currentPosition, FlatRideComponentSpeed componentSpeed, double fromPosition, double targetPosition, boolean positiveSpeed){
        double newPosition = currentPosition + componentSpeed.getSpeed();
        if(SpeedUtil.hasPassed(fromPosition, newPosition, targetPosition, positiveSpeed, 0d)){
            componentSpeed.setHard(0);
            this.setInstructionPosition(targetPosition);
        }
    }
}
