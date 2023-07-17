package com.jverbruggen.jrides.effect.train;

import com.jverbruggen.jrides.effect.platform.EntityMovementTrigger;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.List;

public class SequenceTrainEffectTrigger extends BaseTrainEffectTrigger {
    private final List<EntityMovementTrigger> triggerSequence;

    private Train cachedTrain;
    private boolean reversed;
    private boolean started;
    private boolean finished;
    private int triggerPointer;

    public SequenceTrainEffectTrigger(List<EntityMovementTrigger> triggerSequence){
        this.triggerSequence = triggerSequence;
        this.cachedTrain = null;
        this.reversed = false;
        this.started = false;
        this.finished = false;
        this.triggerPointer = 0;
    }

    @Override
    public boolean finishedPlaying() {
        return finished;
    }

    private void finish(){
        finished = true;
        started = false;
    }

    private void onItemFinish(){
        runNext();
    }

    private void runNext(){
        if(reachedEnd()){
            finish();
            return;
        }

        EntityMovementTrigger trigger = triggerSequence.get(triggerPointer);
        trigger.onFinish(this::onItemFinish);

        if(reversed){
            trigger.executeReversed(cachedTrain);
        }else{
            trigger.execute(cachedTrain);
        }

        incrementPointer();
    }

    private boolean reachedEnd(){
        if(!reversed) return triggerPointer >= triggerSequence.size();
        else return triggerPointer < 0;
    }

    private void incrementPointer(){
        if(!reversed) triggerPointer++;
        else triggerPointer--;
    }

    private void start(Train train){
        if(this.started) return;

        this.triggerPointer = this.reversed ? this.triggerSequence.size()-1 : 0;
        this.cachedTrain = train;
        this.started = true;
        this.finished = false;

        runNext();
    }

    @Override
    public void execute(Train train) {
        if(this.started) return;

        this.reversed = false;
        start(train);
    }

    @Override
    public void executeReversed(Train train) {
        if(this.started) return;

        this.reversed = true;
        start(train);
    }
}
