package com.jverbruggen.jrides.config.ride;

import java.util.List;

public class RideConfig {
    private RideConfigObject[] rides;

    public List<RideConfigObject> getRides() {
        return List.of(rides);
    }
}
