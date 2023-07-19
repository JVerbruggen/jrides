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
    public void execute(Train train) {
        this.delayedEntityTask.start();
    }

    @Override
    public void executeReversed(Train train) {
        execute(train);
    }

    @Override
    public void onFinish(Runnable runnable) {
        delayedEntityTask.setOnFinishRunnable(runnable);
    }
}
