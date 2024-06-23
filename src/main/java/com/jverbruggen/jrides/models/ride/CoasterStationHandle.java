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

package com.jverbruggen.jrides.models.ride;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;
import com.jverbruggen.jrides.models.ride.gate.Gate;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;

import java.util.List;

public class CoasterStationHandle extends StationHandle {
    private Train stationaryTrain;
    private CoasterHandle coasterHandle;

    public CoasterStationHandle(CoasterHandle coasterHandle, String name, String shortName, TriggerContext triggerContext,
                                List<Gate> entryGates, MinMaxWaitingTimer waitingTimer, List<TrainEffectTriggerHandle> entryBlockingEffectTriggers,
                                List<TrainEffectTriggerHandle> exitBlockingEffectTriggers, List<TrainEffectTriggerHandle> exitEffectTriggers,
                                PlayerLocation ejectLocation){
        super(name, shortName, entryGates, ejectLocation, waitingTimer, triggerContext, entryBlockingEffectTriggers, exitBlockingEffectTriggers, exitEffectTriggers);
        this.coasterHandle = coasterHandle;
        this.stationaryTrain = null;

        coasterHandle.addStationHandle(this);
    }

    public boolean hasTrain(){
        return stationaryTrain != null;
    }

    public Train getStationaryTrain(){
        return stationaryTrain;
    }

    public CoasterHandle getCoasterHandle() {
        return coasterHandle;
    }

    public void setStationaryTrain(Train train) {
        if(train == null){
            if(this.stationaryTrain != null)
                this.stationaryTrain.setStationaryAt(null);
            this.stationaryTrain = null;
            return;
        }

        if(stationaryTrain != null) throw new RuntimeException("Two trains cannot be in the same station!");
        this.stationaryTrain = train;
        train.setStationaryAt(this);
    }

    @Override
    public void closeRestraints(){
        if(stationaryTrain == null) return;

        if(stationaryTrain.getRestraintState()) return;

        stationaryTrain.setRestraintForAll(true);
        getTriggerContext().getRestraintTrigger().getLock().setLocked(false);
    }

    public void onPlayerEnter(Player player){
        int passengerCount = getStationaryTrain().getPassengers().size();
        if(passengerCount == 1){
            getCoasterHandle().getRideController().getControlMode().getWaitingTimer().setPreferredWaitingTimeFromNow(15);
        }
    }

    public boolean shouldEject(){
        return true;
    }

    public boolean isExit(){
        return true;
    }

    @Override
    public boolean hasVehicle() {
        return hasTrain();
    }

    @Override
    public Vehicle getStationaryVehicle() {
        return getStationaryTrain();
    }

    @Override
    public boolean canOperate(MessageAgent messageAgent) {
        if(messageAgent == null) return true;
        if(!messageAgent.isPlayer()) return true;

        Player player = messageAgent.getPlayer(ServiceProvider.getSingleton(PlayerManager.class));
        return player.getOperating() == getCoasterHandle().getRideController();
    }
}
