package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.models.math.Matrix4x4;
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
        Attachment attachedTo = getAttachedTo();
        if(attachedTo == null) throw new RuntimeException("Component " + identifier + " not attached to anything");

        return attachedTo.getRotation();
    }

    @Override
    public Vector3 getPosition() {
        Attachment attachedTo = getAttachedTo();
        if(attachedTo == null) throw new RuntimeException("Component " + identifier + " not attached to anything");

        return attachedTo.getPosition();
    }

    @Override
    public Matrix4x4 getPositionMatrix(){
        Attachment attachedTo = getAttachedTo();
        if(attachedTo == null) throw new RuntimeException("Component " + identifier + " not attached to anything");

        return attachedTo.getCachedMatrix();
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
        Attachment attachedTo = getAttachedTo();
        if(attachedTo == null) return null;
        if(attachedTo.getParent() == null) return null;

        FlatRideComponent parent = attachedTo.getParent();
        if(parent == null) throw new RuntimeException("Expected flat ride component " + identifier + " to have parent");

        return parent.getPlayerControl();
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
