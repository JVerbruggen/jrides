package com.jverbruggen.jrides.effect.handle;

import com.jverbruggen.jrides.models.properties.frame.Frame;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

public interface EffectTriggerHandle {
    static <T extends EffectTriggerHandle> EffectTriggerHandle FindNearestNextEffect(LinkedList<T> linkedList, Predicate<T> filter, Frame forFrame){
        if(linkedList == null || linkedList.size() == 0 || forFrame == null)
            return null;
        else if(linkedList.size() == 1)
            return linkedList.getFirst();
        else{
            T selectedEffect = linkedList.getFirst();
            if(selectedEffect.shouldPlay(forFrame)){
                Iterator<T> iterator = linkedList.iterator();
                while(iterator.hasNext()){
                    T comparing = iterator.next();
                    if(!comparing.shouldPlay(forFrame) && filter.test(comparing)){
                        selectedEffect = comparing;
                        break;
                    }
                }
            }else{
                Iterator<T> iterator = linkedList.descendingIterator();
                while(iterator.hasNext()){
                    T comparing = iterator.next();
                    if(comparing.shouldPlay(forFrame) && filter.test(comparing)){
                        selectedEffect = comparing;
                    }else break;
                }
            }
            return selectedEffect;
        }
    }

    boolean shouldPlay(Frame frame);
}
