package com.jverbruggen.jrides.models.ride.coaster;

import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.List;

public interface Coaster extends Ride {
    List<Train> getTrains();
    void addTrain(Train train);
}
