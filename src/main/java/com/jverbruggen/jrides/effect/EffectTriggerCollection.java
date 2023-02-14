package com.jverbruggen.jrides.effect;

import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EffectTriggerCollection {
    private final List<EffectTriggerHandle> effectTriggers;

    public EffectTriggerCollection(List<EffectTriggerHandle> effectTriggers) {
        this.effectTriggers = effectTriggers;
    }

    public int size(){
        return effectTriggers.size();
    }

    public EffectTriggerHandle first(){
        return effectTriggers.get(0);
    }

    public LinkedList<EffectTriggerHandle> getLinkedList(){
        return new LinkedList<>(effectTriggers);
    }
}
