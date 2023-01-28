package com.jverbruggen.jrides.config.trigger;

public abstract class BaseTriggerConfig implements TriggerConfig {
    protected TriggerType triggerType;

    public BaseTriggerConfig(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    @Override
    public TriggerType getType() {
        return triggerType;
    }
}
