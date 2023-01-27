package com.jverbruggen.jrides.config.gates;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.gate.GateType;

public class GateConfig {
    private final Vector3 location;
    private final GateType type;

    public GateConfig(Vector3 location, GateType type) {
        this.location = location;
        this.type = type;
    }

    public GateType getType() {
        return type;
    }

    public Vector3 getLocation() {
        return location;
    }
}
