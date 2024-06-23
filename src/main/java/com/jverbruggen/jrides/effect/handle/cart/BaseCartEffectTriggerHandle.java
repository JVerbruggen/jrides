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

package com.jverbruggen.jrides.effect.handle.cart;

import com.jverbruggen.jrides.effect.cart.CartEffectTrigger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;

public abstract class BaseCartEffectTriggerHandle implements CartEffectTriggerHandle {
    protected final Frame frame;
    protected final CartEffectTrigger cartEffectTrigger;
    protected CartEffectTriggerHandle nextCartEffectTriggerHandle;

    public BaseCartEffectTriggerHandle(Frame frame, CartEffectTrigger cartEffectTrigger) {
        this.frame = frame;
        this.cartEffectTrigger = cartEffectTrigger;
        this.nextCartEffectTriggerHandle = null;
    }

    @Override
    public boolean shouldPlay(Frame forFrame) {
        Section section = forFrame.getSection();
        return this.getFrame().getSection().equals(section)
                && section.hasPassed(this.getFrame(), forFrame);
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public CartEffectTrigger getCartEffectTrigger() {
        return cartEffectTrigger;
    }

    @Override
    public void setNext(CartEffectTriggerHandle cartEffectTriggerHandle) {
        nextCartEffectTriggerHandle = cartEffectTriggerHandle;
    }

    @Override
    public CartEffectTriggerHandle next() {
        return nextCartEffectTriggerHandle;
    }

    @Override
    public String toString() {
        return "<handle to " + cartEffectTrigger + ">";
    }

    @Override
    public void execute(Train train, CoasterCart cart) {
        executeForCart(cart);
    }
}
