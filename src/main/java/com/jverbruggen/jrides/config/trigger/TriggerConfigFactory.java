package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.config.trigger.music.MusicTriggerConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class TriggerConfigFactory {
    private Map<String, TriggerConfig> triggerConfigList;

    public TriggerConfigFactory() {
        this.triggerConfigList = new HashMap<>();
    }

    public TriggerConfig fromConfigurationSection(String rideIdentifier, String effectName, ConfigurationSection configurationSection){
        TriggerType type = TriggerType.fromString(configurationSection.getString("type"));
        TriggerConfig triggerConfig;
        String mapKey = rideIdentifier + ":" + effectName;
        if(triggerConfigList.containsKey(mapKey)) return triggerConfigList.get(mapKey);

        switch(type){
            case MUSIC:
                triggerConfig = MusicTriggerConfig.fromConfigurationSection(configurationSection);
                break;
            case COMMAND:
                triggerConfig = CommandTriggerConfig.fromConfigurationSection(configurationSection);
                break;
            case MULTI_ARMORSTAND_MOVEMENT:
                triggerConfig = MultiEntityMovementConfig.fromConfigurationSection(configurationSection);
                break;
            default:
                throw new RuntimeException("Trigger type " + type + " is not supported");
        }

        triggerConfigList.put(mapKey, triggerConfig);

        return triggerConfig;
    }
}
