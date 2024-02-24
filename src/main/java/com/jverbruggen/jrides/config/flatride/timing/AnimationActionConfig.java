package com.jverbruggen.jrides.config.flatride.timing;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.AnimationInstruction;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.InstructionBinding;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.TimingAction;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.flatride.AnimationHandle;
import com.jverbruggen.jrides.state.ride.flatride.AnimationLoader;

import java.util.ArrayList;
import java.util.List;

public class AnimationActionConfig extends BaseConfig implements ActionConfig{
    private final String animation;

    public AnimationActionConfig(String animation) {
        this.animation = animation;
    }

    public String getAnimation() {
        return animation;
    }

    @Override
    public List<TimingAction> getTimingAction(FlatRideHandle flatRideHandle, List<FlatRideComponent> flatRideComponents) {
        List<TimingAction> timingActions = new ArrayList<>();
        AnimationLoader animationLoader = ServiceProvider.getSingleton(AnimationLoader.class);

        AnimationHandle animationHandle = animationLoader.loadFlatRideAnimation(this.animation, flatRideHandle);
        timingActions.add(new InstructionBinding(new AnimationInstruction(animationHandle), flatRideComponents));

        return timingActions;
    }
}
