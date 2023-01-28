package com.jverbruggen.jrides.config.trigger.music;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import org.bukkit.configuration.ConfigurationSection;

public class MusicTriggerConfig extends BaseTriggerConfig {
    private final MusicTriggerConfigHandler handler;
    private final String resource;

    public MusicTriggerConfig(MusicTriggerConfigHandler handler, String resource) {
        super(TriggerType.MUSIC);
        this.handler = handler;
        this.resource = resource;
    }

    public MusicTriggerConfigHandler getHandler() {
        return handler;
    }

    public String getResource() {
        return resource;
    }

    public static MusicTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        MusicTriggerConfigHandler handler = MusicTriggerConfigHandler.fromString(configurationSection.getString("handler"));
        String resource = configurationSection.getString("resource");

        return new MusicTriggerConfig(handler, resource);
    }
}