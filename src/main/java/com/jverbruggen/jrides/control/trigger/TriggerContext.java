package com.jverbruggen.jrides.control.trigger;

public class TriggerContext {
    private final DispatchTrigger dispatchTrigger;
    private final GateTrigger gateTrigger;
    private final RestraintTrigger restraintTrigger;

    public TriggerContext(DispatchTrigger dispatchTrigger, GateTrigger gateTrigger, RestraintTrigger restraintTrigger) {
        this.dispatchTrigger = dispatchTrigger;
        this.gateTrigger = gateTrigger;
        this.restraintTrigger = restraintTrigger;
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
}
