package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.TrainHandle;

import java.util.List;
import java.util.stream.Collectors;

public class TrainVisualFactory {
    public List<TrainVisual> createVisuals(CoasterHandle handle, MapScope mapScope){
        List<TrainHandle> trains = handle.getTrains();

        return trains.stream()
                .map(t -> new TrainVisual(t, mapScope))
                .collect(Collectors.toList());
    }
}
