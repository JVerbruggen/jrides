package com.jverbruggen.jrides.effect;

import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.frame.Frame;

public interface EffectTrigger {
    boolean finishedPlaying();
    EffectTriggerHandle createHandle(Frame frame, boolean reversed);
}
