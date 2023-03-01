package com.jverbruggen.jrides.effect.handle.train;

import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.Iterator;
import java.util.LinkedList;

public interface TrainEffectTriggerHandle extends EffectTriggerHandle {
    Frame getFrame();
    TrainEffectTrigger getTrainEffectTrigger();
    void execute(Train train);

    void setNext(TrainEffectTriggerHandle trainEffectTriggerHandle);
    TrainEffectTriggerHandle next();
}
