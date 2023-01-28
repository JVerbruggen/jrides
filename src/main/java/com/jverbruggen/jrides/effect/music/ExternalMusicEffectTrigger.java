package com.jverbruggen.jrides.effect.music;

import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.event.ride.OnrideMusicTriggerEvent;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.plugin.PluginManager;

public class ExternalMusicEffectTrigger implements EffectTrigger, MusicEffectTrigger {
    private final PluginManager pluginManager;
    private final String musicResource;
    private final Frame frame;
    private EffectTrigger next;

    public ExternalMusicEffectTrigger(Frame frame, String musicResource) {
        this.frame = frame;
        this.pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        this.musicResource = musicResource;
    }

    @Override
    public void execute(Train train) {
        pluginManager.callEvent(new OnrideMusicTriggerEvent(musicResource));
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public void setNext(EffectTrigger effectTrigger) {
        next = effectTrigger;
    }

    @Override
    public EffectTrigger next() {
        return next;
    }
}
