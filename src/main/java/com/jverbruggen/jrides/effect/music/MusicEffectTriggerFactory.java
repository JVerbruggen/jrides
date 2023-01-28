package com.jverbruggen.jrides.effect.music;

import com.jverbruggen.jrides.config.trigger.TriggerConfig;
import com.jverbruggen.jrides.config.trigger.music.MusicTriggerConfig;

public class MusicEffectTriggerFactory {
    public MusicEffectTrigger getMusicEffectTrigger(TriggerConfig triggerConfig){
        MusicTriggerConfig musicTriggerConfig = (MusicTriggerConfig) triggerConfig;

        return new ExternalMusicEffectTrigger(musicTriggerConfig.getResource());
    }
}
