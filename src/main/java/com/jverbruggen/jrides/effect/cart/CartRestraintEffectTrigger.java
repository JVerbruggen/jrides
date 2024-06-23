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

package com.jverbruggen.jrides.effect.cart;

import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
import org.bukkit.Bukkit;

public class CartRestraintEffectTrigger extends BaseCartEffectTrigger {
    public final boolean locked;

    public CartRestraintEffectTrigger(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean finishedPlaying() {
        return true;
    }

    @Override
    public boolean execute(CoasterCart cart) {
        cart.setRestraint(locked);
        return true;
    }

    @Override
    public boolean executeReversed(CoasterCart cart) {
        return execute(cart);
    }
}
