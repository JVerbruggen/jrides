package com.jverbruggen.jrides.models.math;

public class VectorQuaternionState {
    private final Vector3 vector;
    private final Quaternion quaternion;

    public VectorQuaternionState(Vector3 vector, Quaternion quaternion) {
        this.vector = vector.clone();
        this.quaternion = quaternion.clone();
    }

    public Vector3 getVector() {
        return vector;
    }

    public Quaternion getQuaternion() {
        return quaternion;
    }

    public static VectorQuaternionState zero(){
        return new VectorQuaternionState(new Vector3(), new Quaternion());
    }
}
