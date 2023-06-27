package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.animator.flatride.AbstractInterconnectedFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.interfaces.HasSpeed;
import com.jverbruggen.jrides.animator.flatride.interfaces.PlayerControllable;
import com.jverbruggen.jrides.models.math.Quaternion;

import java.util.List;

public class Rotor extends AbstractInterconnectedFlatRideComponent implements HasSpeed, PlayerControllable {
    private final Quaternion rotation;
    private final FlatRideComponentSpeed flatRideComponentSpeed;

    public Rotor(String identifier, String groupIdentifier, boolean root, List<FlatRideModel> flatRideModels, FlatRideComponentSpeed flatRideComponentSpeed) {
        super(identifier, groupIdentifier, root, flatRideModels);
        this.rotation = new Quaternion();
        this.flatRideComponentSpeed = flatRideComponentSpeed;
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
        rotation.rotateY(flatRideComponentSpeed.getSpeed());

        for(Attachment attachment : getChildren()){
            attachment.update();
            attachment.getChild().tick();
        }

        updateFlatRideModels();
    }

    @Override
    public void setAllowControl(boolean allow) {

    }

    @Override
    public void setControlSpeed(float speed) {

    }
}
