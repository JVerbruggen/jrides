package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.models.ride.StationHandle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TriggerContext {
    private final DispatchLockCollection dispatchLockCollection;
    private final DispatchLock trainPresentLock;
    private final DispatchTrigger dispatchTrigger;
    private final GateTrigger gateTrigger;
    private final RestraintTrigger restraintTrigger;
    private @Nullable StationHandle parentStation;

    public TriggerContext(DispatchLockCollection dispatchLockCollection, DispatchLock trainPresentLock, DispatchTrigger dispatchTrigger, GateTrigger gateTrigger, RestraintTrigger restraintTrigger) {
        this.dispatchLockCollection = dispatchLockCollection;
        this.trainPresentLock = trainPresentLock;
        this.dispatchTrigger = dispatchTrigger;
        this.gateTrigger = gateTrigger;
        this.restraintTrigger = restraintTrigger;
        this.parentStation = null;
    }

    public DispatchTrigger getDispatchTrigger() {
        return dispatchTrigger;
    }

    public DispatchLock getTrainPresentLock() {
        return trainPresentLock;
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
                "trainPresentLock=" + trainPresentLock +
                ", parentStation=" + parentStation +
                '}';
    }
}
