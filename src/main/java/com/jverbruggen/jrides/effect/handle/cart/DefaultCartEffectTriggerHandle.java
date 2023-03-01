package com.jverbruggen.jrides.effect.handle.cart;

import com.jverbruggen.jrides.effect.cart.CartEffectTrigger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;

public class DefaultCartEffectTriggerHandle extends BaseCartEffectTriggerHandle {
    public DefaultCartEffectTriggerHandle(Frame frame, CartEffectTrigger cartEffectTrigger) {
        super(frame, cartEffectTrigger);
    }

    @Override
    public void execute(Cart cart) {
        getCartEffectTrigger().execute(cart);
    }
}
