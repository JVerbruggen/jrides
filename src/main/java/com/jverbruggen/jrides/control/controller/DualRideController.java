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
import com.jverbruggen.jrides.control.trigger.TriggerContext;

public class DualRideController extends SingularRideController implements RideController {
    private TriggerContext leftStationTriggerContext;
    private TriggerContext rightStationTriggerContext;

    public DualRideController(TriggerContext leftStationTriggerContext, TriggerContext rightStationTriggerContext, RideHandle rideHandle) {
        this.leftStationTriggerContext = leftStationTriggerContext;
        this.rightStationTriggerContext = rightStationTriggerContext;

        this.changeMode(this.getControlModeFactory().getForWithoutOperator(rideHandle));
        this.setRideHandle(rideHandle);
    }

    public TriggerContext getLeftStationTriggerContext() {
        return leftStationTriggerContext;
    }

    public TriggerContext getRightStationTriggerContext() {
        return rightStationTriggerContext;
    }
}
