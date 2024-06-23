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

package com.jverbruggen.jrides.effect.train;

import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.event.player.PlayerFinishedRideEvent;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.stream.Collectors;

public class EjectEffectTrigger extends BaseTrainEffectTrigger {
    private final boolean asFinished;

    public EjectEffectTrigger(boolean asFinished) {
        this.asFinished = asFinished;
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public boolean execute(Train train) {
        if(asFinished)
            PlayerFinishedRideEvent.sendFinishedRideEvent(train.getPassengers()
                .stream()
                .map(p -> (JRidesPlayer)p)
                .collect(Collectors.toList()), train.getHandle().getCoasterHandle().getRide());

        train.ejectPassengers();
        return true;
    }

    @Override
    public boolean executeReversed(Train train) {
        return execute(train);
    }
}
