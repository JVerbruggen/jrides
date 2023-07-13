package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.animator.flatride.AbstractInterconnectedFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasPosition;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasSpeed;
import com.jverbruggen.jrides.animator.flatride.interfaces.PlayerControllable;
import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxis;
import com.jverbruggen.jrides.config.flatride.structure.actuator.RotorPlayerControlConfig;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Rotor extends AbstractInterconnectedFlatRideComponent implements HasSpeed, PlayerControllable, HasPosition {
    private final FlatRideComponentSpeed flatRideComponentSpeed;
    private final RotorAxis rotorAxis;
    private RotorPlayerControl playerControl;
    private boolean allowsControlState;
    private double lowerOperatingRange;
    private double upperOperatingRange;

    public Rotor(String identifier, String groupIdentifier, boolean root, List<FlatRideModel> flatRideModels, FlatRideComponentSpeed flatRideComponentSpeed, RotorAxis rotorAxis) {
        super(identifier, groupIdentifier, root, flatRideModels);
        this.rotorAxis = rotorAxis;
        this.playerControl = null;
        this.flatRideComponentSpeed = flatRideComponentSpeed;
        this.allowsControlState = false;
        this.lowerOperatingRange = 0;
        this.upperOperatingRange = 360;
    }

    @Override
    public Quaternion getRotation() {
        if(getAttachedTo() == null) throw new RuntimeException("Rotor " + getIdentifier() + " not attached to anything");

        return Quaternion.multiply(getAttachedTo().getRotation(), rotorAxis.getQuaternion());
    }

    public FlatRideComponentSpeed getFlatRideComponentSpeed() {
        return flatRideComponentSpeed;
    }

    @Override
    public void tick() {
        if(allowsControl()){
            playerControl.apply();
        }

        rotorAxis.addRotation(flatRideComponentSpeed.getSpeed());

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
}
