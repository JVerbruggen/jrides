package com.jverbruggen.jrides.animator.flatride.basic;

import com.jverbruggen.jrides.animator.flatride.AbstractInterconnectedFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;

import java.util.List;

public class StaticStructureComponent extends AbstractInterconnectedFlatRideComponent {
    public StaticStructureComponent(String identifier, String groupIdentifier, boolean root, List<FlatRideModel> flatRideModels) {
        super(identifier, groupIdentifier, root, flatRideModels);
    }
}
