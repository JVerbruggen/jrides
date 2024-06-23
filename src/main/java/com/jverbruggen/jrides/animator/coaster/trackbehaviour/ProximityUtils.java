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

package com.jverbruggen.jrides.animator.coaster.trackbehaviour;

import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.List;

public class ProximityUtils {
    public static boolean isTooCloseToOtherTrains(Train train, List<Train> otherTrains, double minTrainDistance){
        boolean tooCloseToOther = false;
        int trainFrameValue = train.getMiddleOfTrainFrame().getValue();

        for(Train otherTrain : otherTrains){
            if(otherTrain == train) continue;

            int otherTrainFrameValue = otherTrain.getMiddleOfTrainFrame().getValue();
            if(otherTrainFrameValue < trainFrameValue)
                break; // Do not care about trains behind us

            int distanceFrom = otherTrainFrameValue - trainFrameValue;
            if(distanceFrom < minTrainDistance){
                tooCloseToOther = true;
            }
        }
        return tooCloseToOther;
    }
}
