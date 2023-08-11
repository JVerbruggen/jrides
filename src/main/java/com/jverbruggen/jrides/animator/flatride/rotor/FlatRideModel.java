package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.MatrixMath;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public class FlatRideModel {
    private final VirtualEntity entity;
    private final Vector3 offset;
    private final Quaternion rotationOffset;

    public FlatRideModel(VirtualEntity entity, Vector3 offset, Quaternion rotationOffset) {
        this.entity = entity;
        this.offset = offset;
        this.rotationOffset = rotationOffset;
    }

    public void updateLocation(Vector3 parentLocation, Quaternion parentRotation){
        Matrix4x4 matrix = MatrixMath.rotateTranslate(parentLocation, parentRotation, offset, rotationOffset);
        entity.setLocation(matrix.toVector3());
        entity.setRotation(matrix.getRotation());
    }

    public VirtualEntity getEntity() {
        return entity;
    }
}
