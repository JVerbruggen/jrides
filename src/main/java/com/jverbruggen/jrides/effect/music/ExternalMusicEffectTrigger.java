package com.jverbruggen.jrides.effect.music;

import com.jverbruggen.jrides.event.ride.OnrideMusicTriggerEvent;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class ExternalMusicEffectTrigger implements MusicEffectTrigger {
    private final PluginManager pluginManager;
    private final String musicResource;
    private final String musicType;

    public ExternalMusicEffectTrigger(String musicResource, String musicType) {
        this.musicType = musicType;
        this.pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        this.musicResource = musicResource;
    }

    @Override
    public void execute(Train train) {
        pluginManager.callEvent(new OnrideMusicTriggerEvent(musicResource, musicType, train.getPassengers()));
        Bukkit.broadcastMessage(musicResource);
    }

    @Override
    public void executeReversed(Train train) {
        throw new RuntimeException("Cannot execute music effect reversed");
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public String toString() {
        return "<MusicEffect " + musicResource + ">";
    }
}
