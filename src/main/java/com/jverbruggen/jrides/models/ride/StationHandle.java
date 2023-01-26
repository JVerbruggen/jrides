package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class StationHandle {
    private DispatchTrigger dispatchTrigger;
    private Train stationaryTrain;
    private String name;

    public StationHandle(String name, DispatchLockCollection dispatchLockCollection){
        this.dispatchTrigger = new DispatchTrigger(dispatchLockCollection);
        this.stationaryTrain = null;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DispatchTrigger getDispatchTrigger(){
        return dispatchTrigger;
    }

    public boolean hasTrain(){
        return stationaryTrain != null;
    }

    public Train getStationaryTrain(){
        return stationaryTrain;
    }

    public void setStationaryTrain(Train train) {
        if(train == null){
            this.stationaryTrain = null;
            return;
        }

        if(stationaryTrain != null) throw new RuntimeException("Two trains cannot be in the same station!");
        this.stationaryTrain = train;
    }
}
