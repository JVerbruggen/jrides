package com.jverbruggen.jrides.effect.platform;

import com.jverbruggen.jrides.config.trigger.MultiEntityMovementConfig;
import com.jverbruggen.jrides.config.trigger.TriggerConfig;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;

import java.util.List;

public class MultipleEffectExecutorTriggerFactory {
    private final ViewportManager viewportManager;

    public MultipleEffectExecutorTriggerFactory() {
        viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
    }

    public MultipleEffectExecutorTrigger getMultipleEffectExecutorTrigger(TriggerConfig triggerConfig){
        if(!(triggerConfig instanceof MultiEntityMovementConfig multiEntityMovementConfig))
            throw new RuntimeException("Incorrect config for effect trigger, expected multi-entity-movement");

        List<TrainEffectTrigger> entityMovementTriggers = multiEntityMovementConfig.createTriggers(viewportManager);

        return new MultipleEffectExecutorTrigger(entityMovementTriggers);
    }
}
