package com.jverbruggen.jrides.animator.flatride.timing.instruction;

import com.jverbruggen.jrides.animator.flatride.BlenderExportPositionRecord;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.interfaces.Component6DOFPosition;
import com.jverbruggen.jrides.state.ride.flatride.Animation;
import com.jverbruggen.jrides.state.ride.flatride.AnimationHandle;

public class AnimationInstruction implements Instruction {
    private final AnimationHandle animationHandle;
    private int frameIndexState;

    public AnimationInstruction(AnimationHandle animationHandle) {
        this.animationHandle = animationHandle;
        this.frameIndexState = 0;
    }

    @Override
    public void applyTo(FlatRideComponent component) {
        Animation animation = animationHandle.getAnimation(component.getIdentifier());
        if(animation.getFrames().size() <= frameIndexState) return;

        BlenderExportPositionRecord position = animation.getFrames().get(frameIndexState);

        ((Component6DOFPosition)component).setPositionRotation(
                position.toMinecraftVector(),
                position.toMinecraftQuaternion());
    }

    @Override
    public boolean canHandle(FlatRideComponent component) {
        return component instanceof Component6DOFPosition;
    }

    @Override
    public void tick() {
        frameIndexState++;
    }

    @Override
    public void reset() {
        frameIndexState = 0;
    }

    @Override
    public void cleanUp(FlatRideComponent component) {

    }
}
