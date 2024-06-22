package com.jverbruggen.jrides.effect.handle.cart;

import com.jverbruggen.jrides.effect.cart.CartEffectTrigger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;

public class DefaultCartEffectTriggerHandle extends BaseCartEffectTriggerHandle {
    public DefaultCartEffectTriggerHandle(Frame frame, CartEffectTrigger cartEffectTrigger) {
        super(frame, cartEffectTrigger);
    }

    @Override
    public boolean executeForCart(CoasterCart cart) {
        return getCartEffectTrigger().execute(cart);
    }
}
