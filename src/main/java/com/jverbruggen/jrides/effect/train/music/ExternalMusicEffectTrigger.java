/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.effect.train.music;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.event.ride.OnrideMusicTriggerEvent;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
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
    public boolean execute(Train train) {
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

        return true;
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
