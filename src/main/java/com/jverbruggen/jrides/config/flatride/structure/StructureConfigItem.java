package com.jverbruggen.jrides.config.flatride.structure;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;

import java.util.List;

public interface StructureConfigItem {
    String getIdentifier();
    void createAndAddTo(List<FlatRideComponent> components);
}
