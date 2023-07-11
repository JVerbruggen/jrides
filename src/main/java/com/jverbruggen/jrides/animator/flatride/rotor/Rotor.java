package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.animator.flatride.AbstractInterconnectedFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasSpeed;
import com.jverbruggen.jrides.animator.flatride.interfaces.PlayerControllable;
import com.jverbruggen.jrides.config.flatride.structure.actuator.RotorPlayerControlConfig;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Rotor extends AbstractInterconnectedFlatRideComponent implements HasSpeed, PlayerControllable {
    private final Quaternion rotation;
    private final FlatRideComponentSpeed flatRideComponentSpeed;
    private RotorPlayerControl playerControl;
    private boolean allowsControlState;

    public Rotor(String identifier, String groupIdentifier, boolean root, List<FlatRideModel> flatRideModels, FlatRideComponentSpeed flatRideComponentSpeed) {
        super(identifier, groupIdentifier, root, flatRideModels);
        this.playerControl = null;
        this.rotation = new Quaternion();
        this.flatRideComponentSpeed = flatRideComponentSpeed;
        this.allowsControlState = false;
    }

    @Override
    public Quaternion getRotation() {
        if(getAttachedTo() == null) throw new RuntimeException("Rotor " + getIdentifier() + " not attached to anything");

        return Quaternion.multiply(getAttachedTo().getRotation(), rotation);
    }

    public FlatRideComponentSpeed getFlatRideComponentSpeed() {
        return flatRideComponentSpeed;
    }

    @Override
    public void tick() {
        if(allowsControl()){
            playerControl.apply();
        }

        rotation.rotateY(flatRideComponentSpeed.getSpeed());

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
}
