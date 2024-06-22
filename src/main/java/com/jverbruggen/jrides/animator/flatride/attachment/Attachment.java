package com.jverbruggen.jrides.animator.flatride.attachment;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

import javax.annotation.Nullable;

public interface Attachment {
    Vector3 getPosition();
    Quaternion getRotation();
    Matrix4x4 getCachedMatrix();
    FlatRideComponent getChild();
    @Nullable FlatRideComponent getParent();
    void update();
}
