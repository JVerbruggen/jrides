package com.jverbruggen.jrides.models.math;

public class VectorQuaternionState {
    private final Vector3 vector;
    private final Quaternion quaternion;

    public VectorQuaternionState(Vector3 vector, Quaternion quaternion) {
        this.vector = vector;
        this.quaternion = quaternion;
    }

    public Vector3 getVector() {
        return vector;
    }

    public Quaternion getQuaternion() {
        return quaternion;
    }
}
