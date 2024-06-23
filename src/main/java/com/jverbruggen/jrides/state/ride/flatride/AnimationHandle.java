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

package com.jverbruggen.jrides.state.ride.flatride;

import com.jverbruggen.jrides.animator.flatride.BlenderExportPositionRecord;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AnimationHandle {
    private final HashMap<String, Animation> animationHashMap;

    public AnimationHandle(){
        this.animationHashMap = new LinkedHashMap<>();
    }

    public Animation getAnimation(String animationIdentifier){
        if(!animationHashMap.containsKey(animationIdentifier)){
            String animationKeys = String.join(", ", animationHashMap.keySet());
            throw new RuntimeException("Animation with key '" + animationIdentifier + "' does not exist. Existing: " + animationKeys);
        }

        return animationHashMap.get(animationIdentifier);
    }

    public void putAnimation(Animation animation){
        String animationTarget = animation.getFrames().get(0).getObject();
        if (animationHashMap.containsKey(animationTarget)) {
            throw new RuntimeException("AnimationHandle has defined animations for " + animationTarget + " more than once!");
        }

        animationHashMap.put(animationTarget, animation);
    }

    public static AnimationHandle createAnimationHandle(List<BlenderExportPositionRecord> rawPositions){
        AnimationHandle animationHandle = new AnimationHandle();

        Animation animation = null;
        for(BlenderExportPositionRecord rawPosition : rawPositions){
            String rawPositionTarget = rawPosition.getObject();
            if(animation == null){
                animation = new Animation(rawPositionTarget);
            }else if(!animation.getTarget().equalsIgnoreCase(rawPositionTarget)){
                animationHandle.putAnimation(animation);
                animation = new Animation(rawPositionTarget);
            }

            animation.addPosition(rawPosition);
        }

        if(animation != null)
            animationHandle.putAnimation(animation);

        return animationHandle;
    }

}
