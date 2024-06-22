package com.jverbruggen.jrides.animator.flatride.rotor.axis;

import com.jverbruggen.jrides.models.math.MathUtil;
import com.jverbruggen.jrides.models.math.Quaternion;

public class RotorAxisY implements RotorAxis {
    private double value;

    public RotorAxisY() {
        this.value = 0;
    }

    protected void add(double value){
        this.value = MathUtil.floorMod(this.value + value, 360d);
    }

    @Override
    public void setRotation(double toValue){
        double fromRotation = getRotation();
        if(fromRotation == toValue) return;

        addRotation(toValue - fromRotation);
    }

    @Override
    public void addRotation(double addValue) {
        add(addValue);
    }

    @Override
    public double getRotation() {
        return this.value;
    }

    @Override
    public Quaternion getQuaternion() {
        return Quaternion.fromYawPitchRoll(0, this.value, 0);
    }
}
