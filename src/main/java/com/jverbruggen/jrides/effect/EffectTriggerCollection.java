package com.jverbruggen.jrides.effect;

import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;

import java.util.LinkedList;
import java.util.List;

public class EffectTriggerCollection<T extends EffectTriggerHandle> {
    private final LinkedList<T> effectTriggers;

    public EffectTriggerCollection(LinkedList<T> effectTriggers) {
        this.effectTriggers = effectTriggers;
    }

    public int size(){
        return effectTriggers.size();
    }

    public T first(){
        return effectTriggers.get(0);
    }

    public LinkedList<T> getLinkedList(){
        return effectTriggers;
    }
}
