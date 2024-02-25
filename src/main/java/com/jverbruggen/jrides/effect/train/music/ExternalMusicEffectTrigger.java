package com.jverbruggen.jrides.effect.train.music;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.event.ride.OnrideMusicTriggerEvent;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExternalMusicEffectTrigger extends BaseTrainEffectTrigger implements MusicEffectTrigger {
    private final PluginManager pluginManager;
    private final String resource;
    private final String descriptor;

    public ExternalMusicEffectTrigger(String resource, String descriptor) {
        this.pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        this.resource = resource;
        this.descriptor = descriptor;
    }

    @Override
    public void execute(Train train) {
        List<Player> passengers;
        if(train != null){
            passengers = train.getPassengers();
        }else{
            passengers = new ArrayList<>();
        }

        pluginManager.callEvent(new OnrideMusicTriggerEvent(passengers
                .stream()
                .map(p -> (JRidesPlayer)p)
                .collect(Collectors.toList()), resource, descriptor));
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
        return "<MusicEffect " + resource + ">";
    }
}
