package com.jverbruggen.jrides.effect.handle.train;

import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.section.Section;

public abstract class BaseTrainEffectTriggerHandle implements TrainEffectTriggerHandle {
    protected final Frame frame;
    protected final TrainEffectTrigger trainEffectTrigger;
    protected TrainEffectTriggerHandle nextTrainEffectTriggerHandle;

    public BaseTrainEffectTriggerHandle(Frame frame, TrainEffectTrigger trainEffectTrigger) {
        this.frame = frame;
        this.trainEffectTrigger = trainEffectTrigger;
        this.nextTrainEffectTriggerHandle = null;
    }

    @Override
    public boolean shouldPlay(Frame forFrame) {
        Section section = forFrame.getSection();
        return this.getFrame().getSection().equals(section)
                && section.hasPassed(this.getFrame(), forFrame);
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public TrainEffectTrigger getTrainEffectTrigger() {
        return trainEffectTrigger;
    }

    @Override
    public void setNext(TrainEffectTriggerHandle trainEffectTriggerHandle) {
        nextTrainEffectTriggerHandle = trainEffectTriggerHandle;
    }

    @Override
    public TrainEffectTriggerHandle next() {
        return nextTrainEffectTriggerHandle;
    }

    @Override
    public String toString() {
        return "<handle to " + trainEffectTrigger + ">";
    }
}
