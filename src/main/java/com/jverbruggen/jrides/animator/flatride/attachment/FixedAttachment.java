package com.jverbruggen.jrides.animator.flatride.attachment;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public record FixedAttachment(FlatRideComponent child, Vector3 position, Quaternion rotation) implements Attachment {
    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public Quaternion getRotation() {
        return rotation;
    }

    @Override
    public Matrix4x4 getCachedMatrix() {
        Matrix4x4 matrix = new Matrix4x4();
        matrix.translate(getPosition());
        matrix.rotate(getRotation());
        return matrix;
    }

    @Override
    public FlatRideComponent getChild() {
        return child;
    }

    @Override
    public FlatRideComponent getParent() {
        return null;
    }

    @Override
    public void update() {

    }
}
