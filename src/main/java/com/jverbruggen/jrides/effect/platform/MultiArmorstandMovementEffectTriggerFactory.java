package com.jverbruggen.jrides.effect.platform;

import com.jverbruggen.jrides.config.trigger.MultiArmorstandMovementConfig;
import com.jverbruggen.jrides.config.trigger.TriggerConfig;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;

import java.util.List;

public class MultiArmorstandMovementEffectTriggerFactory {
    private final ViewportManager viewportManager;

    public MultiArmorstandMovementEffectTriggerFactory() {
        viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
    }

    public MultiArmorstandMovementEffectTrigger getMultiArmorstandMovementEffectTrigger(TriggerConfig triggerConfig){
        if(!(triggerConfig instanceof MultiArmorstandMovementConfig multiArmorstandMovementConfig))
            throw new RuntimeException("Incorrect config for effect trigger, expected multi-armorstand-movement");

        List<ArmorstandMovementEffectTrigger> armorstandMovements = multiArmorstandMovementConfig.create(viewportManager);

        return new MultiArmorstandMovementEffectTrigger(armorstandMovements);
    }
}
