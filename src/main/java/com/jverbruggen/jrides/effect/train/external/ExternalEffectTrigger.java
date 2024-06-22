package com.jverbruggen.jrides.effect.train.external;

import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.event.ride.ExternalTriggerEvent;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.plugin.PluginManager;

import java.util.Map;

public class ExternalEffectTrigger extends BaseTrainEffectTrigger {
    private final Map<String, String> data;

    public ExternalEffectTrigger(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public boolean execute(Train train) {
        PluginManager pluginManager = ServiceProvider.getSingleton(PluginManager.class);
        pluginManager.callEvent(new ExternalTriggerEvent(train.getName(), data));
        return true;
    }

    @Override
    public boolean executeReversed(Train train) {
        return execute(train);
    }
}
