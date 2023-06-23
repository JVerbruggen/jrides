package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Rotor implements FlatRideComponent {
    private final String identifier;
    private final List<Attachment> children;
    private @Nullable Attachment attachedTo;
    private Quaternion rotation;
    private final List<RotorModel> rotorModels;

    public Rotor(String identifier, List<RotorModel> rotorModels) {
        this.identifier = identifier;
        this.children = new ArrayList<>();
        this.attachedTo = null;
        this.rotation = new Quaternion();
        this.rotorModels = rotorModels;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void attach(FlatRideComponent child, Quaternion offsetRotation, Vector3 offsetPosition){
        Attachment attachment = new RotorAttachment(this, child, offsetRotation, offsetPosition);

        child.setAttachedTo(attachment);
        children.add(attachment);
    }

    @Override
    public @Nullable Attachment getAttachedTo() {
        return attachedTo;
    }

    @Override
    public void setAttachedTo(@Nullable Attachment attachment) {
        attachedTo = attachment;
    }

    @Override
    public Quaternion getRotation() {
        return Quaternion.multiply(attachedTo.getRotation(), rotation);
    }

    @Override
    public Vector3 getPosition() {
        if(attachedTo == null) throw new RuntimeException("Rotor " + identifier + " not attached to anything");

        return attachedTo.getPosition();
    }

    @Override
    public void tick() {
        rotation.rotateY(2);

        for(Attachment attachment : children){
            attachment.update();
            attachment.getChild().tick();
        }

        for(RotorModel rotorModel : rotorModels){
            rotorModel.updateLocation(getPosition(), getRotation());
        }
    }
}
