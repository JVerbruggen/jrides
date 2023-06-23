package com.jverbruggen.jrides.effect.cart.rotation;

import com.jverbruggen.jrides.effect.cart.BaseCartEffectTrigger;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;

public class CartRotationEffectTrigger extends BaseCartEffectTrigger {
    private final Vector3 rotation;
    private final int animationTicks;
    private int animationTickState;

    public CartRotationEffectTrigger(Vector3 rotation, int animationTicks) {
        this.rotation = rotation;
        this.animationTicks = animationTicks;
        this.animationTickState = 0;
    }

    @Override
    public void execute(CoasterCart cart) {

    }

    @Override
    public void executeReversed(CoasterCart cart) {

    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }
}
