package com.jverbruggen.jrides.effect.train.cart;

import com.jverbruggen.jrides.effect.cart.CartEffectTrigger;
import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class AllCartsTrainEffectTrigger extends BaseTrainEffectTrigger implements TrainEffectTrigger {
    private final CartEffectTrigger cartEffectTrigger;

    public AllCartsTrainEffectTrigger(CartEffectTrigger cartEffectTrigger) {
        this.cartEffectTrigger = cartEffectTrigger;
    }

    @Override
    public void execute(Train train) {
        train.getCarts().forEach(cartEffectTrigger::execute);
    }

    @Override
    public void executeReversed(Train train) {
        train.getCarts().forEach(cartEffectTrigger::executeReversed);
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }
}
