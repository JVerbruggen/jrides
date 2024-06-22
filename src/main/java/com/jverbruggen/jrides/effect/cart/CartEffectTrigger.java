package com.jverbruggen.jrides.effect.cart;

import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;

public interface CartEffectTrigger extends EffectTrigger {
    /**
     *
     * @param cart
     * @return true if can process more effects
     */
    boolean execute(CoasterCart cart);

    /**
     *
     * @param cart
     * @return true if can process more effects
     */
    boolean executeReversed(CoasterCart cart);
}
