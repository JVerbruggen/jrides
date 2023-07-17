package com.jverbruggen.jrides.animator.flatride.rotor.axis;

import com.jverbruggen.jrides.models.math.Quaternion;

public interface RotorAxis {
    void setRotation(double toValue);
    void addRotation(double addValue);
    double getRotation();
    Quaternion getQuaternion();
}
