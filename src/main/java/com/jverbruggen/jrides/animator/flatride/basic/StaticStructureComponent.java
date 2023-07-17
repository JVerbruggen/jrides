package com.jverbruggen.jrides.animator.flatride.basic;

import com.jverbruggen.jrides.animator.flatride.AbstractInterconnectedFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.config.flatride.structure.attachment.joint.RelativeAttachmentJointConfig;

import java.util.List;

public class StaticStructureComponent extends AbstractInterconnectedFlatRideComponent {
    public StaticStructureComponent(String identifier, String groupIdentifier, boolean root, RelativeAttachmentJointConfig joint, List<FlatRideModel> flatRideModels) {
        super(identifier, groupIdentifier, root, joint, flatRideModels);
    }
}
