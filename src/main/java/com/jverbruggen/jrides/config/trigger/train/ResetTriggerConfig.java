
package com.jverbruggen.jrides.config.trigger.train;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.train.ResetEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

public class ResetTriggerConfig extends BaseTriggerConfig {
    public ResetTriggerConfig() {
        super(TriggerType.RESET);
    }

    public static ResetTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        return new ResetTriggerConfig();
    }

    @Override
    public EffectTrigger createTrigger() {
        return new ResetEffectTrigger();
    }
}
