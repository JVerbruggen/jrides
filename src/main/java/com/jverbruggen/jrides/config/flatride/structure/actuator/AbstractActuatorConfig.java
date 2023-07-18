package com.jverbruggen.jrides.config.flatride.structure.actuator;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.AbstractStructureConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.AttachmentConfig;

import java.util.List;

public abstract class AbstractActuatorConfig extends AbstractStructureConfig {
    private final FlatRideComponentSpeed flatRideComponentSpeed;

    public AbstractActuatorConfig(String identifier, boolean root, AttachmentConfig attachmentConfig, FlatRideComponentSpeed flatRideComponentSpeed, List<ModelConfig> flatRideModels) {
        super(identifier, root, flatRideModels, attachmentConfig);
        this.flatRideComponentSpeed = flatRideComponentSpeed;
    }

    public FlatRideComponentSpeed getFlatRideComponentSpeed() {
        return flatRideComponentSpeed;
    }
}
