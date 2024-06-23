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

package com.jverbruggen.jrides.control.controlmode;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;

public class SemiAutomaticMode extends BaseControlMode implements ControlMode {

    public SemiAutomaticMode(RideHandle rideHandle, TriggerContext triggerContext, MinMaxWaitingTimer waitingTimer) {
        super(rideHandle, triggerContext, waitingTimer);

        waitingTimer.setReachedTimeFunction(waitingTimer::reachedMinimum);
    }

    @Override
    public void tick() {
        super.tick();

        MinMaxWaitingTimer waitingTimer = getWaitingTimer();

        StationHandle stationHandle = triggerContext.getParentStation();
        if(stationHandle == null) return;

        Vehicle stationaryVehicle = stationHandle.getStationaryVehicle();
        if(stationaryVehicle == null) return;

        waitingTimer.sendGenericWaitingNotification(stationaryVehicle.getPassengers());


        if(getOperator() != null){
            if(waitingTimer.reachedMaximum()){
                languageFile.sendMessage(getOperator(),
                        LanguageFileField.NOTIFICATION_OPERATOR_IDLE_TOO_LONG,
                        b->b.add(LanguageFileTag.rideDisplayName, rideHandle.getRide().getDisplayName()));
                getOperator().getOperating().setOperator(null);
            }
        }
    }
}
