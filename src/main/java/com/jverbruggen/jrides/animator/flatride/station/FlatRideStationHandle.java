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

package com.jverbruggen.jrides.animator.flatride.station;

import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;
import com.jverbruggen.jrides.models.ride.gate.Gate;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;

import java.util.List;

public class FlatRideStationHandle extends StationHandle {
    private final FlatRideUniVehicle vehicle;

    public FlatRideStationHandle(String name, String shortName, List<Gate> entryGates, PlayerLocation ejectLocation, MinMaxWaitingTimer waitingTimer, TriggerContext triggerContext, List<TrainEffectTriggerHandle> entryBlockingEffectTriggers, List<TrainEffectTriggerHandle> exitBlockingEffectTriggers, List<TrainEffectTriggerHandle> exitEffectTriggers) {
        super(name, shortName, entryGates, ejectLocation, waitingTimer, triggerContext, entryBlockingEffectTriggers, exitBlockingEffectTriggers, exitEffectTriggers);
        this.vehicle = new FlatRideUniVehicle(name + "_vehicle", false,
                triggerContext.getRestraintTrigger().getLock(), triggerContext.getVehiclePresentLock());
    }

    @Override
    public Vehicle getStationaryVehicle() {
        if(vehicle.isStationary()) return vehicle;
        return null;
    }

    public FlatRideUniVehicle getVehicle() {
        return vehicle;
    }

    public void setFlatRideHandle(FlatRideHandle flatRideHandle) {
        this.vehicle.setFlatRideHandle(flatRideHandle);
    }

    @Override
    public boolean canOperate(MessageAgent messageAgent) {
        if(messageAgent == null) return true;
        if(!messageAgent.isPlayer()) return true;

        Player player = messageAgent.getPlayer(ServiceProvider.getSingleton(PlayerManager.class));
        return player.getOperating() == vehicle.getFlatRideHandle().getRideController();
    }
}
