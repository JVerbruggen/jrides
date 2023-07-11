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

public class CommonTrigMath {
    static final double sq2p1 = 2.414213562373095048802e0;
    static final double sq2m1 = .414213562373095048802e0;
    static final double p4 = .161536412982230228262e2;
    static final double p3 = .26842548195503973794141e3;
    static final double p2 = .11530293515404850115428136e4;
    static final double p1 = .178040631643319697105464587e4;
    static final double p0 = .89678597403663861959987488e3;
    static final double q4 = .5895697050844462222791e2;
    static final double q3 = .536265374031215315104235e3;
    static final double q2 = .16667838148816337184521798e4;
    static final double q1 = .207933497444540981287275926e4;
    static final double q0 = .89678597403663861962481162e3;
    static final double PIO2 = 1.5707963267948966135E0;

    private static double mxatan(double arg) {
        double argsq = arg * arg, value;

        value = ((((p4 * argsq + p3) * argsq + p2) * argsq + p1) * argsq + p0);
        value = value / (((((argsq + q4) * argsq + q3) * argsq + q2) * argsq + q1) * argsq + q0);
        return value * arg;
    }

    private static double msatan(double arg) {
        return arg < sq2m1 ? mxatan(arg)
                : arg > sq2p1 ? PIO2 - mxatan(1 / arg)
                : PIO2 / 2 + mxatan((arg - 1) / (arg + 1));
    }

    public static double atan(double arg) {
        return arg > 0 ? msatan(arg) : -msatan(-arg);
    }

    public static double atan2(double arg1, double arg2) {
        if (arg1 + arg2 == arg1)
            return arg1 >= 0 ? PIO2 : -PIO2;
        arg1 = atan(arg1 / arg2);
        return arg2 < 0 ? arg1 <= 0 ? arg1 + Math.PI : arg1 - Math.PI : arg1;
    }
}