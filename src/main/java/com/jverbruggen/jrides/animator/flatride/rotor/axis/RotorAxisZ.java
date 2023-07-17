package com.jverbruggen.jrides.animator.flatride.rotor.axis;

import com.jverbruggen.jrides.models.math.Quaternion;

public class RotorAxisZ extends RotorAxisY {
    @Override
    public void addRotation(double addValue) {
        add(addValue);
    }

    @Override
    public Quaternion getQuaternion() {
        return Quaternion.fromYawPitchRoll(0, 0, getRotation());
    }
}