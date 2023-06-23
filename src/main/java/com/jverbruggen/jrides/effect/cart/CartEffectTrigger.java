package com.jverbruggen.jrides.effect.cart;

import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;

public interface CartEffectTrigger extends EffectTrigger {
    void execute(CoasterCart cart);
    void executeReversed(CoasterCart cart);
}
