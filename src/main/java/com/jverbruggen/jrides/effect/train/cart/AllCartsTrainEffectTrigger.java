/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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
    public boolean execute(Train train) {
        return train.getCarts().stream().allMatch(cartEffectTrigger::execute);
    }

    @Override
    public boolean executeReversed(Train train) {
        return train.getCarts().stream().allMatch(cartEffectTrigger::executeReversed);
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }
}
