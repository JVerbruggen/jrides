package com.jverbruggen.jrides.config.trigger.external;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.effect.train.external.ExternalEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExternalTriggerConfig extends BaseTriggerConfig {
    private final Map<String, String> data;

    public ExternalTriggerConfig(Map<String, String> data) {
        super(TriggerType.EXTERNAL_EVENT);

        this.data = data;
    }

    public static ExternalTriggerConfig fromConfigurationSection(@Nonnull ConfigurationSection configurationSection){
        Map<String, String> data = configurationSection.getKeys(false)
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        k -> Objects.requireNonNull(configurationSection.getString(k))));

        return new ExternalTriggerConfig(data);
    }

    @Override
    public TrainEffectTrigger createTrigger() {
        return new ExternalEffectTrigger(data);
    }
}