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

    public TriggerConfig fromConfigurationSection(String effectName, ConfigurationSection configurationSection){
        TriggerType type = TriggerType.fromString(configurationSection.getString("type"));
        TriggerConfig triggerConfig;
        if(triggerConfigList.containsKey(effectName)) return triggerConfigList.get(effectName);

        switch(type){
            case MUSIC:
                triggerConfig = MusicTriggerConfig.fromConfigurationSection(configurationSection);
                break;
            case MULTI_ARMORSTAND_MOVEMENT:
                triggerConfig = MultiArmorstandMovementConfig.fromConfigurationSection(configurationSection);
                break;
//            case MOVING_FALLING_BLOCK_PLATFORM:
//                triggerConfig = MovingFallingBlockPlatformConfig.fromConfigurationSection(configurationSection);
//                break;
            default:
                throw new RuntimeException("Trigger type " + type + " is not supported");
        }

        triggerConfigList.put(effectName, triggerConfig);

        return triggerConfig;
    }
}
