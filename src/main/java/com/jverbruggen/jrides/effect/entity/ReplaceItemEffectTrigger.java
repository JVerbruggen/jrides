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
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class ReplaceItemEffectTrigger extends BaseTrainEffectTrigger implements EntityMovementTrigger {
    private final TrainModelItem trainModelItem;
    private final VirtualEntity targetEntity;

    private final DelayedEntityTask delayedEntityTask;


    public ReplaceItemEffectTrigger(TrainModelItem trainModelItem, VirtualEntity targetEntity, int delayTicks) {
        this.trainModelItem = trainModelItem;
        this.targetEntity = targetEntity;
        this.delayedEntityTask = new DelayedEntityTask(this::runTask, delayTicks);
    }

    private void runTask(){
        targetEntity.setModel(trainModelItem);
    }

    @Override
    public boolean finishedPlaying() {
        return delayedEntityTask.isFinished();
    }

    @Override
    public boolean execute(Train train) {
        this.delayedEntityTask.start();
        return true;
    }

    @Override
    public void onFinish(Runnable runnable) {
        delayedEntityTask.addOnFinishRunnable(runnable);
    }
}
