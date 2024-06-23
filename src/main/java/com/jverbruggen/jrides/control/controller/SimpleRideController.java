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

package com.jverbruggen.jrides.control.controller;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.control.controller.base.SingularRideController;
import com.jverbruggen.jrides.control.controlmode.ControlMode;
import com.jverbruggen.jrides.control.controlmode.factory.ControlModeFactory;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class SimpleRideController extends SingularRideController implements RideController {
    private final TriggerContext triggerContext;

    public SimpleRideController(RideHandle rideHandle, TriggerContext triggerContext) {
        super();
        this.triggerContext = triggerContext;
        this.changeMode(this.getControlModeFactory().getForWithoutOperator(rideHandle));

        this.setRideHandle(rideHandle);
    }

    @Override
    public TriggerContext getTriggerContext() {
        return triggerContext;
    }

    public static SimpleRideController createVoid(RideHandle rideHandle){
        SimpleRideController simpleRideController = new SimpleRideController(rideHandle, new TriggerContext(
                null, null, null, null, null
        ));

        simpleRideController.supportsMenu = false;

        return simpleRideController;
    }
}
