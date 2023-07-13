package com.jverbruggen.jrides.animator.flatride.rotor.axis;

import com.jverbruggen.jrides.models.math.Quaternion;

public class RotorAxisX extends RotorAxisY {
    @Override
    public void addRotation(double addValue) {
        add(addValue);
    }

    @Override
    public Quaternion getQuaternion() {
        return Quaternion.fromYawPitchRoll(getRotation(), 0, 0);
    }
}
