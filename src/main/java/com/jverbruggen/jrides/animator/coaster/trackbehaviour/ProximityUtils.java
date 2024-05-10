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
