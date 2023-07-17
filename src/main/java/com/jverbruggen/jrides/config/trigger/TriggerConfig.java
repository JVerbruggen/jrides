package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.effect.EffectTrigger;

public interface TriggerConfig {
    TriggerType getType();
    EffectTrigger createTrigger();
}
