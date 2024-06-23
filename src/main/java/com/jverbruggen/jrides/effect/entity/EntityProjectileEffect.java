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

public class EntityProjectileEffect extends BaseTrainEffectTrigger implements EntityMovementTrigger{
    private final VirtualEntity virtualEntity;
    private final DelayedEntityTask delayedEntityTask;
    private final boolean resetOnStart;

    private final Vector3 initialPosition;
    private final Quaternion initialRotation;

    private final Vector3 initialPositionalVelocity;
    private final Quaternion initialRotationalVelocity;

    private final Vector3 positionalAcceleration;
    private final Quaternion rotationalAcceleration;

    private final Vector3 currentPositionalVelocity;
    private final Quaternion currentRotationalVelocity;

    public EntityProjectileEffect(VirtualEntity virtualEntity, boolean resetOnStart, Vector3 initialPosition, Quaternion initialRotation,
                                  Vector3 initialPositionalVelocity, Quaternion initialRotationalVelocity, Vector3 positionalAcceleration, Quaternion rotationalAcceleration,
                                  int animationTimeTicks, int delayTicks) {
        this.virtualEntity = virtualEntity;
        this.delayedEntityTask = new DelayedEntityTask(this::runTask, delayTicks, animationTimeTicks);
        this.delayedEntityTask.addOnFinishRunnable(this::onFinishCallback);
        this.resetOnStart = resetOnStart;

        this.initialPosition = initialPosition;
        this.initialRotation = initialRotation;

        this.initialPositionalVelocity = initialPositionalVelocity;
        this.initialRotationalVelocity = initialRotationalVelocity;

        this.positionalAcceleration = positionalAcceleration;
        this.rotationalAcceleration = rotationalAcceleration;

        this.currentPositionalVelocity = initialPosition.clone();
        this.currentRotationalVelocity = initialRotation.clone();
    }

    private void runTask(){
        addPosition();
        addRotation();
    }

    private void addPosition(){
        if(positionalAcceleration == null) return;

        this.currentPositionalVelocity.x += this.positionalAcceleration.x;
        this.currentPositionalVelocity.y += this.positionalAcceleration.y;
        this.currentPositionalVelocity.z += this.positionalAcceleration.z;

        virtualEntity.setLocation(Vector3.add(currentPositionalVelocity, virtualEntity.getLocation()));
    }

    private void addRotation(){
        if(rotationalAcceleration == null) return;

        this.currentRotationalVelocity.multiply(this.rotationalAcceleration);

        Quaternion result = Quaternion.multiply(virtualEntity.getRotation(), currentRotationalVelocity);
        virtualEntity.setRotation(result);
    }

    protected void start(){
        if(delayedEntityTask.isBusy()) return;

        if(resetOnStart){
            virtualEntity.setLocation(initialPosition);
            virtualEntity.setRotation(initialRotation);
        }

        resetVelocity();
        virtualEntity.setRendered(true);

        delayedEntityTask.start();
    }

    private void resetVelocity(){
        this.currentPositionalVelocity.setTo(this.initialPositionalVelocity);
        this.currentRotationalVelocity.setTo(this.initialRotationalVelocity);
    }

    private void onFinishCallback(){
        virtualEntity.setRendered(false);
    }

    @Override
    public boolean finishedPlaying() {
        return delayedEntityTask.isFinished();
    }

    @Override
    public void onFinish(Runnable runnable) {
        delayedEntityTask.addOnFinishRunnable(runnable);
    }

    @Override
    public boolean execute(Train train) {
        start();
        return true;
    }
}
