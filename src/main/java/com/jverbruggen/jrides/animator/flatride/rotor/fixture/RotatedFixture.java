package com.jverbruggen.jrides.animator.flatride.rotor.fixture;

import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.MatrixMath;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;

public class RotatedFixture implements Fixture {
    private final boolean axisX;
    private final boolean axisY;
    private final boolean axisZ;

    public RotatedFixture(boolean axisX, boolean axisY, boolean axisZ) {
        this.axisX = axisX;
        this.axisY = axisY;
        this.axisZ = axisZ;
    }

    public boolean isAxisX() {
        return axisX;
    }

    public boolean isAxisY() {
        return axisY;
    }

    public boolean isAxisZ() {
        return axisZ;
    }

    private boolean anyAxis(){
        return axisX || axisY || axisZ;
    }

    public void updateLocation(VirtualEntity entity, Vector3 parentLocation, Quaternion parentRotation, Vector3 offset, Quaternion rotationOffset) {
        Matrix4x4 matrix4x4 = MatrixMath.rotateTranslate(parentLocation, parentRotation, offset, rotationOffset);

        entity.setLocation(matrix4x4.toVector3());
        entity.setRotation(matrix4x4.getRotation());
    }
}
