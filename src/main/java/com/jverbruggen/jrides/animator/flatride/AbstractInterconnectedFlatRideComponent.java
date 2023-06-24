package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.attachment.RelativeAttachment;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInterconnectedFlatRideComponent extends AbstractFlatRideComponent {
    private final List<Attachment> children;

    public AbstractInterconnectedFlatRideComponent(String identifier, String groupIdentifier, boolean root, List<FlatRideModel> flatRideModels) {
        super(identifier, groupIdentifier, root, flatRideModels);
        this.children = new ArrayList<>();
    }

    @Override
    public void attach(FlatRideComponent child, Quaternion offsetRotation, Vector3 offsetPosition){
        Attachment attachment = new RelativeAttachment(this, child, offsetRotation, offsetPosition);

        child.setAttachedTo(attachment);
        children.add(attachment);
    }

    public List<Attachment> getChildren() {
        return children;
    }
}
