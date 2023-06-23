package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public class RotorModel {
    private final VirtualEntity entity;
    private final Vector3 offset;

    public RotorModel(VirtualEntity entity, Vector3 offset) {
        this.entity = entity;
        this.offset = offset;
    }

    public VirtualEntity getEntity() {
        return entity;
    }

    public Vector3 getOffset() {
        return offset;
    }

    public void updateLocation(Vector3 parentLocation, Quaternion parentRotation){
        entity.setLocation(Vector3.add(parentLocation, this.offset), parentRotation);
    }
}
