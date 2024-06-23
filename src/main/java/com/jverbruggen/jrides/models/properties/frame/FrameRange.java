/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.models.properties.frame;

import com.jverbruggen.jrides.models.properties.frame.factory.FrameFactory;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

public class FrameRange {
    private final Frame lower;
    private final Frame upper;
    private final int cycle;

    public FrameRange(Frame lower, Frame upper, int cycle) {
        this.lower = lower;
        this.upper = upper;
        this.cycle = cycle;
    }

    public Frame getInBetween(double percentage){
        int lowerValue = lower.getValue();
        int upperValue = upper.getValue();

        int frameIndex;
        if(lowerValue < upperValue){
            int delta = upperValue-lowerValue;
            frameIndex = (int) (lowerValue+(delta*percentage));
        }
        else {
            int lowerValuePart = cycle - lowerValue;
            int upperValuePart = upperValue;
            int totalParts = lowerValuePart + upperValuePart;
            double distribution = (double)lowerValuePart / (double)totalParts;
            if(percentage > distribution){
                //                         = (0.8        - 0.6         ) / 0.4 = 0.5 (50%)
                double upperPartPercentage = (percentage - distribution) / upperValuePart;
                frameIndex = (int) (upperValue*upperPartPercentage);
            }else{
                //                         = 0.4 / 0.6 = 0.66 (66%)
                double lowerPartPercentage = percentage / distribution;
                frameIndex = (int) (lowerValue + (lowerValuePart * lowerPartPercentage));
            }
        }

        return ServiceProvider.getSingleton(FrameFactory.class).getBlankStaticFrame(frameIndex);
    }
}
