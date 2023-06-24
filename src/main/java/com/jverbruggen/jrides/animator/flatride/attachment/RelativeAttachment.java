package com.jverbruggen.jrides.animator.flatride.attachment;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.MatrixMath;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public class RelativeAttachment implements Attachment {
    private FlatRideComponent parent;
    private FlatRideComponent child;
    private Quaternion offsetRotation;
    private Vector3 offsetPosition;
    private Matrix4x4 cacheMatrix;

    public RelativeAttachment(FlatRideComponent parent, FlatRideComponent child, Quaternion offsetRotation, Vector3 offsetPosition) {
        this.parent = parent;
        this.child = child;
        this.offsetRotation = offsetRotation;
        this.offsetPosition = offsetPosition;
        this.cacheMatrix = null;

        update();
    }

    @Override
    public Vector3 getPosition() {
        return cacheMatrix.toVector3();
    }

    @Override
    public Quaternion getRotation() {
        return cacheMatrix.getRotation();
    }

    @Override
    public FlatRideComponent getChild() {
        return child;
    }

    @Override
    public void update() {
        cacheMatrix = MatrixMath.rotateTranslate(
                parent.getPosition(),
                parent.getRotation(),
                this.offsetPosition,
                this.offsetRotation);
    }
}
