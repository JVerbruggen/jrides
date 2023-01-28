package com.jverbruggen.jrides.effect.music;

import com.jverbruggen.jrides.config.trigger.TriggerConfig;
import com.jverbruggen.jrides.config.trigger.music.MusicTriggerConfig;
import com.jverbruggen.jrides.models.properties.Frame;

public class MusicEffectTriggerFactory {
    public MusicEffectTrigger getMusicEffectTrigger(Frame frame, TriggerConfig triggerConfig){
        MusicTriggerConfig musicTriggerConfig = (MusicTriggerConfig) triggerConfig;

        return new ExternalMusicEffectTrigger(frame, musicTriggerConfig.getResource());
    }
}
