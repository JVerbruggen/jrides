package com.jverbruggen.jrides.effect.cart;

import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;

public class CartRestraintEffectTrigger extends BaseCartEffectTrigger {
    public final boolean locked;

    public CartRestraintEffectTrigger(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public void execute(CoasterCart cart) {
        cart.setRestraint(locked);
    }

    @Override
    public void executeReversed(CoasterCart cart) {

    }
}
