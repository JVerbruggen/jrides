package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLockCollection;

public class TriggerContext {
    private final DispatchLockCollection dispatchLockCollection;
    private final DispatchTrigger dispatchTrigger;
    private final GateTrigger gateTrigger;
    private final RestraintTrigger restraintTrigger;

    public TriggerContext(DispatchTrigger dispatchTrigger, GateTrigger gateTrigger, RestraintTrigger restraintTrigger) {
        this.dispatchLockCollection = null;
        this.dispatchTrigger = dispatchTrigger;
        this.gateTrigger = gateTrigger;
        this.restraintTrigger = restraintTrigger;
    }

    public TriggerContext(DispatchLockCollection dispatchLockCollection) {
        this.dispatchLockCollection = dispatchLockCollection;
        this.dispatchTrigger = new DispatchTrigger(dispatchLockCollection);
        this.gateTrigger = null;
        this.restraintTrigger = null;
    }

    public DispatchTrigger getDispatchTrigger() {
        return dispatchTrigger;
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
}
