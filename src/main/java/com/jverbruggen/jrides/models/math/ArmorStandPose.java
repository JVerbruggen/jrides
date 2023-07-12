/**
 * This code has been kindly borrowed from bergerkiller's BKCommonLib
 *
 * MIT License
 *
 * Copyright (C) 2013-2015 bergerkiller Copyright (C) 2016-2020 Berger Healer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, and/or sublicense the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
