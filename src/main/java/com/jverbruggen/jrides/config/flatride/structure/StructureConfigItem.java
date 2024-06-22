package com.jverbruggen.jrides.config.flatride.structure;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;

import java.util.List;

public interface StructureConfigItem {
    String getIdentifier();
    void createAndAddTo(List<FlatRideComponent> components, FlatRideHandle rideHandle);
}
