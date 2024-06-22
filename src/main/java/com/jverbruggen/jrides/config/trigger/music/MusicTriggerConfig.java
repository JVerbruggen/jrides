package com.jverbruggen.jrides.config.trigger.music;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.effect.train.music.ExternalMusicEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

public class MusicTriggerConfig extends BaseTriggerConfig {
    private final MusicTriggerConfigHandler handler;
    private final String resource;
    private final String descriptor;

    public MusicTriggerConfig(MusicTriggerConfigHandler handler, String resource, String descriptor) {
        super(TriggerType.MUSIC);
        this.handler = handler;
        this.resource = resource;
        this.descriptor = descriptor;
    }

    @SuppressWarnings("unused")
    public MusicTriggerConfigHandler getHandler() {
        return handler;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getResource() {
        return resource;
    }

    public static MusicTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        MusicTriggerConfigHandler handler = MusicTriggerConfigHandler.fromString(getString(configurationSection, "handler"));
        String resource = getString(configurationSection, "resource");
        String descriptor = getString(configurationSection, "descriptor", "default");

        return new MusicTriggerConfig(handler, resource, descriptor);
    }

    @Override
    public TrainEffectTrigger createTrigger() {
        return new ExternalMusicEffectTrigger(getResource(), getDescriptor());
    }
}