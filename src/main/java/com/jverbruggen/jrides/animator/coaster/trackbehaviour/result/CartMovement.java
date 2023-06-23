package com.jverbruggen.jrides.animator.coaster.trackbehaviour.result;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public final class CartMovement {
    private final Vector3 location;
    private final Quaternion orientation;

    public CartMovement(Vector3 location, Quaternion orientation) {
        this.location = location;
        this.orientation = orientation;
    }

    public Vector3 getLocation() {
        return location;
    }

    public Quaternion getOrientation() {
        return orientation;
    }
}
