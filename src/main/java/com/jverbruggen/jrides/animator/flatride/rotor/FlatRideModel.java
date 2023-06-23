package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.models.entity.VirtualEntity;
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

    public VirtualEntity getEntity() {
        return entity;
    }

    public Vector3 getOffset() {
        return offset;
    }

    public Quaternion getRotationOffset() {
        return rotationOffset;
    }

    public void updateLocation(Vector3 parentLocation, Quaternion parentRotation){
        entity.setLocation(Vector3.add(parentLocation, this.offset), Quaternion.multiply(parentRotation, this.rotationOffset));
    }
}
