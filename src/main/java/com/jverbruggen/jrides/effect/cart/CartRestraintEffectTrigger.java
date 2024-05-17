package com.jverbruggen.jrides.effect.cart;

import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
import org.bukkit.Bukkit;

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
    public boolean execute(CoasterCart cart) {
        cart.setRestraint(locked);
        return true;
    }

    @Override
    public boolean executeReversed(CoasterCart cart) {
        return execute(cart);
    }
}
