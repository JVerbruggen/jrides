package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.config.trigger.music.MusicTriggerConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class TriggerConfigFactory {
    private final Map<String, TriggerConfig> triggerConfigList;

    public TriggerConfigFactory() {
        this.triggerConfigList = new HashMap<>();
    }

    public TriggerConfig fromConfigurationSection(String rideIdentifier, String effectName, ConfigurationSection configurationSection){
        if(configurationSection == null) throw new RuntimeException("Trigger configuration was null for effect " + effectName);

        TriggerType type = TriggerType.fromString(configurationSection.getString("type"));
        TriggerConfig triggerConfig;
        String mapKey = rideIdentifier + ":" + effectName;
        if(triggerConfigList.containsKey(mapKey)) return triggerConfigList.get(mapKey);

        triggerConfig = switch (type) {
            case MUSIC ->
                    MusicTriggerConfig.fromConfigurationSection(configurationSection);
            case COMMAND ->
                    CommandTriggerConfig.fromConfigurationSection(configurationSection);
            case MULTI_ARMORSTAND_MOVEMENT ->
                    MultiEntityMovementConfig.fromConfigurationSection(configurationSection);
            case EXTERNAL_EVENT ->
                    ExternalTriggerConfig.fromConfigurationSection(configurationSection);
            default ->
                    throw new RuntimeException("Trigger type " + type + " is not supported");
        };

        triggerConfigList.put(mapKey, triggerConfig);

        return triggerConfig;
    }
}
