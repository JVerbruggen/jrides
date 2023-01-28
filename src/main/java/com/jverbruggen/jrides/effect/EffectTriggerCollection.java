package com.jverbruggen.jrides.effect;

import java.util.List;

public class EffectTriggerCollection {
    private final List<EffectTrigger> effectTriggers;

    public EffectTriggerCollection(List<EffectTrigger> effectTriggers) {
        this.effectTriggers = effectTriggers;
    }

    public int size(){
        return effectTriggers.size();
    }

    public EffectTrigger first(){
        return effectTriggers.get(0);
    }
}
