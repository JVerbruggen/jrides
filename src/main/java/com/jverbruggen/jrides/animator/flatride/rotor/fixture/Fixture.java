package com.jverbruggen.jrides.animator.flatride.rotor.fixture;

import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public interface Fixture {
    void updateLocation(VirtualEntity entity, Vector3 parentLocation, Quaternion parentRotation, Vector3 offset, Quaternion rotationOffset);
}
