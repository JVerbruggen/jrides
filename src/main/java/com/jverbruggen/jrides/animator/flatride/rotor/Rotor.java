package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.animator.flatride.AbstractFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.attachment.RelativeAttachment;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class Rotor extends AbstractFlatRideComponent {
    private final List<Attachment> children;
    private Quaternion rotation;
    private final RotorSpeed rotorSpeed;

    public Rotor(String identifier, List<FlatRideModel> flatRideModels, RotorSpeed rotorSpeed) {
        super(identifier, flatRideModels);
        this.children = new ArrayList<>();
        this.rotation = new Quaternion();
        this.rotorSpeed = rotorSpeed;
    }

    @Override
    public void attach(FlatRideComponent child, Quaternion offsetRotation, Vector3 offsetPosition){
        Attachment attachment = new RelativeAttachment(this, child, offsetRotation, offsetPosition);

        child.setAttachedTo(attachment);
        children.add(attachment);
    }

    @Override
    public Quaternion getRotation() {
        if(getAttachedTo() == null) throw new RuntimeException("Rotor " + getIdentifier() + " not attached to anything");

        return Quaternion.multiply(getAttachedTo().getRotation(), rotation);
    }

    @Override
    public void tick() {
        rotation.rotateY(rotorSpeed.getSpeed());

        for(Attachment attachment : children){
            attachment.update();
            attachment.getChild().tick();
        }

        updateFlatRideModels();
    }
}
