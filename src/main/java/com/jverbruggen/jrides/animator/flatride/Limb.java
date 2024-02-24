package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.flatride.interfaces.Component6DOFPosition;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.config.flatride.structure.attachment.joint.RelativeAttachmentJointConfig;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.List;

public class Limb extends AbstractInterconnectedFlatRideComponent implements Component6DOFPosition {
    private Vector3 position;
    private Quaternion rotation;

    public Limb(String identifier, String groupIdentifier, boolean root, RelativeAttachmentJointConfig joint, List<FlatRideModel> flatRideModels) {
        super(identifier, groupIdentifier, root, joint, flatRideModels);
        position = new Vector3(0, 0, 0);
        rotation = new Quaternion();
    }

    @Override
    public Vector3 getPosition() {
        return Vector3.add(super.getPosition(), position);
    }

    @Override
    public Quaternion getRotation() {
        return Quaternion.multiply(super.getRotation(), rotation);
    }

    @Override
    public void setPositionRotation(float x, float y, float z, float rw, float rx, float ry, float rz) {
        position = new Vector3(x, y, z);
        rotation = new Quaternion(rx, ry, rz, rw);
    }
}
