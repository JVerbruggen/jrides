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
