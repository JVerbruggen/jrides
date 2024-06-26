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

package com.jverbruggen.jrides.effect.entity;

import com.jverbruggen.jrides.effect.entity.common.DelayedEntityTask;
import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class EntityContinuousMovementEffectTrigger extends BaseTrainEffectTrigger implements EntityMovementTrigger {
    private final VirtualEntity virtualEntity;

    private final DelayedEntityTask delayedEntityTask;

    private final Vector3 initialLocation;
    private final Quaternion initialRotation;
    private final boolean resetOnStart;

    private final Vector3 locationDelta;
    private final Quaternion rotationDelta;

    public EntityContinuousMovementEffectTrigger(VirtualEntity virtualEntity, Vector3 initialLocation, Quaternion initialRotation, boolean resetOnStart, Vector3 locationDelta, Quaternion rotationDelta, int animationTimeTicks, int delayTicks) {
        this.virtualEntity = virtualEntity;
        this.locationDelta = locationDelta;
        this.rotationDelta = rotationDelta;

        this.delayedEntityTask = new DelayedEntityTask(this::runTask, delayTicks, animationTimeTicks);

        this.initialLocation = initialLocation;
        this.initialRotation = initialRotation;
        this.resetOnStart = resetOnStart;
    }

    public VirtualEntity getVirtualEntity() {
        return virtualEntity;
    }

    private void runTask(){
        addPosition();
        addRotation();
    }

    private void addRotation(){
        if(rotationDelta == null) return;

        Quaternion result = Quaternion.multiply(virtualEntity.getRotation(), rotationDelta);
        virtualEntity.setRotation(result);
    }

    private void addPosition(){
        if(locationDelta == null) return;

        virtualEntity.setLocation(Vector3.add(locationDelta, virtualEntity.getLocation()));
    }

    @Override
    public boolean execute(Train train) {
        start();
        return true;
    }

    @Override
    public boolean executeReversed(Train train) {
        return execute(train);
    }

    protected void start(){
        if(delayedEntityTask.isBusy()) return;

        if(resetOnStart){
            virtualEntity.setLocation(initialLocation);
            virtualEntity.setRotation(initialRotation);
        }

        delayedEntityTask.start();
    }

    @Override
    public boolean finishedPlaying() {
        return delayedEntityTask.isFinished();
    }

    @Override
    public void onFinish(Runnable runnable) {
        delayedEntityTask.addOnFinishRunnable(runnable);
    }
}
