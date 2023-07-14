package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.animator.flatride.rotor.fixture.Fixture;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public class FlatRideModel {
    private final VirtualEntity entity;
    private final Vector3 offset;
    private final Quaternion rotationOffset;
    private final Fixture fixture;

    public FlatRideModel(VirtualEntity entity, Vector3 offset, Quaternion rotationOffset, Fixture fixture) {
        this.entity = entity;
        this.offset = offset;
        this.rotationOffset = rotationOffset;
        this.fixture = fixture;
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

    public Fixture getFixture() {
        return fixture;
    }

    public void updateLocation(Vector3 parentLocation, Quaternion parentRotation){
        if(getFixture() == null) return;
        getFixture().updateLocation(getEntity(), parentLocation, parentRotation, getOffset(), getRotationOffset());
    }
}
