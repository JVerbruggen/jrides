package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;

public abstract class BaseTriggerConfig extends BaseConfig implements TriggerConfig {
    protected TriggerType triggerType;

    public BaseTriggerConfig(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    @Override
    public TriggerType getType() {
        return triggerType;
    }
}
