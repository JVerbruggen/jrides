package com.jverbruggen.jrides.config.trigger.train;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.train.EjectEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

public class EjectTriggerConfig extends BaseTriggerConfig {
    private final boolean asFinished;

    public EjectTriggerConfig(boolean asFinished) {
        super(TriggerType.EJECT);
        this.asFinished = asFinished;
    }

    public static EjectTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        boolean asFinished = getBoolean(configurationSection, "asFinished", true);

        return new EjectTriggerConfig(asFinished);
    }

    @Override
    public EffectTrigger createTrigger() {
        return new EjectEffectTrigger(asFinished);
    }
}
