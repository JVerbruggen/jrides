package com.jverbruggen.jrides.effect.handle.cart;

import com.jverbruggen.jrides.effect.cart.CartEffectTrigger;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;

public interface CartEffectTriggerHandle extends EffectTriggerHandle {
    Frame getFrame();
    CartEffectTrigger getCartEffectTrigger();
    void execute(Cart cart);

    void setNext(CartEffectTriggerHandle cartEffectTriggerHandle);
    CartEffectTriggerHandle next();
}
