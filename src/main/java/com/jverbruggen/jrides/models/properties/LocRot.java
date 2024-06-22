package com.jverbruggen.jrides.models.properties;

import com.jverbruggen.jrides.models.math.Vector3;

public record LocRot(Vector3 location, Vector3 rotation) {

    public static LocRot fromLocationRotation(Vector3 location, Vector3 rotation) {
        if(location == null) throw new RuntimeException("Needs location to create LocRot");

        if(rotation == null) rotation = Vector3.zero();
        return new LocRot(location, rotation);
    }
}
