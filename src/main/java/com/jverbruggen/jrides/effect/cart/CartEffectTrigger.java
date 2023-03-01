package com.jverbruggen.jrides.effect.cart;

import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;

public interface CartEffectTrigger extends EffectTrigger {
    void execute(Cart cart);
    void executeReversed(Cart cart);
}
