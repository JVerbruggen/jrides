package com.jverbruggen.jrides.effect.handle;

import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.models.properties.Frame;

public abstract class BaseEffectTriggerHandle implements EffectTriggerHandle {
    protected final Frame frame;
    protected final EffectTrigger effectTrigger;
    protected EffectTriggerHandle nextEffectTriggerHandle;

    public BaseEffectTriggerHandle(Frame frame, EffectTrigger effectTrigger) {
        this.frame = frame;
        this.effectTrigger = effectTrigger;
        this.nextEffectTriggerHandle = null;
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public EffectTrigger getEffectTrigger() {
        return effectTrigger;
    }

    @Override
    public void setNext(EffectTriggerHandle effectTriggerHandle) {
        nextEffectTriggerHandle = effectTriggerHandle;
    }

    @Override
    public EffectTriggerHandle next() {
        return nextEffectTriggerHandle;
    }
}
