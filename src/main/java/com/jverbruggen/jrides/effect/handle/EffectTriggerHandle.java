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

package com.jverbruggen.jrides.effect.handle;

import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

public interface EffectTriggerHandle {
    static <T extends EffectTriggerHandle> T FindNearestNextEffect(Class<T> clazz, LinkedList<T> linkedList, Frame forFrame){
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
                    if(!comparing.shouldPlay(forFrame) && clazz.isInstance(comparing)){
                        selectedEffect = comparing;
                        break;
                    }
                }
            }else{
                Iterator<T> iterator = linkedList.descendingIterator();
                while(iterator.hasNext()){
                    T comparing = iterator.next();
                    boolean shouldPlay = comparing.shouldPlay(forFrame) && clazz.isInstance(comparing);
                    boolean isCloser = Frame.isBetweenFrames(forFrame, selectedEffect.getFrame(), comparing.getFrame());

                    if(shouldPlay || isCloser){
                        selectedEffect = comparing;
                    }else break;
                }
            }
            return selectedEffect;
        }
    }

    Frame getFrame();

    void execute(Train train, CoasterCart cart);

    boolean shouldPlay(Frame frame);
}
