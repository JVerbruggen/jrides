package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.config.flatride.structure.ControlConfig;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractFlatRideComponent implements FlatRideComponent {
    private final String identifier;
    private final String groupIdentifier;
    private final boolean root;
    private @Nullable Attachment attachedTo;
    private final List<FlatRideModel> flatRideModels;

    public AbstractFlatRideComponent(String identifier, String groupIdentifier, boolean root, List<FlatRideModel> flatRideModels) {
        this.identifier = identifier;
        this.groupIdentifier = groupIdentifier;
        this.root = root;
        this.attachedTo = null;
        this.flatRideModels = flatRideModels;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getGroupIdentifier() {
        return groupIdentifier;
    }

    @Override
    public boolean isRoot() {
        return root;
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
        if(attachedTo == null) throw new RuntimeException("Rotor " + identifier + " not attached to anything");

        return attachedTo.getRotation();
    }

    @Override
    public Vector3 getPosition() {
        if(attachedTo == null) throw new RuntimeException("Rotor " + identifier + " not attached to anything");

        return attachedTo.getPosition();
    }

    @Override
    public void tick() {
        updateFlatRideModels();
    }

    @Override
    public void attach(FlatRideComponent child, Quaternion offsetRotation, Vector3 offsetPosition) {
        throw new RuntimeException("Cannot attach to this component");
    }

    @Nullable
    @Override
    public PlayerControl getPlayerControl() {
        if(attachedTo == null) return null;
        if(attachedTo.getParent() == null) return null;

        return attachedTo.getParent().getPlayerControl();
    }

    protected void updateFlatRideModels(){
        for(FlatRideModel flatRideModel : flatRideModels){
            flatRideModel.updateLocation(getPosition(), getRotation());
        }
    }

    @Override
    public boolean equalsToIdentifier(String identifier) {
        return identifier.equals(this.identifier) || identifier.equals(this.groupIdentifier);
    }
}
