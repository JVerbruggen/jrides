package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.effect.platform.EntityMovementTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;

public interface EntityMovementConfig {
    EntityMovementTrigger createTrigger(VirtualEntity virtualEntity);
    Vector3 getInitialLocation();
}
