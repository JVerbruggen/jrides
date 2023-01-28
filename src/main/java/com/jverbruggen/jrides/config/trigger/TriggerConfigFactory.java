package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.config.trigger.music.MusicTriggerConfig;
import org.bukkit.configuration.ConfigurationSection;

public class TriggerConfigFactory {
    public TriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        TriggerType type = TriggerType.fromString(configurationSection.getString("type"));

        switch(type){
            case MUSIC:
                return MusicTriggerConfig.fromConfigurationSection(configurationSection);
            default:
                throw new RuntimeException("Trigger type " + type + " is not supported");
        }
    }
}
