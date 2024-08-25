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

package com.jverbruggen.jrides.animator.blender;

import com.jverbruggen.jrides.animator.flatride.BlenderExportPositionRecord;
import com.jverbruggen.jrides.common.Sync;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.VectorQuaternionState;
import com.jverbruggen.jrides.models.ride.Has6DOFPosition;
import com.jverbruggen.jrides.state.ride.flatride.Animation;
import com.jverbruggen.jrides.state.ride.flatride.AnimationHandle;
import org.bukkit.Bukkit;

public class BlenderAnimationExecutor {
    private final Vector3 offset;
    private final Has6DOFPosition target;
    private final AnimationHandle animationHandle;
    private String animationTarget;
    private int frameIndexState;
    private boolean playing;
    private int playingTaskId;

    public BlenderAnimationExecutor(Vector3 offset, AnimationHandle animationHandle, Has6DOFPosition target, String animationTarget) {
        this.offset = offset;
        this.target = target;
        this.animationHandle = animationHandle;
        this.animationTarget = animationTarget;
        this.frameIndexState = 0;
        this.playing = false;
        this.playingTaskId = -1;
    }

    public BlenderAnimationExecutor(AnimationHandle animationHandle) {
        this(null, animationHandle, null, null);
    }

    public boolean hasNextFrame(){
        return this.frameIndexState < animationHandle.getAnimation(animationTarget).getFrames().size() - 1;
    }

    public void nextFrame(){
        this.frameIndexState++;
    }

    public void reset(){
        this.frameIndexState = 0;
    }

    public BlenderAnimationExecutor withAnimationTarget(String animationTarget){
        this.animationTarget = animationTarget;
        return this;
    }

    public void playAnimationFrame(){
        if(target == null) return;

        BlenderExportPositionRecord position = getPositionRecordOrNull();
        if(position == null) return;

        target.setPositionRotation(
                Vector3.add(position.toMinecraftVector(), offset),
                position.toMinecraftQuaternion());
    }

    public void playAnimationFull(){
        if(this.playing) return;
        this.reset();

        this.playing = true;

        this.playingTaskId = Sync.runRepeated(() -> {
            playAnimationFrame();

            if(hasNextFrame()) {
                nextFrame();
            }else{
                Sync.stopTask(this.playingTaskId);
                this.playing = false;
            }
        }, 1);
    }

    public BlenderExportPositionRecord getPositionRecordOrNull(){
        Animation animation = animationHandle.getAnimation(animationTarget);
        if(animation.getFrames().size() <= frameIndexState) return null;

        return animation.getFrames().get(frameIndexState);
    }
}
