package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.config.trigger.entity.MultiEntityMovementConfig;
import com.jverbruggen.jrides.config.trigger.external.CommandAsPlayerTriggerConfig;
import com.jverbruggen.jrides.config.trigger.external.CommandForPlayerTriggerConfig;
import com.jverbruggen.jrides.config.trigger.external.CommandTriggerConfig;
import com.jverbruggen.jrides.config.trigger.external.ExternalTriggerConfig;
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
        if(triggerConfigList.containsKey(mapKey)){
            JRidesPlugin.getLogger().warning("Duplicate trigger identifier in triggers of ride " + rideIdentifier + ": '" + effectName + "'. Ignoring new one.");
            return triggerConfigList.get(mapKey);
        }

        triggerConfig = switch (type) {
            case MUSIC ->
                    MusicTriggerConfig.fromConfigurationSection(configurationSection);
            case COMMAND ->
                    CommandTriggerConfig.fromConfigurationSection(configurationSection);
            case COMMAND_FOR_PLAYER ->
                    CommandForPlayerTriggerConfig.fromConfigurationSection(configurationSection);
            case COMMAND_AS_PLAYER ->
                    CommandAsPlayerTriggerConfig.fromConfigurationSection(configurationSection);
            case ANIMATION_SEQUENCE ->
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
