package com.jverbruggen.jrides.effect.handle.cart;

import com.jverbruggen.jrides.effect.cart.CartEffectTrigger;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;

public interface CartEffectTriggerHandle extends EffectTriggerHandle {
    CartEffectTrigger getCartEffectTrigger();

    /**
     *
     * @param cart
     * @return true if can process more effects
     */
    boolean executeForCart(CoasterCart cart);

    void setNext(CartEffectTriggerHandle cartEffectTriggerHandle);
    CartEffectTriggerHandle next();
}
