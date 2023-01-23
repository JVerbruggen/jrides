package com.jverbruggen.jrides.models.math;

public class ArmorStandPose {
    public static Vector3 getArmorStandPose(Quaternion rotation) {
        double qx = rotation.getX();
        double qy = rotation.getY();
        double qz = rotation.getZ();
        double qw = rotation.getW();

        double rx = 1.0 + 2.0 * (-qy * qy - qz * qz);
        double ry = 2.0 * (qx * qy + qz * qw);
        double rz = 2.0 * (qx * qz - qy * qw);
        double uz = 2.0 * (qy * qz + qx * qw);
        double fz = 1.0 + 2.0 * (-qx * qx - qy * qy);

        if(Math.abs(rz) < (1.0 - 1E-15)) {
            return new Vector3(MathUtil.atan2(uz, fz), fastAsin(rz), MathUtil.atan2(-ry, rx));
        }else {
            final double sign = (rz < 0) ? -1.0 : 1.0;
            return new Vector3(0.0, sign * 90.0, -sign * 2.0 * MathUtil.atan2(qx, qw));
        }
    }

    private static float fastAsin(double x) {
        return MathUtil.atan(x / Math.sqrt(1.0 - x * x));
    }
}
