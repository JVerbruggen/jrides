package com.jverbruggen.jrides.config.flatride.timing;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.TimingAction;

import java.util.List;

public interface ActionConfig {
    List<TimingAction> getTimingAction(FlatRideHandle flatRideHandle, List<FlatRideComponent> flatRideComponents);
}
