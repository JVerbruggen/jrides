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

package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.models.ride.StationHandle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TriggerContext {
    private final DispatchLockCollection dispatchLockCollection;
    private final DispatchLock vehiclePresentLock;
    private final SimpleDispatchTrigger dispatchTrigger;
    private final GateTrigger gateTrigger;
    private final RestraintTrigger restraintTrigger;
    private @Nullable StationHandle parentStation;

    public TriggerContext(DispatchLockCollection dispatchLockCollection, DispatchLock vehiclePresentLock, SimpleDispatchTrigger dispatchTrigger, GateTrigger gateTrigger, RestraintTrigger restraintTrigger) {
        this.dispatchLockCollection = dispatchLockCollection;
        this.vehiclePresentLock = vehiclePresentLock;
        this.dispatchTrigger = dispatchTrigger;
        this.gateTrigger = gateTrigger;
        this.restraintTrigger = restraintTrigger;
        this.parentStation = null;
    }

    public SimpleDispatchTrigger getDispatchTrigger() {
        return dispatchTrigger;
    }

    public DispatchLock getVehiclePresentLock() {
        return vehiclePresentLock;
    }

    public GateTrigger getGateTrigger() {
        return gateTrigger;
    }

    public RestraintTrigger getRestraintTrigger() {
        return restraintTrigger;
    }

    public DispatchLockCollection getDispatchLockCollection() {
        return dispatchLockCollection;
    }

    @Nullable
    public StationHandle getParentStation() {
        return parentStation;
    }

    public void setParentStation(@Nonnull StationHandle parentStation) {
        this.parentStation = parentStation;
    }

    @Override
    public String toString() {
        return "TriggerContext{" +
                "vehiclePresentLock=" + vehiclePresentLock +
                ", parentStation=" + parentStation +
                '}';
    }
}
